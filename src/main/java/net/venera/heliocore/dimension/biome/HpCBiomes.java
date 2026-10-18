package net.venera.heliocore.dimension.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.venera.heliocore.HeliopauseCore;

public class HpCBiomes {
    public static final ResourceKey<Biome> LUNAR_HIGHLANDS = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "lunar_highlands"));
    public static final ResourceKey<Biome> LUNAR_MARIA = ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "lunar_maria"));
    
    public static void bootstrap(BootstrapContext<Biome> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var configuredCarvers = context.lookup(Registries.CONFIGURED_CARVER);
        
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 95, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HUSK, 95, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 100, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITHER_SKELETON, 100, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CAVE_SPIDER, 100, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4));
        
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatures, configuredCarvers);
        
        context.register(LUNAR_HIGHLANDS , new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5f)
                .downfall(0.0f)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .skyColor(0) 
                        .fogColor(0)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .grassColorOverride(0xFFFFFF)
                        .foliageColorOverride(8368696)
//                        .ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
//                        .ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, 0.0111))
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build());
        
        context.register(LUNAR_MARIA , new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5f)
                .downfall(0.0f)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .skyColor(0) 
                        .fogColor(0)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .grassColorOverride(0xCFCFCF)
                        .foliageColorOverride(8368696)
//                        .ambientLoopSound(SoundEvents.AMBIENT_NETHER_WASTES_LOOP)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
//                        .ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111))
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build());
    }
}
