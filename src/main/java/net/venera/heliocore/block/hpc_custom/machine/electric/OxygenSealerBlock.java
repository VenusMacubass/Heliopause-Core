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
import net.venera.heliocore.block.entity.machine.electric.OxygenSealerEntity;
import net.venera.heliocore.block.hpc_custom.machine.BaseMachineBlock;
import org.jetbrains.annotations.Nullable;

public class OxygenSealerBlock extends BaseMachineBlock<OxygenSealerEntity> {
    public static final MapCodec<OxygenSealerBlock> CODEC = simpleCodec(OxygenSealerBlock::new);
    public OxygenSealerBlock(Properties properties) {
        this(properties, HpCBlockEntities.OXYGEN_SEALER_ENTITY);
    }

    public OxygenSealerBlock(Properties properties, DeferredHolder<BlockEntityType<?>, BlockEntityType<OxygenSealerEntity>> blockEntityType) {
        super(properties, blockEntityType);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return this.blockEntityType.get().create(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) return null;

        return createTickerHelper(blockEntityType, this.blockEntityType.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState));
    }
}
