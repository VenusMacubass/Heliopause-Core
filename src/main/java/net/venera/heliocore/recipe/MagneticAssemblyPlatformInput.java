package net.venera.heliocore.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record MagneticAssemblyPlatformInput(ItemStack engine, ItemStack base, 
                                            ItemStack fin1, ItemStack fin2, ItemStack fin3, ItemStack fin4,
                                            ItemStack hull1, ItemStack hull2, ItemStack hull3, ItemStack hull4, ItemStack hull5, ItemStack hull6, ItemStack hull7, ItemStack hull8,
                                            ItemStack booster1, ItemStack booster2,
                                            ItemStack nose) implements RecipeInput {
        @Override
        public ItemStack getItem(int index) {
            return switch (index) {
                case 0 -> engine;
                case 1 -> base;
                case 2 -> fin1;
                case 3 -> fin2;
                case 4 -> fin3;
                case 5 -> fin4;
                case 6 -> hull1;
                case 7 -> hull2;
                case 8 -> hull3;
                case 9 -> hull4;
                case 10 -> hull5;
                case 11 -> hull6;
                case 12 -> hull7;
                case 13 -> hull8;
                case 14 -> booster1;
                case 15 -> booster2;
                case 16 -> nose;
                default -> ItemStack.EMPTY;
            };
        }

        @Override
        public int size() {
            return 17;
        }
}
