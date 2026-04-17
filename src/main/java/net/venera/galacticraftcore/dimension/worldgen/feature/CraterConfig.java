package net.venera.galacticraftcore.dimension.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record CraterConfig(int minRadius, int maxRadius, double squish) implements FeatureConfiguration {
    public static final Codec<CraterConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("min_radius").forGetter(CraterConfig::minRadius),
                    Codec.INT.fieldOf("max_radius").forGetter(CraterConfig::maxRadius),
                    Codec.DOUBLE.fieldOf("squish").forGetter(CraterConfig::squish)
            ).apply(instance, CraterConfig::new)
    );
}
