package net.venera.heliocore.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.recipe.HpCRecipes;
import net.venera.heliocore.recipe.MagneticAssemblyPlatformRecipe;
import net.venera.heliocore.screen.block_entity.MagneticAssemblyPlatformScreen;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@JeiPlugin
public class JEIHeliopausePlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MagneticAssemblyPlatformRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        List<MagneticAssemblyPlatformRecipe> magneticAssemblyPlatformRecipes = recipeManager.getAllRecipesFor(HpCRecipes.MAGNETIC_ASSEMBLY_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(MagneticAssemblyPlatformRecipeCategory.MAGNETIC_ASSEMBLY_RECIPE_TYPE, magneticAssemblyPlatformRecipes);
    
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MagneticAssemblyPlatformScreen.class, 80, 35, 20, 20, MagneticAssemblyPlatformRecipeCategory.MAGNETIC_ASSEMBLY_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(HpCBlocks.MAGNETIC_ASSEMBLY_PLATFORM.asItem()), MagneticAssemblyPlatformRecipeCategory.MAGNETIC_ASSEMBLY_RECIPE_TYPE);
    }
}
