package net.venera.galacticraftcore.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;

import javax.annotation.Nullable;


public interface IMachineRecipe extends Recipe<CraftingInput> {
    int getProcessingTime();
    int getEnergyCapacity();
    boolean isActive();
}
