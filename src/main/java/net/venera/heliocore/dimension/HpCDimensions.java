package net.venera.heliocore.dimension;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.dimension.biome.HpCBiomes;
import net.venera.heliocore.dimension.worldgen.HpCNoiseSettings;

import java.util.List;
import java.util.OptionalLong;

public class HpCDimensions {
    
    public static final ResourceKey<Level> MOON_LEVEL_KEY = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon")
    );
    
    public static final ResourceKey<DimensionType> MOON_TYPE_KEY = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon")
    );

    public static final ResourceKey<LevelStem> MOON_STEM_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon"));

    public static void bootstrap(BootstrapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);

        MultiNoiseBiomeSource moonBiomeSource = MultiNoiseBiomeSource.createFromList(
                new Climate.ParameterList<>(List.of(
                        // LUNAR MARIA (High Erosion = Flat)
                        Pair.of(Climate.parameters(
                                Climate.Parameter.span(-1.0F, 1.0F), // Temperature 
                                Climate.Parameter.span(-1.0F, 1.0F), // Humidity 
                                Climate.Parameter.span(-1.0F, 1.0F), // Continentalness 
                                Climate.Parameter.point(0.8F),       // Erosion 
                                Climate.Parameter.span(-1.0F, 1.0F), // Depth 
                                Climate.Parameter.span(-1.0F, 1.0F), // Weirdness 
                                0.0F
                        ), biomeRegistry.getOrThrow(HpCBiomes.LUNAR_MARIA)), 

                        // LUNAR HIGHLANDS (Low Erosion = Jagged)
                        Pair.of(Climate.parameters(
                                Climate.Parameter.span(-1.0F, 1.0F), // Temperature 
                                Climate.Parameter.span(-1.0F, 1.0F), // Humidity 
                                Climate.Parameter.span(-1.0F, 1.0F), // Continentalness 
                                Climate.Parameter.point(-0.8F),      // Erosion
                                Climate.Parameter.span(-1.0F, 1.0F), // Depth 
                                Climate.Parameter.span(-1.0F, 1.0F), // Weirdness 
                                0.0F
                        ), biomeRegistry.getOrThrow(HpCBiomes.LUNAR_HIGHLANDS)) 
                ))
        );

        LevelStem moonStem = new LevelStem(
                dimTypes.getOrThrow(HpCDimensions.MOON_TYPE_KEY),
                new NoiseBasedChunkGenerator(moonBiomeSource, noiseSettings.getOrThrow(HpCNoiseSettings.MOON_NOISE_SETTINGS_KEY))
        );

        context.register(MOON_STEM_KEY, moonStem);
    }
    
    public static void typeBootstrap(BootstrapContext<DimensionType> context) {
        context.register(MOON_TYPE_KEY, new DimensionType(
                OptionalLong.empty(), // fixedTime
                true,                 // hasSkylight 
                false,                // hasCeiling
                false,                // ultraWarm
                false,                // natural
                4.0,                  // coordinateScale
                true,                 // bedWorks
                true,                 // respawnAnchorWorks
                -64,                  // minY
                320,                  // height
                320,                  // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, 
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon"), 
                0.0f,                 // ambientLight 
                new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)
        ));
    }
}
