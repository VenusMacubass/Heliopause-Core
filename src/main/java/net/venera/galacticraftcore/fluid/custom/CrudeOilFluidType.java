package net.venera.galacticraftcore.fluid.custom;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;

public class CrudeOilFluidType extends FluidType {
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowTexture;
    private final int tintColor;

    public CrudeOilFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowTexture, int tintColor) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowTexture = flowTexture;
        this.tintColor = tintColor;
    }

    public ResourceLocation getStillTexture() {
        return stillTexture;
    }

    public ResourceLocation getFlowTexture() {
        return flowTexture;
    }

    public int getTintColor() {
        return tintColor;
    }
}
