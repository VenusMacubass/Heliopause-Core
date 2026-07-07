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
    
    private static final int LOSS_PER_BLOCK = 0;

    public EnergyGrid(int id, Set<BlockPos> wires, Set<GridPort> ports, Level level) {
        this.id = id;
        this.wires = wires;
        
        for (GridPort port : ports) {
            if (port.type() == GridPort.PortType.INPUT) inputs.add(port);
            else if (port.type() == GridPort.PortType.OUTPUT) outputs.add(port);
        }
        buildDistanceCache(level);
    }
    
    private void buildDistanceCache(Level level) {
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
        long totalDemand = 0;

        for (GridPort sinkPort : inputs) {
            if (!level.isLoaded(sinkPort.machinePos())) continue;
            BlockEntity sinkBe = level.getBlockEntity(sinkPort.machinePos());
            if (sinkBe instanceof BaseElectricMachineEntity battery && battery.getEnergyStorage() != null) {
                int space = battery.getEnergyStorage().receiveEnergy(Integer.MAX_VALUE, true);
                if (space > 0) {
                    demandMap.put(sinkPort, space);
                    totalDemand += space;
                }
            }
        }
        if (totalDemand == 0) return; 
        
        double gridRatio = Math.min(1.0, (double) totalSupply / totalDemand);
        long totalEnergySpentByGrid = 0;

        for (Map.Entry<GridPort, Integer> entry : demandMap.entrySet()) {
            GridPort sinkPort = entry.getKey();
            int requested = entry.getValue();
            
            int targetReceive = (int) Math.round(requested * gridRatio);
            if (targetReceive <= 0) continue;
            
            int minDistance = Integer.MAX_VALUE;
            for (GridPort activeSource : supplyMap.keySet()) {
                int dist = distanceCache.getOrDefault(activeSource, Collections.emptyMap()).getOrDefault(sinkPort, Integer.MAX_VALUE);
                if (dist < minDistance) {
                    minDistance = dist;
                }
            }
            if (minDistance == Integer.MAX_VALUE) minDistance = 0; 
            
            int loss = minDistance * LOSS_PER_BLOCK;
            int costToGrid = targetReceive + loss;
            
            if (totalSupply >= costToGrid) {
                BlockEntity sinkBe = level.getBlockEntity(sinkPort.machinePos());
                if (sinkBe instanceof BaseElectricMachineEntity battery) {
                    battery.getEnergyStorage().receiveEnergy(targetReceive, false); 
                    totalSupply -= costToGrid;
                    totalEnergySpentByGrid += costToGrid;
                }
            }
        }
        
        if (totalEnergySpentByGrid > 0) {
            long remainingToExtract = totalEnergySpentByGrid;
            long initialTotalSupply = supplyMap.values().stream().mapToLong(i -> i).sum();

            for (Map.Entry<GridPort, Integer> entry : supplyMap.entrySet()) {
                if (remainingToExtract <= 0) break;

                GridPort sourcePort = entry.getKey();
                int maxAvailable = entry.getValue();
                
                double supplyRatio = (double) maxAvailable / initialTotalSupply;
                int extractAmount = (int) Math.round(totalEnergySpentByGrid * supplyRatio);
                
                extractAmount = (int) Math.min(extractAmount, remainingToExtract);
                extractAmount = Math.min(extractAmount, maxAvailable);

                if (extractAmount > 0) {
                    BlockEntity sourceBe = level.getBlockEntity(sourcePort.machinePos());
                    if (sourceBe instanceof BaseElectricMachineEntity generator) {
                        generator.getEnergyStorage().extractEnergy(extractAmount, false); 
                        remainingToExtract -= extractAmount;
                    }
                }
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
