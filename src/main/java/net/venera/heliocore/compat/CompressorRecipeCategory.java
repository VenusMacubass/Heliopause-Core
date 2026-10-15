package net.venera.heliocore.compat;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.venera.heliocore.recipe.CoalCompressorRecipe;
import org.jetbrains.annotations.Nullable;

public class CompressorRecipeCategory implements IRecipeCategory {
    @Override
    public RecipeType getRecipeType() {
        return null;
    }

    @Override
    public Component getTitle() {
        return null;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return null;
    }
    

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Object recipe, IFocusGroup focuses) {

    }
}
