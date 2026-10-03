package net.venera.heliocore.entity.villager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.item.HpCItems;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = HeliopauseCore.MOD_ID)
public class HpCVillagerTrades {
    
    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.FARMER){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(HpCItems.DEHYDRATED_CARROT.get(), 3),
                    16, 2, 0.05F
            ));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(HpCItems.DEHYDRATED_APPLE.get(), 3),
                    16, 10, 0.05F
            ));
            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(HpCItems.DEHYDRATED_BERRIES.get(), 4),
                    12, 20, 0.05F
            ));
            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(HpCItems.DEHYDRATED_GLOW_BERRIES.get(), 4),
                    12, 30, 0.05F
            ));
        }
        
        if (event.getType() == VillagerProfession.BUTCHER){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(HpCItems.DEHYDRATED_CHICKEN.get(), 2),
                    16, 2, 0.05F
            ));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 2),
                    new ItemStack(HpCItems.CANNED_CHICKEN.get(), 2),
                    12, 30, 0.05F
            ));
        }

        if (event.getType() == VillagerProfession.ARMORER){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 12),
                    new ItemStack(HpCItems.STEEL_HELMET.get(), 1),
                    12, 2, 0.2F
            ));
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 20),
                    new ItemStack(HpCItems.STEEL_CHESTPLATE.get(), 1),
                    12, 2, 0.2F
            ));
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 16),
                    new ItemStack(HpCItems.STEEL_LEGGINGS.get(), 1),
                    12, 2, 0.2F
            ));
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 10),
                    new ItemStack(HpCItems.STEEL_BOOTS.get(), 1),
                    12, 2, 0.2F
            ));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(HpCItems.ALUMINIUM_INGOT.get(), 3),
                    new ItemStack(Items.EMERALD, 1),
                    12, 10, 0.05F
            ));
        }

        if (event.getType() == VillagerProfession.WEAPONSMITH){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 4),
                    new ItemStack(HpCItems.STEEL_AXE.get(), 1),
                    3, 1, 0.2F
            ));
            trades.get(2).add((entity, randomSource) -> {
                ItemStack tool = new ItemStack(HpCItems.STEEL_SWORD.get());
                int enchantLevel = 5 + randomSource.nextInt(15);
                EnchantmentHelper.enchantItem(randomSource, tool, enchantLevel, entity.level().registryAccess(), Optional.empty());
                int emeraldCost = 7 + (enchantLevel / 2);
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, emeraldCost),
                        tool,
                        3, 10, 0.05F
                );
            });
        }

        if (event.getType() == VillagerProfession.TOOLSMITH){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 12),
                    new ItemStack(HpCItems.STANDARD_WRENCH.get(), 1),
                    16, 2, 0.2F
            ));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(HpCItems.TIN_INGOT.get(), 4),
                    new ItemStack(Items.EMERALD, 1),
                    12, 10, 0.05F
            ));
            
        }

        if (event.getType() == HpCVillagers.WHITESMITH_VILLAGER.value()){
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 12),
                    new ItemStack(HpCItems.STANDARD_WRENCH.get(), 1),
                    16, 2, 0.2F
            ));
            
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(HpCItems.EMPTY_CAN.get(), 2),
                    16, 2, 0.2F
            ));
            
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(HpCItems.EMPTY_BAG.get(), 4),
                    16, 2, 0.2F
            ));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(HpCItems.COMPRESSED_TIN.get(), 4),
                    16, 10, 0.2F
            ));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(HpCItems.COMPRESSED_IRON.get(), 4),
                    16, 10, 0.2F
            ));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(HpCItems.ALUMINIUM_INGOT.get(), 3),
                    new ItemStack(Items.EMERALD, 1),
                    16, 10, 0.2F
            ));

            trades.get(3).add((entity, randomSource) -> {
                ItemStack tool = new ItemStack(HpCItems.STEEL_PICKAXE.get());
                int enchantLevel = 5 + randomSource.nextInt(15);
                EnchantmentHelper.enchantItem(randomSource, tool, enchantLevel, entity.level().registryAccess(), Optional.empty());
                int emeraldCost = 5 + (enchantLevel / 2);
                return new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), tool, 3, 20, 0.2F);
            });
            trades.get(3).add((entity, randomSource) -> {
                ItemStack tool = new ItemStack(HpCItems.STEEL_SHOVEL.get());
                int enchantLevel = 5 + randomSource.nextInt(15);
                EnchantmentHelper.enchantItem(randomSource, tool, enchantLevel, entity.level().registryAccess(), Optional.empty());
                int emeraldCost = 4 + (enchantLevel / 2);
                return new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), tool, 3, 20, 0.2F);
            });
            trades.get(3).add((entity, randomSource) -> {
                ItemStack tool = new ItemStack(HpCItems.STEEL_AXE.get());
                int enchantLevel = 10 + randomSource.nextInt(15);
                EnchantmentHelper.enchantItem(randomSource, tool, enchantLevel, entity.level().registryAccess(), Optional.empty());
                int emeraldCost = 6 + (enchantLevel / 2);
                return new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), tool, 3, 20, 0.2F);
            });
            
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 2),
                    new ItemStack(HpCItems.STEEL_ROD.get(), 1),
                    12, 30, 0.2F
            ));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(HpCItems.TIN_CANISTER.get(), 1),
                    16, 30, 0.2F
            ));
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(HpCItems.COPPER_CANISTER.get(), 1),
                    12, 30, 0.2F
            ));
            
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 64),
                    new ItemStack(HpCItems.T1_ROCKET_FIN.get(), 1),
                    5, 40, 0.05F
            ));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 64),
                    new ItemStack(HpCItems.CANISTER.get(), 1),
                    5, 40, 0.05F
            ));
        }
    }

    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        genericTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 1),
                new ItemStack(HpCItems.CHIPS.get(), 1), 1, 10, 0.2f));

        rareTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 16),
                new ItemStack(HpCItems.TEKTITES.get(), 4), 1, 10, 0.2f));
    }
}
