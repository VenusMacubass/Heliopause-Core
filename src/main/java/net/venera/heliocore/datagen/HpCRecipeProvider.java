package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.recipe.CoalCompressorRecipeBuilder;
import net.venera.heliocore.util.HpCTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HpCRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public HpCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        List<ItemLike> ALUMINIUM_SMELTABLES = List.of(HpCBlocks.ALUMINIUM_ORE, HpCItems.RAW_ALUMINIUM);
        List<ItemLike> TIN_SMELTABLES = List.of(HpCBlocks.TIN_ORE, HpCItems.RAW_TIN);

        //region Metals
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HpCItems.TIN_INGOT.get(), 9)
                .requires(HpCBlocks.TIN_BLOCK.get(), 1)
                .unlockedBy("has_tin_block", has(HpCBlocks.TIN_BLOCK.get()))
                .save(recipeOutput, "tin_ingots_from_block");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCBlocks.TIN_BLOCK.get())
                .pattern("TTT")
                .pattern("TTT")
                .pattern("TTT")
                .define('T', HpCItems.TIN_INGOT.get())
                .unlockedBy("has_tin_ingots", has(HpCItems.TIN_INGOT.get()))
                .save(recipeOutput, "tin_block_from_ingots");
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HpCItems.ALUMINIUM_INGOT.get(), 9)
                .requires(HpCBlocks.ALUMINIUM_BLOCK.get(), 1)
                .unlockedBy("has_aluminium_block", has(HpCBlocks.ALUMINIUM_BLOCK.get()))
                .save(recipeOutput, "aluminium_ingots_from_block");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCBlocks.ALUMINIUM_BLOCK.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', HpCItems.ALUMINIUM_INGOT.get())
                .unlockedBy("has_aluminium_ingot", has(HpCItems.ALUMINIUM_INGOT.get()))
                .save(recipeOutput, "aluminium_block_from_ingots");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HpCItems.IRIDIUM_INGOT.get(), 9)
                .requires(HpCBlocks.IRIDIUM_BLOCK.get(),    1)
                .unlockedBy("has_iridium_block", has(HpCBlocks.IRIDIUM_BLOCK.get()))
                .save(recipeOutput, "iridium_ingots_from_block");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCBlocks.IRIDIUM_BLOCK.get())
                .pattern("III")
                .pattern("III")
                .pattern("III").define('I', HpCItems.IRIDIUM_INGOT.get())
                .unlockedBy("has_iridium_ingot", has(HpCItems.IRIDIUM_INGOT.get()))
                .save(recipeOutput, "iridium_block_from_ingots");
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HpCItems.RAW_SILICON.get(), 9)
                .requires(HpCBlocks.SILICON_BLOCK.get(),    1)
                .unlockedBy("has_silicon_block", has(HpCBlocks.SILICON_BLOCK.get()))
                .save(recipeOutput, "raw_silicon_from_silicon_block");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCBlocks.SILICON_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS").define('S', HpCItems.RAW_SILICON.get())
                .unlockedBy("has_raw_silicon", has(HpCItems.RAW_SILICON.get()))
                .save(recipeOutput, "silicon_block_from_raw_silicon");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HpCItems.RAW_TIN.get(), 9)
                .requires(HpCBlocks.RAW_TIN_BLOCK.get(),    1)
                .unlockedBy("has_raw_tin_block", has(HpCBlocks.RAW_TIN_BLOCK.get()))
                .save(recipeOutput, "raw_tin_from_block");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCBlocks.RAW_TIN_BLOCK.get())
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .define('R', HpCItems.RAW_TIN.get())
                .unlockedBy("has_raw_tin", has(HpCItems.RAW_TIN.get()))
                .save(recipeOutput, "raw_tin_block_crafting");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HpCItems.RAW_ALUMINIUM.get(), 9)
                .requires(HpCBlocks.RAW_ALUMINIUM_BLOCK.get(),    1)
                .unlockedBy("has_raw_aluminium_block", has(HpCBlocks.RAW_ALUMINIUM_BLOCK.get()))
                .save(recipeOutput, "raw_aluminium_from_block");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCBlocks.RAW_ALUMINIUM_BLOCK.get())
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .define('R', HpCItems.RAW_ALUMINIUM.get())
                .unlockedBy("has_raw_aluminium", has(HpCItems.RAW_ALUMINIUM.get()))
                .save(recipeOutput, "raw_aluminium_block_crafting");
        //endregion

        //region Ingredients
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCItems.COPPER_CANISTER.get(), 2)
                .pattern("C C")
                .pattern("C C")
                .pattern("CCC")
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(recipeOutput, "copper_canister_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCItems.TIN_CANISTER.get(), 2)
                .pattern("T T")
                .pattern("T T")
                .pattern("TTT")
                .define('T', HpCItems.TIN_INGOT.get())
                .unlockedBy("has_tin_ingot", has(HpCItems.TIN_INGOT.get()))
                .save(recipeOutput, "tin_canister_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HpCItems.HYDROCARBONS.get(), 3)
                .requires(Items.COAL, 1)
                .unlockedBy("has_coal", has(Items.COAL))
                .save(recipeOutput, "hydrocarbons_from_coal_crafting");
        //endregion

        //region Tools
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, HpCItems.STEEL_SWORD.get())
                .pattern(" C ")
                .pattern(" C ")
                .pattern(" S ")
                .define('C', HpCItems.COMPRESSED_STEEL.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_sword_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, HpCItems.GLASS_SWORD.get())
                .pattern(" C ")
                .pattern(" C ")
                .pattern(" S ")
                .define('C', Items.GLASS)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "glass_sword_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HpCItems.STEEL_AXE.get())
                .pattern(" CC")
                .pattern(" SC")
                .pattern(" S ")
                .define('C', HpCItems.COMPRESSED_STEEL.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_axe_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HpCItems.STEEL_PICKAXE.get())
                .pattern("CCC")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', HpCItems.COMPRESSED_STEEL.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_pickaxe_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HpCItems.STEEL_SHOVEL.get())
                .pattern(" C ")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', HpCItems.COMPRESSED_STEEL.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_shovel_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HpCItems.STEEL_HOE.get())
                .pattern(" CC")
                .pattern(" S ")
                .pattern(" S ")
                .define('C', HpCItems.COMPRESSED_STEEL.get())
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_hoe_crafting");
        //endregion

        //region Armors
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, HpCItems.STEEL_HELMET.get())
                .pattern("SSS")
                .pattern("S S")
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_helmet_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, HpCItems.STEEL_CHESTPLATE.get())
                .pattern("S S")
                .pattern("SSS")
                .pattern("SSS")
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_chestplate_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, HpCItems.STEEL_LEGGINGS.get())
                .pattern("SSS")
                .pattern("S S")
                .pattern("S S")
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_leggings_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, HpCItems.STEEL_BOOTS.get())
                .pattern("S S")
                .pattern("S S")
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_boots_crafting");
        //endregion

        //region Special Tools
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HpCItems.STANDARD_WRENCH)
                .pattern("  S")
                .pattern(" C ")
                .pattern("C  ")
                .define('C', HpCItems.COMPRESSED_BRONZE.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_bronze", has(HpCItems.COMPRESSED_BRONZE.get()))
                .save(recipeOutput, "standard_wrench_crafting");
        //endregion

        //region Foods
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HpCItems.DEHYDRATED_APPLE.get())
                .pattern("CA ")
                .pattern("A  ")
                .define('C', HpCItems.TIN_CANISTER.get())
                .define('A', Items.APPLE)
                .unlockedBy("has_tin_canister", has(HpCItems.TIN_CANISTER.get()))
                .save(recipeOutput, "dehydrated_apple_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HpCItems.DEHYDRATED_POTATO.get())
                .pattern("CP ")
                .pattern("P  ")
                .define('C', HpCItems.TIN_CANISTER.get())
                .define('P', Items.POTATO)
                .unlockedBy("has_tin_canister", has(HpCItems.TIN_CANISTER.get()))
                .save(recipeOutput, "dehydrated_potato_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HpCItems.DEHYDRATED_BEEF.get())
                .pattern("CB ")
                .pattern("B  ")
                .pattern("   ")
                .define('C', HpCItems.TIN_CANISTER.get())
                .define('B', Items.BEEF)
                .unlockedBy("has_tin_canister", has(HpCItems.TIN_CANISTER.get()))
                .save(recipeOutput, "dehydrated_beef_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HpCItems.HAMBURGER.get())
                .pattern(" B ")
                .pattern("CP ")
                .pattern(" B ")
                .define('C', HpCItems.CHEESE_SLICE.get())
                .define('B', Items.BREAD)
                .define('P', Items.COOKED_BEEF)
                .unlockedBy("has_patty", has(Items.COOKED_BEEF))
                .save(recipeOutput, "cheeseburger_crafting");

        //endregion
        
        //region Smelting and Blasting
        oreSmelting(recipeOutput, TIN_SMELTABLES, RecipeCategory.MISC, HpCItems.TIN_INGOT.get(),0.5f, 400, "tin_ingot");
        oreBlasting(recipeOutput, TIN_SMELTABLES, RecipeCategory.MISC, HpCItems.TIN_INGOT.get(),0.5f, 200, "tin_ingot");
        oreSmelting(recipeOutput, ALUMINIUM_SMELTABLES, RecipeCategory.MISC, HpCItems.ALUMINIUM_INGOT.get(),0.7f, 400, "aluminium_ingot");
        oreBlasting(recipeOutput, ALUMINIUM_SMELTABLES, RecipeCategory.MISC, HpCItems.ALUMINIUM_INGOT.get(),0.7f, 200, "aluminium_ingot");
        oreSmelting(recipeOutput, HpCBlocks.IRIDIUM_ORE, RecipeCategory.MISC, HpCItems.IRIDIUM_INGOT.get(),1.8f, 400, "iridium_ingot");
        oreBlasting(recipeOutput, HpCBlocks.IRIDIUM_ORE, RecipeCategory.MISC, HpCItems.IRIDIUM_INGOT.get(),1.8f, 200, "iridium_ingot");
        oreSmelting(recipeOutput, HpCBlocks.MOON_TEKTITES_REGOLITH, RecipeCategory.MISC, HpCItems.TEKTITES.get(),2.5f, 400, "regolith_tektite_shards");
        oreBlasting(recipeOutput, HpCBlocks.MOON_TEKTITES_REGOLITH, RecipeCategory.MISC, HpCItems.TEKTITES.get(),2.5f, 200, "regolith_tektite_shards");
        oreSmelting(recipeOutput, HpCBlocks.MOON_TEKTITES, RecipeCategory.MISC, HpCItems.TEKTITES.get(),2.5f, 400, "tektite_shards");
        oreBlasting(recipeOutput, HpCBlocks.MOON_TEKTITES, RecipeCategory.MISC, HpCItems.TEKTITES.get(),2.5f, 200, "tektite_shards");
        //endregion
        
        //region Low Functionality Items and Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HpCBlocks.ARC_LAMP.get())
                .pattern(" P ")
                .pattern("HGH")
                .pattern(" W ")
                .define('H', HpCItems.HYDROCARBONS.get())
                .define('P', Items.GLASS_PANE)
                .define('G', Blocks.GLOWSTONE)
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .unlockedBy("has_glowstone", has(Blocks.GLOWSTONE))
                .save(recipeOutput, "arc_lamp_crafting");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HpCBlocks.MAGNETIC_CRAFTING_TABLE.get(), 1)
                .requires(HpCItems.COMPRESSED_IRON.get(), 1).requires(Items.CRAFTING_TABLE, 1)
                .unlockedBy("has_compressed_iron", has(HpCItems.COMPRESSED_IRON.get()))
                .save(recipeOutput, "magnetic_crafting_table_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.COPPER_WIRE.get(), 6)
                .pattern("PPP")
                .pattern("CCC")
                .pattern("PPP")
                .define('P', HpCItems.HYDROCARBONS.get())
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "copper_wire_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.FLUID_PIPE.get())
                .pattern("GGG")
                .pattern("   ")
                .pattern("GGG")
                .define('G', Items.GLASS_PANE)
                .unlockedBy("has_glass_pane", has(Items.GLASS_PANE))
                .save(recipeOutput, "fluid_pipe_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.COPPER_WIRE_BLOCK.get())
                .pattern("   ")
                .pattern("WBW")
                .pattern("   ")
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .define('B', HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get())
                .unlockedBy("has_wire", has(HpCBlocks.COPPER_WIRE.get()))
                .save(recipeOutput, "copper_wire_block_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.FLUID_PIPE_BLOCK.get())
                .pattern("   ")
                .pattern("PBP")
                .pattern("   ")
                .define('P', HpCBlocks.FLUID_PIPE.get())
                .define('B', HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get())
                .unlockedBy("has_fluid_pipe", has(HpCBlocks.FLUID_PIPE.get()))
                .save(recipeOutput, "fluid_pipe_block_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.LAUNCH_PAD.get(), 9)
                .pattern("CCC")
                .pattern("III")
                .define('C', HpCItems.COMPRESSED_IRON.get())
                .define('I', Blocks.IRON_BLOCK)
                .unlockedBy("has_compressed_iron", has(HpCItems.COMPRESSED_IRON.get()))
                .save(recipeOutput, "launch_pad_crafting");
        //endregion

        //region Building Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.PRISMATIC_GLASS.get(), 2)
                .pattern(" TT")
                .pattern(" TT")
                .pattern("   ")
                .define('T', HpCItems.TEKTITES.get())
                .unlockedBy("has_tektites", has(HpCItems.TEKTITES.get()))
                .save(recipeOutput, "prismatic_glass_from_tekties_4_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get(), 4)
                .pattern("SS ")
                .pattern("SS ")
                .pattern(" T ")
                .define('T', HpCItems.COMPRESSED_TIN.get())
                .define('S', HpCTags.Items.STONES)
                .unlockedBy("has_compressed_tin", has(HpCItems.COMPRESSED_TIN.get()))
                .save(recipeOutput, "base_building_block_black_crafting");

        SingleItemRecipeBuilder.stonecutting(
                        Ingredient.of(HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get()), //Input
                        RecipeCategory.BUILDING_BLOCKS,
                        HpCBlocks.BASE_BUILDING_WHITE_BLOCK.get() //Output
                ).unlockedBy("has_black_block", has(HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get()))
                .save(recipeOutput, "white_block_from_black_block_stonecutting");
        //Walls
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.BASE_BUILDING_WALL_WHITE.get(), 6)
                .pattern("TTT")
                .pattern("TTT")
                .define('T', HpCBlocks.BASE_BUILDING_WHITE_BLOCK.get())
                .unlockedBy("has_base_building_block", has(HpCBlocks.BASE_BUILDING_WHITE_BLOCK.get()))
                .save(recipeOutput, "base_building_white_wall_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.BASE_BUILDING_WALL_BLACK.get(), 6)
                .pattern("TTT")
                .pattern("TTT")
                .define('T', HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get())
                .unlockedBy("has_base_building_block", has(HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get()))
                .save(recipeOutput, "base_building_black_wall_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.MOON_ROCK_WALL.get(), 6)
                .pattern("LLL")
                .pattern("LLL")
                .define('L', HpCBlocks.MOON_ROCK.get())
                .unlockedBy("has_moon_rock", has(HpCBlocks.MOON_ROCK.get()))
                .save(recipeOutput, "moon_rock_wall_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.MOON_DUNGEON_BRICK_WALL.get(), 6)
                .pattern("LLL")
                .pattern("LLL")
                .define('L', HpCBlocks.MOON_DUNGEON_BRICKS.get())
                .unlockedBy("has_moon_dungeon_bricks", has(HpCBlocks.MOON_DUNGEON_BRICKS.get()))
                .save(recipeOutput, "moon_dungeon_brick_wall_crafting");
       

        //Stairs
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.BASE_BUILDING_STAIRS_WHITE.get(), 4)
                .pattern("T  ")
                .pattern("TT ")
                .pattern("TTT")
                .define('T', HpCBlocks.BASE_BUILDING_WHITE_BLOCK.get())
                .unlockedBy("has_base_building_block", has(HpCBlocks.BASE_BUILDING_WHITE_BLOCK.get()))
                .save(recipeOutput, "base_building_stairs_white_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.BASE_BUILDING_STAIRS_BLACK.get(), 4)
                .pattern("T  ")
                .pattern("TT ")
                .pattern("TTT")
                .define('T', HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get())
                .unlockedBy("has_base_building_block", has(HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get()))
                .save(recipeOutput, "base_building_stairs_black_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.MOON_ROCK_STAIRS.get(), 4)
                .pattern("L  ")
                .pattern("LL ")
                .pattern("LLL")
                .define('L', HpCBlocks.MOON_ROCK.get())
                .unlockedBy("has_moon_rock", has(HpCBlocks.MOON_ROCK.get()))
                .save(recipeOutput, "moon_rock_stairs_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.MOON_DUNGEON_BRICK_STAIRS.get(), 4)
                .pattern("L  ")
                .pattern("LL ")
                .pattern("LLL")
                .define('L', HpCBlocks.MOON_DUNGEON_BRICKS.get())
                .unlockedBy("has_moon_dungeon_bricks", has(HpCBlocks.MOON_DUNGEON_BRICKS.get()))
                .save(recipeOutput, "moon_dungeon_brick_stairs_crafting");

        //Slabs
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.BASE_BUILDING_SLAB_WHITE.get(), 6)
                .pattern("TTT")
                .define('T', HpCBlocks.BASE_BUILDING_WHITE_BLOCK.get())
                .unlockedBy("has_base_building_block", has(HpCBlocks.BASE_BUILDING_WHITE_BLOCK.get()))
                .save(recipeOutput, "base_building_slab_white_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.BASE_BUILDING_SLAB_BLACK.get(), 6)
                .pattern("TTT")
                .define('T', HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get())
                .unlockedBy("has_base_building_block", has(HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get()))
                .save(recipeOutput, "tin_building_slab_black_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.MOON_ROCK_SLAB.get(), 6)
                .pattern("LLL")
                .define('L', HpCBlocks.MOON_ROCK.get())
                .unlockedBy("has_moon_rock", has(HpCBlocks.MOON_ROCK.get()))
                .save(recipeOutput, "moon_rock_slab_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.MOON_DUNGEON_BRICK_SLAB.get(), 6)
                .pattern("LLL")
                .define('L', HpCBlocks.MOON_DUNGEON_BRICKS.get())
                .unlockedBy("has_moon_dungeon_bricks", has(HpCBlocks.MOON_DUNGEON_BRICKS.get()))
                .save(recipeOutput, "moon_dungeon_brick_slab_crafting");
        //endregion
        
        //region Compressors
        CoalCompressorRecipeBuilder.compress(RecipeCategory.MISC, HpCItems.COMPRESSED_COPPER.get())
                .pattern("C  ")
                .pattern("C  ")
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_copper", has(Items.COPPER_INGOT))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":compressed_copper_compressor");
        CoalCompressorRecipeBuilder.compress(RecipeCategory.MISC, HpCItems.COMPRESSED_TIN.get())
                .pattern("T  ")
                .pattern("T  ")
                .define('T', HpCItems.TIN_INGOT.get())
                .unlockedBy("has_tin", has(HpCItems.TIN_INGOT.get()))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":compressed_tin_compressor");
        CoalCompressorRecipeBuilder.compress(RecipeCategory.MISC, HpCItems.COMPRESSED_BRONZE.get())
                .pattern("CT  ")
                .define('C', HpCItems.COMPRESSED_COPPER.get())
                .define('T', HpCItems.COMPRESSED_TIN.get())
                .unlockedBy("has_compressed_tin", has(HpCItems.COMPRESSED_TIN.get()))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":compressed_bronze_compressor");
        CoalCompressorRecipeBuilder.compress(RecipeCategory.MISC, HpCItems.COMPRESSED_IRON.get())
                .pattern("I  ")
                .pattern("I  ")
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":compressed_iron_compressor");
        CoalCompressorRecipeBuilder.compress(RecipeCategory.MISC, HpCItems.COMPRESSED_ALUMINIUM.get())
                .pattern("A  ")
                .pattern("A  ")
                .define('A', HpCItems.ALUMINIUM_INGOT.get())
                .unlockedBy("has_aluminium", has(HpCItems.ALUMINIUM_INGOT.get()))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":compressed_aluminium_compressor");
        CoalCompressorRecipeBuilder.compress(RecipeCategory.MISC, HpCItems.COMPRESSED_STEEL.get())
                .pattern("C  ")
                .pattern("I  ")
                .pattern("C  ")
                .define('C', Items.COAL)
                .define('I', HpCItems.COMPRESSED_IRON.get())
                .unlockedBy("has_compressed_iron", has(HpCItems.COMPRESSED_IRON.get()))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":compressed_steel_compressor");
        CoalCompressorRecipeBuilder.compress(RecipeCategory.MISC, HpCItems.COMPRESSED_HD_PLATE.get())
                .pattern("SAB")
                .pattern("SAB")
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('A', HpCItems.COMPRESSED_ALUMINIUM.get())
                .define('B', HpCItems.COMPRESSED_BRONZE.get())
                .unlockedBy("has_bronze", has(HpCItems.COMPRESSED_BRONZE.get()))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":compressed_hd_plate_compressor");
        CoalCompressorRecipeBuilder.compress(RecipeCategory.MISC, HpCItems.COMPRESSED_IRIDIUM.get())
                .pattern("I")
                .define('I', HpCItems.IRIDIUM_INGOT.get())
                .unlockedBy("has_iridium", has(HpCItems.IRIDIUM_INGOT.get()))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":compressed_iridium_compressor");
        
        //endregion


        

    }
    //region Helpers
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
            SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{itemlike}), category, result, experience, cookingTime, serializer, recipeFactory).group(group).unlockedBy(getHasName(itemlike), has(itemlike)).save(recipeOutput, HeliopauseCore.MOD_ID + ":" + getItemName(result) + suffix + "_" + getItemName(itemlike));
        }
    }
    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> serializer, AbstractCookingRecipe.Factory<T> recipeFactory, ItemLike ingredient, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group, String suffix) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(ingredient), category, result, experience, cookingTime, serializer, recipeFactory)
                .group(group)
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":" + getItemName(result) + suffix + "_" + getItemName(ingredient));
    }
    //endregion
}
