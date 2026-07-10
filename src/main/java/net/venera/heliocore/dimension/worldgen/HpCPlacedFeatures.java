package net.venera.heliocore.dimension.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.venera.heliocore.HeliopauseCore;

import java.util.List;

public class HpCPlacedFeatures {  //Where to place and how to place
    //region Keys
    public static final ResourceKey<PlacedFeature> TIN_ORE_KEY = registerKey("tin_ore_placed");
    public static final ResourceKey<PlacedFeature> ALUMINIUM_ORE_KEY = registerKey("aluminium_ore_placed");
    public static final ResourceKey<PlacedFeature> SILICON_ORE_KEY = registerKey("silicon_ore_placed");
    public static final ResourceKey<PlacedFeature> IRIDIUM_ORE_KEY = registerKey("iridium_ore_placed");
    
    public static final ResourceKey<PlacedFeature> MOON_TEKTITE_PLACED_KEY = registerKey("moon_tektite_placed");
    public static final ResourceKey<PlacedFeature> MOON_TIN_ORE_PLACED_KEY = registerKey("moon_tin_ore_placed");
    public static final ResourceKey<PlacedFeature> MOON_IRON_ORE_PLACED_KEY = registerKey("moon_iron_ore_placed");
    public static final ResourceKey<PlacedFeature> MOON_COPPER_ORE_PLACED_KEY = registerKey("moon_copper_ore_placed");
    public static final ResourceKey<PlacedFeature> MOON_ALUMINIUM_ORE_PLACED_KEY = registerKey("moon_aluminium_ore_placed");
    public static final ResourceKey<PlacedFeature> MOON_SILICON_ORE_PLACED_KEY = registerKey("moon_silicon_ore_placed");
    public static final ResourceKey<PlacedFeature> MOON_IRIDIUM_ORE_PLACED_KEY = registerKey("moon_iridium_ore_placed");

    public static final ResourceKey<PlacedFeature> MARIA_CRATER_PLACED_KEY = registerKey("maria_crater_placed");
    public static final ResourceKey<PlacedFeature> HIGHLANDS_CRATER_PLACED_KEY = registerKey("highlands_crater_placed");

    public static final ResourceKey<PlacedFeature> CRUDE_OIL_PLACED_DEFAULT = registerKey("crude_oil_placed_default");
    public static final ResourceKey<PlacedFeature> CRUDE_OIL_PLACED_ABUNDANT = registerKey("crude_oil_placed_abundant");

    //endregion
    
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, TIN_ORE_KEY, configuredFeatures.getOrThrow(HpCConfiguredFeatures.OVERWORLD_TIN_ORE_KEY),
                HpCOrePlacement.commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(72))));
        
        register(context, ALUMINIUM_ORE_KEY, configuredFeatures.getOrThrow(HpCConfiguredFeatures.OVERWORLD_ALUMINIUM_ORE_KEY),
                HpCOrePlacement.commonOrePlacement(6, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(16))));
        
        register(context, SILICON_ORE_KEY, configuredFeatures.getOrThrow(HpCConfiguredFeatures.OVERWORLD_SILICON_ORE_KEY),
                HpCOrePlacement.commonOrePlacement(3, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
        
        register(context, IRIDIUM_ORE_KEY, configuredFeatures.getOrThrow(HpCConfiguredFeatures.OVERWORLD_IRIDIUM_ORE_KEY),
                HpCOrePlacement.rareOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(256))));



        register(context, MOON_TEKTITE_PLACED_KEY, configuredFeatures.getOrThrow(HpCConfiguredFeatures.MOON_TEKTITE_KEY), List.of(
                CountPlacement.of(3), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome()));
        
        register(context, MOON_TIN_ORE_PLACED_KEY, configuredFeatures.getOrThrow(HpCConfiguredFeatures.MOON_TIN_ORE_KEY),
                HpCOrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        // Iron: Less frequent than Earth (7 per chunk), spread evenly.
        register(context, MOON_IRON_ORE_PLACED_KEY, configuredFeatures.getOrThrow(HpCConfiguredFeatures.MOON_IRON_ORE_KEY),
                HpCOrePlacement.commonOrePlacement(7, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        // Copper: Rarer than Earth (6 per chunk instead of 16).
        register(context, MOON_COPPER_ORE_PLACED_KEY, configuredFeatures.getOrThrow(HpCConfiguredFeatures.MOON_COPPER_ORE_KEY),
                HpCOrePlacement.commonOrePlacement(6, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(80))));

        // Aluminium: More abundant than Moon Iron (12 per chunk).
        register(context, MOON_ALUMINIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(HpCConfiguredFeatures.MOON_ALUMINIUM_ORE_KEY),
                HpCOrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

        // Silicon: Copper-like abundance (16 per chunk), spread everywhere.
        register(context, MOON_SILICON_ORE_PLACED_KEY, configuredFeatures.getOrThrow(HpCConfiguredFeatures.MOON_SILICON_ORE_KEY),
                HpCOrePlacement.commonOrePlacement(16, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(100))));

        // Iridium: More abundant than Earth (2 times per chunk guaranteed), sub-surface.
        register(context, MOON_IRIDIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(HpCConfiguredFeatures.MOON_IRIDIUM_ORE_KEY),
                HpCOrePlacement.commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(20), VerticalAnchor.absolute(120))));


        register(context, CRUDE_OIL_PLACED_DEFAULT, configuredFeatures.getOrThrow(HpCConfiguredFeatures.CRUDE_OIL_RESERVOIR),
                List.of(
                        RarityFilter.onAverageOnceEvery(20),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(0)),
                        BiomeFilter.biome()
                ));
        register(context, CRUDE_OIL_PLACED_ABUNDANT, configuredFeatures.getOrThrow(HpCConfiguredFeatures.CRUDE_OIL_RESERVOIR),
                List.of(
                        RarityFilter.onAverageOnceEvery(12),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(0)),
                        BiomeFilter.biome()
                ));

        
        
        context.register(MARIA_CRATER_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(HpCConfiguredFeatures.MARIA_CRATER_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(9), 
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome()
                )
        ));

        context.register(HIGHLANDS_CRATER_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(HpCConfiguredFeatures.HIGHLANDS_CRATER_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(6), 
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                        BiomeFilter.biome()
                )
        ));
    }
    
    private static ResourceKey<PlacedFeature> registerKey(String name){
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, name));
    }
    
    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers){
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));;
    }
}
