package net.venera.galacticraftcore.data.radiation;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.data.ModAttachments;

@EventBusSubscriber(modid = GalacticraftCore.MOD_ID)
public class RadiationHandler {
    private final double EARTH_BG_RAD = 0.27;
    private final double NETHER_BG_RAD = 0.23;
    private final double END_BG_RAD = 0.47;
    private final double MOON_DAY_BG_RAD = 54;
    private final double MOON_NIGHT_BG_RAD = 46;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (player.tickCount % 20 != 0) return;
        RadiationData radiationData = player.getData(ModAttachments.RADIATION_DATA);
        radiationData.addRadiation(1f);



        applyRadiationEffects(player, radiationData);
        player.displayClientMessage(Component.literal("Radiation level:" + radiationData.getRadiation()), false);
    }


    private static void applyRadiationEffects(Player player, RadiationData radData) {
        double radLevel = radData.getRadiation();
            if (radLevel > 200) {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 2000, radLevel > 600 ? 2 : 1));
            }
            if (radLevel > 400) {
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 2000, 1));
                player.hurt(new DamageSources(player.level().registryAccess()).source(DamageTypes.MAGIC), radLevel > 600 ? 2 : 1);
            }
            if (radLevel > 600) {
                player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 2000, 1));
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 2000, 1));
            } else {
            if (radLevel > 800) {
                player.hurt( new DamageSources(player.level().registryAccess()).source(DamageTypes.MAGIC), (float)(radLevel-800)*0.1f);
            }
        }
    }
}

























