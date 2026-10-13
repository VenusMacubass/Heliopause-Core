package net.venera.heliocore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.entity.machine.electric.OxygenSealerEntity;
import net.venera.heliocore.data.atmospherics.AtmosphericProperty;
import net.venera.heliocore.data.atmospherics.OxygenVolumeHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlock.class)
public class MixinLiquidBlock {
    @Inject(method = "isRandomlyTicking", at = @At("RETURN"), cancellable = true)
    protected void heliocore$forceRandomTickOnMoon(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    protected void heliocore$moonAtmosphereTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {

        if (level.dimension().location().getNamespace().equals(HeliopauseCore.MOD_ID)) {
            long posLong = pos.asLong();
            boolean isSealed = OxygenVolumeHelper.isPositionSealed(posLong);
            boolean inRegulatedRoom = false;
            
            if (isSealed) {
                BlockPos sealerPos = OxygenVolumeHelper.getSealerForAir(posLong, level);
                if (sealerPos != null && level.getBlockEntity(sealerPos) instanceof OxygenSealerEntity sealer) {
                    inRegulatedRoom = sealer.isThermallyRegulating();
                }
            }
            
            if (inRegulatedRoom) {
                return;
            }

            FluidState fluidState = state.getFluidState();
            double temp = AtmosphericProperty.getEnvironmentalTemperature(level, level.getBiome(pos));

            if (fluidState.is(FluidTags.WATER)) {
                if (temp <= 0.0) {
                    level.setBlockAndUpdate(pos, Blocks.ICE.defaultBlockState());
                    level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else if (!isSealed || temp >= 100.0) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
                    level.sendParticles(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
                }
                ci.cancel();
            }

            else if (fluidState.is(FluidTags.LAVA)) {
                if (temp <= 0.0) {
                    BlockState newState = fluidState.isSource() ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState();
                    level.setBlockAndUpdate(pos, newState);
                    level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
                    level.sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
                    ci.cancel();
                }
            }
        }
    }
}
