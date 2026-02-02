package net.venera.galacticraftcore.block.custom.machine.electric;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.venera.galacticraftcore.block.custom.machine.BaseMachineBlock;
import net.venera.galacticraftcore.block.entity.machine.electric.EnergyStorageUnitEntity;
import net.venera.galacticraftcore.block.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class EnergyStorageUnitBlock extends BaseMachineBlock<EnergyStorageUnitEntity> {
    public static final MapCodec<EnergyStorageUnitBlock> CODEC = simpleCodec(EnergyStorageUnitBlock::new);
    public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 16);
    public EnergyStorageUnitBlock(Properties properties) {
        this(properties, ModBlockEntities.ENERGY_STORAGE_UNIT_ENTITY);
    }
    public EnergyStorageUnitBlock(Properties properties, Supplier<BlockEntityType<EnergyStorageUnitEntity>> type) {
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
