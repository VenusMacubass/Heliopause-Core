package net.venera.heliocore.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.venera.heliocore.block.hpc_custom.FluidPipeBlock;
import net.venera.heliocore.fluid.IFluidMachine;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class PipeNetworkHelper {
    public static Set<BlockEntity> findConnectedInventories(Level level, BlockPos startPipePos, BlockPos sourceMachinePos) {
        Set<BlockPos> visitedPositions = new HashSet<>();
        Set<BlockEntity> foundInventories = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        
        queue.add(startPipePos);
        visitedPositions.add(startPipePos);
        visitedPositions.add(sourceMachinePos);

        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();
            
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(dir);
                
                if (visitedPositions.contains(neighborPos)) continue;
                
                if (level.getBlockState(neighborPos).getBlock() instanceof FluidPipeBlock) {
                    visitedPositions.add(neighborPos);
                    queue.add(neighborPos); 
                }
                
                else {
                    BlockEntity be = level.getBlockEntity(neighborPos);
                    if (be instanceof IFluidMachine) {
                        foundInventories.add(be); 
                        visitedPositions.add(neighborPos);
                    }
                }
            }
        }
        return foundInventories;
    }
}