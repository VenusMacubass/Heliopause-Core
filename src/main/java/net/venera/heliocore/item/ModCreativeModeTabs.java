package net.venera.heliocore.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.ModBlocks;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.fluid.ModFluids;
import net.venera.heliocore.item.custom.CanisterItem;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HeliopauseCore.MOD_ID);

    public static final Supplier<CreativeModeTab> HELIOPAUSE_CORE_ITEMS = CREATIVE_MODE_TAB.register("heliocore_items",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.RADIOACTIVE_CORE.get()))
                    .title(Component.translatable("creativetab.heliocore_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.IRIDIUM_INGOT.get());
                        output.accept(ModItems.TIN_INGOT.get());
                        output.accept(ModItems.ALUMINIUM_INGOT.get());
                        output.accept(ModItems.STANDARD_WRENCH.get());
                        output.accept(ModItems.COPPER_CANISTER.get());
                        output.accept(ModItems.TIN_CANISTER.get());
                        output.accept(ModItems.OXYGEN_MASK.get());
                        output.accept(ModItems.DEHYDRATED_APPLE.get());
                        output.accept(ModItems.DEHYDRATED_RABBIT.get());
                        output.accept(ModItems.DEHYDRATED_POTATO.get());
                        output.accept(ModItems.DEHYDRATED_BERRIES.get());
                        output.accept(ModItems.DEHYDRATED_GLOW_BERRIES.get());
                        output.accept(ModItems.DEHYDRATED_COD.get());
                        output.accept(ModItems.DEHYDRATED_CHICKEN.get());
                        output.accept(ModItems.DEHYDRATED_BEEF.get());
                        output.accept(ModItems.CHEESE_SLICE.get());
                        output.accept(ModItems.HAMBURGER.get());
                        output.accept(ModItems.RADIOACTIVE_CORE.get());
                        output.accept(ModItems.RAW_ALUMINIUM.get());
                        output.accept(ModItems.RAW_TIN.get());
                        output.accept(ModItems.RAW_SILICON.get());
                        output.accept(ModItems.COMPRESSED_ALUMINIUM.get());
                        output.accept(ModItems.COMPRESSED_BRONZE.get());
                        output.accept(ModItems.COMPRESSED_COPPER.get());
                        output.accept(ModItems.COMPRESSED_IRIDIUM.get());
                        output.accept(ModItems.COMPRESSED_IRON.get());
                        output.accept(ModItems.COMPRESSED_STEEL.get());
                        output.accept(ModItems.COMPRESSED_TIN.get());
                        output.accept(ModItems.COMPRESSED_HD_PLATE.get());
                        output.accept(ModItems.STEEL_SWORD.get());
                        output.accept(ModItems.STEEL_AXE.get());
                        output.accept(ModItems.STEEL_PICKAXE.get());
                        output.accept(ModItems.STEEL_SHOVEL.get());
                        output.accept(ModItems.STEEL_HOE.get());
                        output.accept(ModItems.STEEL_HELMET.get());
                        output.accept(ModItems.STEEL_CHESTPLATE.get());
                        output.accept(ModItems.STEEL_LEGGINGS.get());
                        output.accept(ModItems.STEEL_BOOTS.get());
                        output.accept(ModItems.GLASS_SWORD.get());
                        output.accept(ModFluids.CRUDE_OIL.getBucket());
                        output.accept(ModFluids.REFINED_FUEL.getBucket());
                        output.accept(ModItems.CANISTER.get());
                        output.accept(ModItems.CANISTER.get().setCanisterData(new ItemStack(ModItems.CANISTER.get()), CanisterData.CRUDE_OIL, CanisterItem.MAX_CAPACITY));
                        output.accept(ModItems.CANISTER.get().setCanisterData(new ItemStack(ModItems.CANISTER.get()), CanisterData.REFINED_FUEL, CanisterItem.MAX_CAPACITY));
                        output.accept(ModItems.SMALL_BATTERY.get());
                        output.accept(ModItems.SMALL_BATTERY.get().createFullInstance());

                    })
                    .build()
    );

    public static final Supplier<CreativeModeTab> HELIOPAUSE_CORE_BLOCKS = CREATIVE_MODE_TAB.register("heliocore_blocks",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.RADIOACTIVE_BLOCK.get()))
                    .title(Component.translatable("creativetab.heliocore_blocks"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "heliocore_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.ALUMINIUM_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_ALUMINIUM_ORE.get());
                        output.accept(ModBlocks.ALUMINIUM_BLOCK.get());
                        output.accept(ModBlocks.RADIOACTIVE_BLOCK.get());
                        output.accept(ModBlocks.CHEESE_BLOCK.get());
                        output.accept(ModBlocks.PIZZA_BLOCK.get());
                        output.accept(ModBlocks.MOON_REGOLITH.get());
                        output.accept(ModBlocks.MOON_DIRT.get());
                        output.accept(ModBlocks.MOON_ROCK.get());
                        output.accept(ModBlocks.TIN_BUILDING_BLOCK.get());
                        output.accept(ModBlocks.TIN_BLOCK.get());
                        output.accept(ModBlocks.TIN_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_TIN_ORE.get());
                        output.accept(ModBlocks.RAW_TIN_BLOCK.get());
                        output.accept(ModBlocks.RAW_ALUMINIUM_BLOCK.get());
                        output.accept(ModBlocks.SILICON_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_SILICON_ORE.get());
                        output.accept(ModBlocks.SILICON_BLOCK.get());
                        output.accept(ModBlocks.MOON_COBBLESTONE.get());
                        output.accept(ModBlocks.MOON_TIN_ORE.get());
                        output.accept(ModBlocks.MOON_SILICON_ORE.get());
                        output.accept(ModBlocks.MOON_ALUMINIUM_ORE.get());
                        output.accept(ModBlocks.MOON_IRON_ORE);
                        output.accept(ModBlocks.MOON_COPPER_ORE.get());
                        output.accept(ModBlocks.MOON_DUNGEON_BRICKS.get());
                        output.accept(ModBlocks.TIN_BUILDING_WALL.get());
                        output.accept(ModBlocks.MOON_ROCK_WALL.get());
                        output.accept(ModBlocks.MOON_DUNGEON_BRICK_WALL.get());
                        output.accept(ModBlocks.TIN_BUILDING_STAIRS.get());
                        output.accept(ModBlocks.MOON_ROCK_STAIRS.get());
                        output.accept(ModBlocks.MOON_DUNGEON_BRICK_STAIRS.get());
                        output.accept(ModBlocks.TIN_BUILDING_SLAB.get());
                        output.accept(ModBlocks.MOON_ROCK_SLAB.get());
                        output.accept(ModBlocks.MOON_DUNGEON_BRICK_SLAB.get());
                        output.accept(ModBlocks.ARC_LAMP.get());
                        output.accept(ModBlocks.IRIDIUM_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_IRIDIUM_ORE.get());
                        output.accept(ModBlocks.MOON_IRIDIUM_ORE.get());
                        output.accept(ModBlocks.IRIDIUM_BLOCK.get());
                        output.accept(ModBlocks.COAL_COMPRESSOR.get());
                        output.accept(ModBlocks.REFINERY.get());
                        output.accept(ModBlocks.CARGO_MANAGER_BLOCK.get());
                        output.accept(ModBlocks.FUEL_MANAGER_BLOCK.get());
                        output.accept(ModBlocks.LAUNCH_PAD.get());
                        output.accept(ModBlocks.FLUID_TANK.get());
                        output.accept(ModBlocks.ENERGY_STORAGE_UNIT.get());
                        output.accept(ModBlocks.BASIC_SOLAR_BLOCK.get());
                        output.accept(ModBlocks.COPPER_WIRE.get());
                        output.accept(ModBlocks.FLUID_PIPE.get());
                        output.accept(ModBlocks.T1_ROCKET_BOT.get());
                        output.accept(ModBlocks.PRISMATIC_GLASS.get());
                        output.accept(ModBlocks.COPPER_WIRE_BLOCK.get());

                    })
                    .build()
    );

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
