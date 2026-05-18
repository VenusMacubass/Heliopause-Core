package net.venera.heliocore.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LaunchPlatformEntity extends BlockEntity {
    public LaunchPlatformEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LAUNCH_PLATFORM_ENTITY.get(), pos, state);
    }
}