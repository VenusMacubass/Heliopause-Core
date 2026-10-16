package net.venera.heliocore.compat.jei;

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
import net.venera.heliocore.recipe.PCBFabricatorRecipe;
import org.jetbrains.annotations.Nullable;

public class PCBFabricatorRecipeCategory implements IRecipeCategory<PCBFabricatorRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "pcb_fabricator");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/pcb_fabricator/pcb_fabricator_gui.png");

    public static final RecipeType<PCBFabricatorRecipe> PCB_FABRICATOR_RECIPE_TYPE = new RecipeType<>(UID, PCBFabricatorRecipe.class);

    private static final int BG_U = 0;
    private static final int BG_V = 12;
    private static final int WIDTH = 175;
    private static final int HEIGHT = 70;

    private final IDrawable background;
    private final IDrawable icon;

    public PCBFabricatorRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, BG_U, BG_V, WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK, new ItemStack(HpCBlocks.PCB_FABRICATOR.get())
        );
    }

    @Override
    public RecipeType<PCBFabricatorRecipe> getRecipeType() {
        return PCB_FABRICATOR_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei_title." + HeliopauseCore.MOD_ID + ".pcb_fabricator");
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
    public void setRecipe(IRecipeLayoutBuilder builder, PCBFabricatorRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 34 - BG_V)
                .addIngredients(recipe.core());

        builder.addSlot(RecipeIngredientRole.INPUT, 26, 34 - BG_V)
                .addIngredients(recipe.wire());

        builder.addSlot(RecipeIngredientRole.INPUT, 47, 19 - BG_V)
                .addIngredients(recipe.logic1());

        builder.addSlot(RecipeIngredientRole.INPUT, 55, 49 - BG_V)
                .addIngredients(recipe.logic2());

        builder.addSlot(RecipeIngredientRole.INPUT, 86, 34 - BG_V)
                .addIngredients(recipe.sub1());

        builder.addSlot(RecipeIngredientRole.INPUT, 104, 34 - BG_V)
                .addIngredients(recipe.sub2());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 152, 34 - BG_V)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(PCBFabricatorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics, 0, 0);
    }
}