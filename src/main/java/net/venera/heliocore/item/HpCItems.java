package net.venera.heliocore.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.component.BatteryData;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.data.component.HpCDataComponents;
import net.venera.heliocore.item.hpc_custom.*;

import java.util.List;

public class HpCItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HeliopauseCore.MOD_ID);

    //region Metals
    public static final DeferredItem<Item> RAW_TIN = ITEMS.register("raw_tin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_ALUMINIUM = ITEMS.register("raw_aluminium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SILICON = ITEMS.register("silicon", () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> ALUMINIUM_INGOT = ITEMS.register("aluminium_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TIN_INGOT = ITEMS.register("tin_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> IRIDIUM_INGOT = ITEMS.register("iridium_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TEKTITES = ITEMS.register("tektites", () -> new Item(new Item.Properties()));
    
    //endregion
   
    //region Foods
    public static final DeferredItem<Item> DEHYDRATED_APPLE = ITEMS.register("dehydrated_apple",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.DEHYDRATED_FOOD)) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.dehydrated_apple"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            }
    );
    
    public static final DeferredItem<Item> DEHYDRATED_POTATO = ITEMS.register("dehydrated_potato",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_BERRIES = ITEMS.register("dehydrated_berries",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_GLOW_BERRIES = ITEMS.register("dehydrated_glow_berries",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_BEEF = ITEMS.register("dehydrated_beef",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_RABBIT = ITEMS.register("dehydrated_rabbit",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_COD = ITEMS.register("dehydrated_cod",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_CHICKEN = ITEMS.register("dehydrated_chicken",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.DEHYDRATED_FOOD)));
    
    public static final DeferredItem<Item> CHEESE_SLICE = ITEMS.register("cheese_slice",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.EDIBLE_INGREDIENT)));
    
    public static final DeferredItem<Item> HAMBURGER = ITEMS.register("hamburger",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.MODERN_FOOD)));

    public static final DeferredItem<BatteryItem> RADIOACTIVE_CORE = ITEMS.register("radioactive_core",
            () -> new BatteryItem(new Item.Properties().component(HpCDataComponents.BATTERY_COMPONENT.get(), 
                    new BatteryData(Integer.MAX_VALUE, Integer.MAX_VALUE)), Integer.MAX_VALUE, 5));
    //endregion

    //region Ingredients
    public static final DeferredItem<Item> COPPER_CANISTER = ITEMS.register("copper_canister", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TIN_CANISTER = ITEMS.register("tin_canister", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_ALUMINIUM = ITEMS.register("compressed_aluminium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_BRONZE = ITEMS.register("compressed_bronze", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_COPPER = ITEMS.register("compressed_copper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_IRIDIUM = ITEMS.register("compressed_iridium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_IRON = ITEMS.register("compressed_iron", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_STEEL = ITEMS.register("compressed_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_TIN = ITEMS.register("compressed_tin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_HD_PLATE = ITEMS.register("heavy_duty_plate", () -> new Item(new Item.Properties()));
    public static final DeferredItem<CanisterItem> CANISTER = ITEMS.register("canister", () -> new CanisterItem(new Item.Properties().component(
            HpCDataComponents.CANISTER_COMPONENT.get(), new CanisterData(null, 0)).stacksTo(1)));
    public static final DeferredItem<GasTankItem> COMPRESSED_GAS_TANK = ITEMS.register("compressed_gas_tank", () -> new GasTankItem(new Item.Properties().component(
            HpCDataComponents.GAS_TANK_COMPONENT.get(), new GasTankData(null, 0)).stacksTo(1)));
    public static final DeferredItem<Item> PETROCHEMICALS = ITEMS.register("petrochemicals", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> AIR_FAN = ITEMS.register("air_fan", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BASIC_CIRCUIT_BOARD = ITEMS.register("basic_circuit_board", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ELECTROMAGNETIC_SENSORS = ITEMS.register("electromagnetic_sensors", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FLUID_FILTER = ITEMS.register("fluid_filter", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GAS_REGULATOR =  ITEMS.register("gas_regulator", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> THERMAL_REGULATOR =  ITEMS.register("thermal_regulator", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STEEL_ROD =  ITEMS.register("steel_rod", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> THERMAL_ISOLATOR =  ITEMS.register("thermal_isolator", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SOLAR_PANEL_SEMICONDUCTOR_BASE = ITEMS.register("solar_panel_semiconductor_base", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SINGULAR_SOLAR_PANEL = ITEMS.register("singular_solar_panel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TIER_1_SOLAR_PANEL = ITEMS.register("tier_1_solar_panel", () -> new Item(new Item.Properties()));
    //endregion

    //region Tools
    public static final DeferredItem<SwordItem> STEEL_SWORD = ITEMS.register("steel_sword", () -> new SwordItem(HpCToolTiers.STEEL, new Item.Properties()
            .attributes(SwordItem.createAttributes(HpCToolTiers.STEEL, 2, -2.4f))));
    public static final DeferredItem<AxeItem> STEEL_AXE = ITEMS.register("steel_axe", () -> new AxeItem(HpCToolTiers.STEEL, new Item.Properties()
            .attributes(AxeItem.createAttributes(HpCToolTiers.STEEL, 5, -3.1f))));
    public static final DeferredItem<PickaxeItem> STEEL_PICKAXE = ITEMS.register("steel_pickaxe", () -> new PickaxeItem(HpCToolTiers.STEEL, new Item.Properties()
            .attributes(PickaxeItem.createAttributes(HpCToolTiers.STEEL, 0f, -2.8f))));
    public static final DeferredItem<ShovelItem> STEEL_SHOVEL = ITEMS.register("steel_shovel", () -> new ShovelItem(HpCToolTiers.STEEL, new Item.Properties()
            .attributes(ShovelItem.createAttributes(HpCToolTiers.STEEL, 0.5f, -3f))));
    public static final DeferredItem<HoeItem> STEEL_HOE = ITEMS.register("steel_hoe", () -> new HoeItem(HpCToolTiers.STEEL, new Item.Properties()
            .attributes(HoeItem.createAttributes(HpCToolTiers.STEEL, -3f, -1f))));
    public static final DeferredItem<Item> STANDARD_WRENCH = ITEMS.register("standard_wrench", () -> new StandardWrench(new Item.Properties().durability(125)));
    public static final DeferredItem<SwordItem> GLASS_SWORD = ITEMS.register("glass_sword", () ->
            new SwordItem(HpCToolTiers.GLASS, (new Item.Properties().attributes(SwordItem.createAttributes(HpCToolTiers.GLASS, 64, 1.8f)))));
    //endregion

    //region Armors
    public static final DeferredItem<ArmorItem> STEEL_HELMET = ITEMS.register("steel_helmet", () ->
            new ArmorItem(HpCArmorMaterials.STEEL_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(18))));
    public static final DeferredItem<ArmorItem> STEEL_CHESTPLATE = ITEMS.register("steel_chestplate", () ->
            new ArmorItem(HpCArmorMaterials.STEEL_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(18))));
    public static final DeferredItem<ArmorItem> STEEL_LEGGINGS = ITEMS.register("steel_leggings", () ->
            new ArmorItem(HpCArmorMaterials.STEEL_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(18))));
    public static final DeferredItem<ArmorItem> STEEL_BOOTS = ITEMS.register("steel_boots", () ->
            new ArmorItem(HpCArmorMaterials.STEEL_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(18))));
    public static final DeferredItem<ArmorItem> OXYGEN_MASK = ITEMS.register("oxygen_mask", () ->
            new ArmorItem(HpCArmorMaterials.SPACE_GEAR_MATERIAL, ArmorItem.Type.HELMET, new  Item.Properties()));
    public static final DeferredItem<ArmorItem> OXYGEN_CONNECTORS = ITEMS.register("oxygen_connectors", () ->
            new ArmorItem(HpCArmorMaterials.SPACE_GEAR_MATERIAL, ArmorItem.Type.BODY, new  Item.Properties()));
    public static final DeferredItem<Item> MASS_BELT = ITEMS.register("mass_belt", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> T1_THERMAL_INSULATION_HEAD = ITEMS.register("t1_thermal_insulation_head", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> T1_THERMAL_INSULATION_TORSO = ITEMS.register("t1_thermal_insulation_torso", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> T1_THERMAL_INSULATION_LEGGINGS = ITEMS.register("t1_thermal_insulation_leggings", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> T1_THERMAL_INSULATION_HANDS_AND_FEET = ITEMS.register("t1_thermal_insulation_hands_and_feet", () -> new Item(new Item.Properties()));
    //endregion

    
    
    public static final DeferredItem<Item> ROCKET_ITEM = ITEMS.register("rocket_item", () -> 
            new RocketItem(new Item.Properties()));
    
    public static final DeferredItem<Item> THERMAL_CONTROLLER = ITEMS.register("thermal_controller", () ->
            new Item(new Item.Properties()));

    

    public static final DeferredItem<BatteryItem> SMALL_BATTERY = ITEMS.register("small_battery", () ->
            new BatteryItem(new Item.Properties().component(HpCDataComponents.BATTERY_COMPONENT.get(), 
                    new BatteryData(0, 10000)), 10000, 20));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
