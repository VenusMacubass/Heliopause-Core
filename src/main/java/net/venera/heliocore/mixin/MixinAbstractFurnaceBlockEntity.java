package net.venera.heliocore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.venera.heliocore.data.atmospherics.OxygenVolumeHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public class MixinAbstractFurnaceBlockEntity {

    @Inject(method = "serverTick", at = @At("HEAD"), cancellable = true)
    private static void heliocore$stopFurnaceInVacuum(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        if (!level.isClientSide() && OxygenVolumeHelper.isVacuumDimension(level)) {
            boolean hasOxygen = OxygenVolumeHelper.isPositionSealed(pos.asLong()) ||
                    (OxygenVolumeHelper.getSealerForWall(pos.asLong()) != null);

            if (!hasOxygen) {
                if (state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT)) {
                    level.setBlock(pos, state.setValue(BlockStateProperties.LIT, false), 3);
                }
                ci.cancel();
            } else {
                int currentLitTime = ((FurnaceAccessor) blockEntity).getLitTime();

                if (currentLitTime > 0 && state.hasProperty(BlockStateProperties.LIT) && !state.getValue(BlockStateProperties.LIT)) {
                    level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 3);
                }
            }
        }
    }
}
