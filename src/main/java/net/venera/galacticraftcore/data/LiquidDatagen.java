package net.venera.galacticraftcore.data;

import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.data.builder.ShapelessRecipeNoRemainderBuilder;
import net.venera.galacticraftcore.init.LiquidBlockReg;
import net.venera.galacticraftcore.fluid.ModFluids;
import net.venera.galacticraftcore.init.conditions.CraftWithIceCondition;
import net.venera.galacticraftcore.init.conditions.CraftWithWaterBottleCondition;
import net.venera.galacticraftcore.init.conditions.CraftWithWaterBucketCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal")
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class LiquidDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();
		PackOutput packOutput = generator.getPackOutput();

		generator.addProvider(event.includeServer(), new Recipes(packOutput, event.getLookupProvider()));
		//generator.addProvider(event.includeClient(), new Language(packOutput));
		//generator.addProvider(event.includeClient(), new ItemModels(packOutput, helper));
		//generator.addProvider(event.includeClient(), new BlockStates(packOutput, helper));
	}

	public static class Recipes extends RecipeProvider {

		public Recipes(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(packOutput, lookupProvider);
		}

		@Override
		protected void buildRecipes(RecipeOutput recipeOutput) {
			buildWaterRecipes(ModFluids.LIQUID_DIRT, Blocks.DIRT, recipeOutput);
			buildWaterRecipes(ModFluids.LIQUID_COARSE_DIRT, Blocks.COARSE_DIRT, recipeOutput);
			buildWaterRecipes(ModFluids.LIQUID_PODZOL, Blocks.PODZOL, recipeOutput);

			buildLavaRecipe(ModFluids.LIQUID_STONE, Blocks.STONE, recipeOutput);
			buildLavaRecipe(ModFluids.LIQUID_GRANITE, Blocks.GRANITE, recipeOutput);
			buildLavaRecipe(ModFluids.LIQUID_DIORITE, Blocks.DIORITE, recipeOutput);
			buildLavaRecipe(ModFluids.LIQUID_ANDESITE, Blocks.ANDESITE, recipeOutput);
			buildLavaRecipe(ModFluids.LIQUID_SANDSTONE, Blocks.SANDSTONE, recipeOutput);
			buildLavaRecipe(ModFluids.LIQUID_RED_SANDSTONE, Blocks.RED_SANDSTONE, recipeOutput);

			buildLavaRecipe(ModFluids.LIQUID_NETHERRACK, Blocks.NETHERRACK, recipeOutput);
			buildLavaRecipe(ModFluids.LIQUID_SOUL_SAND, Blocks.SOUL_SAND, recipeOutput);
			buildLavaRecipe(ModFluids.LIQUID_MAGMA, Blocks.MAGMA_BLOCK, recipeOutput);
			buildLavaRecipe(ModFluids.LIQUID_GLOWSTONE, Blocks.GLOWSTONE, recipeOutput);

//			buildWaterRecipes(ModFluids.LIQUID_SAND, Blocks.SAND, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_RED_SAND, Blocks.RED_SAND, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_GRAVEL, Blocks.GRAVEL, recipeOutput);
//
//			ShapelessRecipeNoRemainderBuilder.shapeless(ModFluids.LIQUID_ORE.getBucket())
//					.requires(Items.LAVA_BUCKET)
//					.requires(Tags.Items.ORES_DIAMOND).requires(Tags.Items.ORES_REDSTONE)
//					.requires(Tags.Items.ORES_LAPIS).requires(Tags.Items.ORES_COAL)
//					.unlockedBy("has_lava_bucket", has(Items.LAVA_BUCKET))
//					.save(recipeOutput, "liquidblocks:ore_bucket_with_bucket");
//
//
//			buildWaterRecipes(ModFluids.LIQUID_CLAY, Blocks.CLAY, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_TERRACOTTA, Blocks.TERRACOTTA, recipeOutput);
//
//			buildLavaRecipe(ModFluids.LIQUID_WHITE_GLAZED_TERRACOTTA, Blocks.WHITE_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_ORANGE_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_MAGENTA_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_YELLOW_GLAZED_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_LIME_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_PINK_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_GRAY_GLAZED_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_CYAN_GLAZED_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_PURPLE_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_BLUE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_BROWN_GLAZED_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_GREEN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_RED_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, recipeOutput);
//			buildLavaRecipe(ModFluids.LIQUID_BLACK_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA, recipeOutput);
//
//			buildWaterRecipes(ModFluids.LIQUID_WHITE_CONCRETE, Blocks.WHITE_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_ORANGE_CONCRETE, Blocks.ORANGE_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_MAGENTA_CONCRETE, Blocks.MAGENTA_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_LIGHT_BLUE_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_YELLOW_CONCRETE, Blocks.YELLOW_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_LIME_CONCRETE, Blocks.LIME_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_PINK_CONCRETE, Blocks.PINK_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_GRAY_CONCRETE, Blocks.GRAY_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_LIGHT_GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_CYAN_CONCRETE, Blocks.CYAN_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_PURPLE_CONCRETE, Blocks.PURPLE_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_BLUE_CONCRETE, Blocks.BLUE_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_BROWN_CONCRETE, Blocks.BROWN_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_GREEN_CONCRETE, Blocks.GREEN_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_RED_CONCRETE, Blocks.RED_CONCRETE_POWDER, recipeOutput);
//			buildWaterRecipes(ModFluids.LIQUID_BLACK_CONCRETE, Blocks.BLACK_CONCRETE_POWDER, recipeOutput);
		}

		private void buildWaterRecipes(LiquidBlockReg reg, Block block, RecipeOutput recipeOutput) {
			ResourceLocation location = BuiltInRegistries.BLOCK.getKey(block);
			ItemStack waterBottle = Items.POTION.getDefaultInstance();

			ShapelessRecipeNoRemainderBuilder.shapeless(reg.getBucket())
					.requires(block).requires(Items.BUCKET).requires(DataComponentIngredient.of(true, waterBottle))
					.group("liquidblocks").unlockedBy("has_" + location.getPath(), has(block))
					.save(recipeOutput.withConditions(new CraftWithWaterBottleCondition()), ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, location.getPath() + "_with_bottle"));

			ShapelessRecipeNoRemainderBuilder.shapeless(reg.getBucket())
					.requires(block).requires(Items.WATER_BUCKET)
					.group("liquidblocks").unlockedBy("has_" + location.getPath(), has(block))
					.save(recipeOutput.withConditions(new CraftWithWaterBucketCondition()),
							ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, location.getPath() + "_with_bucket"));

			ShapelessRecipeNoRemainderBuilder.shapeless(reg.getBucket())
					.requires(block).requires(Items.ICE)
					.group("liquidblocks").unlockedBy("has_" + location.getPath(), has(block))
					.save(recipeOutput.withConditions(new CraftWithIceCondition()),
							ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, location.getPath() + "_with_ice"));
		}

		private void buildLavaRecipe(LiquidBlockReg reg, Block block, RecipeOutput recipeConsumer) {
			ResourceLocation location = BuiltInRegistries.BLOCK.getKey(block);
			ShapelessRecipeNoRemainderBuilder.shapeless(reg.getBucket())
					.requires(block).requires(Items.LAVA_BUCKET)
					.unlockedBy("has_lava_bucket", has(Items.LAVA_BUCKET))
					.save(recipeConsumer, "liquidblocks:" + location.getPath() + "_with_lava_bucket");
		}
	}

