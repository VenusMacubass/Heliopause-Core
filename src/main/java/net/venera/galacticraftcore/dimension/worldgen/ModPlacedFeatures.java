package net.venera.galacticraftcore.dimension.worldgen;

import io.netty.bootstrap.Bootstrap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.venera.galacticraftcore.GalacticraftCore;

import java.util.List;

public class ModPlacedFeatures {  //Where to place and how to place
    public static final ResourceKey<PlacedFeature> TIN_ORE_PLACED_KEY = registerKey("tin_ore_placed");
    public static final ResourceKey<PlacedFeature> NETHER_TIN_ORE_PLACED_KEY = registerKey("nether_tin_ore_placed");
    public static final ResourceKey<PlacedFeature> END_TIN_ORE_PLACED_KEY = registerKey("end_tin_ore_placed");
    public static final ResourceKey<PlacedFeature> MOON_TIN_ORE_PLACED_KEY = registerKey("moon_tin_ore_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, TIN_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_TIN_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(80))));
        register(context, NETHER_TIN_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.NETHER_TIN_ORE_KEY),
                ModOrePlacement.commonOrePlacement(9, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(80))));
        register(context, END_TIN_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.END_TIN_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(80))));
        register(context, MOON_TIN_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.MOON_TIN_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

    }
    
    private static ResourceKey<PlacedFeature> registerKey(String name){
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, name));
    }
    
    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers){
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));;
    }
}
