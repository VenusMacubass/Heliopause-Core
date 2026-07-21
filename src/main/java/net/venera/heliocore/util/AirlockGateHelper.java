package net.venera.heliocore.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.heliocore.block.HpCBlocks;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class AirlockGateHelper {
    private static final int MAX_DOOR_SIZE = 100; 

    public static boolean toggleAirlockBlocks(Level level, BlockPos switchPos, Direction switchFacing, boolean isOpening) {
        Direction right = (switchFacing.getAxis() == Direction.Axis.Z) ? Direction.EAST : Direction.SOUTH;
        Direction left = right.getOpposite();
        Direction[] planeDirections = {Direction.UP, Direction.DOWN, left, right};
        
        BlockPos seed = null;
        for (Direction dir : planeDirections) {
            BlockPos checkPos = switchPos.relative(dir);
            BlockState state = level.getBlockState(checkPos);
            if (state.isAir() || state.is(HpCBlocks.BASE_BUILDING_WALL_BLACK.get())) {
                seed = checkPos;
                break;
            }
        }

        if (seed == null) return false; 
        Set<BlockPos> innerBlocks = new HashSet<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        queue.add(seed);
        visited.add(seed);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            innerBlocks.add(current);

            if (innerBlocks.size() > MAX_DOOR_SIZE) return false;

            for (Direction dir : planeDirections) {
                BlockPos neighbor = current.relative(dir);
                if (!visited.add(neighbor)) continue;

                BlockState neighborState = level.getBlockState(neighbor);

                if (neighborState.is(HpCBlocks.AIRLOCK_FRAME_BLOCK.get()) || neighborState.is(HpCBlocks.AIRLOCK_FRAME_SWITCH_BLOCK.get())) {
                    continue;
                } else if (neighborState.isAir() || neighborState.is(HpCBlocks.BASE_BUILDING_WALL_BLACK.get())) {
                    queue.add(neighbor);
                } else {
                    return false;
                }
            }
        }
        
        BlockState targetState = isOpening ? Blocks.AIR.defaultBlockState() : HpCBlocks.BASE_BUILDING_WALL_BLACK.get().defaultBlockState();

        for (BlockPos pos : innerBlocks) {
            level.setBlock(pos, targetState, 3);
        }

        return true;
    }
}