//	private static class Language extends LanguageProvider {
//		public Language(PackOutput packOutput) {
//			super(packOutput, GalacticraftCore.MOD_ID, "en_us");
//		}
//
//		@Override
//		protected void addTranslations() {
//			add("itemGroup.liquidblocks", "Liquid Blocks");
//
//			generateLang(LiquidRegistry.LIQUID_DIRT, "Liquid Dirt");
//			generateLang(LiquidRegistry.LIQUID_COARSE_DIRT, "Liquid Course Dirt");
//			generateLang(LiquidRegistry.LIQUID_PODZOL, "Liquid Podzol");
//			generateLang(LiquidRegistry.LIQUID_STONE, "Liquid Stone");
//			generateLang(LiquidRegistry.LIQUID_GRANITE, "Liquid Granite");
//			generateLang(LiquidRegistry.LIQUID_DIORITE, "Liquid Diorite");
//			generateLang(LiquidRegistry.LIQUID_ANDESITE, "Liquid Andesite");
//			generateLang(LiquidRegistry.LIQUID_SANDSTONE, "Liquid Sandstone");
//			generateLang(LiquidRegistry.LIQUID_RED_SANDSTONE, "Liquid Red Sandstone");
//			generateLang(LiquidRegistry.LIQUID_NETHERRACK, "Liquid Netherrack");
//			generateLang(LiquidRegistry.LIQUID_SOUL_SAND, "Liquid Soul Sand");
//			generateLang(LiquidRegistry.LIQUID_MAGMA, "Liquid Magma");
//			generateLang(LiquidRegistry.LIQUID_GLOWSTONE, "Liquid Glowstone");
//			generateLang(LiquidRegistry.LIQUID_SAND, "Liquid Sand");
//			generateLang(LiquidRegistry.LIQUID_RED_SAND, "Liquid Red Sand");
//			generateLang(LiquidRegistry.LIQUID_GRAVEL, "Liquid Gravel");
//			generateLang(LiquidRegistry.LIQUID_ORE, "Liquid Ore");
//			generateLang(LiquidRegistry.LIQUID_CLAY, "Liquid Clay");
//			generateLang(LiquidRegistry.LIQUID_TERRACOTTA, "Liquid Terracotta");
//			generateLang(LiquidRegistry.LIQUID_WHITE_GLAZED_TERRACOTTA, "Liquid White Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_ORANGE_GLAZED_TERRACOTTA, "Liquid Orange Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_MAGENTA_GLAZED_TERRACOTTA, "Liquid Magenta Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_LIGHT_BLUE_GLAZED_TERRACOTTA, "Liquid Light Blue Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_YELLOW_GLAZED_TERRACOTTA, "Liquid Yellow Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_LIME_GLAZED_TERRACOTTA, "Liquid Lime Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_PINK_GLAZED_TERRACOTTA, "Liquid Pink Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_GRAY_GLAZED_TERRACOTTA, "Liquid Gray Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_LIGHT_GRAY_GLAZED_TERRACOTTA, "Liquid Light Gray Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_CYAN_GLAZED_TERRACOTTA, "Liquid Cyan Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_PURPLE_GLAZED_TERRACOTTA, "Liquid Purple Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_BLUE_GLAZED_TERRACOTTA, "Liquid Blue Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_BROWN_GLAZED_TERRACOTTA, "Liquid Brown Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_GREEN_GLAZED_TERRACOTTA, "Liquid Green Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_RED_GLAZED_TERRACOTTA, "Liquid Red Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_BLACK_GLAZED_TERRACOTTA, "Liquid Black Glazed Terracotta");
//			generateLang(LiquidRegistry.LIQUID_WHITE_CONCRETE, "Liquid White Concrete");
//			generateLang(LiquidRegistry.LIQUID_ORANGE_CONCRETE, "Liquid Orange Concrete");
//			generateLang(LiquidRegistry.LIQUID_MAGENTA_CONCRETE, "Liquid Magenta Concrete");
//			generateLang(LiquidRegistry.LIQUID_LIGHT_BLUE_CONCRETE, "Liquid Light Blue Concrete");
//			generateLang(LiquidRegistry.LIQUID_YELLOW_CONCRETE, "Liquid Yellow Concrete");
//			generateLang(LiquidRegistry.LIQUID_LIME_CONCRETE, "Liquid Lime Concrete");
//			generateLang(LiquidRegistry.LIQUID_PINK_CONCRETE, "Liquid Pink Concrete");
//			generateLang(LiquidRegistry.LIQUID_GRAY_CONCRETE, "Liquid Gray Concrete");
//			generateLang(LiquidRegistry.LIQUID_LIGHT_GRAY_CONCRETE, "Liquid Light Gray Concrete");
//			generateLang(LiquidRegistry.LIQUID_CYAN_CONCRETE, "Liquid Cyan Concrete");
//			generateLang(LiquidRegistry.LIQUID_PURPLE_CONCRETE, "Liquid Purple Concrete");
//			generateLang(LiquidRegistry.LIQUID_BLUE_CONCRETE, "Liquid Blue Concrete");
//			generateLang(LiquidRegistry.LIQUID_BROWN_CONCRETE, "Liquid Brown Concrete");
//			generateLang(LiquidRegistry.LIQUID_GREEN_CONCRETE, "Liquid Green Concrete");
//			generateLang(LiquidRegistry.LIQUID_RED_CONCRETE, "Liquid Red Concrete");
//			generateLang(LiquidRegistry.LIQUID_BLACK_CONCRETE, "Liquid Black Concrete");
//
//			addConfig("general", "General", "General settings");
//			addConfig("liquidCausesNausea", "Liquid Causes Nausea", "When enabled causes water based liquid blocks to cause nausea when collided [default: true]");
//			addConfig("liquidCausesSlowness", "Liquid Causes Slowness", "When enabled causes water based liquid blocks to cause slowness when collided [default: true]");
//			addConfig("completelyFill", "Completely Fill", "When enabled causes will try to make the liquid turn every bit into a block [default: true]");
//			addConfig("netherrackFireChance", "Netherrack Fire Chance", "Makes liquid netherrack have a 1 in X chance of drying with fire on top (0 = disabled) [default: 30]");
//			addConfig("oreChance", "Ore Chance", "Makes liquid ore have a 1 in X chance of drying into an ore block (higher = less chance) [default: 64]");
//			addConfig("crafting", "Crafting", "Crafting settings");
//			addConfig("craftWithIce", "Craft With Ice", "Enable crafting buckets with ice [default: true]");
//			addConfig("craftWithWaterBottle", "Craft With Water Bottle", "Enable crafting buckets with a water bottle where applicable [default: true]");
//			addConfig("craftWithWaterBucket", "Craft With Water Bucket", "Enable crafting buckets with a bucket of the fitting liquid [default: true]");
//			addConfig("craftLiquidOre", "Craft Liquid Ore", "Enables the crafting of the Liquid Ore bucket [default: true]");
//		}
//
//		private void generateLang(LiquidBlockReg blockReg, String name) {
//			addFluid(name, blockReg.getSourceRegistry());
//			addFluid("Flowing " + name, blockReg.getFlowing());
//			add("fluid_type.liquidblocks." + blockReg.getFluidType().getId().getPath(), name);
//
//			addItem(blockReg.getBucketRegistry(), name + " Bucket");
//		}
//
//		private void addFluid(String name, DeferredHolder<Fluid, BaseFlowingFluid> registryObject) {
//			add("fluid." + registryObject.getId().getNamespace() + "." + registryObject.getId().getPath(), name);
//		}
//
//		/**
//		 * Add the translation for a config entry
//		 *
//		 * @param path        The path of the config entry
//		 * @param name        The name of the config entry
//		 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
//		 */
//		private void addConfig(String path, String name, @Nullable String description) {
//			this.add("liquidblocks.configuration." + path, name);
//			if (description != null && !description.isEmpty())
//				this.add("liquidblocks.configuration." + path + ".tooltip", description);
//		}
//	}

