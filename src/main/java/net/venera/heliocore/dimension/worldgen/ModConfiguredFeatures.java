package net.venera.heliocore.dimension.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.ModBlocks;
import net.venera.heliocore.dimension.worldgen.feature.CraterConfig;
import net.venera.heliocore.dimension.worldgen.feature.ModFeatures;
import net.venera.heliocore.util.ModTags;

import java.util.List;

public class ModConfiguredFeatures { //What to place
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_TIN_ORE_KEY = registerKey("overworld_tin_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_TIN_ORE_KEY = registerKey("nether_tin_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> END_TIN_ORE_KEY = registerKey("end_tin_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MOON_TIN_ORE_KEY = registerKey("moon_tin_ore");

    public static final ResourceKey<ConfiguredFeature<?, ?>> MARIA_CRATER_KEY = registerKey("maria_crater");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HIGHLANDS_CRATER_KEY = registerKey("highlands_crater");
    
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context){

        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest netherrackReplaceables = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endReplaceables = new BlockMatchTest(Blocks.END_STONE);
        RuleTest moonReplaceables = new TagMatchTest(ModTags.Blocks.MOON_STONE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldTinOres = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.TIN_ORE.get().defaultBlockState()));

        List<OreConfiguration.TargetBlockState> netherTinOres = List.of(
                OreConfiguration.target(netherrackReplaceables, ModBlocks.TIN_ORE.get().defaultBlockState()));

        List<OreConfiguration.TargetBlockState> endTinOres = List.of(
                OreConfiguration.target(endReplaceables, ModBlocks.TIN_ORE.get().defaultBlockState()));

        List<OreConfiguration.TargetBlockState> moonTinOres = List.of(
                OreConfiguration.target(moonReplaceables, ModBlocks.MOON_TIN_ORE.get().defaultBlockState()));
        
        register(context, OVERWORLD_TIN_ORE_KEY, Feature.ORE, new OreConfiguration(overworldTinOres, 12));
        register(context, NETHER_TIN_ORE_KEY, Feature.ORE, new OreConfiguration(netherTinOres, 12));
        register(context, END_TIN_ORE_KEY, Feature.ORE, new OreConfiguration(endTinOres, 12));
        register(context, MOON_TIN_ORE_KEY, Feature.ORE, new OreConfiguration(moonTinOres, 12));

        context.register(MARIA_CRATER_KEY, new ConfiguredFeature<>(
                ModFeatures.CRATER_FEATURE.get(),
                new CraterConfig(5, 25, 5.0) 
        ));

        context.register(HIGHLANDS_CRATER_KEY, new ConfiguredFeature<>(
                ModFeatures.CRATER_FEATURE.get(),
                new CraterConfig(3, 15, 2.0) 
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
