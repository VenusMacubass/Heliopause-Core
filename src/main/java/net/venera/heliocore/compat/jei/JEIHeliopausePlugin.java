package net.venera.heliocore.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.data.component.HpCDataComponents;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.recipe.CompressorRecipe;
import net.venera.heliocore.recipe.HpCRecipes;
import net.venera.heliocore.recipe.MagneticAssemblyPlatformRecipe;
import net.venera.heliocore.recipe.PCBFabricatorRecipe;
import net.venera.heliocore.screen.HpCMenuTypes;
import net.venera.heliocore.screen.block_entity.*;
import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JEIHeliopausePlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new MagneticAssemblyPlatformRecipeCategory(guiHelper),
                new PCBFabricatorRecipeCategory(guiHelper),
                new CompressorRecipeCategory(guiHelper)
    );
        
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level == null) return;
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<MagneticAssemblyPlatformRecipe> magneticAssemblyPlatformRecipes = recipeManager
                .getAllRecipesFor(HpCRecipes.MAGNETIC_ASSEMBLY_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(MagneticAssemblyPlatformRecipeCategory.MAGNETIC_ASSEMBLY_RECIPE_TYPE, magneticAssemblyPlatformRecipes);

        List<PCBFabricatorRecipe> pcbFabricatorRecipes = recipeManager
                .getAllRecipesFor(HpCRecipes.PCB_FABRICATOR_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(PCBFabricatorRecipeCategory.PCB_FABRICATOR_RECIPE_TYPE, pcbFabricatorRecipes);

        List<CompressorRecipe> compressorRecipes = recipeManager
                .getAllRecipesFor(HpCRecipes.COMPRESSOR_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
        registration.addRecipes(CompressorRecipeCategory.COMPRESSOR_RECIPE_TYPE, compressorRecipes);

        registration.addRecipes(RecipeTypes.CRAFTING, generateBurgerJEIDisplays());
        registration.addRecipes(RecipeTypes.CRAFTING, generateCannedStewJEIDisplays());
        registration.addRecipes(RecipeTypes.CRAFTING, generatePizzaJEIDisplays());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MagneticAssemblyPlatformScreen.class,
                118, 133, 24, 18, MagneticAssemblyPlatformRecipeCategory.MAGNETIC_ASSEMBLY_RECIPE_TYPE
        );

        registration.addRecipeClickArea(PCBFabricatorScreen.class,
                124, 34, 24, 18, PCBFabricatorRecipeCategory.PCB_FABRICATOR_RECIPE_TYPE
        );
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(MagneticAssemblyPlatformMenu.class, HpCMenuTypes.MAGNETIC_ASSEMBLY_PLATFORM_MENU.get(),
                MagneticAssemblyPlatformRecipeCategory.MAGNETIC_ASSEMBLY_RECIPE_TYPE, 37, 17, 0, 36
        );

        registration.addRecipeTransferHandler(PCBFabricatorMenu.class, HpCMenuTypes.PCB_FABRICATOR_MENU.get(),
                PCBFabricatorRecipeCategory.PCB_FABRICATOR_RECIPE_TYPE, 36, 6, 0, 36
        );
        registration.addRecipeTransferHandler(CoalCompressorMenu.class, HpCMenuTypes.COAL_COMPRESSOR_MENU.get(),
                CompressorRecipeCategory.COMPRESSOR_RECIPE_TYPE, 36, 9, 0, 36  
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(HpCBlocks.MAGNETIC_ASSEMBLY_PLATFORM.asItem()), MagneticAssemblyPlatformRecipeCategory.MAGNETIC_ASSEMBLY_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(HpCBlocks.PCB_FABRICATOR.asItem()), PCBFabricatorRecipeCategory.PCB_FABRICATOR_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(HpCBlocks.COAL_COMPRESSOR.asItem()), CompressorRecipeCategory.COMPRESSOR_RECIPE_TYPE);
    }


    private List<RecipeHolder<CraftingRecipe>> generateBurgerJEIDisplays() {
        List<RecipeHolder<CraftingRecipe>> dummyRecipes = new ArrayList<>();

        Ingredient redMeats = Ingredient.of(Items.COOKED_BEEF, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT);
        Ingredient chickenMeat = Ingredient.of(Items.COOKED_CHICKEN);
        Ingredient fishMeats = Ingredient.of(Items.COOKED_COD, Items.COOKED_SALMON);
        
        dummyRecipes.add(createDummyBurger("red_burger_base", HpCItems.RED_BURGER.get(), redMeats, false));
        dummyRecipes.add(createDummyBurger("red_burger_loaded", HpCItems.RED_BURGER.get(), redMeats, true));
        
        dummyRecipes.add(createDummyBurger("chicken_burger_base", HpCItems.CHICKEN_BURGER.get(), chickenMeat, false));
        dummyRecipes.add(createDummyBurger("chicken_burger_loaded", HpCItems.CHICKEN_BURGER.get(), chickenMeat, true));
        
        dummyRecipes.add(createDummyBurger("fish_burger_base", HpCItems.FISH_BURGER.get(), fishMeats, false));
        dummyRecipes.add(createDummyBurger("fish_burger_loaded", HpCItems.FISH_BURGER.get(), fishMeats, true));

        return dummyRecipes;
    }

    private RecipeHolder<CraftingRecipe> createDummyBurger(String idPath, Item outputItem, Ingredient meat, boolean fullyLoaded) {
        Ingredient buns = Ingredient.of(HpCItems.BURGER_BUN.get());

        NonNullList<Ingredient> inputs = NonNullList.create();
        inputs.add(buns);
        inputs.add(buns);
        inputs.add(meat);

        ItemStack output = new ItemStack(outputItem);
        
        if (fullyLoaded) {
            inputs.add(Ingredient.of(HpCItems.CHEESE_SLICE.get()));
            inputs.add(Ingredient.of(HpCItems.CHEESE_SLICE.get()));
            inputs.add(Ingredient.of(Items.DRIED_KELP, Items.BEETROOT, Items.CARROT, Items.BAKED_POTATO));

            output.set(HpCDataComponents.BURGER_CHEESE.get(), 2);
            output.set(HpCDataComponents.BURGER_VEGGIE.get(), true);
            
            FoodProperties baseFood = output.get(DataComponents.FOOD);
            if (baseFood != null) {
                FoodProperties upgradedFood = new FoodProperties.Builder()
                        .nutrition(baseFood.nutrition() + 5) 
                        .saturationModifier(1.5f + 0.5f)     
                        .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 120), 1f)
                        .build();
                output.set(DataComponents.FOOD, upgradedFood);
            }
        }
        
        ShapelessRecipe recipe = new ShapelessRecipe(HeliopauseCore.MOD_ID + "_burgers", CraftingBookCategory.MISC, output, inputs);

        return new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, idPath), recipe);
    }

    private List<RecipeHolder<CraftingRecipe>> generateCannedStewJEIDisplays() {
        List<RecipeHolder<CraftingRecipe>> dummyRecipes = new ArrayList<>();

        dummyRecipes.add(createDummyCannedStew("canned_mushroom_stew", Items.MUSHROOM_STEW, HpCItems.CANNED_MUSHROOM_STEW.get()));
        dummyRecipes.add(createDummyCannedStew("canned_rabbit_stew", Items.RABBIT_STEW, HpCItems.CANNED_RABBIT_STEW.get()));
        dummyRecipes.add(createDummyCannedStew("canned_beetroot_soup", Items.BEETROOT_SOUP, HpCItems.CANNED_BEETROOT_SOUP.get()));
        dummyRecipes.add(createDummyCannedStew("canned_suspicious_stew", Items.SUSPICIOUS_STEW, HpCItems.CANNED_SUSPICIOUS_STEW.get()));

        return dummyRecipes;
    }

    private RecipeHolder<CraftingRecipe> createDummyCannedStew(String idPath, Item stewInput, Item canOutput) {
        NonNullList<Ingredient> inputs = NonNullList.create();
        inputs.add(Ingredient.of(stewInput));
        inputs.add(Ingredient.of(HpCItems.EMPTY_CAN.get()));

        ShapelessRecipe recipe = new ShapelessRecipe(
                "heliocore_canned_stews",
                CraftingBookCategory.MISC,
                new ItemStack(canOutput),
                inputs
        );

        return new RecipeHolder<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, idPath), recipe);
    }

    private List<RecipeHolder<CraftingRecipe>> generatePizzaJEIDisplays() {
        List<RecipeHolder<CraftingRecipe>> dummyRecipes = new ArrayList<>();

        Ingredient basePizza = Ingredient.of(HpCBlocks.DEFAULT_PIZZA.get());
        Ingredient chicken = Ingredient.of(Items.COOKED_CHICKEN);
        Ingredient beef = Ingredient.of(Items.COOKED_BEEF, Items.COOKED_MUTTON);
        Ingredient mushroom = Ingredient.of(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM);
        Ingredient fish = Ingredient.of(Items.COOKED_COD, Items.COOKED_SALMON);
        Ingredient veggie = Ingredient.of(Items.CARROT, Items.KELP);
        
        NonNullList<Ingredient> singleInputs = NonNullList.create();
        singleInputs.add(basePizza);
        singleInputs.add(beef);

        ItemStack singleOutput = new ItemStack(HpCBlocks.DEFAULT_PIZZA.get());
        singleOutput.set(HpCDataComponents.PIZZA_TOPPINGS.get(), 2); // Beef bitmask

        dummyRecipes.add(new RecipeHolder<>(
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "pizza_topping_single"),
                new ShapelessRecipe("heliocore_pizza", CraftingBookCategory.MISC, singleOutput, singleInputs)
        ));
        
        NonNullList<Ingredient> loadedInputs = NonNullList.create();
        loadedInputs.add(basePizza);
        loadedInputs.add(chicken);
        loadedInputs.add(beef);
        loadedInputs.add(mushroom);
        loadedInputs.add(fish);
        loadedInputs.add(veggie);

        ItemStack loadedOutput = new ItemStack(HpCBlocks.DEFAULT_PIZZA.get());
        loadedOutput.set(HpCDataComponents.PIZZA_TOPPINGS.get(), 31);

        dummyRecipes.add(new RecipeHolder<>(
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "pizza_topping_loaded"),
                new ShapelessRecipe("heliocore_pizza", CraftingBookCategory.MISC, loadedOutput, loadedInputs)
        ));

        return dummyRecipes;
    }
}