//	private static class BlockStates extends BlockStateProvider {
//		public BlockStates(PackOutput packOutput, ExistingFileHelper helper) {
//			super(packOutput, GalacticraftCore.MOD_ID, helper);
//		}
//
//		@Override
//		protected void registerStatesAndModels() {
//			ModelFile liquidModel = models().getExistingFile(modLoc("block/liquid_block"));
//			simpleBlock(LiquidRegistry.LIQUID_DIRT.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_COARSE_DIRT.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_PODZOL.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_STONE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_GRANITE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_DIORITE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_ANDESITE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_SANDSTONE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_RED_SANDSTONE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_NETHERRACK.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_SOUL_SAND.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_MAGMA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_GLOWSTONE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_SAND.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_RED_SAND.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_GRAVEL.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_ORE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_CLAY.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_WHITE_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_ORANGE_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_MAGENTA_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_LIGHT_BLUE_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_YELLOW_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_LIME_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_PINK_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_GRAY_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_LIGHT_GRAY_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_CYAN_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_PURPLE_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_BLUE_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_BROWN_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_GREEN_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_RED_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_BLACK_GLAZED_TERRACOTTA.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_WHITE_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_ORANGE_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_MAGENTA_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_LIGHT_BLUE_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_YELLOW_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_LIME_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_PINK_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_GRAY_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_LIGHT_GRAY_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_CYAN_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_PURPLE_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_BLUE_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_BROWN_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_GREEN_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_RED_CONCRETE.getFluidblock(), liquidModel);
//			simpleBlock(LiquidRegistry.LIQUID_BLACK_CONCRETE.getFluidblock(), liquidModel);
//		}
//	}

