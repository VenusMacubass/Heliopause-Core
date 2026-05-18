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
        if (inputs.isEmpty() || outputs.isEmpty()) return; // Nothing to do

        // We iterate through each Output (Generator) one by one
        for (GridPort sourcePort : outputs) {
            if (!level.isLoaded(sourcePort.machinePos())) continue;
            BlockEntity sourceBe = level.getBlockEntity(sourcePort.machinePos());
            if (!(sourceBe instanceof BaseElectricMachineEntity generator)) continue;

            var genStorage = generator.getEnergyStorage();
            if (genStorage == null) continue;

            // 1. POLL OUTPUT: How much energy can this generator provide this tick?
            int availableToPush = genStorage.extractEnergy(Integer.MAX_VALUE, true); // Simulate
            if (availableToPush <= 0) continue;

            // 2. POLL INPUTS: Find all batteries that need power
            Map<GridPort, Integer> demandMap = new HashMap<>();
            int totalDemand = 0;

            for (GridPort sinkPort : inputs) {
                if (!level.isLoaded(sinkPort.machinePos())) continue;
                BlockEntity sinkBe = level.getBlockEntity(sinkPort.machinePos());
                if (sinkBe instanceof BaseElectricMachineEntity battery) {
                    var batStorage = battery.getEnergyStorage();
                    if (batStorage != null) {
                        int space = batStorage.receiveEnergy(Integer.MAX_VALUE, true); // Simulate
                        if (space > 0) {
                            demandMap.put(sinkPort, space);
                            totalDemand += space;
                        }
                    }
                }
            }

            if (totalDemand == 0) continue; // All batteries are full
            
            for (Map.Entry<GridPort, Integer> entry : demandMap.entrySet()) {
                GridPort sinkPort = entry.getKey();
                int requested = entry.getValue();

                // Proportional math: If a battery is 50% of the total demand, it gets up to 50% of the supply
                double shareRatio = (double) requested / totalDemand;
                int allottedEnergy = (int) (availableToPush * shareRatio);

                // Fetch the distance tax
                int distance = distanceCache.getOrDefault(sourcePort, Collections.emptyMap()).getOrDefault(sinkPort, 0);
                int loss = distance * LOSS_PER_BLOCK;
                
                int energyToDeliver = allottedEnergy - loss;

                if (energyToDeliver > 0) {
                    energyToDeliver = Math.min(energyToDeliver, requested);
                    
                    int extracted = genStorage.extractEnergy(energyToDeliver + loss, false);
                    
                    if (extracted > 0) {
                        BlockEntity sinkBe = level.getBlockEntity(sinkPort.machinePos());
                        if (sinkBe instanceof BaseElectricMachineEntity battery) {
                            battery.getEnergyStorage().receiveEnergy(extracted - loss, false);
                        }
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
