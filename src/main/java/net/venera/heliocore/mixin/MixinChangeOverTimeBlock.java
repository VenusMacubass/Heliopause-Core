package net.venera.heliocore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.heliocore.HeliopauseCore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChangeOverTimeBlock.class)
public interface MixinChangeOverTimeBlock {
    @Inject(method = "changeOverTime", at = @At("HEAD"), cancellable = true)
    default void heliocore$stopMoonRust(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (level.dimension().location().getNamespace().equals(HeliopauseCore.MOD_ID) &&
                level.dimension().location().getPath().equals("moon")) {
            ci.cancel(); 
        }
    }
}