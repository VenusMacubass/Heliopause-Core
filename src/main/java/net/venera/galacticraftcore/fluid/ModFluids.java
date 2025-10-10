package net.venera.galacticraftcore.fluid;

import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.blockentity.LiquidBlockEntity;
import net.venera.galacticraftcore.init.LiquidBlockReg;
import net.venera.galacticraftcore.item.LiquidBucketItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.venera.galacticraftcore.item.ModItems;
import org.joml.Vector3f;

import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class ModFluids {
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, GalacticraftCore.MOD_ID);
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, GalacticraftCore.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GalacticraftCore.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GalacticraftCore.MOD_ID);

	//Dirts
	public static final LiquidBlockReg LIQUID_DIRT = new LiquidBlockReg.Builder("liquid_dirt", () -> Blocks.DIRT, 0xFF392C20).mapColor(MapColor.DIRT).build();
	public static final LiquidBlockReg LIQUID_COARSE_DIRT = new LiquidBlockReg.Builder("liquid_coarse_dirt", () -> Blocks.COARSE_DIRT, 0xFF392C20).mapColor(MapColor.DIRT).build();
	public static final LiquidBlockReg LIQUID_PODZOL = new LiquidBlockReg.Builder("liquid_podzol", () -> Blocks.PODZOL, 0xFF392C20).mapColor(MapColor.DIRT).build();

	//Stones
	public static final LiquidBlockReg LIQUID_STONE = new LiquidBlockReg.Builder("liquid_stone", () -> Blocks.STONE, 0xFF7f7f7f).hot().mapColor(MapColor.STONE).build();
	public static final LiquidBlockReg LIQUID_GRANITE = new LiquidBlockReg.Builder("liquid_granite", () -> Blocks.GRANITE, 0xFFb48877).hot().mapColor(MapColor.DIRT).build();
	public static final LiquidBlockReg LIQUID_DIORITE = new LiquidBlockReg.Builder("liquid_diorite", () -> Blocks.DIORITE, 0xFFe0e0e3).hot().mapColor(MapColor.QUARTZ).build();
	public static final LiquidBlockReg LIQUID_ANDESITE = new LiquidBlockReg.Builder("liquid_andesite", () -> Blocks.ANDESITE, 0xFFababac).hot().mapColor(MapColor.STONE).build();
	public static final LiquidBlockReg LIQUID_SANDSTONE = new LiquidBlockReg.Builder("liquid_sandstone", () -> Blocks.SANDSTONE, 0xFFe6d9ae).hot().mapColor(MapColor.SAND).build();
	public static final LiquidBlockReg LIQUID_RED_SANDSTONE = new LiquidBlockReg.Builder("liquid_red_sandstone", () -> Blocks.RED_SANDSTONE, 0xFFb45e26).hot().mapColor(MapColor.COLOR_ORANGE).build();

	//Nether stuff
	public static final LiquidBlockReg LIQUID_NETHERRACK = new LiquidBlockReg.Builder("liquid_netherrack", () -> Blocks.NETHERRACK, 0xFF9e5d5d).hot().mapColor(MapColor.NETHER).build();
	public static final LiquidBlockReg LIQUID_SOUL_SAND = new LiquidBlockReg.Builder("liquid_soul_sand", () -> Blocks.SOUL_SAND, 0xFF695243).hot().mapColor(MapColor.COLOR_BROWN).build();
	public static final LiquidBlockReg LIQUID_MAGMA = new LiquidBlockReg.Builder("liquid_magma", () -> Blocks.MAGMA_BLOCK, 0xFFca4e06).hot().mapColor(MapColor.NETHER).luminosity(12).build();
	public static final LiquidBlockReg LIQUID_GLOWSTONE = new LiquidBlockReg.Builder("liquid_glowstone", () -> Blocks.GLOWSTONE, 0xFFf9d49c).hot().mapColor(MapColor.SAND).luminosity(12).build();

