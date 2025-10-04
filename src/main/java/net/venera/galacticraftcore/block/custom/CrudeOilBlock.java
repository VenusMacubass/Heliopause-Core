package net.venera.galacticraftcore.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.venera.galacticraftcore.util.ModTags;

public class CrudeOilBlock extends LiquidBlock {
    public CrudeOilBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public void entityInside(BlockState blockState, Level world, BlockPos blockPos, Entity entity) {
        if (world.getBlockState(entity.blockPosition().offset(0, (int) Math.floor(entity.getEyeHeight(entity.getPose())), 0)).getFluidState().getType().is(ModTags.Fluids.OIL)) {
            if (entity instanceof LivingEntity living) {
                if (living instanceof Player player) {
                    if (player.isCreative()) {
                        return;
                    }
                }
                living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 3 * 20));
            }
        }
    }
}
