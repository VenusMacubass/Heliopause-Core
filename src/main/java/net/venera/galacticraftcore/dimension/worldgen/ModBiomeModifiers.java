package net.venera.galacticraftcore.dimension.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.dimension.biome.ModBiomes;
import net.venera.galacticraftcore.util.ModTags;

public class ModBiomeModifiers {  //In what biome and in what stage shall I place?
    //CF -> PF ->  BM
    public static final ResourceKey<BiomeModifier> ADD_TIN_ORE = registerKey("add_tin_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_TIN_ORE = registerKey("add_nether_tin_ore");
    public static final ResourceKey<BiomeModifier> ADD_END_TIN_ORE = registerKey("add_end_tin_ore");
    public static final ResourceKey<BiomeModifier> ADD_MOON_TIN_ORE = registerKey("add_moon_tin_ore");

    public static final ResourceKey<BiomeModifier> ADD_MOON_CRATERS = registerKey("add_moon_craters");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        HolderSet<Biome> moonBiomes = HolderSet.direct(
                biomes.getOrThrow(ModBiomes.LUNAR_MARIA),
                biomes.getOrThrow(ModBiomes.LUNAR_HIGHLANDS)
        );

        context.register(ADD_TIN_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.TIN_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_NETHER_TIN_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.NETHER_TIN_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_END_TIN_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_END),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.END_TIN_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));


        context.register(ADD_MOON_TIN_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                moonBiomes,
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MOON_TIN_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));



        context.register(ADD_MOON_CRATERS, new BiomeModifiers.AddFeaturesBiomeModifier(
                // Notice we only use the LUNAR_HIGHLANDS here, NOT a combined HolderSet!
                HolderSet.direct(biomes.getOrThrow(ModBiomes.LUNAR_HIGHLANDS)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MOON_CRATER_PLACED_KEY)),
                // SURFACE_STRUCTURES runs after the terrain is shaped, but before ores generate
                GenerationStep.Decoration.SURFACE_STRUCTURES
        ));
    }
    
    private static ResourceKey<BiomeModifier> registerKey(String name){
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, name));
    }
}
