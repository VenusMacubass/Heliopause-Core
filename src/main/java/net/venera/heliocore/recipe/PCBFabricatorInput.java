package net.venera.heliocore.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record PCBFabricatorInput(ItemStack core, ItemStack wire, ItemStack logic1, ItemStack logic2, ItemStack sub1, ItemStack sub2) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> core;
            case 1 -> wire;
            case 2 -> logic1;
            case 3 -> logic2;
            case 4 -> sub1;
            case 5 -> sub2;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 6;
    }
}