package net.venera.heliocore.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.temperature.EnvironmentalTemperature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IceBlock.class)
public class MixinIceBlock {
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    protected void heliocore$moonIceMelt(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (level.dimension().location().getNamespace().equals(HeliopauseCore.MOD_ID)) {
            double temp = EnvironmentalTemperature.getEnvironmentalTemperature(level, level.getBiome(pos));

            if (temp >= 100.0) {
                // Above boiling point: Ice sublimates directly into vapor!
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
                level.sendParticles(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.2, 0.2, 0.2, 0.05);
            }
            else if (temp > 0.0) {
                // Above freezing, but below boiling: Melts into liquid water
                level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
                level.neighborChanged(pos, Blocks.WATER, pos);
            }
            ci.cancel();
        }
    }
}
