package net.venera.heliocore.dimension.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.dimension.biome.HpCBiomes;
import net.venera.heliocore.util.HpCTags;

public class HpCBiomeModifiers {  //In what biome and in what stage shall I place?
    //CF -> PF ->  BM
    public static final ResourceKey<BiomeModifier> ADD_TIN_ORE = registerKey("add_tin_ore");
    public static final ResourceKey<BiomeModifier> ADD_ALUMINIUM_ORE = registerKey("add_aluminium_ore");
    public static final ResourceKey<BiomeModifier> ADD_SILICON_ORE = registerKey("add_silicon_ore");
    public static final ResourceKey<BiomeModifier> ADD_IRIDIUM_ORE = registerKey("add_iridium_ore");
    
    public static final ResourceKey<BiomeModifier> ADD_MOON_TEKTITES = registerKey("add_moon_tektites");
    public static final ResourceKey<BiomeModifier> ADD_MOON_TIN_ORE = registerKey("add_moon_tin_ore");
    public static final ResourceKey<BiomeModifier> ADD_MOON_IRON_ORE = registerKey("add_moon_iron_ore");
    public static final ResourceKey<BiomeModifier> ADD_MOON_COPPER_ORE = registerKey("add_moon_copper_ore");
    public static final ResourceKey<BiomeModifier> ADD_MOON_ALUMINIUM_ORE = registerKey("add_moon_aluminium_ore");
    public static final ResourceKey<BiomeModifier> ADD_MOON_SILICON_ORE = registerKey("add_moon_silicon_ore");
    public static final ResourceKey<BiomeModifier> ADD_MOON_IRIDIUM_ORE = registerKey("add_moon_iridium_ore");

    public static final ResourceKey<BiomeModifier> ADD_CRUDE_OIL_DEFAULT = registerKey("add_crude_oil_default");
    public static final ResourceKey<BiomeModifier> ADD_CRUDE_OIL_ABUNDANT = registerKey("add_crude_oil_abundant");
    
    public static final ResourceKey<BiomeModifier> ADD_MARIA_CRATERS = registerKey("add_maria_craters");
    public static final ResourceKey<BiomeModifier> ADD_HIGHLANDS_CRATERS = registerKey("add_highlands_craters");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        HolderSet<Biome> moonBiomes = HolderSet.direct(
                biomes.getOrThrow(HpCBiomes.LUNAR_MARIA),
                biomes.getOrThrow(HpCBiomes.LUNAR_HIGHLANDS)
        );

        context.register(ADD_TIN_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.TIN_ORE_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_ALUMINIUM_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.ALUMINIUM_ORE_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_SILICON_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.SILICON_ORE_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_IRIDIUM_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.IRIDIUM_ORE_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_MOON_TEKTITES, new BiomeModifiers.AddFeaturesBiomeModifier(
                moonBiomes,
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.MOON_TEKTITE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_MOON_TIN_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                moonBiomes,
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.MOON_TIN_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_MOON_IRON_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                moonBiomes, 
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.MOON_IRON_ORE_PLACED_KEY)), 
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_MOON_COPPER_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                moonBiomes, 
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.MOON_COPPER_ORE_PLACED_KEY)), 
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_MOON_ALUMINIUM_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                moonBiomes, HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.MOON_ALUMINIUM_ORE_PLACED_KEY)), 
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_MOON_SILICON_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                moonBiomes, HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.MOON_SILICON_ORE_PLACED_KEY)), 
                GenerationStep.Decoration.UNDERGROUND_ORES));
        context.register(ADD_MOON_IRIDIUM_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                moonBiomes, HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.MOON_IRIDIUM_ORE_PLACED_KEY)), 
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_CRUDE_OIL_DEFAULT, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.CRUDE_OIL_PLACED_DEFAULT)),
                GenerationStep.Decoration.LAKES
        ));
        context.register(ADD_CRUDE_OIL_ABUNDANT, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(HpCTags.Biomes.HAS_ABUNDANT_OIL),
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.CRUDE_OIL_PLACED_ABUNDANT)),
                GenerationStep.Decoration.LAKES
        ));

        context.register(ADD_MARIA_CRATERS, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(HpCBiomes.LUNAR_MARIA)),
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.MARIA_CRATER_PLACED_KEY)),
                GenerationStep.Decoration.SURFACE_STRUCTURES
        ));

        context.register(ADD_HIGHLANDS_CRATERS, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(HpCBiomes.LUNAR_HIGHLANDS)),
                HolderSet.direct(placedFeatures.getOrThrow(HpCPlacedFeatures.HIGHLANDS_CRATER_PLACED_KEY)),
                GenerationStep.Decoration.SURFACE_STRUCTURES
        ));
    }
    
    private static ResourceKey<BiomeModifier> registerKey(String name){
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, name));
    }
}
