package net.venera.galacticraftcore.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.data.component.BatteryData;
import net.venera.galacticraftcore.data.component.CanisterData;
import net.venera.galacticraftcore.data.component.ModDataComponents;
import net.venera.galacticraftcore.item.custom.*;

import java.util.List;
import java.util.Stack;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GalacticraftCore.MOD_ID);

    //region Metals
    public static final DeferredItem<Item> RAW_TIN = ITEMS.register("gcc_item_raw_tin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_ALUMINIUM = ITEMS.register("gcc_item_raw_aluminium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_SILICON = ITEMS.register("gcc_item_raw_silicon", () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> ALUMINIUM_INGOT = ITEMS.register("aluminium_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TIN_INGOT = ITEMS.register("tin_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> IRIDIUM_INGOT = ITEMS.register("iridium_ingot", () -> new Item(new Item.Properties()));
    
    //endregion
   
    //region Foods
    public static final DeferredItem<Item> DEHYDRATED_APPLE = ITEMS.register("gcc_item_dehydrated_apple",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.galacticraftcore.gcc_item_dehydrated_apple"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            }
    );
    
    public static final DeferredItem<Item> DEHYDRATED_POTATO = ITEMS.register("gcc_item_dehydrated_potato",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_BERRIES = ITEMS.register("dehydrated_berries",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_GLOW_BERRIES = ITEMS.register("dehydrated_glow_berries",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_BEEF = ITEMS.register("gcc_item_dehydrated_beef",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_RABBIT = ITEMS.register("dehydrated_rabbit",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_COD = ITEMS.register("dehydrated_cod",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_CHICKEN = ITEMS.register("dehydrated_chicken",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));
    
    public static final DeferredItem<Item> CHEESE_SLICE = ITEMS.register("gcc_item_cheese_slice",
            () -> new Item(new Item.Properties().food(ModFoodProperties.EDIBLE_INGREDIENT)));
    
    public static final DeferredItem<Item> CHEESEBURGER = ITEMS.register("gcc_item_cheeseburger",
            () -> new Item(new Item.Properties().food(ModFoodProperties.MODERN_FOOD)));

    public static final DeferredItem<Item> RADIOACTIVE_CORE = ITEMS.register("gcc_item_radioactive_core",
            () -> new FuelItem(new Item.Properties(), 20000));
    //endregion

    //region Ingredients
    public static final DeferredItem<Item> COPPER_CANISTER = ITEMS.register("gcc_item_copper_canister", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TIN_CANISTER = ITEMS.register("gcc_item_tin_canister", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_ALUMINIUM = ITEMS.register("gcc_item_compressed_aluminium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_BRONZE = ITEMS.register("gcc_item_compressed_bronze", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_COPPER = ITEMS.register("gcc_item_compressed_copper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_IRIDIUM = ITEMS.register("gcc_item_compressed_iridium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_IRON = ITEMS.register("gcc_item_compressed_iron", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_STEEL = ITEMS.register("gcc_item_compressed_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_TIN = ITEMS.register("gcc_item_compressed_tin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_HD_PLATE = ITEMS.register("gcc_item_heavy_duty_plate", () -> new Item(new Item.Properties()));
    public static final DeferredItem<CanisterItem> CANISTER = ITEMS.register("gcc_canister", () -> new CanisterItem(new Item.Properties().component(
            ModDataComponents.CANISTER_COMPONENT.get(), new CanisterData(null, 0)).stacksTo(1)));
    //endregion

    //region Tools
    public static final DeferredItem<SwordItem> STEEL_SWORD = ITEMS.register("gcc_item_steel_sword", () -> new SwordItem(ModToolTiers.STEEL, new Item.Properties()
            .attributes(SwordItem.createAttributes(ModToolTiers.STEEL, 2, -2.4f))));
    public static final DeferredItem<AxeItem> STEEL_AXE = ITEMS.register("gcc_item_steel_axe", () -> new AxeItem(ModToolTiers.STEEL, new Item.Properties()
            .attributes(AxeItem.createAttributes(ModToolTiers.STEEL, 5, -3.1f))));
    public static final DeferredItem<PickaxeItem> STEEL_PICKAXE = ITEMS.register("gcc_item_steel_pickaxe", () -> new PickaxeItem(ModToolTiers.STEEL, new Item.Properties()
            .attributes(PickaxeItem.createAttributes(ModToolTiers.STEEL, 0f, -2.8f))));
    public static final DeferredItem<ShovelItem> STEEL_SHOVEL = ITEMS.register("gcc_item_steel_shovel", () -> new ShovelItem(ModToolTiers.STEEL, new Item.Properties()
            .attributes(ShovelItem.createAttributes(ModToolTiers.STEEL, 0.5f, -3f))));
    public static final DeferredItem<HoeItem> STEEL_HOE = ITEMS.register("gcc_item_steel_hoe", () -> new HoeItem(ModToolTiers.STEEL, new Item.Properties()
            .attributes(HoeItem.createAttributes(ModToolTiers.STEEL, -3f, -1f))));
    public static final DeferredItem<Item> STANDARD_WRENCH = ITEMS.register("gcc_item_standard_wrench", () -> new StandardWrench(new Item.Properties().durability(125)));

    //endregion

    //region Armors
    public static final DeferredItem<ArmorItem> STEEL_HELMET = ITEMS.register("gcc_item_steel_helmet", () ->
            new ArmorItem(ModArmorMaterials.STEEL_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(18))));
    public static final DeferredItem<ArmorItem> STEEL_CHESTPLATE = ITEMS.register("gcc_item_steel_chestplate", () ->
            new ArmorItem(ModArmorMaterials.STEEL_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(18))));
    public static final DeferredItem<ArmorItem> STEEL_LEGGINGS = ITEMS.register("gcc_item_steel_leggings", () ->
            new ArmorItem(ModArmorMaterials.STEEL_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(18))));
    public static final DeferredItem<ArmorItem> STEEL_BOOTS = ITEMS.register("gcc_item_steel_boots", () ->
            new ArmorItem(ModArmorMaterials.STEEL_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(18))));
    //endregion

   
    public static final DeferredItem<Item> LUNAR_SAPPHIRE = ITEMS.register("gcc_item_lunar_sapphire",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<SwordItem> GLASS_SWORD = ITEMS.register("glass_sword", () ->
            new SwordItem(ModToolTiers.GLASS, (new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.GLASS, 64, 1.8f)))));

    

    public static final DeferredItem<BatteryItem> SMALL_BATTERY = ITEMS.register("small_battery", () ->
            new BatteryItem(new Item.Properties().component(ModDataComponents.BATTERY_COMPONENT.get(), new BatteryData(0, 500)),
                    500, 1));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
