package net.venera.galacticraftcore.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.item.custom.FuelItem;
import net.venera.galacticraftcore.item.custom.StandardWrench;
import net.venera.galacticraftcore.item.custom.TempSword;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GalacticraftCore.MOD_ID);

    public static final DeferredItem<Item> IRIDIUM_INGOT = ITEMS.register("gcc_item_iridium_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ALUMINIUM_INGOT = ITEMS.register("gcc_item_aluminium_ingot", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STANDARD_WRENCH = ITEMS.register("gcc_item_standard_wrench", () -> new StandardWrench(new Item.Properties().durability(125)));
    public static final DeferredItem<Item> COPPER_CANISTER = ITEMS.register("gcc_item_copper_canister", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TIN_CANISTER = ITEMS.register("gcc_item_tin_canister", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DEHYDRATED_APPLE = ITEMS.register("gcc_item_dehydrated_apple",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD))
            {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.galacticraftcore.gcc_item_dehydrated_apple"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            }
            );
    public static final DeferredItem<Item> DEHYDRATED_CARROT = ITEMS.register("gcc_item_dehydrated_carrot",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));
    public static final DeferredItem<Item> DEHYDRATED_POTATO = ITEMS.register("gcc_item_dehydrated_potato",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));
    public static final DeferredItem<Item> DEHYDRATED_MELON = ITEMS.register("gcc_item_dehydrated_melon",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> RADIOACTIVE_CORE = ITEMS.register("gcc_item_radioactive_core",
            () -> new FuelItem(new Item.Properties(), 20000));

    public static final DeferredItem<Item> RAW_ALUMINIUM = ITEMS.register("gcc_item_raw_aluminium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_TIN = ITEMS.register("gcc_item_raw_tin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_SILICON= ITEMS.register("gcc_item_raw_silicon", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TIN_INGOT = ITEMS.register("gcc_item_tin_ingot", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COMPRESSED_ALUMINIUM = ITEMS.register("gcc_item_compressed_aluminium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_BRONZE = ITEMS.register("gcc_item_compressed_bronze", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_COPPER = ITEMS.register("gcc_item_compressed_copper", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_IRIDIUM = ITEMS.register("gcc_item_compressed_iridium", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_IRON = ITEMS.register("gcc_item_compressed_iron", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_STEEL = ITEMS.register("gcc_item_compressed_steel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPRESSED_TIN = ITEMS.register("gcc_item_compressed_tin", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SENSOR_LENS = ITEMS.register("gcc_item_sensor_lens", () -> new Item(new Item.Properties()));

    public static final DeferredItem<SwordItem> STEEL_SWORD = ITEMS.register("gcc_item_steel_sword", ()-> new SwordItem(ModToolTiers.STEEL, new Item.Properties()
            .attributes(SwordItem.createAttributes(ModToolTiers.STEEL, 2,-2.4f))));
    public static final DeferredItem<AxeItem> STEEL_AXE = ITEMS.register("gcc_item_steel_axe", ()-> new AxeItem(ModToolTiers.STEEL, new Item.Properties()
            .attributes(AxeItem.createAttributes(ModToolTiers.STEEL, 5,-3.1f))));
    public static final DeferredItem<PickaxeItem> STEEL_PICKAXE = ITEMS.register("gcc_item_steel_pickaxe", ()-> new PickaxeItem(ModToolTiers.STEEL, new Item.Properties()
            .attributes(PickaxeItem.createAttributes(ModToolTiers.STEEL, 0f,-2.8f))));
    public static final DeferredItem<ShovelItem> STEEL_SHOVEL = ITEMS.register("gcc_item_steel_shovel", ()-> new ShovelItem(ModToolTiers.STEEL, new Item.Properties()
            .attributes(ShovelItem.createAttributes(ModToolTiers.STEEL, 0.5f,-3f))));
    public static final DeferredItem<HoeItem> STEEL_HOE = ITEMS.register("gcc_item_steel_hoe", ()-> new HoeItem(ModToolTiers.STEEL, new Item.Properties()
            .attributes(HoeItem.createAttributes(ModToolTiers.STEEL, -3f,-1f))));

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


    public static final DeferredItem<Item> RAW_IRIDIUM = ITEMS.register("gcc_item_raw_iridium",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LUNAR_SAPPHIRE = ITEMS.register("gcc_item_lunar_sapphire",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<BucketItem> CRUDE_OIL_BUCKET = ITEMS.register("gcc_item_crude_oil_bucket",
            () -> new BucketItem(Fluids.FLOWING_LAVA, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(2)));

    public static final DeferredItem<TempSword> TEMP_SWORD = ITEMS.register("temp_item_claymore", () ->
            new TempSword(ModToolTiers.STEEL, (new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.STEEL, 12,-3.9f)))));













    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
