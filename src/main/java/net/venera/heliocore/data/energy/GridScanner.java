package net.venera.heliocore.data.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.heliocore.block.custom.machine.electric.WireBlock;
import net.venera.heliocore.block.entity.machine.electric.BaseElectricMachineEntity;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class GridScanner {
    public record ScanResult(Set<BlockPos> wires, Set<GridPort> ports) {}
    
    public static ScanResult scanNetwork(Level level, BlockPos startWirePos) {
        Set<BlockPos> foundWires = new HashSet<>();
        Set<GridPort> foundPorts = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        
        queue.add(startWirePos);
        foundWires.add(startWirePos); 

        while (!queue.isEmpty()) {
            BlockPos currentWire = queue.poll();
            
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = currentWire.relative(dir);
                BlockState state = level.getBlockState(neighborPos);
                
                if (state.getBlock() instanceof WireBlock) {
                    if (foundWires.add(neighborPos)) {
                        queue.add(neighborPos); 
                    }
                }
                else {
                    BlockEntity be = level.getBlockEntity(neighborPos);
                    if (be instanceof BaseElectricMachineEntity machine) {
                        Direction machineFace = dir.getOpposite();
                        
                        if (machine.isOutputSide(machineFace)) {
                            foundPorts.add(new GridPort(neighborPos, machineFace, GridPort.PortType.OUTPUT));
                        }
                        else if (machine.isInputSide(machineFace)) {
                            foundPorts.add(new GridPort(neighborPos, machineFace, GridPort.PortType.INPUT));
                        }
                    }
                }
            }
        }
        return new ScanResult(foundWires, foundPorts);
    }
}
