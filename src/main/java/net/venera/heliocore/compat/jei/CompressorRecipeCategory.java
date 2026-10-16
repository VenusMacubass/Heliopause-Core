package net.venera.heliocore.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.recipe.CompressorRecipe;
import org.jetbrains.annotations.Nullable;

public class CompressorRecipeCategory implements IRecipeCategory<CompressorRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "coal_compressor");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/coal_compressor/coal_compressor_gui.png");

    public static final RecipeType<CompressorRecipe> COMPRESSOR_RECIPE_TYPE = new RecipeType<>(UID, CompressorRecipe.class);

    private static final int BG_U = 0;
    private static final int BG_V = 12;
    private static final int WIDTH = 175;
    private static final int HEIGHT = 68;

    private final IDrawable background;
    private final IDrawable icon;

    public CompressorRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, BG_U, BG_V, WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK, new ItemStack(HpCBlocks.COAL_COMPRESSOR.get())
        );
    }

    @Override
    public RecipeType<CompressorRecipe> getRecipeType() {
        return COMPRESSOR_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei_title." + HeliopauseCore.MOD_ID + ".compressor");
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CompressorRecipe recipe, IFocusGroup focuses) {
        final int GRID_START_X = 9;
        final int GRID_START_Y = 17 - BG_V;
        final int SLOT_SPACING = 18;

        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        int recipeWidth = recipe.pattern.width();
        int recipeHeight = recipe.pattern.height();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int x = GRID_START_X + col * SLOT_SPACING;
                int y = GRID_START_Y + row * SLOT_SPACING;

                IRecipeSlotBuilder slotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, x, y);
                if (col < recipeWidth && row < recipeHeight) {
                    int patternIndex = row * recipeWidth + col;
                    Ingredient ingredient = ingredients.get(patternIndex);
                    if (!ingredient.isEmpty()) {
                        slotBuilder.addIngredients(ingredient);
                    }
                }
            }
        }
        
        builder.addSlot(RecipeIngredientRole.OUTPUT, 129, 35 - BG_V)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(CompressorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics, 0, 0);
    }
}
