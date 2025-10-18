package net.venera.galacticraftcore.data.radiation;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.data.ModAttachments;

import java.awt.*;

@EventBusSubscriber(modid = GalacticraftCore.MOD_ID)
public class RadiationHandler {
    private static final double EARTH_BG_RAD = 0.27;
    private static final double NETHER_BG_RAD = 0.23;
    private static final double END_BG_RAD = 0.47;
    private static final double MOON_DAY_BG_RAD = 54;
    private static final double MOON_NIGHT_BG_RAD = 46;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide || player.tickCount % 20 != 0) {return;}
        RadiationData radiationData = player.getData(ModAttachments.RADIATION_DATA);
        //radiationData.setRadiation(0);



        radiationChange(player, radiationData);
        applyRadiationEffects(player, radiationData);
        GalacticraftCore.LOGGER.info("Radiation level:" + radiationData.getRadiation());
    }

    private static void radiationChange(Player player, RadiationData radiationData) {
        double radLevel = radiationData.getRadiation();
        var dim = player.level().dimension();
        double difficulty = 0.01f;
        switch (player.level().getDifficulty()) {
            case PEACEFUL:
                difficulty = 0f;
                break;
            case EASY:
                difficulty = 0.007f;
                break;
            case NORMAL:
                difficulty = 0.01f;
                break;
            case HARD:
                difficulty = 0.013f;
                break;
        }
        if (dim.equals(Level.OVERWORLD)) {
            radiationData.addRadiation(Math.abs(EARTH_BG_RAD - radLevel) * difficulty);
        } else if (dim.equals(Level.NETHER)) {
            radiationData.addRadiation(Math.abs(NETHER_BG_RAD - radLevel) * difficulty);
        } else if (dim.equals(Level.END)) {
            radiationData.addRadiation(Math.abs(END_BG_RAD - radLevel) * difficulty);
        } else {
            throw new IllegalStateException("Unexpected value: " + dim);
        }

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
        }
        if (radLevel > 800) {
            player.hurt( new DamageSources(player.level().registryAccess()).source(DamageTypes.MAGIC), (float)(radLevel-800)*0.1f);

        }
    }
}

























