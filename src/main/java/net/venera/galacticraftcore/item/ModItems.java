package net.venera.galacticraftcore.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.item.custom.FuelItem;
import net.venera.galacticraftcore.item.custom.StandardWrench;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GalacticraftCore.MOD_ID);

    public static final DeferredItem<Item> IRIDIUM_INGOT = ITEMS.register("iridium_ingot", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ALUMINIUM_INGOT = ITEMS.register("aluminium_ingot", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> STANDARD_WRENCH = ITEMS.register("standard_wrench", () -> new StandardWrench(new Item.Properties().durability(125)));

    public static final DeferredItem<Item> COPPER_CANISTER = ITEMS.register("copper_canister", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TIN_CANISTER = ITEMS.register("tin_canister", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DEHYDRATED_APPLE = ITEMS.register("dehydrated_apple",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD))
            {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.galacticraftcore.dehydrated_apple"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            }
            );

    public static final DeferredItem<Item> DEHYDRATED_CARROT = ITEMS.register("dehydrated_carrot",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_POTATO = ITEMS.register("dehydrated_potato",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> DEHYDRATED_MELON = ITEMS.register("dehydrated_melon",
            () -> new Item(new Item.Properties().food(ModFoodProperties.DEHYDRATED_FOOD)));

    public static final DeferredItem<Item> RADIOACTIVE_CORE = ITEMS.register("radioactive_core",
            () -> new FuelItem(new Item.Properties(), 20000));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
