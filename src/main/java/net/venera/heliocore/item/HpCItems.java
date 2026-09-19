package net.venera.heliocore.item;

import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.data.component.BatteryData;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.data.component.HpCDataComponents;
import net.venera.heliocore.data.radiation.RadiationData;
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
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.APPLE, 2))) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.dehydrated_apple"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(15, false);
                    }
                    return result;
                }
            }
    );
    public static final DeferredItem<Item> CHIPS = ITEMS.register("chips",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.BAKED_POTATO, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.chips"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(15, false); 
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> DEHYDRATED_CARROT = ITEMS.register("dehydrated_carrot",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.CARROT, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.dehydrated_carrot"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(15, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> DEHYDRATED_BERRIES = ITEMS.register("dehydrated_berries",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.SWEET_BERRIES, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.dehydrated_berries"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(15, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> DEHYDRATED_GLOW_BERRIES = ITEMS.register("dehydrated_glow_berries",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.GLOW_BERRIES, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.dehydrated_glow_berries"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(15, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> DEHYDRATED_KELP = ITEMS.register("dehydrated_kelp",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.DRIED_KELP, 3))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.dehydrated_kelp"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(15, false);
                    }
                    return result;
                }
            });
    
    public static final DeferredItem<Item> DEHYDRATED_CHICKEN = ITEMS.register("dehydrated_chicken",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.CHICKEN, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.dehydrated_chicken"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(20, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> DEHYDRATED_BEEF = ITEMS.register("dehydrated_beef",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.BEEF, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.dehydrated_beef"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(20, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> DEHYDRATED_RABBIT = ITEMS.register("dehydrated_rabbit",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.RABBIT, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.dehydrated_rabbit"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(20, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> DEHYDRATED_COD = ITEMS.register("dehydrated_cod",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.COD, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.dehydrated_cod"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(20, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> DEHYDRATED_SALMON = ITEMS.register("dehydrated_salmon",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.SALMON, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.dehydrated_salmon"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(20, false);
                    }
                    return result;
                }
            });

    public static final DeferredItem<Item> CANNED_CHICKEN = ITEMS.register("canned_chicken",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.COOKED_CHICKEN, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.canned_chicken"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(30, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> CANNED_STEAK = ITEMS.register("canned_steak",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.COOKED_BEEF, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.canned_steak"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(30, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> CANNED_MUTTON = ITEMS.register("canned_mutton",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.COOKED_MUTTON, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.canned_mutton"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(30, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> CANNED_RABBIT = ITEMS.register("canned_rabbit",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.COOKED_RABBIT, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.canned_rabbit"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(30, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> CANNED_COD = ITEMS.register("canned_cod",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.COOKED_COD, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.canned_cod"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(30, false);
                    }
                    return result;
                }
            });
    public static final DeferredItem<Item> CANNED_SALMON = ITEMS.register("canned_salmon",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.COOKED_SALMON, 2))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.canned_salmon"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(30, false);
                    }
                    return result;
                }
            });
    
    public static final DeferredItem<Item> CANNED_BEETROOT_SOUP = ITEMS.register("canned_beetroot_soup",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.BEETROOT_SOUP, 1))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.canned_beetroot_soup"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    // 1. Process the eating (reduces stack size, restores hunger)
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);

                    // 2. Apply radiation safely on the server side
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        // Note: Make sure HpCAttachments is correct for your codebase
                        var radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(50, false);
                    }

                    // 3. Return the Empty Can!
                    if (entityLiving instanceof Player player && !player.getAbilities().instabuild) {
                        // Change EMPTY_CAN to match whatever your actual item is named in your registry
                        ItemStack emptyCan = new ItemStack(HpCItems.EMPTY_CAN.get());

                        // If they ate the last one in the stack, swap it to an empty can
                        if (result.isEmpty()) {
                            return emptyCan;
                        } else {
                            // Otherwise, try to put it in their inventory, or drop if full
                            if (!player.getInventory().add(emptyCan)) {
                                player.drop(emptyCan, false);
                            }
                        }
                    }

                    return result;
                }
            });
    public static final DeferredItem<Item> CANNED_MUSHROOM_STEW = ITEMS.register("canned_mushroom_stew",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.MUSHROOM_STEW, 1))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.canned_mushroom_stew"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    // 1. Process the eating (reduces stack size, restores hunger)
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);

                    // 2. Apply radiation safely on the server side
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        // Note: Make sure HpCAttachments is correct for your codebase
                        var radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(50, false);
                    }

                    // 3. Return the Empty Can!
                    if (entityLiving instanceof Player player && !player.getAbilities().instabuild) {
                        // Change EMPTY_CAN to match whatever your actual item is named in your registry
                        ItemStack emptyCan = new ItemStack(HpCItems.EMPTY_CAN.get());

                        // If they ate the last one in the stack, swap it to an empty can
                        if (result.isEmpty()) {
                            return emptyCan;
                        } else {
                            // Otherwise, try to put it in their inventory, or drop if full
                            if (!player.getInventory().add(emptyCan)) {
                                player.drop(emptyCan, false);
                            }
                        }
                    }

                    return result;
                }
            });
    public static final DeferredItem<Item> CANNED_RABBIT_STEW = ITEMS.register("canned_rabbit_stew",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.RABBIT_STEW, 1))){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.canned_rabbit_stew"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    // 1. Process the eating (reduces stack size, restores hunger)
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);

                    // 2. Apply radiation safely on the server side
                    if (!level.isClientSide() && entityLiving instanceof Player player) {
                        // Note: Make sure HpCAttachments is correct for your codebase
                        var radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                        radiationData.changeRadiation(50, false);
                    }

                    // 3. Return the Empty Can!
                    if (entityLiving instanceof Player player && !player.getAbilities().instabuild) {
                        // Change EMPTY_CAN to match whatever your actual item is named in your registry
                        ItemStack emptyCan = new ItemStack(HpCItems.EMPTY_CAN.get());

                        // If they ate the last one in the stack, swap it to an empty can
                        if (result.isEmpty()) {
                            return emptyCan;
                        } else {
                            // Otherwise, try to put it in their inventory, or drop if full
                            if (!player.getInventory().add(emptyCan)) {
                                player.drop(emptyCan, false);
                            }
                        }
                    }

                    return result;
                }
            });
    public static final DeferredItem<Item> CANNED_SUSPICIOUS_STEW = ITEMS.register("canned_suspicious_stew",
            () -> new Item(new Item.Properties().food(getInheritedFoodProps(Items.SUSPICIOUS_STEW, 1)).component(DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY)){
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.heliocore.canned_suspicious_stew"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
                @Override
                public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
                    var effects = stack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
                    ItemStack result = super.finishUsingItem(stack, level, entityLiving);
                    
                    if (!level.isClientSide()) {
                        if (entityLiving instanceof Player player) {
                            RadiationData radiationData = player.getData(HpCAttachments.RADIATION_DATA);
                            radiationData.changeRadiation(50, false);
                        }
                        
                        if (effects != null) {
                            for (var entry : effects.effects()) {
                                entityLiving.addEffect(entry.createEffectInstance());
                            }
                        }
                    }
                    
                    if (entityLiving instanceof Player player && !player.getAbilities().instabuild) {
                        ItemStack emptyCan = new ItemStack(HpCItems.EMPTY_CAN.get()); 

                        if (result.isEmpty()) {
                            return emptyCan;
                        } else {
                            if (!player.getInventory().add(emptyCan)) {
                                player.drop(emptyCan, false);
                            }
                        }
                    }

                    return result;
                }
            });

    public static final DeferredItem<Item> CHEESE_SLICE = ITEMS.register("cheese_slice",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.EDIBLE_INGREDIENT)));

    public static final DeferredItem<Item> BURGER_BUN = ITEMS.register("burger_bun",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.EDIBLE_INGREDIENT)));
    
    public static final DeferredItem<Item> RED_BURGER = ITEMS.register("red_burger",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.COMPLEX_FOOD)));
    public static final DeferredItem<Item> CHICKEN_BURGER = ITEMS.register("chicken_burger",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.COMPLEX_FOOD)));
    public static final DeferredItem<Item> FISH_BURGER = ITEMS.register("fish_burger",
            () -> new Item(new Item.Properties().food(HpCFoodProperties.COMPLEX_FOOD)));
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
    public static final DeferredItem<Item> THERMAL_INSULATION_MATERIAL =  ITEMS.register("thermal_insulation_material", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RADIATION_PROTECTION_MATERIAL =  ITEMS.register("radiation_protection_material", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PRESSURE_PROTECTION_MATERIAL =  ITEMS.register("pressure_protection_material", () -> new Item(new Item.Properties()));
    
    public static final DeferredItem<Item> SOLAR_PANEL_SEMICONDUCTOR_BASE = ITEMS.register("solar_panel_semiconductor_base", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SINGULAR_SOLAR_PANEL = ITEMS.register("singular_solar_panel", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TIER_1_SOLAR_PANEL = ITEMS.register("tier_1_solar_panel", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> T1_ROCKET_BASE =  ITEMS.register("t1_rocket_base", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> T1_ROCKET_ENGINE =  ITEMS.register("t1_rocket_engine", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> T1_ROCKET_FIN =  ITEMS.register("t1_rocket_fin", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> T1_ROCKET_NOSE_CONE =  ITEMS.register("t1_rocket_nose_cone", () -> new Item(new Item.Properties()));
    public static final DeferredItem<BatteryItem> RADIOACTIVE_CORE = ITEMS.register("radioactive_core",
            () -> new BatteryItem(new Item.Properties().component(HpCDataComponents.BATTERY_COMPONENT.get(),
                    new BatteryData(Integer.MAX_VALUE, Integer.MAX_VALUE)), Integer.MAX_VALUE, 5));

    public static final DeferredItem<Item> EMPTY_CAN = ITEMS.register("empty_can", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EMPTY_BAG = ITEMS.register("empty_bag", () -> new Item(new Item.Properties()));
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
    public static final DeferredItem<Item> DEBUG_STICK = ITEMS.register("debug_stick", () -> new HpCDebugStickItem(new Item.Properties()));
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
            new ArmorItem(HpCArmorMaterials.SPACE_GEAR_MATERIAL, ArmorItem.Type.HELMET, new  Item.Properties()){
                @Override
                public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
                    if (armorType == EquipmentSlot.HEAD) {
                        return false;
                    }
                    return super.canEquip(stack, armorType, entity);
                }
                @Override
                public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
                    return InteractionResultHolder.pass(player.getItemInHand(hand));
                }
            });
    public static final DeferredItem<ArmorItem> OXYGEN_CONNECTORS = ITEMS.register("oxygen_connectors", () ->
            new ArmorItem(HpCArmorMaterials.SPACE_GEAR_MATERIAL, ArmorItem.Type.BODY, new  Item.Properties()){
                @Override
                public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
                    if (armorType == EquipmentSlot.BODY) {
                        return false;
                    }
                    return super.canEquip(stack, armorType, entity);
                }
                @Override
                public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
                    return InteractionResultHolder.pass(player.getItemInHand(hand));
                }
            });
    public static final DeferredItem<Item> MASS_BELT = ITEMS.register("mass_belt", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> T1_THERMAL_INSULATION_HEAD = ITEMS.register("t1_thermal_insulation_head", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> T1_THERMAL_INSULATION_TORSO = ITEMS.register("t1_thermal_insulation_torso", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> T1_THERMAL_INSULATION_LEGGINGS = ITEMS.register("t1_thermal_insulation_leggings", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> T1_THERMAL_INSULATION_HANDS_AND_FEET = ITEMS.register("t1_thermal_insulation_hands_and_feet", () -> new Item(new Item.Properties()));

    public static final DeferredItem<ArmorItem> T1_SPACE_SUIT_HELMET = ITEMS.register("t1_space_suit_helmet", () ->
            new ArmorItem(HpCArmorMaterials.SPACE_SUIT_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(18))));
    public static final DeferredItem<ArmorItem> T1_SPACE_SUIT_CHESTPLATE = ITEMS.register("t1_space_suit_chestplate", () ->
            new ArmorItem(HpCArmorMaterials.SPACE_SUIT_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(18))));
    public static final DeferredItem<ArmorItem> T1_SPACE_SUIT_LEGGINGS = ITEMS.register("t1_space_suit_leggings", () ->
            new ArmorItem(HpCArmorMaterials.SPACE_SUIT_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(18))));
    public static final DeferredItem<ArmorItem> T1_SPACE_SUIT_BOOTS = ITEMS.register("t1_space_suit_boots", () ->
            new ArmorItem(HpCArmorMaterials.SPACE_SUIT_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(18))));
    //endregion

    public static final DeferredItem<Item> EXTINGUISHED_TORCH_ITEM = ITEMS.register("extinguished_torch", //don't delete
            () -> new StandingAndWallBlockItem(HpCBlocks.EXTINGUISHED_TORCH.get(), HpCBlocks.EXTINGUISHED_WALL_TORCH.get(), new Item.Properties(), Direction.DOWN));
    
    
    
    public static final DeferredItem<Item> ROCKET_ITEM = ITEMS.register("rocket_item", () -> 
            new RocketItem(new Item.Properties()));

    

    public static final DeferredItem<BatteryItem> SMALL_BATTERY = ITEMS.register("small_battery", () ->
            new BatteryItem(new Item.Properties().component(HpCDataComponents.BATTERY_COMPONENT.get(), 
                    new BatteryData(0, 10000)), 10000, 20));


    //region Registry and Helpers
    private static FoodProperties getInheritedFoodProps(Item ingredient, int ingredientCount) {
        FoodProperties baseFood = ingredient.components().get(net.minecraft.core.component.DataComponents.FOOD);

        // Multiply nutrition by the amount of ingredients used in the recipe
        int nutrition = baseFood != null ? baseFood.nutrition() * ingredientCount : 2;

        // Reverse-engineer the modifier: modifier = saturation / (nutrition * 2)
        float modifier = 0.1f; // Default fallback
        if (baseFood != null && baseFood.nutrition() > 0) {
            modifier = baseFood.saturation() / (baseFood.nutrition() * 2.0f);
        }

        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(modifier) // We pass the MODIFIER here, not the raw points!
                .alwaysEdible();

        // If the original ingredient eats fast, this will too
        if (baseFood != null && baseFood.eatDurationTicks() < 32) {
            builder.fast();
        }

        return builder.build();
    }
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
    //endregion
}
