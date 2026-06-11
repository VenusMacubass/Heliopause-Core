package net.venera.heliocore.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;

public class FluidHelper {
    public static FluidType.Properties createTypeProperties() {
        return FluidType.Properties.create()
                .canSwim(true)
                .canDrown(true)
                .pathType(PathType.LAVA)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .rarity(Rarity.COMMON)
                .density(900);
    }

    public static FluidType.Properties createGasProperties(String name) {
        return FluidType.Properties.create()
                .descriptionId("fluid.heliocore." + name)
                .density(-1000)    
                .viscosity(0)      
                .temperature(300);
    
    }
}
