package net.venera.heliocore.recipe.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.data.component.HpCDataComponents;
import net.venera.heliocore.recipe.HpCRecipes;

public class PizzaRecipe extends CustomRecipe {
    public PizzaRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level level) {
        ItemStack pizzaStack = ItemStack.EMPTY;
        int proposedToppings = 0;
        // First pass: find the pizza and collect proposed toppings
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(HpCBlocks.DEFAULT_PIZZA.get().asItem())) {
                    if (!pizzaStack.isEmpty()) return false; // Only one pizza allowed
                    pizzaStack = stack;
                } else {
                    int bit = getToppingBit(stack.getItem());
                    if (bit > 0) {
                        // Reject if they put two of the SAME topping in the crafting grid
                        if ((proposedToppings & bit) != 0) return false;
                        proposedToppings |= bit;
                    } else {
                        return false; // Reject non-topping items
                    }
                }
            }
        }

        if (pizzaStack.isEmpty() || proposedToppings == 0) return false;

        // Reject if the pizza ALREADY has one of the proposed toppings on it
        Integer existingMask = pizzaStack.get(HpCDataComponents.PIZZA_TOPPINGS.get());
        int currentMask = (existingMask != null) ? existingMask : 0;

        return (currentMask & proposedToppings) == 0;
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack pizzaStack = ItemStack.EMPTY;
        int currentMask = 0;

        // Find the pizza and calculate the new toppings
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(HpCBlocks.DEFAULT_PIZZA.get().asItem())) {
                    pizzaStack = stack.copy();
                    pizzaStack.setCount(1);

                    // Read existing toppings so players can add more later
                    Integer existingMask = pizzaStack.get(HpCDataComponents.PIZZA_TOPPINGS.get());
                    if (existingMask != null) {
                        currentMask |= existingMask;
                    }
                } else {
                    // Add the new topping using bitwise OR
                    currentMask |= getToppingBit(stack.getItem());
                }
            }
        }

        if (!pizzaStack.isEmpty()) {
            pizzaStack.set(HpCDataComponents.PIZZA_TOPPINGS.get(), currentMask);
        }

        return pizzaStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2; // Needs at least 2 slots (Pizza + 1 Topping)
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HpCRecipes.PIZZA_TOPPING.get();
    }

    private int getToppingBit(Item item) {
        if (item == Items.COOKED_CHICKEN) return 1;
        if (item == Items.COOKED_BEEF || item == Items.COOKED_MUTTON) return 2;
        if (item == Items.BROWN_MUSHROOM || item == Items.RED_MUSHROOM) return 4;
        if (item == Items.COOKED_COD || item == Items.COOKED_SALMON) return 8;
        if (item == Items.CARROT || item == Items.KELP) return 16;
        return 0;
    }
}
