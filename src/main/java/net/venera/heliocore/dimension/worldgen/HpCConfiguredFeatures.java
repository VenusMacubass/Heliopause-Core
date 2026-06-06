package net.venera.heliocore.dimension.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.dimension.worldgen.feature.CraterConfig;
import net.venera.heliocore.dimension.worldgen.feature.HpCFeatures;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.util.HpCTags;

import java.util.List;

public class HpCConfiguredFeatures { //What to place
    
    //region Resource Keys
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_TIN_ORE_KEY = registerKey("overworld_tin_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ALUMINIUM_ORE_KEY = registerKey("overworld_aluminium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_SILICON_ORE_KEY = registerKey("overworld_silicon_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_IRIDIUM_ORE_KEY = registerKey("overworld_iridium_ore");
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_TIN_ORE_KEY = registerKey("moon_tin_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_ALUMINIUM_ORE_KEY = registerKey("moon_aluminium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_SILICON_ORE_KEY = registerKey("moon_silicon_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_IRIDIUM_ORE_KEY = registerKey("moon_iridium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_IRON_ORE_KEY = registerKey("moon_iron_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_COPPER_ORE_KEY = registerKey("moon_copper_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MARIA_CRATER_KEY = registerKey("maria_crater");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HIGHLANDS_CRATER_KEY = registerKey("highlands_crater");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CRUDE_OIL_RESERVOIR = registerKey("crude_oil_lake");
    //endregion
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context){

        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
//        RuleTest netherrackReplaceables = new BlockMatchTest(Blocks.NETHERRACK);
//        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE);
        RuleTest moonReplaceables = new TagMatchTest(HpCTags.Blocks.MOON_STONE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldTinOres = List.of(
                OreConfiguration.target(stoneReplaceables, HpCBlocks.TIN_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, HpCBlocks.DEEPSLATE_TIN_ORE.get().defaultBlockState()));
        
        List<OreConfiguration.TargetBlockState> overworldAluminiumOres = List.of(
                OreConfiguration.target(stoneReplaceables, HpCBlocks.ALUMINIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, HpCBlocks.DEEPSLATE_ALUMINIUM_ORE.get().defaultBlockState())); 
        
        List<OreConfiguration.TargetBlockState> overworldSiliconOres = List.of(
                OreConfiguration.target(stoneReplaceables, HpCBlocks.SILICON_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, HpCBlocks.DEEPSLATE_SILICON_ORE.get().defaultBlockState()));
        
        List<OreConfiguration.TargetBlockState> overworldIridiumOres = List.of(
                OreConfiguration.target(stoneReplaceables, HpCBlocks.IRIDIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, HpCBlocks.DEEPSLATE_IRIDIUM_ORE.get().defaultBlockState()));
        
//        List<OreConfiguration.TargetBlockState> endTinOres = List.of(
//                OreConfiguration.target(endReplaceables, ModBlocks.TIN_ORE.get().defaultBlockState()));

        List<OreConfiguration.TargetBlockState> moonTinOres = List.of(
                OreConfiguration.target(moonReplaceables, HpCBlocks.MOON_TIN_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> moonIronOres = List.of(
                OreConfiguration.target(moonReplaceables, HpCBlocks.MOON_IRON_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> moonCopperOres = List.of(
                OreConfiguration.target(moonReplaceables, HpCBlocks.MOON_COPPER_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> moonAluminiumOres = List.of(
                OreConfiguration.target(moonReplaceables, HpCBlocks.MOON_ALUMINIUM_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> moonSiliconOres = List.of(
                OreConfiguration.target(moonReplaceables, HpCBlocks.MOON_SILICON_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> moonIridiumOres = List.of(
                OreConfiguration.target(moonReplaceables, HpCBlocks.MOON_IRIDIUM_ORE.get().defaultBlockState()));


        List<OreConfiguration.TargetBlockState> deepslateOilTargets = List.of(
                OreConfiguration.target(
                        new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), // Safely targets deepslate and tuff
                        HpCFluids.CRUDE_OIL.getFluidblock().defaultBlockState()
                )
        );
        
        
        
        //region Registry
        register(context, OVERWORLD_TIN_ORE_KEY, Feature.ORE, new OreConfiguration(overworldTinOres, 10));
        register(context, OVERWORLD_ALUMINIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldAluminiumOres, 10));
        register(context, OVERWORLD_SILICON_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSiliconOres, 2));
        register(context, OVERWORLD_IRIDIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldIridiumOres, 2));
        
        
        register(context, MOON_TIN_ORE_KEY, Feature.ORE, new OreConfiguration(moonTinOres, 10));
        register(context, MOON_IRON_ORE_KEY, Feature.ORE, new OreConfiguration(moonIronOres, 7)); // Slightly smaller than Earth Iron
        register(context, MOON_COPPER_ORE_KEY, Feature.ORE, new OreConfiguration(moonCopperOres, 9)); // Standard copper vein size
        register(context, MOON_ALUMINIUM_ORE_KEY, Feature.ORE, new OreConfiguration(moonAluminiumOres, 8)); // Abundant veins
        register(context, MOON_SILICON_ORE_KEY, Feature.ORE, new OreConfiguration(moonSiliconOres, 4)); // Small veins
        register(context, MOON_IRIDIUM_ORE_KEY, Feature.ORE, new OreConfiguration(moonIridiumOres, 3)); // Slightly larger than Earth Iridium

        context.register(CRUDE_OIL_RESERVOIR, new ConfiguredFeature<>(
                Feature.LAKE,
                new LakeFeature.Configuration(
                        BlockStateProvider.simple(HpCFluids.CRUDE_OIL.getFluidblock()),
                        BlockStateProvider.simple(Blocks.DEEPSLATE)
                )
        ));
        
        //endregion

//        context.register(CRUDE_OIL_RESERVOIR, new ConfiguredFeature<>(
//                Feature.GEODE,
//                new GeodeConfiguration(
//                        new GeodeBlockSettings(
//                                BlockStateProvider.simple(HpCFluids.CRUDE_OIL.getFluidblock()), // 1. Filling
//                                BlockStateProvider.simple(HpCFluids.CRUDE_OIL.getFluidblock()), // 2. Inner
//                                BlockStateProvider.simple(HpCFluids.CRUDE_OIL.getFluidblock()), // 3. Alternate
//                                BlockStateProvider.simple(HpCFluids.CRUDE_OIL.getFluidblock()), // 4. Middle
//                                BlockStateProvider.simple(HpCFluids.CRUDE_OIL.getFluidblock()), // 5. Outer
//                                List.of(HpCFluids.CRUDE_OIL.getFluidblock().defaultBlockState()),
//                                BlockTags.FEATURES_CANNOT_REPLACE,
//                                BlockTags.GEODE_INVALID_BLOCKS
//                        ),
//
//                        // 2. STANDARD RADII: This gives the engine the numerical space to draw curves.
//                        new GeodeLayerSettings(10D, 20D, 20D, 20D),
//
//                        new GeodeCrackSettings(
//                                0.001D,
//                                0.1D,
//                                1
//                        ),
//
//                        // 3. THE SHAPE: Wide, Flat, and Organic
//                        0.35D,                 // Noise multiplier
//                        0.08D,                 // Chance to use alternate inner layer
//                        true,                  // Use potential placements (smooths out the interior)
//                        UniformInt.of(5, 20),   // Outer wall distance (LARGE enough to curve organically!)
//                        UniformInt.of(2, 5),   // Number of blobs 
//                        UniformInt.of(4, 10),   // Point offset (Stretch them horizontally to make a wide lake)
//                        -4,                    // Min Gen Offset Y (Squash the spheres vertically)
//                        3,                     // Max Gen Offset Y (Guaranteeing a flat, low ceiling!)
//                        0.05D,                 // Noise correction
//                        1                      // Invalid blocks threshold
//                )
//        ));
        
        
        context.register(MARIA_CRATER_KEY, new ConfiguredFeature<>(
                HpCFeatures.CRATER_FEATURE.get(),
                new CraterConfig(5, 20, 5.0) 
        ));

        context.register(HIGHLANDS_CRATER_KEY, new ConfiguredFeature<>(
                HpCFeatures.CRATER_FEATURE.get(),
                new CraterConfig(3, 12, 2.0) 
        ));

//        context.register(CRUDE_OIL_RESERVOIR, new ConfiguredFeature<>(
//                Feature.REPLACE_BLOBS,
//                new ReplaceSphereConfiguration(
//                        Blocks.DEEPSLATE.defaultBlockState(),                          // 1. Target block to replace
//                        HpCFluids.CRUDE_OIL.getFluidblock().defaultBlockState(),   // 2. Your new block
//                        UniformInt.of(3, 12)                                        // 3. The radius of the blob
//                )
//        ));
    }
    
    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, name));
    }
    
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<? , ?>> key,  F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
