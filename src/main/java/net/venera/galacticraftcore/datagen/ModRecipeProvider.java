package net.venera.galacticraftcore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        //Ingredient Lists
        List<ItemLike> ALUMINIUM_SMELTABLES = List.of(ModBlocks.ALUMINIUM_ORE, ModItems.RAW_ALUMINIUM);
        List<ItemLike> TIN_SMELTABLES = List.of(ModBlocks.TIN_ORE, ModItems.RAW_TIN);

        //Metals
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ALUMINIUM_BLOCK.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.ALUMINIUM_INGOT.get())
                .unlockedBy("has_aluminium_ingot", has(ModItems.ALUMINIUM_INGOT.get()))
                .save(recipeOutput, "aluminium_block_from_ingots");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ALUMINIUM_INGOT.get(), 9)
                .requires(ModBlocks.ALUMINIUM_BLOCK.get(), 1)
                .unlockedBy("has_aluminium_block", has(ModBlocks.ALUMINIUM_BLOCK.get()))
                .save(recipeOutput, "aluminium_ingots_from_block");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TIN_BLOCK.get())
                .pattern("TTT")
                .pattern("TTT")
                .pattern("TTT")
                .define('T', ModItems.TIN_INGOT.get())
                .unlockedBy("has_tin_ingots", has(ModItems.TIN_INGOT.get()))
                .save(recipeOutput, "tin_block_from_ingots");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TIN_INGOT.get(), 9)
                .requires(ModBlocks.TIN_BLOCK.get(), 1)
                .unlockedBy("has_tin_block", has(ModBlocks.ALUMINIUM_BLOCK.get()))
                .save(recipeOutput, "tin_ingots_from_block");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SILICON_BLOCK.get())
                 .pattern("SSS")
                 .pattern("SSS")
                 .pattern("SSS").define('S', ModItems.RAW_SILICON.get())
                 .unlockedBy("has_raw_silicon", has(ModItems.RAW_SILICON.get()))
                 .save(recipeOutput, "silicon_block_from_raw_silicon");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_SILICON.get(), 9)
                    .requires(ModBlocks.SILICON_BLOCK.get(),    1)
                    .unlockedBy("has_silicon_block", has(ModBlocks.SILICON_BLOCK.get()))
                    .save(recipeOutput, "raw_silicon_from_silicon_block");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.IRIDIUM_BLOCK.get())
                .pattern("III")
                .pattern("III")
                .pattern("III").define('I', ModItems.IRIDIUM_INGOT.get())
                .unlockedBy("has_iridium_ingot", has(ModItems.IRIDIUM_INGOT.get()))
                .save(recipeOutput, "iridium_block_from_ingots");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.IRIDIUM_INGOT.get(), 9)
                .requires(ModBlocks.IRIDIUM_BLOCK.get(),    1)
                .unlockedBy("has_iridium_block", has(ModBlocks.IRIDIUM_BLOCK.get()))
                .save(recipeOutput, "iridium_ingots_from_iridium_block");

        //Raw Metal Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RAW_TIN_BLOCK.get())
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .define('R', ModItems.RAW_TIN.get())
                .unlockedBy("has_raw_tin", has(ModItems.RAW_TIN.get()))
                .save(recipeOutput, "raw_tin_block_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RAW_ALUMINIUM_BLOCK.get())
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .define('R', ModItems.RAW_ALUMINIUM.get())
                .unlockedBy("has_raw_aluminium", has(ModItems.RAW_ALUMINIUM.get()))
                .save(recipeOutput, "raw_aluminium_block_crafting");



        //GC Ingredients
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COPPER_CANISTER.get(), 2)
                .pattern("C C")
                .pattern("C C")
                .pattern("CCC")
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(recipeOutput, "copper_canister_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TIN_CANISTER.get(), 2)
                .pattern("T T")
                .pattern("T T")
                .pattern("TTT")
                .define('T', ModItems.TIN_INGOT.get())
                .unlockedBy("has_tin_ingot", has(ModItems.TIN_INGOT.get()))
                .save(recipeOutput, "tin_canister_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SENSOR_LENS)
                .pattern("RGR")
                .pattern("GIG")
                .pattern("RGR")
                .define('R', Items.REDSTONE)
                .define('G', Items.GLASS_PANE)
                .define('I', ModItems.COMPRESSED_IRIDIUM)
                .unlockedBy("has_compressed_iridium", has(ModItems.IRIDIUM_INGOT.get()))
                .save(recipeOutput, "sensor_lens_crafting");



        //Tools
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.STEEL_SWORD.get())
                .pattern(" C ")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', ModItems.COMPRESSED_STEEL.get())
                .define('S', Items.STICK)
                .unlockedBy("has_compressed_steel", has(ModItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_sword_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STEEL_AXE.get())
                .pattern(" CC")
                .pattern(" SC")
                .pattern(" S ")
                .define('C', ModItems.COMPRESSED_STEEL.get())
                .define('S', Items.STICK)
                .unlockedBy("has_compressed_steel", has(ModItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_axe_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STEEL_PICKAXE.get())
                .pattern("CCC")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', ModItems.COMPRESSED_STEEL.get())
                .define('S', Items.STICK)
                .unlockedBy("has_compressed_steel", has(ModItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_pickaxe_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STEEL_SHOVEL.get())
                .pattern(" C ")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', ModItems.COMPRESSED_STEEL.get())
                .define('S', Items.STICK)
                .unlockedBy("has_compressed_steel", has(ModItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_shovel_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STEEL_HOE.get())
                .pattern(" CC")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', ModItems.COMPRESSED_STEEL.get())
                .define('S', Items.STICK)
                .unlockedBy("has_compressed_steel", has(ModItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_hoe_crafting");



        //Mod Armors
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.STEEL_HELMET.get())
                .pattern("SSS")
                .pattern("S S")
                .define('S', ModItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_compressed_steel", has(ModItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_helmet_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.STEEL_CHESTPLATE.get())
                .pattern("S S")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', ModItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_compressed_steel", has(ModItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_chestplate_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.STEEL_LEGGINGS.get())
                .pattern("SSS")
                .pattern("S S")
                .pattern("S S")
                .define('S', ModItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_compressed_steel", has(ModItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_leggings_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.STEEL_BOOTS.get())
                .pattern("S S")
                .pattern("S S")
                .define('S', ModItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_compressed_steel", has(ModItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_boots_crafting");



        //Special Tools
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STANDARD_WRENCH)
                .pattern("  S")
                .pattern(" C ")
                .pattern("C  ")
                .define('C', ModItems.COMPRESSED_BRONZE.get())
                .define('S', ModItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_compressed_steel", has(ModItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "standard_wrench_crafting");



        //Mod Foods
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.DEHYDRATED_APPLE.get())
                .pattern("CA ")
                .pattern("A  ")
                .define('C', ModItems.TIN_CANISTER.get())
                .define('A', Items.APPLE)
                .unlockedBy("has_tin_canister", has(ModItems.TIN_CANISTER.get()))
                .save(recipeOutput, "dehydrated_apple_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.DEHYDRATED_CARROT.get())
                .pattern("CB ")
                .pattern("B  ")
                .define('C', ModItems.TIN_CANISTER.get())
                .define('B', Items.CARROT)
                .unlockedBy("has_tin_canister", has(ModItems.TIN_CANISTER.get()))
                .save(recipeOutput, "dehydrated_carrot_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.DEHYDRATED_MELON.get())
                .pattern("CM ")
                .pattern("M  ")
                .define('C', ModItems.TIN_CANISTER.get())
                .define('M', Items.MELON)
                .unlockedBy("has_tin_canister", has(ModItems.TIN_CANISTER.get()))
                .save(recipeOutput, "dehydrated_melon_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.DEHYDRATED_POTATO.get())
                .pattern("CP ")
                .pattern("P  ")
                .define('C', ModItems.TIN_CANISTER.get())
                .define('P', Items.POTATO)
                .unlockedBy("has_tin_canister", has(ModItems.TIN_CANISTER.get()))
                .save(recipeOutput, "dehydrated_potato_crafting");
//        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.DEHYDRATED_APPLE.get())
//                .pattern("CA ")
//                .pattern("A  ")
//                .pattern("   ")
//                .define('C', ModItems.TIN_CANISTER.get())
//                .define('A', Items.APPLE)
//                .unlockedBy("has_compressed_steel", has(ModItems.TIN_CANISTER.get()))
//                .save(recipeOutput, "dehydrated_apple_crafting");


//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Smelting and Blasting
        oreSmelting(recipeOutput, TIN_SMELTABLES, RecipeCategory.MISC, ModItems.TIN_INGOT.get(),0.5f, 400, "tin_ingot");
        oreBlasting(recipeOutput, TIN_SMELTABLES, RecipeCategory.MISC, ModItems.TIN_INGOT.get(),0.5f, 200, "tin_ingot");
        oreSmelting(recipeOutput, ALUMINIUM_SMELTABLES, RecipeCategory.MISC, ModItems.ALUMINIUM_INGOT.get(),0.7f, 400, "aluminium_ingot");
        oreBlasting(recipeOutput, ALUMINIUM_SMELTABLES, RecipeCategory.MISC, ModItems.ALUMINIUM_INGOT.get(),0.7f, 200, "aluminium_ingot");
        oreSmelting(recipeOutput, ModItems.RAW_IRIDIUM, RecipeCategory.MISC, ModItems.IRIDIUM_INGOT.get(),1.8f, 400, "iridium_ingot");
        oreBlasting(recipeOutput, ModItems.RAW_IRIDIUM, RecipeCategory.MISC, ModItems.IRIDIUM_INGOT.get(),1.8f, 200, "iridium_ingot");
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Special Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.ARC_LAMP.get())
                .pattern("SGS")
                .pattern("GRG")
                .pattern("SGS")
                .define('S', ModItems.COMPRESSED_STEEL.get())
                .define('G', Items.GLOWSTONE_DUST)
                .define('R', Blocks.REDSTONE_BLOCK)
                .unlockedBy("has_compressed_steel", has(ModItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "arc_lamp_crafting");


        //Building Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TIN_BUILDING_BLOCK.get())
                .pattern("SS ")
                .pattern("SS ")
                .pattern(" T ")
                .define('T', ModItems.COMPRESSED_TIN.get())
                .define('S', Blocks.STONE)
                .unlockedBy("has_compressed_tin", has(ModItems.COMPRESSED_TIN.get()))
                .save(recipeOutput, "tin_building_block_crafting");
        //Walls
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TIN_BUILDING_WALL.get(), 6)
                .pattern("TTT")
                .pattern("TTT")
                .define('T', ModBlocks.TIN_BUILDING_BLOCK.get())
                .unlockedBy("has_tin_building_block", has(ModBlocks.TIN_BUILDING_BLOCK.get()))
                .save(recipeOutput, "tin_building_wall_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_ROCK_WALL.get(), 6)
                .pattern("LLL")
                .pattern("LLL")
                .define('L', ModBlocks.MOON_ROCK.get())
                .unlockedBy("has_moon_rock", has(ModBlocks.MOON_ROCK.get()))
                .save(recipeOutput, "moon_rock_wall_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARS_COBBLESTONE_WALL.get(), 6)
                .pattern("MMM")
                .pattern("MMM")
                .define('M', ModBlocks.MARS_COBBLESTONE.get())
                .unlockedBy("has_mars_cobblestone", has(ModBlocks.MARS_COBBLESTONE.get()))
                .save(recipeOutput, "mars_cobblestone_wall_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_DUNGEON_BRICK_WALL.get(), 6)
                .pattern("LLL")
                .pattern("LLL")
                .define('L', ModBlocks.MOON_DUNGEON_BRICKS.get())
                .unlockedBy("has_moon_dungeon_bricks", has(ModBlocks.MOON_DUNGEON_BRICKS.get()))
                .save(recipeOutput, "moon_dungeon_brick_wall_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARS_DUNGEON_BRICK_WALL.get(), 6)
                .pattern("MMM")
                .pattern("MMM")
                .define('M', ModBlocks.MARS_DUNGEON_BRICKS.get())
                .unlockedBy("has_mars_dungeon_bricks", has(ModBlocks.MARS_DUNGEON_BRICKS.get()))
                .save(recipeOutput, "mars_dungeon_brick_wall_crafting");

        //Stairs
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TIN_BUILDING_STAIRS.get(), 4)
                .pattern("T  ")
                .pattern("TT ")
                .pattern("TTT")
                .define('T', ModBlocks.TIN_BUILDING_BLOCK.get())
                .unlockedBy("has_tin_building_block", has(ModBlocks.TIN_BUILDING_BLOCK.get()))
                .save(recipeOutput, "tin_building_stairs_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_ROCK_STAIRS.get(), 4)
                .pattern("L  ")
                .pattern("LL ")
                .pattern("LLL")
                .define('L', ModBlocks.MOON_ROCK.get())
                .unlockedBy("has_moon_rock", has(ModBlocks.MOON_ROCK.get()))
                .save(recipeOutput, "moon_rock_stairs_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_DUNGEON_BRICK_STAIRS.get(), 4)
                .pattern("L  ")
                .pattern("LL ")
                .pattern("LLL")
                .define('L', ModBlocks.MOON_DUNGEON_BRICKS.get())
                .unlockedBy("has_moon_dungeon_bricks", has(ModBlocks.MOON_DUNGEON_BRICKS.get()))
                .save(recipeOutput, "moon_dungeon_brick_stairs_crafting");

        //Slabs
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TIN_BUILDING_SLAB.get(), 6)
                .pattern("TTT")
                .define('T', ModBlocks.TIN_BUILDING_BLOCK.get())
                .unlockedBy("has_tin_building_block", has(ModBlocks.TIN_BUILDING_BLOCK.get()))
                .save(recipeOutput, "tin_building_slab_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_ROCK_SLAB.get(), 6)
                .pattern("LLL")
                .define('L', ModBlocks.MOON_ROCK.get())
                .unlockedBy("has_moon_rock", has(ModBlocks.MOON_ROCK.get()))
                .save(recipeOutput, "moon_rock_slab_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOON_DUNGEON_BRICK_SLAB.get(), 6)
                .pattern("LLL")
                .define('L', ModBlocks.MOON_DUNGEON_BRICKS.get())
                .unlockedBy("has_moon_dungeon_bricks", has(ModBlocks.MOON_DUNGEON_BRICKS.get()))
                .save(recipeOutput, "moon_dungeon_brick_slab_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARS_COBBLESTONE_SLAB.get(), 6)
                .pattern("MMM")
                .define('M', ModBlocks.MARS_COBBLESTONE.get())
                .unlockedBy("has_mars_cobblestone", has(ModBlocks.MARS_COBBLESTONE.get()))
                .save(recipeOutput, "mars_cobblestone_slab_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MARS_DUNGEON_BRICK_SLAB.get(), 6)
                .pattern("MMM")
                .define('M', ModBlocks.MARS_DUNGEON_BRICKS.get())
                .unlockedBy("has_mars_dungeon_bricks", has(ModBlocks.MARS_DUNGEON_BRICKS.get()))
                .save(recipeOutput, "mars_dungeon_brick_slab_crafting");







    }
//----------------------------------------------------------------------------------------------------------------------
    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, ingredients, category, result, experience, cookingTime, group, "_from_smelting");
    }
    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, ingredients, category, result, experience, cookingTime, group, "_from_blasting");
    }
    protected static void oreSmelting(RecipeOutput recipeOutput, ItemLike ingredient, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, ingredient, category, result, experience, cookingTime, group, "_from_smelting");
    }
    protected static void oreBlasting(RecipeOutput recipeOutput, ItemLike ingredient, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, ingredient, category, result, experience, cookingTime, group, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> recipeFactory, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group, String suffix) {
        for(ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{itemlike}), category, result, experience, cookingTime, serializer, recipeFactory).group(group).unlockedBy(getHasName(itemlike), has(itemlike)).save(recipeOutput, GalacticraftCore.MOD_ID + ":" + getItemName(result) + suffix + "_" + getItemName(itemlike));
        }
    }
    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> recipeFactory, ItemLike ingredient, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group, String suffix) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ingredient), category, result, experience, cookingTime, serializer, recipeFactory)
                .group(group)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(recipeOutput, GalacticraftCore.MOD_ID + ":" + getItemName(result) + suffix + "_" + getItemName(ingredient));
    }
}
