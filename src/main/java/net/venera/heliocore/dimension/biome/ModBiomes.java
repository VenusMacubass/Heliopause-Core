package net.venera.heliocore.dimension.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.venera.heliocore.HeliopauseCore;

public class ModBiomes {
    public static final ResourceKey<Biome> LUNAR_HIGHLANDS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "lunar_highlands"));
    public static final ResourceKey<Biome> LUNAR_MARIA = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "lunar_maria"));
    
    public static void bootstrap(BootstrapContext<Biome> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var configuredCarvers = context.lookup(Registries.CONFIGURED_CARVER);
        
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();

        // 2. Generation Rules (Ores and Trees)
        // We leave this mostly empty because your BiomeModifiers automatically inject the ores for us!
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatures, configuredCarvers);

        // 3. The Biome Definition
        context.register(LUNAR_HIGHLANDS , new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5f)
                .downfall(0.0f)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .skyColor(0) // Pitch Black
                        .fogColor(0)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .grassColorOverride(8368696)
                        .foliageColorOverride(8368696)
                        .ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build());
        
        context.register(LUNAR_MARIA , new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5f)
                .downfall(0.0f)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .skyColor(0) // Pitch Black
                        .fogColor(0)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .grassColorOverride(8368696)
                        .foliageColorOverride(8368696)
                        .ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build());
    }
}
