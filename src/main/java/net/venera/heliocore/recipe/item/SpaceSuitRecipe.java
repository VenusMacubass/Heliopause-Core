package net.venera.heliocore.recipe.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.venera.heliocore.recipe.HpCRecipes;

public class SpaceSuitRecipe extends ShapedRecipe {
    public SpaceSuitRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result, boolean showNotification) {
        super(group, category, pattern, result, showNotification);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (!super.matches(input, level)) return false;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (stack.getItem() instanceof ArmorItem) {
                if (stack.getMaxDamage() > 0) {
                    float damagePercent = (float) stack.getDamageValue() / stack.getMaxDamage();
                    if (damagePercent > 0.10f) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider lookup) {
        ItemStack output = super.assemble(input, lookup).copy();

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (stack.getItem() instanceof ArmorItem) {
                var attributes = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
                if (attributes != null) {
                    output.set(DataComponents.ATTRIBUTE_MODIFIERS, attributes);
                }
                
                Integer maxDamage = stack.get(DataComponents.MAX_DAMAGE);
                if (maxDamage != null) {
                    output.set(DataComponents.MAX_DAMAGE, maxDamage);
                }
                
                var enchants = stack.get(DataComponents.ENCHANTMENTS);
                if (enchants != null && !enchants.isEmpty()) {
                    output.set(DataComponents.ENCHANTMENTS, enchants);
                }
                
                if (stack.getMaxDamage() > 0 && stack.getDamageValue() > 0) {
                    float wearPercentage = (float) stack.getDamageValue() / stack.getMaxDamage();
                    int newDamage = (int) (output.getMaxDamage() * wearPercentage);
                    output.set(DataComponents.DAMAGE, newDamage);
                }
                break;
            }
        }
        return output;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return HpCRecipes.SPACE_SUIT_SERIALIZER.get();
    }
}