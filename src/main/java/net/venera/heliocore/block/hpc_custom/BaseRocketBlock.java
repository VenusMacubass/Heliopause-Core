package net.venera.heliocore.block.hpc_custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.venera.heliocore.entity.HpCEntities;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;

import javax.annotation.Nullable;

public abstract class BaseRocketBlock extends Block {
    public BaseRocketBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    public abstract Block getMiddleBlock();
    public abstract Block getTopBlock();
    public abstract Block getBottomBlock();
    public abstract int getRocketTier();
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (pos.getY() < level.getMaxBuildHeight() - 2 &&
                level.getBlockState(pos.above()).canBeReplaced(context) &&
                level.getBlockState(pos.above(2)).canBeReplaced(context)) {
            return this.defaultBlockState();
        }
        return null; 
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {

            Tier1RocketEntity rocketEntity = HpCEntities.TIER_1_ROCKET.get().create(level);
            if (rocketEntity != null) {
                rocketEntity.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                level.addFreshEntity(rocketEntity);
                player.startRiding(rocketEntity);
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos platformPos = pos.below();
        BlockState platformState = level.getBlockState(platformPos);
        return platformState.getBlock() instanceof LaunchPadBlock && platformState.getValue(LaunchPadBlock.IS_CENTER);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            level.setBlock(pos.above(), getMiddleBlock().defaultBlockState(), 3);
            level.setBlock(pos.above(2), getTopBlock().defaultBlockState(), 3);
        }
    }
    
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockState(pos.above()).getBlock() instanceof BaseRocketGeneratedBlock) {
                level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 35);
            }
            if (level.getBlockState(pos.above(2)).getBlock() instanceof BaseRocketGeneratedBlock) {
                level.setBlock(pos.above(2), Blocks.AIR.defaultBlockState(), 35);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
