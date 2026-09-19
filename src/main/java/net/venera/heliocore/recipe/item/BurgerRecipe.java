package net.venera.heliocore.recipe.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.venera.heliocore.data.component.HpCDataComponents;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.recipe.HpCRecipes;

public class BurgerRecipe extends CustomRecipe {
    public BurgerRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level level) {
        int bunCount = 0;
        int meatCount = 0;
        int cheeseCount = 0;
        int veggieCount = 0;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(HpCItems.BURGER_BUN.get())) {
                    bunCount++;
                } else if (getBurgerOutput(stack.getItem()) != null) {
                    meatCount++;
                } else if (stack.is(HpCItems.CHEESE_SLICE.get())) {
                    cheeseCount++;
                } else if (isVeggie(stack)) {
                    veggieCount++;
                } else {
                    return false;
                }
            }
        }
        return bunCount == 2 && meatCount == 1 && cheeseCount <= 2 && veggieCount <= 1;
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        Item outputItem = null;
        int cheeseCount = 0;
        boolean hasVeggie = false;

        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                if (getBurgerOutput(stack.getItem()) != null) {
                    outputItem = getBurgerOutput(stack.getItem());
                } else if (stack.is(HpCItems.CHEESE_SLICE.get())) {
                    cheeseCount++;
                } else if (isVeggie(stack)) {
                    hasVeggie = true;
                }
            }
        }

        if (outputItem == null) return ItemStack.EMPTY;

        ItemStack burgerStack = new ItemStack(outputItem);

        if (cheeseCount > 0) burgerStack.set(HpCDataComponents.BURGER_CHEESE.get(), cheeseCount);
        if (hasVeggie) burgerStack.set(HpCDataComponents.BURGER_VEGGIE.get(), true);

        FoodProperties currentFood = burgerStack.get(DataComponents.FOOD);
        if (currentFood != null) {
            int bonusNutrition = (cheeseCount * 2) + (hasVeggie ? 1 : 0);
            float bonusSaturation = (cheeseCount * 0.2f) + (hasVeggie ? 0.1f : 0f);

            FoodProperties upgradedFood = new FoodProperties.Builder()
                    .nutrition(currentFood.nutrition() + bonusNutrition)
                    .saturationModifier(1.5f + bonusSaturation) // Hardcoded the base 1.5f modifier to stop the explosion
                    .effect(() -> new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.REGENERATION, 120), 1f) // Restored the lost effect
                    .build();

            burgerStack.set(DataComponents.FOOD, upgradedFood);
        }

        return burgerStack;
    }

    // New helper method to ensure matches() and assemble() are perfectly synced
    private boolean isVeggie(ItemStack stack) {
        return stack.is(Items.DRIED_KELP) || stack.is(Items.BEETROOT) || stack.is(Items.CARROT) || stack.is(Items.BAKED_POTATO);
    }
    
    private Item getBurgerOutput(Item meatItem) {
        if (meatItem == Items.COOKED_BEEF || meatItem == Items.COOKED_MUTTON || meatItem == Items.COOKED_PORKCHOP || meatItem == Items.COOKED_RABBIT) {
            return HpCItems.RED_BURGER.get();
        } else if (meatItem == Items.COOKED_CHICKEN) {
            return HpCItems.CHICKEN_BURGER.get();
        } else if (meatItem == Items.COOKED_COD || meatItem == Items.COOKED_SALMON) {
            return HpCItems.FISH_BURGER.get();
        }
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return HpCRecipes.BURGER_RECIPE.get();
    }
}
