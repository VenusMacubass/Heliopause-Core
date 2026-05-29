package net.venera.heliocore.dimension.worldgen.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.heliocore.HeliopauseCore;

import java.util.function.Supplier;

public class HpCFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, HeliopauseCore.MOD_ID);

    public static final Supplier<Feature<CraterConfig>> CRATER_FEATURE = FEATURES.register("crater",
            () -> new CraterFeature(CraterConfig.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
