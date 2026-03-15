package net.venera.galacticraftcore.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class BaseRocketGeneratedBlock extends Block {
    public BaseRocketGeneratedBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            // Only perform server-side modifications
            if (!level.isClientSide) {
                // Find the bottom block of the rocket
                BlockPos scanPos = pos;

                // Move down while we see generated rocket parts (middle/top)
                while (level.getBlockState(scanPos.below()).getBlock() instanceof BaseRocketGeneratedBlock) {
                    scanPos = scanPos.below();
                }

                // If the block below scanPos is a BaseRocketBlock, that's the true bottom
                if (level.getBlockState(scanPos.below()).getBlock() instanceof BaseRocketBlock) {
                    scanPos = scanPos.below();
                }

                // Now scanPos should be the bottom block position (or unchanged if not found)
                // Remove the bottom, middle and top if they match the expected classes
                if (level.getBlockState(scanPos).getBlock() instanceof BaseRocketBlock) {
                    level.setBlock(scanPos, Blocks.AIR.defaultBlockState(), 35);
                }
                if (level.getBlockState(scanPos.above()).getBlock() instanceof BaseRocketGeneratedBlock) {
                    level.setBlock(scanPos.above(), Blocks.AIR.defaultBlockState(), 35);
                }
                if (level.getBlockState(scanPos.above(2)).getBlock() instanceof BaseRocketGeneratedBlock) {
                    level.setBlock(scanPos.above(2), Blocks.AIR.defaultBlockState(), 35);
                }
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
