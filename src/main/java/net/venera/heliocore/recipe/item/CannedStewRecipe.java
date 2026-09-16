package net.venera.heliocore.recipe.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.recipe.HpCRecipes;

public class CannedStewRecipe extends CustomRecipe {

    public CannedStewRecipe(CraftingBookCategory category) {
        super(category);
    }

    // Helper 1: Check if the item is ANY of the valid vanilla stews/soups
    private boolean isStew(ItemStack stack) {
        return stack.is(Items.SUSPICIOUS_STEW) ||
                stack.is(Items.MUSHROOM_STEW) ||
                stack.is(Items.RABBIT_STEW) ||
                stack.is(Items.BEETROOT_SOUP);
    }

    // Helper 2: Map the vanilla stew to your corresponding canned version
    private Item getResultingCan(Item stewItem) {
        if (stewItem == Items.SUSPICIOUS_STEW) return HpCItems.CANNED_SUSPICIOUS_STEW.get();
        if (stewItem == Items.MUSHROOM_STEW) return HpCItems.CANNED_MUSHROOM_STEW.get();
        if (stewItem == Items.RABBIT_STEW) return HpCItems.CANNED_RABBIT_STEW.get();
        if (stewItem == Items.BEETROOT_SOUP) return HpCItems.CANNED_BEETROOT_SOUP.get();
        return net.minecraft.world.item.Items.AIR; // Fallback
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasStew = false;
        boolean hasCan = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                // Now uses the isStew() helper instead of hardcoding Suspicious Stew
                if (isStew(stack) && !hasStew) {
                    hasStew = true;
                } else if (stack.is(HpCItems.EMPTY_CAN.get()) && !hasCan) {
                    hasCan = true;
                } else {
                    return false;
                }
            }
        }
        return hasStew && hasCan;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack stewIngredient = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (isStew(stack)) {
                stewIngredient = stack;
                break;
            }
        }

        if (stewIngredient.isEmpty()) return ItemStack.EMPTY;

        // Dynamically get the right can based on the ingredient
        Item resultingItem = getResultingCan(stewIngredient.getItem());
        ItemStack result = new ItemStack(resultingItem);

        // ONLY attempt to transfer effects if the ingredient was actually a Suspicious Stew
        if (stewIngredient.is(Items.SUSPICIOUS_STEW)) {
            if (stewIngredient.has(DataComponents.SUSPICIOUS_STEW_EFFECTS)) {
                var effects = stewIngredient.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
                result.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, effects);
            }
        }

        return result;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainders = super.getRemainingItems(input);

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            // Returns a bowl for ANY stew found in the grid
            if (isStew(stack)) {
                remainders.set(i, new ItemStack(Items.BOWL));
            }
        }

        return remainders;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HpCRecipes.CANNED_STEW_SERIALIZER.get();
    }
}
