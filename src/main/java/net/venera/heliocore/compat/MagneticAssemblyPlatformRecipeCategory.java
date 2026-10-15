package net.venera.heliocore.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.recipe.MagneticAssemblyPlatformRecipe;
import org.jetbrains.annotations.Nullable;

public class MagneticAssemblyPlatformRecipeCategory  implements IRecipeCategory<MagneticAssemblyPlatformRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "magnetic_assembly");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/magnetic_assemblyplatform/magnetic_assembly_platform.png");
    
    public static final RecipeType<MagneticAssemblyPlatformRecipe> MAGNETIC_ASSEMBLY_RECIPE_TYPE = new RecipeType<>(UID, MagneticAssemblyPlatformRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public MagneticAssemblyPlatformRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 160, 80);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(HpCBlocks.MAGNETIC_ASSEMBLY_PLATFORM));
    }

    @Override
    public RecipeType<MagneticAssemblyPlatformRecipe> getRecipeType() {
        return MAGNETIC_ASSEMBLY_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(HeliopauseCore.MOD_ID + ".jei_title.magnetic_assembly");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MagneticAssemblyPlatformRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 30).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 30).addItemStack(recipe.getResultItem(null));
    }

    @Override @SuppressWarnings("removal")
    public @Nullable IDrawable getBackground() {
        return background;
    }

    //    @Override
//    public void draw(MagneticAssemblyPlatformRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
//        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
//        background.draw(guiGraphics);
//    }
}