//	private static class ItemModels extends ItemModelProvider {
//		public ItemModels(PackOutput packOutput, ExistingFileHelper helper) {
//			super(packOutput, GalacticraftCore.MOD_ID, helper);
//		}
//
//		@Override
//		protected void registerModels() {
//			generateBucket(LiquidRegistry.LIQUID_DIRT);
//			generateBucket(LiquidRegistry.LIQUID_COARSE_DIRT);
//			generateBucket(LiquidRegistry.LIQUID_PODZOL);
//			generateBucket(LiquidRegistry.LIQUID_STONE);
//			generateBucket(LiquidRegistry.LIQUID_GRANITE);
//			generateBucket(LiquidRegistry.LIQUID_DIORITE);
//			generateBucket(LiquidRegistry.LIQUID_ANDESITE);
//			generateBucket(LiquidRegistry.LIQUID_SANDSTONE);
//			generateBucket(LiquidRegistry.LIQUID_RED_SANDSTONE);
//			generateBucket(LiquidRegistry.LIQUID_NETHERRACK);
//			generateBucket(LiquidRegistry.LIQUID_SOUL_SAND);
//			generateBucket(LiquidRegistry.LIQUID_MAGMA);
//			generateBucket(LiquidRegistry.LIQUID_GLOWSTONE);
//			generateBucket(LiquidRegistry.LIQUID_SAND);
//			generateBucket(LiquidRegistry.LIQUID_RED_SAND);
//			generateBucket(LiquidRegistry.LIQUID_GRAVEL);
//			generateBucket(LiquidRegistry.LIQUID_ORE);
//			generateBucket(LiquidRegistry.LIQUID_CLAY);
//			generateBucket(LiquidRegistry.LIQUID_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_WHITE_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_ORANGE_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_MAGENTA_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_LIGHT_BLUE_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_YELLOW_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_LIME_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_PINK_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_GRAY_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_LIGHT_GRAY_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_CYAN_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_PURPLE_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_BLUE_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_BROWN_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_GREEN_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_RED_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_BLACK_GLAZED_TERRACOTTA);
//			generateBucket(LiquidRegistry.LIQUID_WHITE_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_ORANGE_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_MAGENTA_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_LIGHT_BLUE_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_YELLOW_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_LIME_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_PINK_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_GRAY_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_LIGHT_GRAY_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_CYAN_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_PURPLE_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_BLUE_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_BROWN_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_GREEN_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_RED_CONCRETE);
//			generateBucket(LiquidRegistry.LIQUID_BLACK_CONCRETE);
//		}
//
//		private void generateBucket(LiquidBlockReg blockReg) {
//			withExistingParent(blockReg.getBucketRegistry().getId().getPath(),
//					ResourceLocation.fromNamespaceAndPath("neoforge", "item/bucket"))
//					.customLoader(DynamicFluidContainerModelBuilder::begin)
//					.fluid(blockReg.getSource());
//		}
//	}
}
