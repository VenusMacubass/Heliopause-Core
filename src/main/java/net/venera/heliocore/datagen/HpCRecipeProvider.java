package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
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
import net.venera.heliocore.item.HpCTags;
import net.venera.heliocore.recipe.MagneticAssemblyPlatformRecipeBuilder;
import net.venera.heliocore.recipe.PCBFabricatorRecipeBuilder;
import net.venera.heliocore.recipe.item.CannedStewRecipe;
import net.venera.heliocore.recipe.item.SpaceSuitRecipeBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HpCRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public HpCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        List<ItemLike> TEKTITES = List.of(HpCBlocks.MOON_TEKTITES_REGOLITH, HpCBlocks.MOON_TEKTITES);
        List<ItemLike> TIN_SMELTABLES = List.of(HpCBlocks.MOON_TIN_ORE, HpCBlocks.TIN_ORE, HpCBlocks.DEEPSLATE_TIN_ORE, HpCItems.RAW_TIN);
        List<ItemLike> ALUMINIUM_SMELTABLES = List.of(HpCBlocks.MOON_ALUMINIUM_ORE, HpCBlocks.DEEPSLATE_ALUMINIUM_ORE, HpCBlocks.ALUMINIUM_ORE, HpCItems.RAW_ALUMINIUM);
        List<ItemLike> SILICON_SMELTABLES = List.of(HpCBlocks.MOON_SILICON_ORE, HpCBlocks.SILICON_ORE, HpCBlocks.DEEPSLATE_SILICON_ORE);
        List<ItemLike> IRIDIUM_SMELTABLES = List.of(HpCBlocks.MOON_IRIDIUM_ORE, HpCBlocks.IRIDIUM_ORE, HpCBlocks.DEEPSLATE_IRIDIUM_ORE);
        
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
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HpCItems.SILICON.get(), 9)
                .requires(HpCBlocks.SILICON_BLOCK.get(),    1)
                .unlockedBy("has_silicon_block", has(HpCBlocks.SILICON_BLOCK.get()))
                .save(recipeOutput, "raw_silicon_from_silicon_block");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCBlocks.SILICON_BLOCK.get())
                .pattern("SSS")
                .pattern("SSS")
                .pattern("SSS").define('S', HpCItems.SILICON.get())
                .unlockedBy("has_raw_silicon", has(HpCItems.SILICON.get()))
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
        
        //region Smelting and Blasting
        oreSmelting(recipeOutput, HpCBlocks.MOON_COPPER_ORE, RecipeCategory.MISC, Items.COPPER_INGOT,0.5f, 400, "copper_ingot");
        oreBlasting(recipeOutput, HpCBlocks.MOON_COPPER_ORE, RecipeCategory.MISC, Items.COPPER_INGOT,0.5f, 200, "copper_ingot");
        oreSmelting(recipeOutput, TIN_SMELTABLES, RecipeCategory.MISC, HpCItems.TIN_INGOT.get(),0.5f, 400, "tin_ingot");
        oreBlasting(recipeOutput, TIN_SMELTABLES, RecipeCategory.MISC, HpCItems.TIN_INGOT.get(),0.5f, 200, "tin_ingot");
        oreSmelting(recipeOutput, ALUMINIUM_SMELTABLES, RecipeCategory.MISC, HpCItems.ALUMINIUM_INGOT.get(),0.7f, 400, "aluminium_ingot");
        oreBlasting(recipeOutput, ALUMINIUM_SMELTABLES, RecipeCategory.MISC, HpCItems.ALUMINIUM_INGOT.get(),0.7f, 200, "aluminium_ingot");
        oreSmelting(recipeOutput, HpCBlocks.MOON_IRON_ORE, RecipeCategory.MISC, Items.IRON_INGOT,0.5f, 400, "iron_ingot");
        oreBlasting(recipeOutput, HpCBlocks.MOON_IRON_ORE, RecipeCategory.MISC, Items.IRON_INGOT,0.5f, 200, "iron_ingot");
        oreSmelting(recipeOutput, SILICON_SMELTABLES, RecipeCategory.MISC, HpCItems.SILICON.get(),0.5f, 400, "silicon");
        oreBlasting(recipeOutput, SILICON_SMELTABLES, RecipeCategory.MISC, HpCItems.SILICON.get(),0.5f, 200, "silicon");
        oreSmelting(recipeOutput, IRIDIUM_SMELTABLES, RecipeCategory.MISC, HpCItems.IRIDIUM_INGOT.get(),1.8f, 400, "iridium_ingot");
        oreBlasting(recipeOutput, IRIDIUM_SMELTABLES, RecipeCategory.MISC, HpCItems.IRIDIUM_INGOT.get(),1.8f, 200, "iridium_ingot");
        oreSmelting(recipeOutput, TEKTITES, RecipeCategory.MISC, HpCItems.TEKTITES.get(),2.5f, 400, "regolith_tektite_shards");
        oreBlasting(recipeOutput, TEKTITES, RecipeCategory.MISC, HpCItems.TEKTITES.get(),2.5f, 200, "regolith_tektite_shards");
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
        
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HpCItems.PETROCHEMICALS.get(), 3)
                .requires(Items.COAL, 1)
                .unlockedBy("has_coal", has(Items.COAL))
                .save(recipeOutput, "hydrocarbons_from_coal_crafting");
        
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCItems.AIR_FAN.get())
                .pattern("I I")
                .pattern(" S ")
                .pattern("IRI")
                .define('I', HpCItems.COMPRESSED_IRON.get())
                .define('R', Items.REDSTONE)
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "air_fan_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCItems.FLUID_FILTER.get())
                .pattern("TT")
                .pattern("TS")
                .define('T', HpCItems.COMPRESSED_TIN.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "fluid_filter_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCItems.GAS_REGULATOR.get())
                .pattern("S S")
                .pattern("TBT")
                .pattern(" F ")
                .define('T', HpCItems.COMPRESSED_TIN.get())
                .define('B', HpCItems.BASIC_CIRCUIT_BOARD.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('F', HpCItems.FLUID_FILTER.get())
                .unlockedBy("has_fluid_filter", has(HpCItems.FLUID_FILTER.get()))
                .save(recipeOutput, "gas_regulator_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCItems.THERMAL_REGULATOR.get())
                .pattern("S S")
                .pattern("TBT")
                .pattern(" E ")
                .define('T', HpCItems.COMPRESSED_TIN.get())
                .define('B', HpCItems.BASIC_CIRCUIT_BOARD.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('E', HpCItems.ELECTROMAGNETIC_SENSORS.get())
                .unlockedBy("has_electromagnetic_sonsors", has(HpCItems.ELECTROMAGNETIC_SENSORS.get()))
                .save(recipeOutput, "thermal_regulator_crafting");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, HpCItems.ELECTROMAGNETIC_SENSORS.get())
                .requires(Items.GLASS_PANE)
                .requires(HpCItems.BASIC_CIRCUIT_BOARD.get())
                .requires(HpCBlocks.COPPER_WIRE.get())
                .unlockedBy("has_circuit_board", has(HpCItems.BASIC_CIRCUIT_BOARD.get()))
                .save(recipeOutput, "electromagnetic_sensors_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCItems.CANISTER.get())
                .pattern(" C ")
                .pattern("SGS")
                .pattern("TST")
                .define('T', HpCItems.COMPRESSED_TIN.get())
                .define('C', HpCItems.TIN_CANISTER.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('G', Items.GLASS_PANE)
                .unlockedBy("has_tin_canister", has(HpCItems.TIN_CANISTER.get()))
                .save(recipeOutput, "liquid_canister_crafting");
        
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCItems.SINGULAR_SOLAR_PANEL.get(),2)
                .pattern("GGG")
                .pattern("BBB")
                .pattern("WTW")
                .define('T', HpCItems.TIN_INGOT.get())
                .define('B', HpCItems.SOLAR_PANEL_SEMICONDUCTOR_BASE.get())
                .define('G', Items.GLASS_PANE)
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .unlockedBy("has_solar_semiconductor_base", has(HpCItems.SOLAR_PANEL_SEMICONDUCTOR_BASE.get()))
                .save(recipeOutput, "singular_solar_crafting");
        
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCItems.TIER_1_SOLAR_PANEL.get())
                .pattern("SS")
                .pattern("WW")
                .pattern("SS")
                .define('S', HpCItems.SINGULAR_SOLAR_PANEL.get())
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .unlockedBy("has_singular_solar_panel", has(HpCItems.SINGULAR_SOLAR_PANEL.get()))
                .save(recipeOutput, "tier_1_solar_panel_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCItems.STEEL_ROD.get(),2)
                .pattern("S")
                .pattern("S")
                .pattern("S")
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "steel_rod_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCItems.THERMAL_INSULATION_MATERIAL.get(), 4)
                .pattern(" W ")
                .pattern("WRW")
                .pattern(" F ")
                .define('W', Ingredient.of(ItemTags.WOOL))
                .define('F', Items.FEATHER)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(recipeOutput, "thermal_insulation_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCItems.RADIATION_PROTECTION_MATERIAL.get(), 2)
                .pattern("PAP")
                .pattern("PAP")
                .pattern("PAP")
                .define('P', HpCItems.PETROCHEMICALS.get())
                .define('A', HpCItems.COMPRESSED_ALUMINIUM.get())
                .unlockedBy("has_compressed_aluminium", has(HpCItems.COMPRESSED_ALUMINIUM.get()))
                .save(recipeOutput, "radiation_protection_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCItems.PRESSURE_PROTECTION_MATERIAL.get(), 2)
                .pattern("PFP")
                .pattern("PRP")
                .pattern("PWP")
                .define('P', HpCItems.PETROCHEMICALS.get())
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .define('R', ItemTags.WOOL)
                .define('F', HpCBlocks.FLUID_PIPE.get())
                .unlockedBy("has_petrochemicals", has(HpCItems.PETROCHEMICALS.get()))
                .save(recipeOutput, "pressure_protection_crafting");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.EMPTY_BAG.get(), 4)
                .requires(HpCItems.ALUMINIUM_INGOT, 1)
                .requires(HpCItems.PETROCHEMICALS, 1)
                .unlockedBy("has_aluminium", has(HpCItems.ALUMINIUM_INGOT.get()))
                .save(recipeOutput, "empty_bag_crafting");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.EMPTY_CAN.get(), 2)
                .requires(HpCItems.COMPRESSED_TIN, 1)
                .unlockedBy("has_compressed_tin", has(HpCItems.COMPRESSED_TIN.get()))
                .save(recipeOutput, "empty_can_crafting");
        //endregion
        
        //region Rocket Parts
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, HpCItems.T1_ROCKET_NOSE_CONE.get())
                .pattern(" R ")
                .pattern(" H ")
                .pattern("HHH")
                .define('R', HpCItems.STEEL_ROD.get())
                .define('H', HpCItems.COMPRESSED_HD_PLATE.get())
                .unlockedBy("has_steel_rod", has(ItemTags.WOOL))
                .save(recipeOutput, "t1_nose_cone_crafting");
//        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCItems.T1_ROCKET_NOSE_CONE.get())
//                .pattern(" R ")
//                .pattern(" H ")
//                .pattern("HHH")
//                .define('R', HpCItems.STEEL_ROD.get())
//                .define('H', HpCItems.COMPRESSED_HD_PLATE.get())
//                .unlockedBy("has_steel_rod", has(ItemTags.WOOL))
//                .save(recipeOutput, "t1_nose_cone_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, HpCItems.T1_ROCKET_BASE.get())
                .pattern("HCH")
                .pattern("HBH")
                .pattern("PHW")
                .define('P', HpCBlocks.FLUID_PIPE.get())
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .define('C', Items.CHEST)
                .define('B', HpCItems.BASIC_CIRCUIT_BOARD.get())
                .define('H', HpCItems.COMPRESSED_HD_PLATE.get())
                .unlockedBy("has_steel_rod", has(ItemTags.WOOL))
                .save(recipeOutput, "t1_base_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, HpCItems.T1_ROCKET_FIN.get())
                .pattern("A  ")
                .pattern("HA ")
                .pattern("HHA")
                .define('A', HpCItems.COMPRESSED_ALUMINIUM.get())
                .define('H', HpCItems.COMPRESSED_HD_PLATE.get())
                .unlockedBy("has_compressed_hd_plate", has(HpCItems.COMPRESSED_HD_PLATE.get()))
                .save(recipeOutput, "t1_fin_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, HpCItems.T1_ROCKET_ENGINE.get())
                .pattern("PHW")
                .pattern("HFH")
                .pattern("H H")
                .define('P', HpCBlocks.FLUID_PIPE.get())
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .define('F', Items.FLINT_AND_STEEL)
                .define('H', HpCItems.COMPRESSED_HD_PLATE.get())
                .unlockedBy("has_compressed_hd_plate", has(HpCItems.COMPRESSED_HD_PLATE.get()))
                .save(recipeOutput, "t1_engine_crafting");
        //endregion
        
        //region Machines
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.COAL_COMPRESSOR.get())
                .pattern("CXC")
                .pattern("IFI")
                .pattern("IAI")
                .define('A', Items.ANVIL)
                .define('C', Items.COPPER_INGOT)
                .define('X', HpCItems.ALUMINIUM_INGOT.get())
                .define('F', Items.FURNACE)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_furnace", has(Items.FURNACE))
                .save(recipeOutput, "coal_compressor_crafting");
        
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.REFINERY.get())
                .pattern("PXP")
                .pattern("TFT")
                .pattern("SSS")
                .define('P', HpCBlocks.FLUID_PIPE.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('F', Items.FURNACE)
                .define('X', HpCItems.FLUID_FILTER.get())
                .define('T', HpCBlocks.FLUID_TANK.get())
                .unlockedBy("has_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "refinery_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.ENERGY_GENERATOR.get())
                .pattern("CXC")
                .pattern("IFI")
                .pattern("ITI")
                .define('T', HpCBlocks.FLUID_TANK.get())
                .define('C', Items.COPPER_INGOT)
                .define('X', HpCItems.ALUMINIUM_INGOT.get())
                .define('F', Items.FURNACE)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_furnace", has(Items.FURNACE))
                .save(recipeOutput, "energy_generator_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.PCB_FABRICATOR.get())
                .pattern("SAS")
                .pattern("ICI")
                .pattern("WRW")
                .define('A', HpCItems.COMPRESSED_ALUMINIUM.get())
                .define('S', HpCItems.SILICON.get())
                .define('I', HpCItems.COMPRESSED_IRON.get())
                .define('C', Items.CRAFTING_TABLE)
                .define('R', Items.REDSTONE)
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .unlockedBy("has_silicon", has(HpCItems.SILICON.get()))
                .save(recipeOutput, "pcb_fabricator_crafting");
        
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.BASIC_SOLAR_PANEL.get())
                .pattern(" F ")
                .pattern("SPS")
                .pattern("WRW")
                .define('F', HpCItems.TIER_1_SOLAR_PANEL.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('P', HpCItems.STEEL_ROD.get())
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .define('R', HpCItems.SILICON.get())
                .unlockedBy("has_solar_panel_material", has(HpCItems.TIER_1_SOLAR_PANEL.get()))
                .save(recipeOutput, "basic_solar_panel_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.ENERGY_STORAGE_UNIT.get())
                .pattern("SBS")
                .pattern("WBW")
                .pattern("SBS")
                .define('B', HpCItems.SMALL_BATTERY.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .unlockedBy("has_small_battery", has(HpCItems.SMALL_BATTERY.get()))
                .save(recipeOutput, "energy_storage_unit_crafting");
        
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.CARGO_MANAGER.get())
                .pattern("SSS")
                .pattern("ACA")
                .pattern("WHW")
                .define('A', HpCItems.COMPRESSED_ALUMINIUM.get())
                .define('C', Items.CHEST)
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .define('H', Items.HOPPER)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(recipeOutput, "cargo_manager_crafting");
        
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.FUEL_MANAGER.get())
                .pattern("SSS")
                .pattern("AFA")
                .pattern("WBP")
                .define('A', HpCItems.COMPRESSED_ALUMINIUM.get())
                .define('F', HpCBlocks.FLUID_TANK.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('B', HpCItems.SMALL_BATTERY.get())
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .define('P', HpCBlocks.FLUID_PIPE.get())
                .unlockedBy("has_fluid_tank", has(HpCBlocks.FLUID_TANK.get()))
                .save(recipeOutput, "fuel_manager_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.GAS_COMPRESSOR.get())
                .pattern("SFS")
                .pattern("ATA")
                .pattern("SGS")
                .define('A', HpCItems.COMPRESSED_ALUMINIUM.get())
                .define('T', HpCBlocks.FLUID_TANK.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('G', HpCItems.GAS_REGULATOR.get())
                .define('F', HpCItems.AIR_FAN.get())
                .unlockedBy("has_fluid_tank", has(HpCBlocks.FLUID_TANK.get()))
                .save(recipeOutput, "gas_compressor_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.GAS_VAPORIZER.get())
                .pattern("SFS")
                .pattern("ATA")
                .pattern("SGS")
                .define('A', HpCItems.COMPRESSED_ALUMINIUM.get())
                .define('T', HpCBlocks.FLUID_TANK.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('G', HpCItems.GAS_REGULATOR.get())
                .define('F', HpCItems.FLUID_FILTER.get())
                .unlockedBy("has_fluid_tank", has(HpCBlocks.FLUID_TANK.get()))
                .save(recipeOutput, "gas_vaporizer_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.OXYGEN_GENERATOR.get())
                .pattern("SFS")
                .pattern("ATA")
                .pattern("SGS")
                .define('A', HpCItems.AIR_FAN.get())
                .define('T', HpCBlocks.FLUID_TANK.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('G', HpCItems.GAS_REGULATOR.get())
                .define('F', HpCItems.FLUID_FILTER.get())
                .unlockedBy("has_air_fan", has(HpCItems.AIR_FAN.get()))
                .save(recipeOutput, "oxygen_generator_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCBlocks.OXYGEN_SEALER.get())
                .pattern("SFS")
                .pattern("ATC")
                .pattern("SGS")
                .define('A', HpCItems.AIR_FAN.get())
                .define('T', HpCBlocks.FLUID_TANK.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('G', HpCItems.GAS_REGULATOR.get())
                .define('F', HpCItems.FLUID_FILTER.get())
                .define('C', HpCItems.COMPRESSED_ALUMINIUM.get())
                .unlockedBy("has_air_fan", has(HpCItems.AIR_FAN.get()))
                .save(recipeOutput, "oxygen_sealer_crafting");
        //endregion
        
        //region Low Functionality Items and Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCBlocks.FLUID_TANK.get())
                .pattern(" G ")
                .pattern("GHG")
                .pattern("SGS")
                .define('H', HpCItems.PETROCHEMICALS.get())
                .define('S', Items.GLASS)
                .define('G', HpCItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "fluid_tank_crafting");
        
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, HpCBlocks.ARC_LAMP.get())
                .pattern(" P ")
                .pattern("HGH")
                .pattern(" W ")
                .define('H', HpCItems.PETROCHEMICALS.get())
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
                .define('P', HpCItems.PETROCHEMICALS.get())
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_compressed_steel", has(HpCItems.COMPRESSED_STEEL.get()))
                .save(recipeOutput, "copper_wire_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.FLUID_PIPE.get(), 3)
                .pattern("GGG")
                .pattern("   ")
                .pattern("GGG")
                .define('G', Items.GLASS_PANE)
                .unlockedBy("has_glass_pane", has(Items.GLASS_PANE))
                .save(recipeOutput, "fluid_pipe_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.COPPER_WIRE_BLOCK.get())
                .pattern("WBW")
                .define('W', HpCBlocks.COPPER_WIRE.get())
                .define('B', HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get())
                .unlockedBy("has_wire", has(HpCBlocks.COPPER_WIRE.get()))
                .save(recipeOutput, "copper_wire_block_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.FLUID_PIPE_BLOCK.get())
                .pattern("PBP")
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

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.AIRLOCK_FRAME.get(), 5)
                .pattern("AAA")
                .pattern("SGS")
                .pattern("AAA")
                .define('A', HpCItems.COMPRESSED_ALUMINIUM.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('G', HpCItems.GAS_REGULATOR.get())
                .unlockedBy("has_compressed_aluminium", has(HpCItems.COMPRESSED_ALUMINIUM.get()))
                .save(recipeOutput, "airlock_frame_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.AIRLOCK_FRAME_SWITCH.get())
                .pattern("SSS")
                .pattern("IBI")
                .pattern("SSS")
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .define('B', HpCItems.BASIC_CIRCUIT_BOARD.get())
                .define('I', HpCItems.COMPRESSED_IRIDIUM.get())
                .unlockedBy("has_compressed_iridium", has(HpCItems.COMPRESSED_IRIDIUM.get()))
                .save(recipeOutput, "airlock_switch_crafting");
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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCItems.T1_THERMAL_INSULATION_HEAD.get())
                .pattern("TTT")
                .pattern("THT")
                .define('T', HpCItems.THERMAL_INSULATION_MATERIAL.get())
                .define('H', Items.LEATHER_HELMET)
                .unlockedBy("has_thermal_isolator", has(HpCItems.THERMAL_INSULATION_MATERIAL.get()))
                .save(recipeOutput, "t1_thermal_insulation_head_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, HpCItems.T1_THERMAL_INSULATION_TORSO.get())
                .pattern("TCT")
                .pattern("TTT")
                .pattern("TTT")
                .define('T', HpCItems.THERMAL_INSULATION_MATERIAL.get())
                .define('C', Items.LEATHER_CHESTPLATE)
                .unlockedBy("has_thermal_isolator", has(HpCItems.THERMAL_INSULATION_MATERIAL.get()))
                .save(recipeOutput, "t1_thermal_insulation_torso_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, HpCItems.T1_THERMAL_INSULATION_LEGGINGS.get())
                .pattern("TTT")
                .pattern("TLT")
                .pattern("T T")
                .define('T', HpCItems.THERMAL_INSULATION_MATERIAL.get())
                .define('L', Items.LEATHER_LEGGINGS)
                .unlockedBy("has_thermal_isolator", has(HpCItems.THERMAL_INSULATION_MATERIAL.get()))
                .save(recipeOutput, "t1_thermal_insulation_leggings_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, HpCItems.T1_THERMAL_INSULATION_HANDS_AND_FEET.get())
                .pattern("LTL")
                .pattern("TTT")
                .pattern("TBT")
                .define('T', HpCItems.THERMAL_INSULATION_MATERIAL.get())
                .define('L', Items.LEATHER)
                .define('B', Items.LEATHER_BOOTS)
                .unlockedBy("has_thermal_isolator", has(HpCItems.THERMAL_INSULATION_MATERIAL.get()))
                .save(recipeOutput, "t1_thermal_insulation_hands_and_feet_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, HpCItems.MASS_BELT.get())
                .pattern("IGI")
                .define('G', Items.GOLD_BLOCK)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeOutput, "mass_belt_crafting");
        //endregion

        //region Special Functionality Items
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, HpCItems.STANDARD_WRENCH)
                .pattern("  S")
                .pattern(" C ")
                .pattern("C  ")
                .define('C', HpCItems.COMPRESSED_BRONZE.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_bronze", has(HpCItems.COMPRESSED_BRONZE.get()))
                .save(recipeOutput, "standard_wrench_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, HpCItems.SMALL_BATTERY)
                .pattern("XHX")
                .pattern("TRT")
                .pattern("TCT")
                .define('X', HpCItems.COMPRESSED_COPPER.get())
                .define('H', HpCItems.PETROCHEMICALS.get())
                .define('T', HpCItems.COMPRESSED_TIN.get())
                .define('R', Items.REDSTONE)
                .define('C', Items.COAL)
                .unlockedBy("has_redstone", has(Items.REDSTONE))
                .save(recipeOutput, "small_battery_crafting");
        //endregion
        
        //region Oxygen Setup
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCItems.OXYGEN_MASK)
                .pattern("GGG")
                .pattern("GHG")
                .pattern("GPG")
                .define('G', Items.GLASS_PANE)
                .define('H', Ingredient.of(Items.IRON_HELMET, HpCItems.STEEL_HELMET.get()))
                .define('P', HpCBlocks.FLUID_PIPE.get())
                .unlockedBy("has_pipe", has(HpCBlocks.FLUID_PIPE.get()))
                .save(recipeOutput, "oxygen_mask_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCItems.OXYGEN_CONNECTORS)
                .pattern(" P ")
                .pattern("PGP")
                .pattern("H H")
                .define('P', HpCBlocks.FLUID_PIPE.get())
                .define('G', HpCItems.GAS_REGULATOR.get())
                .define('H', HpCItems.PETROCHEMICALS.get())
                .unlockedBy("has_gas_regulator", has(HpCItems.GAS_REGULATOR.get()))
                .save(recipeOutput, "oxygen_connectors_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, HpCItems.COMPRESSED_GAS_TANK)
                .pattern(" H ")
                .pattern("STS")
                .pattern(" S ")
                .define('H', HpCItems.PETROCHEMICALS.get())
                .define('T', HpCItems.TIN_CANISTER.get())
                .define('S', HpCItems.COMPRESSED_STEEL.get())
                .unlockedBy("has_tin_canister", has(HpCItems.TIN_CANISTER.get()))
                .save(recipeOutput, "gas_tank_crafting");
        //endregion
        
        //region Building Blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.PRISMATIC_GLASS.get(), 2)
                .pattern(" TT")
                .pattern(" TT")
                .pattern("   ")
                .define('T', HpCItems.TEKTITES.get())
                .unlockedBy("has_tektites", has(HpCItems.TEKTITES.get()))
                .save(recipeOutput, "prismatic_glass_from_tekties_4_crafting");
        
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.PRISMATIC_GLASS_PANE.get(), 12)
                .pattern("GGG")
                .pattern("GGG")
                .define('G', HpCBlocks.PRISMATIC_GLASS.get())
                .unlockedBy("has_prismatic_glass", has(HpCBlocks.PRISMATIC_GLASS.get()))
                .save(recipeOutput, "prismatic_glass_panes_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, HpCBlocks.TINTED_PRISMATIC_GLASS.get(), 2)
                .pattern(" A ")
                .pattern("AGA")
                .pattern(" A ")
                .define('A', Items.AMETHYST_SHARD)
                .define('G', HpCBlocks.PRISMATIC_GLASS.get())
                .unlockedBy("has_prismatic_glass", has(HpCBlocks.PRISMATIC_GLASS.get()))
                .save(recipeOutput, "tinted_prismatic_glass_crafting");

        
        
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

        //region Foods
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.DEHYDRATED_APPLE.get(), 1)
                .requires(HpCItems.EMPTY_BAG, 1)
                .requires(Items.APPLE, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "dehydrated_apple_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.CHIPS.get(), 1)
                .requires(HpCItems.EMPTY_BAG, 1)
                .requires(Items.BAKED_POTATO, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "chips_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.DEHYDRATED_CARROT.get(), 1)
                .requires(HpCItems.EMPTY_BAG, 1)
                .requires(Items.CARROT, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "dehydrated_carrot_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.DEHYDRATED_BERRIES.get(), 1)
                .requires(HpCItems.EMPTY_BAG, 1)
                .requires(Items.SWEET_BERRIES, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "dehydrated_berries_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.DEHYDRATED_GLOW_BERRIES.get(), 1)
                .requires(HpCItems.EMPTY_BAG, 1)
                .requires(Items.GLOW_BERRIES, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "dehydrated_glow_berries_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.DEHYDRATED_KELP.get(), 1)
                .requires(HpCItems.EMPTY_BAG, 1)
                .requires(Items.DRIED_KELP, 3)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "dehydrated_kelp_crafting");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.DEHYDRATED_CHICKEN.get(), 1)
                .requires(HpCItems.EMPTY_BAG, 1)
                .requires(Items.CHICKEN, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "dehydrated_chicken_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.DEHYDRATED_BEEF.get(), 1)
                .requires(HpCItems.EMPTY_BAG, 1)
                .requires(Items.BEEF, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "dehydrated_beef_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.DEHYDRATED_RABBIT.get(), 1)
                .requires(HpCItems.EMPTY_BAG, 1)
                .requires(Items.RABBIT, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "dehydrated_rabbit_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.DEHYDRATED_COD.get(), 1)
                .requires(HpCItems.EMPTY_BAG, 1)
                .requires(Items.COD, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "dehydrated_cod_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.DEHYDRATED_SALMON.get(), 1)
                .requires(HpCItems.EMPTY_BAG, 1)
                .requires(Items.SALMON, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "dehydrated_salmon_crafting");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.CANNED_CHICKEN.get(), 1)
                .requires(HpCItems.EMPTY_CAN, 1)
                .requires(Items.COOKED_CHICKEN, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "canned_chicken_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.CANNED_STEAK.get(), 1)
                .requires(HpCItems.EMPTY_CAN, 1)
                .requires(Items.COOKED_BEEF, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "canned_steak_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.CANNED_MUTTON.get(), 1)
                .requires(HpCItems.EMPTY_CAN, 1)
                .requires(Items.COOKED_MUTTON, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "canned_mutton_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.CANNED_RABBIT.get(), 1)
                .requires(HpCItems.EMPTY_CAN, 1)
                .requires(Items.COOKED_RABBIT, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "canned_rabbit_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.CANNED_COD.get(), 1)
                .requires(HpCItems.EMPTY_CAN, 1)
                .requires(Items.COOKED_COD, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "canned_cod_crafting");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.CANNED_SALMON.get(), 1)
                .requires(HpCItems.EMPTY_CAN, 1)
                .requires(Items.COOKED_SALMON, 2)
                .unlockedBy("has_empty_bag", has(HpCItems.EMPTY_BAG.get()))
                .save(recipeOutput, "canned_salmon_crafting");

        SpecialRecipeBuilder.special(CannedStewRecipe::new)
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "canned_suspicious_stew"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HpCBlocks.CHEESE_BLOCK.get())
                .pattern("MM")
                .pattern("MM")
                .define('M', Items.MILK_BUCKET)
                .unlockedBy("has_milk_bucket", has(Items.MILK_BUCKET))
                .save(recipeOutput, "cheese_block_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, HpCBlocks.DEFAULT_PIZZA.get())
                .pattern("C  ")
                .pattern("WWW")
                .define('C', HpCItems.CHEESE_SLICE.get())
                .define('W', Items.WHEAT)
                .unlockedBy("has_cheese", has(HpCItems.CHEESE_SLICE.get()))
                .save(recipeOutput, "pizza_crafting");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, HpCItems.BURGER_BUN.get(), 2)
                .requires(Items.BREAD, 1)
                .unlockedBy("has_bread", has(Items.BREAD))
                .save(recipeOutput, "burger_bun_crafting");
        //endregion
        
        //region Space Suits
        SpaceSuitRecipeBuilder.upgrade(RecipeCategory.COMBAT, HpCItems.T1_SPACE_SUIT_HELMET.get())
                .pattern("RSP")
                .pattern("RHP")
                .pattern("RGP")
                .define('H', Ingredient.of(Items.IRON_HELMET, HpCItems.STEEL_HELMET.get()))
                .define('S', HpCItems.ELECTROMAGNETIC_SENSORS.get()) 
                .define('G', Items.GLASS)
                .define('R', HpCItems.RADIATION_PROTECTION_MATERIAL.get())
                .define('P', HpCItems.PRESSURE_PROTECTION_MATERIAL.get())
                .unlockedBy("has_sensors", has(HpCItems.ELECTROMAGNETIC_SENSORS.get()))
                .unlockedBy("has_radiation_protection", has(HpCItems.RADIATION_PROTECTION_MATERIAL.get()))
                .unlockedBy("has_pressure_protection", has(HpCItems.PRESSURE_PROTECTION_MATERIAL.get()))
                .save(recipeOutput, "t1_ss_helmet_crafting");
        SpaceSuitRecipeBuilder.upgrade(RecipeCategory.COMBAT, HpCItems.T1_SPACE_SUIT_CHESTPLATE.get())
                .pattern("RBP")
                .pattern("RCP")
                .pattern("R P")
                .define('C', Ingredient.of(Items.IRON_CHESTPLATE, HpCItems.STEEL_CHESTPLATE.get()))
                .define('B', HpCItems.BASIC_CIRCUIT_BOARD.get())
                .define('R', HpCItems.RADIATION_PROTECTION_MATERIAL.get())
                .define('P', HpCItems.PRESSURE_PROTECTION_MATERIAL.get())
                .unlockedBy("has_board", has(HpCItems.BASIC_CIRCUIT_BOARD.get()))
                .unlockedBy("has_radiation_protection", has(HpCItems.RADIATION_PROTECTION_MATERIAL.get()))
                .unlockedBy("has_pressure_protection", has(HpCItems.PRESSURE_PROTECTION_MATERIAL.get()))
                .save(recipeOutput, "t1_ss_chestplate_crafting");
        SpaceSuitRecipeBuilder.upgrade(RecipeCategory.COMBAT, HpCItems.T1_SPACE_SUIT_LEGGINGS.get())
                .pattern("RGP")
                .pattern("RLP")
                .pattern("R P")
                .define('L', Ingredient.of(Items.IRON_LEGGINGS, HpCItems.STEEL_LEGGINGS.get()))
                .define('G', HpCItems.GAS_REGULATOR.get())
                .define('R', HpCItems.RADIATION_PROTECTION_MATERIAL.get())
                .define('P', HpCItems.PRESSURE_PROTECTION_MATERIAL.get())
                .unlockedBy("has_gas_regulator", has(HpCItems.GAS_REGULATOR.get()))
                .unlockedBy("has_radiation_protection", has(HpCItems.RADIATION_PROTECTION_MATERIAL.get()))
                .unlockedBy("has_pressure_protection", has(HpCItems.PRESSURE_PROTECTION_MATERIAL.get()))
                .save(recipeOutput, "t1_ss_leggings_crafting");
        SpaceSuitRecipeBuilder.upgrade(RecipeCategory.COMBAT, HpCItems.T1_SPACE_SUIT_BOOTS.get())
                .pattern("REP")
                .pattern("RBP")
                .pattern("RHP")
                .define('B', Ingredient.of(Items.IRON_BOOTS, HpCItems.STEEL_BOOTS.get()))
                .define('E', HpCItems.SMALL_BATTERY.get())
                .define('R', HpCItems.RADIATION_PROTECTION_MATERIAL.get())
                .define('P', HpCItems.PRESSURE_PROTECTION_MATERIAL.get())
                .define('H', HpCItems.PETROCHEMICALS.get())
                .unlockedBy("has_small_battery", has(HpCItems.SMALL_BATTERY.get()))
                .unlockedBy("has_radiation_protection", has(HpCItems.RADIATION_PROTECTION_MATERIAL.get()))
                .unlockedBy("has_pressure_protection", has(HpCItems.PRESSURE_PROTECTION_MATERIAL.get()))
                .save(recipeOutput, "t1_ss_boots_crafting");
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
        
        //region PCB Factory
        PCBFabricatorRecipeBuilder.fabricate(RecipeCategory.MISC, HpCItems.BASIC_CIRCUIT_BOARD.get())
                .core(Items.DIAMOND)
                .wire(Tags.Items.INGOTS_COPPER) 
                .logic1(Items.REDSTONE)
                .logic2(Items.QUARTZ)
                .sub1(HpCItems.SILICON.get())
                .sub2(HpCItems.SILICON.get())
                .unlockedBy("has_silicon", has(HpCItems.SILICON.get()))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":basic_circuit_board_fabrication");
        
        PCBFabricatorRecipeBuilder.fabricate(RecipeCategory.MISC, HpCItems.SOLAR_PANEL_SEMICONDUCTOR_BASE.get(), 6)
                .core(Items.DIAMOND)
                .wire(Tags.Items.INGOTS_COPPER)
                .logic1(Items.REDSTONE)
                .logic2(Items.LAPIS_LAZULI)
                .sub1(HpCItems.SILICON.get())
                .sub2(HpCItems.SILICON.get())
                .unlockedBy("has_silicon", has(HpCItems.SILICON.get()))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":solar_panel_base_fabrication");
        //endregion

        //region Rockets
        MagneticAssemblyPlatformRecipeBuilder.fabricate(RecipeCategory.TRANSPORTATION, HpCItems.ROCKET_ITEM.get())
                .nose(HpCTags.Items.NOSES)
                .addHull(HpCTags.Items.HULLS)
                .addHull(HpCTags.Items.HULLS)
                .addHull(HpCTags.Items.HULLS)
                .addHull(HpCTags.Items.HULLS)
                .addHull(HpCTags.Items.HULLS)
                .addHull(HpCTags.Items.HULLS)
                .addHull(HpCTags.Items.HULLS)
                .addHull(HpCTags.Items.HULLS)
                .addFin(HpCTags.Items.FINS)
                .addFin(HpCTags.Items.FINS)
                .addFin(HpCTags.Items.FINS)
                .addFin(HpCTags.Items.FINS)
                .base(HpCTags.Items.BASE)
                .engine(HpCTags.Items.ENGINES)
                .unlockedBy("has_rocket_engine", has(HpCItems.T1_ROCKET_ENGINE.get()))
                .save(recipeOutput, HeliopauseCore.MOD_ID + ":t1_rocket_assembly");
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
