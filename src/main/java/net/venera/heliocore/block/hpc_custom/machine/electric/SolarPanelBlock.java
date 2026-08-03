package net.venera.heliocore.block.hpc_custom.machine.electric;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.hpc_custom.SolarPanelPartBlock;
import net.venera.heliocore.block.hpc_custom.machine.BaseMachineBlock;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import net.venera.heliocore.block.entity.machine.electric.SolarPanelEntity;
import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

public class SolarPanelBlock extends BaseMachineBlock<SolarPanelEntity> {
    public static final MapCodec<SolarPanelBlock> CODEC = simpleCodec(SolarPanelBlock::new);
    
    public SolarPanelBlock(Properties properties) {
        this(properties, HpCBlockEntities.BASIC_SOLAR_PANEL_ENTITY);
    }
    public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 15);
    
    public SolarPanelBlock(Properties properties, Supplier<BlockEntityType<SolarPanelEntity>> type) {
        super(properties, type);
        this.registerDefaultState(this.defaultBlockState().setValue(CHARGE, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        
        if (pos.getY() + 1 >= level.getMaxBuildHeight()) {
            return null;
        }
        
        BlockPos above = pos.above();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos checkPos = above.offset(x, 0, z);
                if (!level.getBlockState(checkPos).canBeReplaced(context)) {
                    return null;
                }
            }
        }

        BlockState superState = super.getStateForPlacement(context);
        if (superState == null) return null;

        return superState.setValue(CHARGE, 0);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockState partState = HpCBlocks.SOLAR_PANEL_PARTS.get().defaultBlockState();

            BlockPos above = pos.above();
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    level.setBlock(above.offset(x, 0, z), partState, 3);
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockPos above = pos.above();
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos partPos = above.offset(x, 0, z);
                    if (level.getBlockState(partPos).getBlock() instanceof SolarPanelPartBlock) {
                        level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CHARGE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return this.blockEntityType.get().create(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;

        return createTickerHelper(blockEntityType, this.blockEntityType.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState));
    }
}
