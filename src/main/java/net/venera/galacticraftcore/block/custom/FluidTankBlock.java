package net.venera.galacticraftcore.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.entity.FluidTankEntity;
import net.venera.galacticraftcore.data.component.CanisterData;
import net.venera.galacticraftcore.fluid.ModFluids;
import net.venera.galacticraftcore.item.custom.CanisterItem;
import org.jetbrains.annotations.Nullable;

public class FluidTankBlock extends BaseEntityBlock {
    public static final MapCodec<FluidTankBlock> CODEC = simpleCodec(FluidTankBlock::new);
    public static final IntegerProperty EXPANSIONS = IntegerProperty.create("expansions", 0, 3);

    public FluidTankBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(EXPANSIONS, 0));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.isClientSide()){return ItemInteractionResult.SUCCESS;}
        if (!(level.getBlockEntity(pos) instanceof FluidTankEntity fluidTankEntity)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        }

        if(stack.getItem() == Items.BUCKET){
            player.setItemInHand(hand, fluidTankEntity.drainTank(stack));
            return ItemInteractionResult.SUCCESS;
        }else if(stack.getItem() == ModFluids.CRUDE_OIL.getBucket()){
            player.setItemInHand(hand, fluidTankEntity.fillTank(stack));
            return ItemInteractionResult.SUCCESS;
        }else if(stack.getItem() == ModFluids.REFINED_FUEL.getBucket()){
            player.setItemInHand(hand, fluidTankEntity.fillTank(stack));
            return ItemInteractionResult.SUCCESS;
        }else if(stack.getItem() instanceof CanisterItem canisterItem){
            CanisterData data = canisterItem.getCanisterData(stack);
            if (data.isEmpty() || (data.isCrudeOil() && fluidTankEntity.data.get(0) > 0) ||
                    (data.isRefinedFuel() && fluidTankEntity.data.get(1) > 0)) {
                fluidTankEntity.drainTank(stack);
            } else if ((data.isCrudeOil() && fluidTankEntity.getOilSpace() > 0) ||
                    (data.isRefinedFuel() && fluidTankEntity.getFuelSpace() > 0)) {
                fluidTankEntity.fillTank(stack);
            }
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
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

        BlockState newState = state.setValue(EXPANSIONS, expansions);

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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EXPANSIONS);
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
