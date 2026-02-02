package net.venera.galacticraftcore.block.custom.machine.electric;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.phys.BlockHitResult;
import net.venera.galacticraftcore.block.custom.machine.BaseMachineBlock;
import net.venera.galacticraftcore.block.entity.ModBlockEntities;
import net.venera.galacticraftcore.block.entity.machine.electric.RefineryEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class RefineryBlock extends BaseMachineBlock<RefineryEntity> {
    public static final MapCodec<RefineryBlock> CODEC = simpleCodec(RefineryBlock::new);
    public RefineryBlock(Properties properties) {
        this(properties, ModBlockEntities.REFINERY_ENTITY);
    }
    protected RefineryBlock(Properties properties, Supplier<BlockEntityType<RefineryEntity>> type) {
        super(properties, type);
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
