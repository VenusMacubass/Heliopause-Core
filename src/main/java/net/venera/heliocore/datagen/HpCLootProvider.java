package net.venera.heliocore.datagen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.venera.heliocore.HeliopauseCore;

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
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS)
                                        .setWeight(6)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(Items.WHEAT_SEEDS)
                                        .setWeight(10)
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

        output.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "chests/moon_village_plumber_house")),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0F, 4.0F))
                                .add(LootItem.lootTableItem(Items.BONE_MEAL)
                                        .setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS)
                                        .setWeight(6)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(Items.WHEAT_SEEDS)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        )
        );

        output.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "chests/moon_village_whitesmith_house")),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0F, 4.0F))
                                .add(LootItem.lootTableItem(Items.BONE_MEAL)
                                        .setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS)
                                        .setWeight(6)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(Items.WHEAT_SEEDS)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        )
        );

        output.accept(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "chests/moon_village_butcher_house")),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(UniformGenerator.between(2.0F, 4.0F))
                                .add(LootItem.lootTableItem(Items.BONE_MEAL)
                                        .setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
                                .add(LootItem.lootTableItem(Items.BEETROOT_SEEDS)
                                        .setWeight(6)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                .add(LootItem.lootTableItem(Items.WHEAT_SEEDS)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        )
        );
    }
}
