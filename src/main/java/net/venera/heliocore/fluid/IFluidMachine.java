package net.venera.heliocore.fluid;

import net.minecraft.core.Direction;

public interface IFluidMachine {
    enum PortType {
        NONE,
        INPUT,
        OUTPUT,
        CONTAINER 
    }
    PortType getFluidPortType(Direction face);
    
    int insertFluid(String fluidType, int amount, boolean simulate);
    int extractFluid(String fluidType, int amount, boolean simulate);
}
