package net.venera.heliocore.block.hpc_custom.machine;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import net.venera.heliocore.block.entity.machine.MagneticAssemblyPlatformEntity;
import org.jetbrains.annotations.Nullable;

public class MagneticAssemblyPlatformBlock extends BaseMachineBlock<MagneticAssemblyPlatformEntity>{
    public static final MapCodec<MagneticAssemblyPlatformBlock> CODEC = simpleCodec(MagneticAssemblyPlatformBlock::new);

    public MagneticAssemblyPlatformBlock(BlockBehaviour.Properties properties) {
        super(properties, HpCBlockEntities.MAGNETIC_ASSEMBLY_PLATFORM_ENTITY);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof MagneticAssemblyPlatformEntity blockEntity) {
                blockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MagneticAssemblyPlatformEntity(blockPos, blockState);
    }
}
