package net.venera.galacticraftcore.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.venera.galacticraftcore.GalacticraftCore;

public class ModDimensions {
    
    public static final ResourceKey<Level> MOON_LEVEL_KEY = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "moon")
    );
    
    public static final ResourceKey<DimensionType> MOON_TYPE_KEY = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "moon")
    );
}
