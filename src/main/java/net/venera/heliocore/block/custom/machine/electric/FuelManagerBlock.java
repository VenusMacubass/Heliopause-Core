package net.venera.heliocore.block.custom.machine.electric;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.venera.heliocore.block.custom.machine.BaseMachineBlock;
import net.venera.heliocore.block.entity.ModBlockEntities;
import net.venera.heliocore.block.entity.machine.electric.FuelManagerEntity;
import org.jetbrains.annotations.Nullable;

public class FuelManagerBlock extends BaseMachineBlock<FuelManagerEntity> {
    public static final MapCodec<FuelManagerBlock> CODEC = simpleCodec(FuelManagerBlock::new);
    public FuelManagerBlock(Properties properties) {
        this(properties, ModBlockEntities.FUEL_MANAGER_ENTITY);
    }
    public FuelManagerBlock(Properties properties, DeferredHolder<BlockEntityType<?>, BlockEntityType<FuelManagerEntity>> blockEntityType) {
        super(properties, blockEntityType);
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
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;

        return createTickerHelper(blockEntityType, this.blockEntityType.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState));
    }
}
