package net.venera.heliocore.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import net.venera.heliocore.item.hpc_custom.GasTankItem;

import java.util.function.Supplier;

public class HpCCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HeliopauseCore.MOD_ID);

    public static final Supplier<CreativeModeTab> HELIOPAUSE_CORE_ITEMS = CREATIVE_MODE_TAB.register("heliocore_items",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(HpCItems.RADIOACTIVE_CORE.get()))
                    .title(Component.translatable("creativetab.heliocore_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(HpCItems.IRIDIUM_INGOT.get());
                        output.accept(HpCItems.TIN_INGOT.get());
                        output.accept(HpCItems.ALUMINIUM_INGOT.get());
                        output.accept(HpCItems.STANDARD_WRENCH.get());
                        output.accept(HpCItems.COPPER_CANISTER.get());
                        output.accept(HpCItems.TIN_CANISTER.get());
                        output.accept(HpCItems.OXYGEN_MASK.get());
                        output.accept(HpCItems.ROCKET_ITEM.get());
                        output.accept(HpCItems.DEHYDRATED_APPLE.get());
                        output.accept(HpCItems.DEHYDRATED_RABBIT.get());
                        output.accept(HpCItems.DEHYDRATED_POTATO.get());
                        output.accept(HpCItems.DEHYDRATED_BERRIES.get());
                        output.accept(HpCItems.DEHYDRATED_GLOW_BERRIES.get());
                        output.accept(HpCItems.DEHYDRATED_COD.get());
                        output.accept(HpCItems.DEHYDRATED_CHICKEN.get());
                        output.accept(HpCItems.DEHYDRATED_BEEF.get());
                        output.accept(HpCItems.CHEESE_SLICE.get());
                        output.accept(HpCItems.HAMBURGER.get());
                        output.accept(HpCItems.RADIOACTIVE_CORE.get());
                        output.accept(HpCItems.RAW_ALUMINIUM.get());
                        output.accept(HpCItems.RAW_TIN.get());
                        output.accept(HpCItems.RAW_SILICON.get());
                        output.accept(HpCItems.COMPRESSED_ALUMINIUM.get());
                        output.accept(HpCItems.COMPRESSED_BRONZE.get());
                        output.accept(HpCItems.COMPRESSED_COPPER.get());
                        output.accept(HpCItems.COMPRESSED_IRIDIUM.get());
                        output.accept(HpCItems.COMPRESSED_IRON.get());
                        output.accept(HpCItems.COMPRESSED_STEEL.get());
                        output.accept(HpCItems.COMPRESSED_TIN.get());
                        output.accept(HpCItems.COMPRESSED_HD_PLATE.get());
                        output.accept(HpCItems.STEEL_SWORD.get());
                        output.accept(HpCItems.STEEL_AXE.get());
                        output.accept(HpCItems.STEEL_PICKAXE.get());
                        output.accept(HpCItems.STEEL_SHOVEL.get());
                        output.accept(HpCItems.STEEL_HOE.get());
                        output.accept(HpCItems.STEEL_HELMET.get());
                        output.accept(HpCItems.STEEL_CHESTPLATE.get());
                        output.accept(HpCItems.STEEL_LEGGINGS.get());
                        output.accept(HpCItems.STEEL_BOOTS.get());
                        output.accept(HpCItems.GLASS_SWORD.get());
                        output.accept(HpCFluids.CRUDE_OIL.getBucket());
                        output.accept(HpCFluids.REFINED_FUEL.getBucket());
                        output.accept(HpCItems.CANISTER.get());
                        output.accept(HpCItems.CANISTER.get().setCanisterData(new ItemStack(HpCItems.CANISTER.get()), CanisterData.CRUDE_OIL, CanisterItem.MAX_CAPACITY));
                        output.accept(HpCItems.CANISTER.get().setCanisterData(new ItemStack(HpCItems.CANISTER.get()), CanisterData.REFINED_FUEL, CanisterItem.MAX_CAPACITY));
                        output.accept(HpCItems.COMPRESSED_GAS_TANK.get());
                        output.accept(HpCItems.COMPRESSED_GAS_TANK.get().setGasTankData(new ItemStack(HpCItems.COMPRESSED_GAS_TANK.get()), GasTankData.OXYGEN_GAS, GasTankItem.MAX_CAPACITY));
                        output.accept(HpCItems.SMALL_BATTERY.get());
                        output.accept(HpCItems.SMALL_BATTERY.get().createFullInstance());

                    })
                    .build()
    );

    public static final Supplier<CreativeModeTab> HELIOPAUSE_CORE_BLOCKS = CREATIVE_MODE_TAB.register("heliocore_blocks",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(HpCBlocks.RADIOACTIVE_BLOCK.get()))
                    .title(Component.translatable("creativetab.heliocore_blocks"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "heliocore_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(HpCBlocks.ALUMINIUM_ORE.get());
                        output.accept(HpCBlocks.DEEPSLATE_ALUMINIUM_ORE.get());
                        output.accept(HpCBlocks.ALUMINIUM_BLOCK.get());
                        output.accept(HpCBlocks.RADIOACTIVE_BLOCK.get());
                        output.accept(HpCBlocks.CHEESE_BLOCK.get());
                        output.accept(HpCBlocks.PIZZA_BLOCK.get());
                        output.accept(HpCBlocks.MOON_REGOLITH.get());
                        output.accept(HpCBlocks.MOON_DIRT.get());
                        output.accept(HpCBlocks.MOON_ROCK.get());
                        output.accept(HpCBlocks.TIN_BUILDING_BLOCK.get());
                        output.accept(HpCBlocks.TIN_BLOCK.get());
                        output.accept(HpCBlocks.TIN_ORE.get());
                        output.accept(HpCBlocks.DEEPSLATE_TIN_ORE.get());
                        output.accept(HpCBlocks.RAW_TIN_BLOCK.get());
                        output.accept(HpCBlocks.RAW_ALUMINIUM_BLOCK.get());
                        output.accept(HpCBlocks.SILICON_ORE.get());
                        output.accept(HpCBlocks.DEEPSLATE_SILICON_ORE.get());
                        output.accept(HpCBlocks.SILICON_BLOCK.get());
                        output.accept(HpCBlocks.MOON_COBBLESTONE.get());
                        output.accept(HpCBlocks.MOON_TIN_ORE.get());
                        output.accept(HpCBlocks.MOON_SILICON_ORE.get());
                        output.accept(HpCBlocks.MOON_ALUMINIUM_ORE.get());
                        output.accept(HpCBlocks.MOON_IRON_ORE);
                        output.accept(HpCBlocks.MOON_COPPER_ORE.get());
                        output.accept(HpCBlocks.MOON_DUNGEON_BRICKS.get());
                        output.accept(HpCBlocks.TIN_BUILDING_WALL.get());
                        output.accept(HpCBlocks.MOON_ROCK_WALL.get());
                        output.accept(HpCBlocks.MOON_DUNGEON_BRICK_WALL.get());
                        output.accept(HpCBlocks.TIN_BUILDING_STAIRS.get());
                        output.accept(HpCBlocks.MOON_ROCK_STAIRS.get());
                        output.accept(HpCBlocks.MOON_DUNGEON_BRICK_STAIRS.get());
                        output.accept(HpCBlocks.TIN_BUILDING_SLAB.get());
                        output.accept(HpCBlocks.MOON_ROCK_SLAB.get());
                        output.accept(HpCBlocks.MOON_DUNGEON_BRICK_SLAB.get());
                        output.accept(HpCBlocks.ARC_LAMP.get());
                        output.accept(HpCBlocks.IRIDIUM_ORE.get());
                        output.accept(HpCBlocks.DEEPSLATE_IRIDIUM_ORE.get());
                        output.accept(HpCBlocks.MOON_IRIDIUM_ORE.get());
                        output.accept(HpCBlocks.IRIDIUM_BLOCK.get());
                        output.accept(HpCBlocks.COAL_COMPRESSOR.get());
                        output.accept(HpCBlocks.REFINERY.get());
                        output.accept(HpCBlocks.CARGO_MANAGER_BLOCK.get());
                        output.accept(HpCBlocks.FUEL_MANAGER_BLOCK.get());
                        output.accept(HpCBlocks.LAUNCH_PAD.get());
                        output.accept(HpCBlocks.FLUID_TANK.get());
                        output.accept(HpCBlocks.ENERGY_STORAGE_UNIT.get());
                        output.accept(HpCBlocks.BASIC_SOLAR_BLOCK.get());
                        output.accept(HpCBlocks.COPPER_WIRE.get());
                        output.accept(HpCBlocks.FLUID_PIPE.get());
                        output.accept(HpCBlocks.PRISMATIC_GLASS.get());
                        output.accept(HpCBlocks.PRISMATIC_GLASS_PANE.get());
                        output.accept(HpCBlocks.TINTED_PRISMATIC_GLASS.get());
                        output.accept(HpCBlocks.COPPER_WIRE_BLOCK.get());

                    })
                    .build()
    );

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
