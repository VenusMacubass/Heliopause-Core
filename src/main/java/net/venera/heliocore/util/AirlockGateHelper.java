package net.venera.heliocore.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.venera.heliocore.block.HpCBlocks;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class AirlockGateHelper {
    private static final int MAX_DOOR_SIZE = 100;

    public static boolean toggleAirlockBlocks(Level level, BlockPos switchPos, Direction switchFacing, boolean isOpening) {
        Direction.Axis axis = switchFacing.getAxis();
        Direction[] planeDirections;

        if (axis == Direction.Axis.Y) {
            planeDirections = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        } else if (axis == Direction.Axis.Z) {
            planeDirections = new Direction[]{Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST};
        } else {
            planeDirections = new Direction[]{Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH};
        }

        Set<BlockPos> validInnerBlocks = null;
        
        for (Direction dir : planeDirections) {
            BlockPos checkPos = switchPos.relative(dir);
            BlockState state = level.getBlockState(checkPos);

            if (state.isAir() || state.is(HpCBlocks.AIRLOCK_GENERATED_BLOCK.get())) {
                validInnerBlocks = findClosedRegion(level, checkPos, planeDirections);
                if (validInnerBlocks != null) {
                    break; 
                }
            }
        }
        
        if (validInnerBlocks == null) return false;

        BlockState targetState = isOpening ? Blocks.AIR.defaultBlockState() : HpCBlocks.AIRLOCK_GENERATED_BLOCK.get().defaultBlockState();
        if (!isOpening && targetState.hasProperty(BlockStateProperties.AXIS)) {
            targetState = targetState.setValue(BlockStateProperties.AXIS, axis);
        }

        for (BlockPos pos : validInnerBlocks) {
            level.setBlock(pos, targetState, 3);
        }

        return true;
    }
    
    private static Set<BlockPos> findClosedRegion(Level level, BlockPos startPos, Direction[] planeDirections) {
        Set<BlockPos> innerBlocks = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        queue.add(startPos);
        innerBlocks.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            if (innerBlocks.size() > MAX_DOOR_SIZE) return null;

            for (Direction dir : planeDirections) {
                BlockPos neighbor = current.relative(dir);
                if (innerBlocks.contains(neighbor)) continue;

                BlockState neighborState = level.getBlockState(neighbor);

                if (neighborState.is(HpCBlocks.AIRLOCK_FRAME_BLOCK.get()) || neighborState.is(HpCBlocks.AIRLOCK_FRAME_SWITCH_BLOCK.get())) {
                    continue; 
                } else if (neighborState.isAir() || neighborState.is(HpCBlocks.AIRLOCK_GENERATED_BLOCK.get())) {
                    queue.add(neighbor);
                    innerBlocks.add(neighbor);
                } else {
                    return null;
                }
            }
        }
        return innerBlocks; 
    }
}
