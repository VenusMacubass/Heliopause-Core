package net.venera.galacticraftcore.block.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.entity.FluidTankEntity;
import net.venera.galacticraftcore.data.component.CanisterData;
import net.venera.galacticraftcore.item.custom.CanisterItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class FluidTankBlock extends BaseEntityBlock {
    public static final MapCodec<FluidTankBlock> CODEC = simpleCodec(FluidTankBlock::new);
    public static final IntegerProperty CONFIGURATION = IntegerProperty.create("configuration", 0, 3);

    public FluidTankBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(CONFIGURATION, 0));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos newPos = findValidTank(level, pos);
        if (!level.isClientSide() && level.getBlockEntity(newPos) instanceof FluidTankEntity fluidTankEntity) {
            ItemStack result;
            switch (stack.getItem()) {
                case CanisterItem canisterItem when stack.getCount() > 1 -> {
                    result = fluidTankEntity.handleInteractions(stack.copyWithCount(1), player);
                    stack.shrink(1);
                    if (!player.addItem(result)) {
                        player.drop(result, false);
                    }
                    return ItemInteractionResult.SUCCESS;
                }
                case CanisterItem canisterItem when stack.getCount() == 1 -> {
                    result = fluidTankEntity.handleInteractions(stack, player);
                    player.setItemInHand(hand, result);
                    return ItemInteractionResult.SUCCESS;
                }
                case BucketItem bucketItem -> {
                    fluidTankEntity.handleInteractions(stack, player);
                    return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
                }

                default -> {
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }



    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    private void updateBlockState(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof FluidTankBlock)) return;

        BlockState aboveState = level.getBlockState(pos.above());
        BlockState belowState = level.getBlockState(pos.below());

        boolean hasTankAbove = aboveState.getBlock() instanceof FluidTankBlock;
        boolean hasTankBelow = belowState.getBlock() instanceof FluidTankBlock;

        int expansions;

        if (hasTankAbove && hasTankBelow) {
            expansions = 3;
        } else if (hasTankBelow) {
            expansions = 2;
        } else if (hasTankAbove) {
            expansions = 1;
        } else {
            expansions = 0;
        }

        BlockState newState = state.setValue(CONFIGURATION, expansions);

        if (!state.equals(newState)) {
            level.setBlock(pos, newState, Block.UPDATE_ALL);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
                                   BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide()) {
            if ((neighborPos.equals(pos.above()) || neighborPos.equals(pos.below())) && neighborBlock instanceof FluidTankBlock) {
                updateBlockState(level, pos);
            }
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);

        if (!level.isClientSide()) {
            // Update neighbors when this block is broken
            level.updateNeighborsAt(pos.above(), this);
            level.updateNeighborsAt(pos.below(), this);
        }
    }

    protected BlockPos findValidTank(Level level, BlockPos pos){
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(!(blockEntity instanceof FluidTankEntity fluidTankEntity)){return pos;}
        if (fluidTankEntity.getTankSpace() <= 0) {
            int offset = 1;
            BlockPos lastTankPos = pos;

            while (true) {
                BlockPos checkPos = pos.above(offset);
                BlockEntity checkBE = level.getBlockEntity(checkPos);

                if (checkBE instanceof FluidTankEntity checkTank) {
                    lastTankPos = checkPos;
                    if (checkTank.getTankSpace() > 0)
                        return checkPos;
                    offset++;
                    continue;
                }
                if(offset > level.getMaxBuildHeight()){return lastTankPos;}
                return lastTankPos;

            }
        }else if (fluidTankEntity.getFluidAmount() <= 0) {
            int offset = 1;
            BlockPos lastTankPos = pos;

            while (true) {
                BlockPos checkPos = pos.below(offset);
                BlockEntity checkBE = level.getBlockEntity(checkPos);

                if (checkBE instanceof FluidTankEntity checkTank) {
                    lastTankPos = checkPos;

                    if (checkTank.getFluidAmount() > 0)
                        return checkPos;

                    offset++;
                    continue;
                }
                if(offset > level.getMinBuildHeight()){return lastTankPos;}
                return lastTankPos;
            }
        }else{
            return pos;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONFIGURATION);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidTankEntity(blockPos, blockState);
    }
}
