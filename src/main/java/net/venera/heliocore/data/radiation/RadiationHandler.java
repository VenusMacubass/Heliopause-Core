package net.venera.heliocore.data.radiation;

import it.unimi.dsi.fastutil.ints.Int2FloatAVLTreeMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.dimension.HpCDimensions;
import net.venera.heliocore.item.HpCTags;
import net.venera.heliocore.util.SyncRadiationPayload;

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
        RadiationData radiationData = aliveEntity.getData(HpCAttachments.RADIATION_DATA);
        
        radiationChange(aliveEntity, radiationData);
        applyRadiationEffects(aliveEntity, radiationData);
        if (aliveEntity.tickCount % (radiationData.getRadiation() >= 400 ? 40:100) == 0) {
            applyRadiationDamage(aliveEntity, radiationData);
        }
        if (aliveEntity instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new SyncRadiationPayload(radiationData.getRadiation()));
        }
    }

    private static void radiationChange(LivingEntity entity, RadiationData radiationData) {
        double radLevel = radiationData.getRadiation();
        var dim = entity.level().dimension();

        double difficulty = switch (entity.level().getDifficulty()) {
            case EASY -> 0.007f;
            case NORMAL -> 0.01f;
            case HARD -> 0.013f;
            default -> 0f;
        };
        
        double targetBgRad;
        if (dim.equals(Level.NETHER)) targetBgRad = NETHER_BG_RAD;
        else if (dim.equals(Level.END)) targetBgRad = END_BG_RAD;
        else if (dim.equals(HpCDimensions.MOON_LEVEL_KEY)) targetBgRad = MOON_BG_RAD;
        else targetBgRad = EARTH_BG_RAD;

        boolean isGaining = targetBgRad > radLevel;
        double amount = Math.abs(targetBgRad - Math.min(radLevel, 100)) * difficulty;
        int limit = getSuitProtectionLevel(entity);
        radiationData.changeRadiation(amount, isGaining, limit);
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
        }
        if (radLevel > 800) {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 1200, 0));
        }
    }

    private static void applyRadiationDamage(LivingEntity entity, RadiationData radData) {
        if(entity instanceof Player player && player.isCreative()){return;}
        double radLevel = radData.getRadiation();
        var damageSource = entity.damageSources().magic();
        if (radLevel > 200) {
            entity.hurt(damageSource, radLevel > 600 ? 2f : 1f);
        }
        if (radLevel > 800) {
            entity.hurt(damageSource, 2f);
        }
        if (radLevel > 900) {
            entity.hurt(damageSource, (float)(radLevel-900)*0.3f);
        }
    }

    public static int getSuitProtectionLevel(LivingEntity entity) {
        int protection = 0;
        for(ItemStack stack : entity.getArmorSlots()){
            if(stack.is(HpCTags.Items.T3_RADIATION_PROTECTORS)) protection += 3;
            else if(stack.is(HpCTags.Items.T2_RADIATION_PROTECTORS)) protection += 2;
            else if(stack.is(HpCTags.Items.T1_RADIATION_PROTECTORS)) protection += 1;
        }

        if (protection >= 12) return 200; // Full T3
        if (protection >= 8)  return 400; // Full T2
        if (protection >= 4)  return 800; // Full T1
        return 1000; 
    }
}
