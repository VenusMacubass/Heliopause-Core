package net.venera.heliocore.data.radiation;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.ModAttachments;
import net.venera.heliocore.dimension.ModDimensions;

@EventBusSubscriber(modid = HeliopauseCore.MOD_ID)
public class RadiationHandler {
    private static final double EARTH_BG_RAD = 0.27;
    private static final double NETHER_BG_RAD = 0.23;
    private static final double END_BG_RAD = 0.48;
    private static final double MOON_BG_RAD = 54;

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if(!(event.getEntity() instanceof LivingEntity aliveEntity)){return;}
        if (aliveEntity.level().isClientSide || aliveEntity.tickCount % 20 != 0) {return;}
        if (!aliveEntity.hasData(ModAttachments.RADIATION_DATA)) {return;}
        RadiationData radiationData = aliveEntity.getData(ModAttachments.RADIATION_DATA);


        radiationChange(aliveEntity, radiationData);
        applyRadiationEffects(aliveEntity, radiationData);
        if (aliveEntity.tickCount % (radiationData.getRadiation() >= 400 ? 40:100) != 0) {
            applyRadiationDamage(aliveEntity, radiationData);
        }
        if(aliveEntity instanceof Player player){
            radiationData.setRadiation(0);
        
        }
    }

    private static void radiationChange(Entity entity, RadiationData radiationData) {
        double radLevel = radiationData.getRadiation();
        var dim = entity.level().dimension();
        double difficulty = switch (entity.level().getDifficulty()) {
            case PEACEFUL -> 0f;
            case EASY -> 0.007f;
            case NORMAL -> 0.01f;
            case HARD -> 0.013f;
            default -> 0f;
        };
        if (dim.equals(Level.OVERWORLD)) {
            radiationData.changeRadiation(Math.abs(EARTH_BG_RAD - Math.min(radLevel, 100)) * difficulty, EARTH_BG_RAD > radLevel);
        } else if (dim.equals(Level.NETHER)) {
            radiationData.changeRadiation(Math.abs(NETHER_BG_RAD - Math.min(radLevel, 100)) * difficulty, NETHER_BG_RAD > radLevel);
        } else if (dim.equals(Level.END)) {
            radiationData.changeRadiation(Math.abs(END_BG_RAD - Math.min(radLevel, 100)) * difficulty,  END_BG_RAD > radLevel);
        }
        else if (dim.equals(ModDimensions.MOON_LEVEL_KEY)) {
            radiationData.changeRadiation(Math.abs(MOON_BG_RAD - Math.min(radLevel, 100)) * difficulty,  MOON_BG_RAD > radLevel);
        } else {
            throw new IllegalStateException("Unexpected value: " + dim);
        }
    }

    private static void applyRadiationEffects(LivingEntity entity, RadiationData radData) {
        if(entity instanceof Player player && player.isCreative()){return;}
        double radLevel = radData.getRadiation();
        if (radLevel > 150) {
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, radLevel > 600 ? 2 : 1));
        }
        if (radLevel > 400) {
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1200, 1));
        }
        if (radLevel > 600) {
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1200, 0));
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 1200, 0));
        }
        if (radLevel > 800) {

        }
    }

    private static void applyRadiationDamage(LivingEntity entity, RadiationData radData) {
        if(entity instanceof Player player && player.isCreative()){return;}
        double radLevel = radData.getRadiation();
        var damageSource = entity.damageSources().magic();
        if (radLevel > 200) {
            entity.hurt(damageSource, radLevel > 600 ? 2f : 1f);
        }
//        if (radLevel > 400) {
//
//        }
        if (radLevel > 800) {
            entity.hurt(damageSource, 2f);
        }
        if (radLevel > 900) {
            entity.hurt(damageSource, (float)(radLevel-900)*0.3f);
        }
    }
}

























