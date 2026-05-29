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

        // Start the search at the first pipe
        queue.add(startPipePos);
        visitedPositions.add(startPipePos);

        // Ensure we don't accidentally crawl back into the source machine
        visitedPositions.add(sourceMachinePos);

        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();

            // Look in all 6 directions from the current pipe
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(dir);

                // If we already looked at this block, skip it
                if (visitedPositions.contains(neighborPos)) continue;

                // 1. Is it a Pipe?
                if (level.getBlockState(neighborPos).getBlock() instanceof FluidPipeBlock) {
                    visitedPositions.add(neighborPos);
                    queue.add(neighborPos); // Add it to the queue to search from it later!
                }

                // 2. Is it a Fluid Machine / Tank?
                else {
                    BlockEntity be = level.getBlockEntity(neighborPos);
                    if (be instanceof IFluidMachine) {
                        foundInventories.add(be); // Add it to our results!
                        visitedPositions.add(neighborPos); // Mark visited so we don't add it twice
                        // Notice we DO NOT add machines to the queue, because pipes don't pass through them!
                    }
                }
            }
        }
        return foundInventories;
    }
}