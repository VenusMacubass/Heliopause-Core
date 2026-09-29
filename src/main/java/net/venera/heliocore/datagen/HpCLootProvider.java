package net.venera.heliocore.datagen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.data.component.HpCDataComponents;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.item.hpc_custom.GasTankItem;

import java.util.Random;
import java.util.function.BiConsumer;

public class HpCLootProvider implements LootTableSubProvider {
    private final HolderLookup.Provider provider;
    public HpCLootProvider(HolderLookup.Provider provider) {this.provider = provider;}

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "chests/moon_village_farmer_house")),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0F, 4.0F))
                                .add(LootItem.lootTableItem(Items.BONE_MEAL)
                                        .setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
                                .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS)
                                        .setWeight(6)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.WHEAT_SEEDS)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.CARROT)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.POTATO)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.MELON_SEEDS)
                                        .setWeight(5)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS)
                                        .setWeight(5)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                
                        )
        );

        output.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "chests/moon_village_librarian_house")),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0F, 4.0F))
                                .add(LootItem.lootTableItem(Blocks.BOOKSHELF)
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
                                .add(LootItem.lootTableItem(Items.PAPER)
                                        .setWeight(15)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 7.0F))))
                                .add(LootItem.lootTableItem(Items.WRITABLE_BOOK)
                                        .setWeight(5)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.BOOK)
                                        .setWeight(5)
                                        .apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.provider))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
        )));

        output.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "chests/moon_village_depot_house")),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0F, 4.0F))
                                .add(LootItem.lootTableItem(HpCItems.FLUID_FILTER)
                                        .setWeight(5)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
                                .add(LootItem.lootTableItem(HpCItems.COMPRESSED_GAS_TANK.get())
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                        .apply(SetComponentsFunction.setComponent(HpCDataComponents.GAS_TANK_COMPONENT.get(), 
                                                new GasTankData(GasTankData.OXYGEN_GAS, GasTankItem.MAX_CAPACITY / 3))))
                                .add(LootItem.lootTableItem(Items.BUCKET)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(HpCBlocks.COPPER_WIRE)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(HpCBlocks.FLUID_PIPE)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                        )
        );

        output.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "chests/moon_village_whitesmith_house")),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0F, 4.0F))
                                .add(LootItem.lootTableItem(HpCItems.COMPRESSED_BRONZE)
                                        .setWeight(15)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(HpCItems.COMPRESSED_IRIDIUM)
                                        .setWeight(5)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(HpCItems.COMPRESSED_ALUMINIUM)
                                        .setWeight(15)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(HpCItems.COMPRESSED_TIN)
                                        .setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(HpCItems.COMPRESSED_STEEL)
                                        .setWeight(15)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(HpCItems.COMPRESSED_HD_PLATE)
                                        .setWeight(5)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                        )
        );

        output.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "chests/moon_village_blacksmith_house")),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0F, 4.0F))
                                .add(LootItem.lootTableItem(Items.DIAMOND)
                                        .setWeight(1)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.EMERALD)
                                        .setWeight(2)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))
                                .add(LootItem.lootTableItem(Items.IRON_INGOT)
                                        .setWeight(6)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F))))
                                .add(LootItem.lootTableItem(Items.GOLD_INGOT)
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.OBSIDIAN)
                                        .setWeight(4)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))

                                // --- GEAR & TOOLS ---
                                .add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(2))
                                .add(LootItem.lootTableItem(Items.IRON_PICKAXE).setWeight(2))
                                .add(LootItem.lootTableItem(Items.IRON_CHESTPLATE).setWeight(2))
                                .add(LootItem.lootTableItem(Items.IRON_HELMET).setWeight(2))

                                // --- FOOD & MISC ---
                                .add(LootItem.lootTableItem(Items.BREAD)
                                        .setWeight(8)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.APPLE)
                                        .setWeight(8)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.OAK_SAPLING)
                                        .setWeight(5)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F))))
                        )
        );

        output.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "chests/moon_village_butcher_house")),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0F, 4.0F))
                                .add(LootItem.lootTableItem(Items.WHEAT_SEEDS)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(Items.WHEAT)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(Items.CHICKEN)
                                        .setWeight(5)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.MUTTON)
                                        .setWeight(5)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.BEEF)
                                        .setWeight(5)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.PORKCHOP)
                                        .setWeight(5)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))))
                                .add(LootItem.lootTableItem(Items.BONE)
                                        .setWeight(3)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        )
        );
    }
}
