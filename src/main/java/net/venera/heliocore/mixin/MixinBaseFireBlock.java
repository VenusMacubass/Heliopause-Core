package net.venera.heliocore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.heliocore.data.atmospherics.OxygenVolumeHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public class MixinBaseFireBlock {

    @Inject(method = "onPlace", at = @At("HEAD"), cancellable = true)
    private void heliocore$extinguishOnPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving, CallbackInfo ci) {
        if (!level.isClientSide() && OxygenVolumeHelper.isVacuumDimension(level)) {

            if (!OxygenVolumeHelper.isPositionSealed(pos.asLong())) {
                level.removeBlock(pos, false);
                ci.cancel();
            }
        }
    }
}