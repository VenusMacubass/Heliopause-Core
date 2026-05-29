package net.venera.heliocore.block.hpc_custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.venera.heliocore.block.entity.LaunchPlatformEntity;
import javax.annotation.Nullable;


public class LaunchPadBlock extends Block implements EntityBlock {
    public static final BooleanProperty IS_CENTER = BooleanProperty.create("is_center");

    public LaunchPadBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(IS_CENTER, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(IS_CENTER);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            checkForPlatformsAround(level, pos);
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock()) {

            if (state.getValue(IS_CENTER)) {
                breakPlatform(level, pos);
                return;
            }

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos possibleCenter = pos.offset(-x, 0, -z);
                    BlockState checkState = level.getBlockState(possibleCenter);
                    if (checkState.is(this) && checkState.getValue(IS_CENTER)) {
                        if (!centerChecker(level, possibleCenter)) {
                            breakPlatform(level, possibleCenter);
                            return;
                        }
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    private void checkForPlatformsAround(Level level, BlockPos placedPos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos possibleCenter = placedPos.offset(-x, 0, -z);
                if (centerChecker(level, possibleCenter)) {
                    markAsCenter(level, possibleCenter);
                }
            }
        }
    }

    private boolean centerChecker(Level level, BlockPos centerPos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos checkPos = centerPos.offset(x, 0, z);
                if (!level.getBlockState(checkPos).is(this)) {
                    return false;
                }
            }
        }

        //For isolating each platform
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) <= 1 && Math.abs(z) <= 1) continue; // skip inner 3x3
                BlockPos outerPos = centerPos.offset(x, 0, z);
                if (level.getBlockState(outerPos).is(this)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void markAsCenter(Level level, BlockPos centerPos) {
        BlockState currentState = level.getBlockState(centerPos);
        if (!currentState.getValue(IS_CENTER)) {
            level.setBlock(centerPos, currentState.setValue(IS_CENTER, true), Block.UPDATE_ALL);
        }
    }

    private void breakPlatform(Level level, BlockPos centerPos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos breakPos = centerPos.offset(x, 0, z);
                BlockState state = level.getBlockState(breakPos);
                if (state.is(this)) {
                    level.destroyBlock(breakPos, !Minecraft.getInstance().player.isCreative());
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(IS_CENTER) ?
                Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0) : //Center
                Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);  //Not center
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(IS_CENTER) ? new LaunchPlatformEntity(pos, state) : null;
    }
}

