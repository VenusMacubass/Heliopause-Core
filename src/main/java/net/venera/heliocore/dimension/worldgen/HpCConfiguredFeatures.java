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
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_TEKTITE_KEY = registerKey("moon_tektite");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MARIA_CRATER_KEY = registerKey("maria_crater");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HIGHLANDS_CRATER_KEY = registerKey("highlands_crater");

    public static final ResourceKey<ConfiguredFeature<?, ?>> CRUDE_OIL_RESERVOIR = registerKey("crude_oil_lake");
    //endregion
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context){

        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest moonDirtReplaceables = new TagMatchTest(HpCTags.Blocks.MOON_DIRT_REPLACEABLES);
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

        List<OreConfiguration.TargetBlockState> moonTektiteOres = List.of(
                OreConfiguration.target(moonDirtReplaceables, HpCBlocks.MOON_TEKTITES.get().defaultBlockState()));
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
                        new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), 
                        HpCFluids.CRUDE_OIL.getFluidblock().defaultBlockState()
                )
        );
        
        
        
        //region Registry
        register(context, OVERWORLD_TIN_ORE_KEY, Feature.ORE, new OreConfiguration(overworldTinOres, 10));
        register(context, OVERWORLD_ALUMINIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldAluminiumOres, 10));
        register(context, OVERWORLD_SILICON_ORE_KEY, Feature.ORE, new OreConfiguration(overworldSiliconOres, 2));
        register(context, OVERWORLD_IRIDIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldIridiumOres, 2));
        
        register(context, MOON_TEKTITE_KEY, Feature.ORE, new OreConfiguration(moonTektiteOres, 8));
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
        
        context.register(MARIA_CRATER_KEY, new ConfiguredFeature<>(
                HpCFeatures.CRATER_FEATURE.get(),
                new CraterConfig(5, 20, 5.0) 
        ));

        context.register(HIGHLANDS_CRATER_KEY, new ConfiguredFeature<>(
                HpCFeatures.CRATER_FEATURE.get(),
                new CraterConfig(3, 12, 2.0) 
        ));
    }
    
    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, name));
    }
    
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<? , ?>> key,  F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
