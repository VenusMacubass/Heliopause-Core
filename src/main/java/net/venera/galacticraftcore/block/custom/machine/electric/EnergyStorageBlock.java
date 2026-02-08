package net.venera.galacticraftcore.block.custom.machine.electric;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.venera.galacticraftcore.block.custom.machine.BaseMachineBlock;
import net.venera.galacticraftcore.block.entity.machine.electric.EnergyStorageEntity;
import net.venera.galacticraftcore.block.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

public class EnergyStorageBlock extends BaseMachineBlock<EnergyStorageEntity> {
    public static final MapCodec<EnergyStorageBlock> CODEC = simpleCodec(EnergyStorageBlock::new);
    public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 16);
    public EnergyStorageBlock(Properties properties) {
        this(properties, ModBlockEntities.ENERGY_STORAGE_ENTITY);
    }
    public EnergyStorageBlock(Properties properties, Supplier<BlockEntityType<EnergyStorageEntity>> type) {
        super(properties, type);
        this.registerDefaultState(this.defaultBlockState().setValue(CHARGE, 0));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return this.blockEntityType.get().create(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder); 
        builder.add(CHARGE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(CHARGE, 0);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;
        
        return createTickerHelper(blockEntityType, this.blockEntityType.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState));
    }
}