//	//Sand
//	public static final LiquidBlockReg LIQUID_SAND = new LiquidBlockReg.Builder("liquid_sand", () -> Blocks.SAND, 0xFFe6d9ae).mapColor(MapColor.SAND).build();
//	public static final LiquidBlockReg LIQUID_RED_SAND = new LiquidBlockReg.Builder("liquid_red_sand", () -> Blocks.RED_SAND, 0xFFb45e26).mapColor(MapColor.COLOR_ORANGE).build();
//	public static final LiquidBlockReg LIQUID_GRAVEL = new LiquidBlockReg.Builder("liquid_gravel", () -> Blocks.GRAVEL, 0xFF817f7f).mapColor(MapColor.STONE).build();
//
//	public static final LiquidBlockReg LIQUID_ORE = new LiquidBlockReg.Builder("liquid_ore", () -> Blocks.STONE, 0xFF7f7f7f).hot().mapColor(MapColor.STONE).build();
//
//	public static final LiquidBlockReg LIQUID_CLAY = new LiquidBlockReg.Builder("liquid_clay", () -> Blocks.CLAY, 0xFFa9afbb).mapColor(MapColor.CLAY).build();
//	public static final LiquidBlockReg LIQUID_TERRACOTTA = new LiquidBlockReg.Builder("liquid_terracotta", () -> Blocks.TERRACOTTA, 0xFF935940).hot().mapColor(MapColor.COLOR_ORANGE).build();
//
//	public static final LiquidBlockReg LIQUID_WHITE_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_white_glazed_terracotta", () -> Blocks.WHITE_GLAZED_TERRACOTTA, 0xFFf9fffe).hot().mapColor(MapColor.TERRACOTTA_WHITE).build();
//	public static final LiquidBlockReg LIQUID_ORANGE_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_orange_glazed_terracotta", () -> Blocks.ORANGE_GLAZED_TERRACOTTA, 0xFFf9801d).hot().mapColor(MapColor.TERRACOTTA_ORANGE).build();
//	public static final LiquidBlockReg LIQUID_MAGENTA_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_magenta_glazed_terracotta", () -> Blocks.MAGENTA_GLAZED_TERRACOTTA, 0xFFc74ebd).hot().mapColor(MapColor.TERRACOTTA_MAGENTA).build();
//	public static final LiquidBlockReg LIQUID_LIGHT_BLUE_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_light_blue_glazed_terracotta", () -> Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, 0xFF3ab3da).hot().mapColor(MapColor.TERRACOTTA_LIGHT_BLUE).build();
//	public static final LiquidBlockReg LIQUID_YELLOW_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_yellow_glazed_terracotta", () -> Blocks.YELLOW_GLAZED_TERRACOTTA, 0xFFfed83d).hot().mapColor(MapColor.TERRACOTTA_YELLOW).build();
//	public static final LiquidBlockReg LIQUID_LIME_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_lime_glazed_terracotta", () -> Blocks.LIME_GLAZED_TERRACOTTA, 0xFF80c71f).hot().mapColor(MapColor.TERRACOTTA_LIGHT_GREEN).build();
//	public static final LiquidBlockReg LIQUID_PINK_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_pink_glazed_terracotta", () -> Blocks.PINK_GLAZED_TERRACOTTA, 0xFFf38baa).hot().mapColor(MapColor.TERRACOTTA_PINK).build();
//	public static final LiquidBlockReg LIQUID_GRAY_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_gray_glazed_terracotta", () -> Blocks.GRAY_GLAZED_TERRACOTTA, 0xFF474f52).hot().mapColor(MapColor.TERRACOTTA_GRAY).build();
//	public static final LiquidBlockReg LIQUID_LIGHT_GRAY_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_light_gray_glazed_terracotta", () -> Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, 0xFF9d9d97).hot().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).build();
//	public static final LiquidBlockReg LIQUID_CYAN_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_cyan_glazed_terracotta", () -> Blocks.CYAN_GLAZED_TERRACOTTA, 0xFF169c9c).hot().mapColor(MapColor.TERRACOTTA_CYAN).build();
//	public static final LiquidBlockReg LIQUID_PURPLE_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_purple_glazed_terracotta", () -> Blocks.PURPLE_GLAZED_TERRACOTTA, 0xFF8932b8).hot().mapColor(MapColor.TERRACOTTA_PURPLE).build();
//	public static final LiquidBlockReg LIQUID_BLUE_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_blue_glazed_terracotta", () -> Blocks.BLUE_GLAZED_TERRACOTTA, 0xFF3c44aa).hot().mapColor(MapColor.TERRACOTTA_BLUE).build();
//	public static final LiquidBlockReg LIQUID_BROWN_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_brown_glazed_terracotta", () -> Blocks.BROWN_GLAZED_TERRACOTTA, 0xFF835432).hot().mapColor(MapColor.TERRACOTTA_BROWN).build();
//	public static final LiquidBlockReg LIQUID_GREEN_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_green_glazed_terracotta", () -> Blocks.GREEN_GLAZED_TERRACOTTA, 0xFF5e7c16).hot().mapColor(MapColor.TERRACOTTA_GREEN).build();
//	public static final LiquidBlockReg LIQUID_RED_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_red_glazed_terracotta", () -> Blocks.RED_GLAZED_TERRACOTTA, 0xFFb02e26).hot().mapColor(MapColor.TERRACOTTA_RED).build();
//	public static final LiquidBlockReg LIQUID_BLACK_GLAZED_TERRACOTTA = new LiquidBlockReg.Builder("liquid_black_glazed_terracotta", () -> Blocks.BLACK_GLAZED_TERRACOTTA, 0xFF1d1d21).hot().mapColor(MapColor.TERRACOTTA_BLACK).build();
//
//	public static final LiquidBlockReg LIQUID_WHITE_CONCRETE = new LiquidBlockReg.Builder("liquid_white_concrete", () -> Blocks.WHITE_CONCRETE, 0xFFf9fffe).mapColor(DyeColor.WHITE.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_ORANGE_CONCRETE = new LiquidBlockReg.Builder("liquid_orange_concrete", () -> Blocks.ORANGE_CONCRETE, 0xFFf9801d).mapColor(DyeColor.ORANGE.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_MAGENTA_CONCRETE = new LiquidBlockReg.Builder("liquid_magenta_concrete", () -> Blocks.MAGENTA_CONCRETE, 0xFFc74ebd).mapColor(DyeColor.MAGENTA.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_LIGHT_BLUE_CONCRETE = new LiquidBlockReg.Builder("liquid_light_blue_concrete", () -> Blocks.LIGHT_BLUE_CONCRETE, 0xFF3ab3da).mapColor(DyeColor.LIGHT_BLUE.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_YELLOW_CONCRETE = new LiquidBlockReg.Builder("liquid_yellow_concrete", () -> Blocks.YELLOW_CONCRETE, 0xFFfed83d).mapColor(DyeColor.YELLOW.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_LIME_CONCRETE = new LiquidBlockReg.Builder("liquid_lime_concrete", () -> Blocks.LIME_CONCRETE, 0xFF80c71f).mapColor(DyeColor.LIME.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_PINK_CONCRETE = new LiquidBlockReg.Builder("liquid_pink_concrete", () -> Blocks.PINK_CONCRETE, 0xFFf38baa).mapColor(DyeColor.PINK.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_GRAY_CONCRETE = new LiquidBlockReg.Builder("liquid_gray_concrete", () -> Blocks.GRAY_CONCRETE, 0xFF474f52).mapColor(DyeColor.GRAY.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_LIGHT_GRAY_CONCRETE = new LiquidBlockReg.Builder("liquid_light_gray_concrete", () -> Blocks.LIGHT_GRAY_CONCRETE, 0xFF9d9d97).mapColor(DyeColor.LIGHT_GRAY.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_CYAN_CONCRETE = new LiquidBlockReg.Builder("liquid_cyan_concrete", () -> Blocks.CYAN_CONCRETE, 0xFF169c9c).mapColor(DyeColor.CYAN.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_PURPLE_CONCRETE = new LiquidBlockReg.Builder("liquid_purple_concrete", () -> Blocks.PURPLE_CONCRETE, 0xFF8932b8).mapColor(DyeColor.PURPLE.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_BLUE_CONCRETE = new LiquidBlockReg.Builder("liquid_blue_concrete", () -> Blocks.BLUE_CONCRETE, 0xFF3c44aa).mapColor(DyeColor.BLUE.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_BROWN_CONCRETE = new LiquidBlockReg.Builder("liquid_brown_concrete", () -> Blocks.BROWN_CONCRETE, 0xFF835432).mapColor(DyeColor.BROWN.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_GREEN_CONCRETE = new LiquidBlockReg.Builder("liquid_green_concrete", () -> Blocks.GREEN_CONCRETE, 0xFF5e7c16).mapColor(DyeColor.GREEN.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_RED_CONCRETE = new LiquidBlockReg.Builder("liquid_red_concrete", () -> Blocks.RED_CONCRETE, 0xFFb02e26).mapColor(DyeColor.RED.getMapColor()).build();
//	public static final LiquidBlockReg LIQUID_BLACK_CONCRETE = new LiquidBlockReg.Builder("liquid_black_concrete", () -> Blocks.BLACK_CONCRETE, 0xFF1d1d21).mapColor(DyeColor.BLACK.getMapColor()).build();
    public static final LiquidBlockReg CRUDE_OIL = new LiquidBlockReg.Builder("crude_oil", () -> Blocks.COAL_BLOCK, 0xFF1f1f1f)
        .crudeOil().modifyFogColor(fogColor(Color.BLACK)).motionScale(0.07D).mapColor(MapColor.COLOR_BLACK).build();


	public static final Supplier<BlockEntityType<LiquidBlockEntity>> LIQUID_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("liquid_tile",
			() -> BlockEntityType.Builder.of(LiquidBlockEntity::new,
					LIQUID_DIRT.getFluidblock(), LIQUID_COARSE_DIRT.getFluidblock(), LIQUID_PODZOL.getFluidblock(),
					LIQUID_STONE.getFluidblock(), LIQUID_GRANITE.getFluidblock(), LIQUID_DIORITE.getFluidblock(),
					LIQUID_ANDESITE.getFluidblock(), LIQUID_SANDSTONE.getFluidblock(), LIQUID_RED_SANDSTONE.getFluidblock(),
					LIQUID_NETHERRACK.getFluidblock(), LIQUID_SOUL_SAND.getFluidblock(), LIQUID_MAGMA.getFluidblock(),
					LIQUID_GLOWSTONE.getFluidblock(),
//                    LIQUID_SAND.getFluidblock(), LIQUID_RED_SAND.getFluidblock(),
//					LIQUID_GRAVEL.getFluidblock(), LIQUID_ORE.getFluidblock(), LIQUID_CLAY.getFluidblock(),
//					LIQUID_TERRACOTTA.getFluidblock(), LIQUID_WHITE_GLAZED_TERRACOTTA.getFluidblock(),
//					LIQUID_ORANGE_GLAZED_TERRACOTTA.getFluidblock(), LIQUID_MAGENTA_GLAZED_TERRACOTTA.getFluidblock(),
//					LIQUID_LIGHT_BLUE_GLAZED_TERRACOTTA.getFluidblock(), LIQUID_YELLOW_GLAZED_TERRACOTTA.getFluidblock(),
//					LIQUID_LIME_GLAZED_TERRACOTTA.getFluidblock(), LIQUID_PINK_GLAZED_TERRACOTTA.getFluidblock(),
//					LIQUID_GRAY_GLAZED_TERRACOTTA.getFluidblock(), LIQUID_LIGHT_GRAY_GLAZED_TERRACOTTA.getFluidblock(),
//					LIQUID_CYAN_GLAZED_TERRACOTTA.getFluidblock(), LIQUID_PURPLE_GLAZED_TERRACOTTA.getFluidblock(),
//					LIQUID_BLUE_GLAZED_TERRACOTTA.getFluidblock(), LIQUID_BROWN_GLAZED_TERRACOTTA.getFluidblock(),
//					LIQUID_GREEN_GLAZED_TERRACOTTA.getFluidblock(), LIQUID_RED_GLAZED_TERRACOTTA.getFluidblock(),
//					LIQUID_BLACK_GLAZED_TERRACOTTA.getFluidblock(), LIQUID_WHITE_CONCRETE.getFluidblock(),
//					LIQUID_ORANGE_CONCRETE.getFluidblock(), LIQUID_MAGENTA_CONCRETE.getFluidblock(),
//					LIQUID_LIGHT_BLUE_CONCRETE.getFluidblock(), LIQUID_YELLOW_CONCRETE.getFluidblock(),
//					LIQUID_LIME_CONCRETE.getFluidblock(), LIQUID_PINK_CONCRETE.getFluidblock(),
//					LIQUID_GRAY_CONCRETE.getFluidblock(), LIQUID_LIGHT_GRAY_CONCRETE.getFluidblock(),
//					LIQUID_CYAN_CONCRETE.getFluidblock(), LIQUID_PURPLE_CONCRETE.getFluidblock(),
//					LIQUID_BLUE_CONCRETE.getFluidblock(), LIQUID_BROWN_CONCRETE.getFluidblock(),
//					LIQUID_GREEN_CONCRETE.getFluidblock(), LIQUID_RED_CONCRETE.getFluidblock(),
//					LIQUID_BLACK_CONCRETE.getFluidblock(),
                    CRUDE_OIL.getFluidblock()).build(null));


	public static final Supplier<CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(Items.BUCKET))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.liquidblocks"))
			.displayItems((displayParameters, output) -> {
				List<ItemStack> stacks = ModItems.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());


//	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
//		for (DeferredHolder<Item, ? extends Item> itemDeferredHolder : ModItems.ITEMS.getEntries()) {
//			if (itemDeferredHolder.get() instanceof LiquidBucketItem) {
//				event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidBucketWrapper(stack), itemDeferredHolder.get());
//			}
//		}
//	}

    public static Vector3f fogColor(Color mcColor){
        return new Vector3f(
            mcColor.getRed() / 255F,
            mcColor.getGreen() / 255F,
            mcColor.getBlue() / 255F
     );}
}
