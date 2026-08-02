package net.venera.heliocore.block.hpc_custom.machine.electric;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import net.venera.heliocore.block.entity.machine.electric.FuelManagerEntity;
import net.venera.heliocore.block.entity.machine.electric.PCBFabricatorEntity;
import net.venera.heliocore.block.hpc_custom.machine.BaseMachineBlock;
import org.jetbrains.annotations.Nullable;

public class PCBFabricatorBlock extends BaseMachineBlock<PCBFabricatorEntity> {
    public static final MapCodec<PCBFabricatorBlock> CODEC = simpleCodec(PCBFabricatorBlock::new);
    public PCBFabricatorBlock(Properties properties) {
        this(properties, HpCBlockEntities.PCB_FABRICATOR_ENTITY);
    }
    public PCBFabricatorBlock(Properties properties, DeferredHolder<BlockEntityType<?>, BlockEntityType<PCBFabricatorEntity>> blockEntityType) {
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
