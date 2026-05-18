package net.venera.heliocore.recipe;

import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;


public interface IMachineRecipe extends Recipe<CraftingInput> {
    int getProcessingTime();
    int getEnergyCapacity();
    boolean isActive();
}
