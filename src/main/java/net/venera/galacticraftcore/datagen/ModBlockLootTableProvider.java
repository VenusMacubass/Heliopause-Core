package net.venera.galacticraftcore.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.item.ModItems;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.ALUMINIUM_BLOCK.get());
        dropSelf(ModBlocks.RADIOACTIVE_BLOCK.get());
        dropSelf(ModBlocks.CHEESE_BLOCK.get());
        dropSelf(ModBlocks.MOON_TURF.get());
        dropSelf(ModBlocks.MOON_DIRT.get());
        dropSelf(ModBlocks.TIN_BUILDING_BLOCK.get());
        dropSelf(ModBlocks.TIN_BLOCK.get());
        dropSelf(ModBlocks.RAW_TIN_BLOCK.get());
        dropSelf(ModBlocks.RAW_ALUMINIUM_BLOCK.get());
        dropSelf(ModBlocks.SILICON_BLOCK.get());
        dropSelf(ModBlocks.MOON_DUNGEON_BRICKS.get());
        dropSelf(ModBlocks.MOON_COBBLESTONE.get());
        dropSelf(ModBlocks.MARS_DUNGEON_BRICKS.get());
        dropSelf(ModBlocks.MARS_COBBLESTONE.get());
        dropSelf(ModBlocks.ARC_LAMP.get());
        dropSelf(ModBlocks.COAL_COMPRESSOR.get());
        dropSelf(ModBlocks.REFINERY.get());
        dropSelf(ModBlocks.LAUNCH_PAD.get());
        dropSelf(ModBlocks.FLUID_TANK.get());
        dropSelf(ModBlocks.ENERGY_STORAGE_UNIT.get());
        dropSelf(ModBlocks.BASIC_SOLAR_BLOCK.get());

        dropSelf(ModBlocks.TIN_BUILDING_WALL.get());
        dropSelf(ModBlocks.MOON_ROCK_WALL.get());
        dropSelf(ModBlocks.MOON_DUNGEON_BRICK_WALL.get());
        dropSelf(ModBlocks.MARS_COBBLESTONE_WALL.get());
        dropSelf(ModBlocks.MARS_DUNGEON_BRICK_WALL.get());
        dropSelf(ModBlocks.TIN_BUILDING_STAIRS.get());
        dropSelf(ModBlocks.MOON_ROCK_STAIRS.get());
        dropSelf(ModBlocks.MOON_DUNGEON_BRICK_STAIRS.get());
        dropSelf(ModBlocks.IRIDIUM_BLOCK.get());

        dropOther(ModBlocks.MOON_ROCK.get(), ModBlocks.MOON_COBBLESTONE.get());
        dropOther(ModBlocks.MARS_ROCK.get(), ModBlocks.MARS_COBBLESTONE.get());

        add(ModBlocks.ALUMINIUM_ORE.get(),
                block -> createOreDrop(ModBlocks.ALUMINIUM_ORE.get(), ModItems.ALUMINIUM_INGOT.get()));

        add(ModBlocks.RADIOACTIVE_BLOCK.get(),
                block -> createMultipleOreDrops(ModBlocks.RADIOACTIVE_BLOCK.get(), ModItems.RADIOACTIVE_CORE.get(), 1,2));

        add(ModBlocks.TIN_ORE.get(),
                block -> createOreDrop(ModBlocks.TIN_ORE.get(), ModItems.RAW_TIN.get()));

        add(ModBlocks.SILICON_ORE.get(),
                block -> createOreDrop(ModBlocks.SILICON_ORE.get(), ModItems.RAW_SILICON.get()));

        add(ModBlocks.MOON_TIN_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.MOON_TIN_ORE.get(), ModItems.RAW_TIN.get(), 1,5));

        add(ModBlocks.MOON_COPPER_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.MOON_COPPER_ORE.get(), Items.RAW_COPPER,1,5));

        add(ModBlocks.SAPPHIRE_ORE.get(),
                block -> createOreDrop(ModBlocks.SAPPHIRE_ORE.get(), ModItems.LUNAR_SAPPHIRE.get()));

        add(ModBlocks.TIN_BUILDING_SLAB.get(), block -> createSlabItemTable(ModBlocks.TIN_BUILDING_SLAB.get()));
        add(ModBlocks.MOON_ROCK_SLAB.get(), block -> createSlabItemTable(ModBlocks.MOON_ROCK_SLAB.get()));
        add(ModBlocks.MOON_DUNGEON_BRICK_SLAB.get(), block -> createSlabItemTable(ModBlocks.MOON_DUNGEON_BRICK_SLAB.get()));
        add(ModBlocks.MARS_COBBLESTONE_SLAB.get(), block -> createSlabItemTable(ModBlocks.MARS_COBBLESTONE_SLAB.get()));
        add(ModBlocks.MARS_DUNGEON_BRICK_SLAB.get(), block -> createSlabItemTable(ModBlocks.MARS_DUNGEON_BRICK_SLAB.get()));

    }

    protected LootTable.Builder createMultipleOreDrops(Block block, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(block,
                        this.applyExplosionDecay(block, LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE)))));
    }
    
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
