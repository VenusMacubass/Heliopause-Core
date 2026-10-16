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
import net.venera.heliocore.recipe.MagneticAssemblyPlatformRecipe;
import org.jetbrains.annotations.Nullable;

public class MagneticAssemblyPlatformRecipeCategory implements IRecipeCategory<MagneticAssemblyPlatformRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "magnetic_assembly");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "textures/gui/magnetic_assembly_platform/magnetic_assembly_platform.png");

    public static final RecipeType<MagneticAssemblyPlatformRecipe> MAGNETIC_ASSEMBLY_RECIPE_TYPE = new RecipeType<>(UID, MagneticAssemblyPlatformRecipe.class);
    
    private static final int BG_U = 0;
    private static final int BG_V = 12;
    private static final int WIDTH = 175;
    private static final int HEIGHT = 144;

    private final IDrawable background;
    private final IDrawable icon;

    public MagneticAssemblyPlatformRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, BG_U, BG_V, WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK, new ItemStack(HpCBlocks.MAGNETIC_ASSEMBLY_PLATFORM.get())
        );
    }

    @Override
    public RecipeType<MagneticAssemblyPlatformRecipe> getRecipeType() {
        return MAGNETIC_ASSEMBLY_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei_title." + HeliopauseCore.MOD_ID + ".magnetic_assembly");
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
    public void setRecipe(IRecipeLayoutBuilder builder, MagneticAssemblyPlatformRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 16 - BG_V)
                .addIngredients(recipe.nose());
        
        if (!recipe.boosters().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 116, 46 - BG_V)
                    .addIngredients(recipe.boosters().getFirst());
        }
        if (recipe.boosters().size() > 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, 44, 46 - BG_V)
                    .addIngredients(recipe.boosters().get(1));
        }

        for (int i = 0; i < recipe.hulls().size() && i < 8; i++) {
            int hullX = (i < 4) ? 94 : 66;
            int hullY = 94 - ((i % 4) * 19) - BG_V;
            builder.addSlot(RecipeIngredientRole.INPUT, hullX, hullY)
                    .addIngredients(recipe.hulls().get(i));
        }
        
        for (int i = 0; i < recipe.fins().size() && i < 4; i++) {
            int finX = 121 - 82 * (i >> 1);
            int finY = 107 - 20 * (i & 1) - BG_V;
            builder.addSlot(RecipeIngredientRole.INPUT, finX, finY)
                    .addIngredients(recipe.fins().get(i));
        }
        
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 116 - BG_V)
                .addIngredients(recipe.base());
        
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 137 - BG_V)
                .addIngredients(recipe.engine());
        
        builder.addSlot(RecipeIngredientRole.OUTPUT, 148, 133 - BG_V)
                .addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(MagneticAssemblyPlatformRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics, 0, 0);
    }
}