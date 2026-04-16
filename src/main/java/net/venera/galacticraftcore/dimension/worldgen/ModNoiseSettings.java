package net.venera.galacticraftcore.dimension.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.venera.galacticraftcore.GalacticraftCore;

public class ModNoiseSettings {
    public static final ResourceKey<NoiseGeneratorSettings> MOON_NOISE_SETTINGS_KEY = ResourceKey.create(Registries.NOISE_SETTINGS,
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "moon_terrain"));

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
       
    }
}
