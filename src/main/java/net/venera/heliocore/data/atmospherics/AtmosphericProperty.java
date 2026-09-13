package net.venera.heliocore.data.atmospherics;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.venera.heliocore.dimension.HpCDimensions;
import net.venera.heliocore.dimension.biome.HpCBiomes;

public class AtmosphericProperty {
    public static double getEnvironmentalTemperature(Level level, Holder<Biome> biome) {
        double dayTemp = 30.0;
        double nightTemp = 10.0;
        
        if (biome.is(BiomeTags.IS_BADLANDS) || biome.is(BiomeTags.HAS_DESERT_PYRAMID)) {
            dayTemp = 45.0; 
            nightTemp = 0.0;  
        } else if (biome.is(BiomeTags.IS_TAIGA) || biome.is(BiomeTags.HAS_IGLOO)) {
            dayTemp = 5.0;
            nightTemp = -15.0;
        }else if (biome.is(HpCBiomes.LUNAR_HIGHLANDS) || biome.is(HpCBiomes.LUNAR_MARIA)){
            dayTemp = 120;
            nightTemp = -171;
        }
        
        long time = level.getDayTime() % 24000;
        double angle = (time / 24000.0) * (2 * Math.PI);
        double averageTemp = (dayTemp + nightTemp) / 2.0;
        double tempSwing = (dayTemp - nightTemp) / 2.0;
        
        return averageTemp + (tempSwing * Math.sin(angle));
    }
    
    public static int getDimensionalPressure(Level level) {
        ResourceKey<Level> dimension = level.dimension();
        if (dimension.equals(Level.OVERWORLD)) {  
            return 760;
        }
        else if (dimension.equals(Level.NETHER)) { 
            return 830;
        }else if (dimension.equals(Level.END)) {
            return 1000;
        }else if(dimension.equals(HpCDimensions.MOON_LEVEL_KEY)){
            return 0;
        }
//        else if(dimension.equals(HpCDimensions.VENUS_LEVEL_KEY)){
//            return 70680;
//        }
        return 760;
    }
}
