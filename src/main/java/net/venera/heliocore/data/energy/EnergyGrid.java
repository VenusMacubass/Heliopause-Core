package net.venera.heliocore.data.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.venera.heliocore.block.entity.machine.electric.BaseElectricMachineEntity;

import java.util.*;

public class EnergyGrid {
    public final int id;
    private final Set<BlockPos> wires;
    private final List<GridPort> inputs = new ArrayList<>();
    private final List<GridPort> outputs = new ArrayList<>();
    
    private final Map<GridPort, Map<GridPort, Integer>> distanceCache = new HashMap<>();
    private static final double LOSS_PER_BLOCK = 0.005D;

    public EnergyGrid(int id, Set<BlockPos> wires, Set<GridPort> ports) {
        this.id = id;
        this.wires = wires;
        
        for (GridPort port : ports) {
            if (port.type() == GridPort.PortType.INPUT) inputs.add(port);
            else if (port.type() == GridPort.PortType.OUTPUT) outputs.add(port);
        }
        buildDistanceCache();
    }
    
    private void buildDistanceCache() {
        if (wires.isEmpty()) {
            for (GridPort source : outputs) {
                Map<GridPort, Integer> dists = new HashMap<>();
                for (GridPort sink : inputs) {
                    dists.put(sink, 0); 
                }
                distanceCache.put(source, dists);
            }
            return;
        }
        for (GridPort source : outputs) {
            Map<GridPort, Integer> distancesForThisSource = new HashMap<>();
            
            Queue<BlockPos> queue = new LinkedList<>();
            Map<BlockPos, Integer> visited = new HashMap<>();

            BlockPos startWire = source.getConnectedWirePos();
            queue.add(startWire);
            visited.put(startWire, 0);

            while (!queue.isEmpty()) {
                BlockPos current = queue.poll();
                int currentDist = visited.get(current);

                for (GridPort sink : inputs) {
                    if (sink.getConnectedWirePos().equals(current) && !distancesForThisSource.containsKey(sink)) {
                        distancesForThisSource.put(sink, currentDist);
                    }
                }
                
                if (distancesForThisSource.size() == inputs.size()) break;
                
                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = current.relative(dir);
                    if (wires.contains(neighbor) && !visited.containsKey(neighbor)) {
                        visited.put(neighbor, currentDist + 1);
                        queue.add(neighbor);
                    }
                }
            }
            distanceCache.put(source, distancesForThisSource);
        }
    }

    public void tickEnergy(Level level) {
        if (inputs.isEmpty() || outputs.isEmpty()) return;

        Map<GridPort, Integer> supplyMap = new HashMap<>();
        long totalSupply = 0;

        for (GridPort sourcePort : outputs) {
            if (!level.isLoaded(sourcePort.machinePos())) continue;
            BlockEntity sourceBe = level.getBlockEntity(sourcePort.machinePos());
            if (sourceBe instanceof BaseElectricMachineEntity generator && generator.getEnergyStorage() != null) {
                int available = generator.getEnergyStorage().extractEnergy(Integer.MAX_VALUE, true);
                if (available > 0) {
                    supplyMap.put(sourcePort, available);
                    totalSupply += available;
                }
            }
        }
        if (totalSupply == 0) return;

        Map<GridPort, Integer> demandMap = new HashMap<>();
        Map<GridPort, Integer> grossCostMap = new HashMap<>();
        Map<GridPort, Double> lossMultiplierMap = new HashMap<>();
        long totalDemand = 0;

        for (GridPort sinkPort : inputs) {
            if (!level.isLoaded(sinkPort.machinePos())) continue;
            BlockEntity sinkBe = level.getBlockEntity(sinkPort.machinePos());
            if (sinkBe instanceof BaseElectricMachineEntity battery && battery.getEnergyStorage() != null) {
                int space = battery.getEnergyStorage().receiveEnergy(Integer.MAX_VALUE, true);
                if (space > 0) {

                    int minDistance = Integer.MAX_VALUE;
                    for (GridPort activeSource : supplyMap.keySet()) {
                        int dist = distanceCache.getOrDefault(activeSource, Collections.emptyMap()).getOrDefault(sinkPort, Integer.MAX_VALUE);
                        if (dist < minDistance) {
                            minDistance = dist;
                        }
                    }
                    
                    if (minDistance == Integer.MAX_VALUE) minDistance = 0;
                    double lossMultiplier = minDistance * LOSS_PER_BLOCK;
                    if (lossMultiplier >= 1.0) continue;
                    int grossCost = (int) Math.ceil(space / (1.0 - lossMultiplier));

                    demandMap.put(sinkPort, space);
                    grossCostMap.put(sinkPort, grossCost);
                    lossMultiplierMap.put(sinkPort, lossMultiplier);
                    totalDemand += grossCost;
                }
            }
        }
        if (totalDemand == 0) return;

        double gridRatio = Math.min(1.0, (double) totalSupply / totalDemand);
        long totalEnergySpentByGrid = 0;
        long remainingSupply = totalSupply;

        for (Map.Entry<GridPort, Integer> entry : demandMap.entrySet()) {
            GridPort sinkPort = entry.getKey();
            int space = entry.getValue();
            int grossCost = grossCostMap.get(sinkPort);
            double lossMultiplier = lossMultiplierMap.get(sinkPort);
            
            int allocatedGross = (int) Math.round(grossCost * gridRatio);
            if (allocatedGross <= 0) continue;
            
            int loss = (int) Math.round(allocatedGross * lossMultiplier);
            int targetReceive = allocatedGross - loss;
            
            if (targetReceive > space) {
                int excess = targetReceive - space;
                targetReceive = space;
                allocatedGross -= excess;
            }
            if (targetReceive <= 0) continue;

            if (remainingSupply >= allocatedGross) {
                BlockEntity sinkBe = level.getBlockEntity(sinkPort.machinePos());
                if (sinkBe instanceof BaseElectricMachineEntity battery) {
                    battery.getEnergyStorage().receiveEnergy(targetReceive, false);
                    remainingSupply -= allocatedGross;
                    totalEnergySpentByGrid += allocatedGross;
                }
            }
        }

        if (totalEnergySpentByGrid > 0) {
            long remainingToExtract = totalEnergySpentByGrid;
            long remainingSupplyPool = totalSupply;

            for (Map.Entry<GridPort, Integer> entry : supplyMap.entrySet()) {
                if (remainingToExtract <= 0) break;

                GridPort sourcePort = entry.getKey();
                int maxAvailable = entry.getValue();

                double dynamicRatio = (double) maxAvailable / remainingSupplyPool;
                int extractAmount = (int) Math.round(remainingToExtract * dynamicRatio);

                extractAmount = (int) Math.min(extractAmount, remainingToExtract);
                extractAmount = Math.min(extractAmount, maxAvailable);

                if (extractAmount > 0) {
                    BlockEntity sourceBe = level.getBlockEntity(sourcePort.machinePos());
                    if (sourceBe instanceof BaseElectricMachineEntity generator) {
                        generator.getEnergyStorage().extractEnergy(extractAmount, false);
                        remainingToExtract -= extractAmount;
                    }
                }
                remainingSupplyPool -= maxAvailable;
            }
        }
    }

    public boolean containsMachine(BlockPos pos) {
        for (GridPort port : inputs) {
            if (port.machinePos().equals(pos)) return true;
        }
        for (GridPort port : outputs) {
            if (port.machinePos().equals(pos)) return true;
        }
        return false;
    }
}
