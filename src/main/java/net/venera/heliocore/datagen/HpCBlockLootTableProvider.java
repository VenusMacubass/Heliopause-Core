package net.venera.heliocore.datagen;

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
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.item.HpCItems;

import java.util.Set;

public class HpCBlockLootTableProvider extends BlockLootSubProvider {
    protected HpCBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(HpCBlocks.ALUMINIUM_BLOCK.get());
        dropSelf(HpCBlocks.RADIOACTIVE_BLOCK.get());
        dropSelf(HpCBlocks.CHEESE_BLOCK.get());
        dropSelf(HpCBlocks.PIZZA_BLOCK.get());
        dropSelf(HpCBlocks.MOON_REGOLITH.get());
        dropSelf(HpCBlocks.MOON_DIRT.get());
        dropSelf(HpCBlocks.BASE_BUILDING_WHITE_BLOCK.get());
        dropSelf(HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get());
        dropSelf(HpCBlocks.TIN_BLOCK.get());
        dropSelf(HpCBlocks.RAW_TIN_BLOCK.get());
        dropSelf(HpCBlocks.RAW_ALUMINIUM_BLOCK.get());
        dropSelf(HpCBlocks.SILICON_BLOCK.get());
        dropSelf(HpCBlocks.MOON_DUNGEON_BRICKS.get());
        dropSelf(HpCBlocks.MOON_COBBLESTONE.get());
        dropSelf(HpCBlocks.IRIDIUM_ORE.get());
        dropSelf(HpCBlocks.DEEPSLATE_IRIDIUM_ORE.get());
        dropSelf(HpCBlocks.MOON_IRIDIUM_ORE.get());
        dropSelf(HpCBlocks.MOON_TEKTITES_REGOLITH.get());
        dropSelf(HpCBlocks.MOON_TEKTITES.get());
        
        dropSelf(HpCBlocks.ARC_LAMP.get());
        dropSelf(HpCBlocks.COAL_COMPRESSOR.get());
        dropSelf(HpCBlocks.REFINERY.get());
        dropSelf(HpCBlocks.LAUNCH_PAD.get());
        dropSelf(HpCBlocks.FLUID_TANK.get());
        dropSelf(HpCBlocks.ENERGY_STORAGE_UNIT.get());
        dropSelf(HpCBlocks.BASIC_SOLAR_BLOCK.get());
        dropSelf(HpCBlocks.CARGO_MANAGER_BLOCK.get());
        dropSelf(HpCBlocks.FUEL_MANAGER_BLOCK.get());
        dropSelf(HpCBlocks.OXYGEN_GENERATOR_BLOCK.get());
        dropSelf(HpCBlocks.GAS_COMPRESSOR_BLOCK.get());
        dropSelf(HpCBlocks.GAS_VAPORIZER_BLOCK.get());
        dropSelf(HpCBlocks.ENERGY_GENERATOR_BLOCK.get());
        dropSelf(HpCBlocks.DECONSTRUCTOR_BLOCK.get());
        dropSelf(HpCBlocks.OXYGEN_SEALER_BLOCK.get());
        dropSelf(HpCBlocks.COPPER_WIRE.get());
        dropSelf(HpCBlocks.FLUID_PIPE.get());

        dropSelf(HpCBlocks.TIN_BUILDING_WALL_WHITE.get());
        dropSelf(HpCBlocks.TIN_BUILDING_WALL_BLACK.get());
        dropSelf(HpCBlocks.MOON_ROCK_WALL.get());
        dropSelf(HpCBlocks.MOON_DUNGEON_BRICK_WALL.get());
        
        dropSelf(HpCBlocks.TIN_BUILDING_STAIRS_WHITE.get());
        dropSelf(HpCBlocks.TIN_BUILDING_STAIRS_BLACK.get());
        dropSelf(HpCBlocks.MOON_ROCK_STAIRS.get());
        dropSelf(HpCBlocks.MOON_DUNGEON_BRICK_STAIRS.get());
        dropSelf(HpCBlocks.IRIDIUM_BLOCK.get());
        
        dropSelf(HpCBlocks.COPPER_WIRE_BLOCK.get());

        dropOther(HpCBlocks.MOON_ROCK.get(), HpCBlocks.MOON_COBBLESTONE.get());
        
        dropWhenSilkTouch(HpCBlocks.PRISMATIC_GLASS_PANE.get());
        dropWhenSilkTouch(HpCBlocks.PRISMATIC_GLASS.get());
        dropWhenSilkTouch(HpCBlocks.TINTED_PRISMATIC_GLASS.get());

        add(HpCBlocks.TIN_ORE.get(),
                block -> createOreDrop(HpCBlocks.TIN_ORE.get(), HpCItems.RAW_TIN.get()));

        add(HpCBlocks.DEEPSLATE_TIN_ORE.get(),
                block -> createOreDrop(HpCBlocks.DEEPSLATE_TIN_ORE.get(), HpCItems.RAW_TIN.get()));

        add(HpCBlocks.MOON_TIN_ORE.get(),
                block -> createOreDrop(HpCBlocks.MOON_TIN_ORE.get(), HpCItems.RAW_TIN.get()));
        
        add(HpCBlocks.ALUMINIUM_ORE.get(),
                block -> createOreDrop(HpCBlocks.ALUMINIUM_ORE.get(), HpCItems.RAW_ALUMINIUM.get()));

        add(HpCBlocks.DEEPSLATE_ALUMINIUM_ORE.get(),
                block -> createOreDrop(HpCBlocks.DEEPSLATE_ALUMINIUM_ORE.get(), HpCItems.RAW_ALUMINIUM.get()));

        add(HpCBlocks.MOON_ALUMINIUM_ORE.get(),
                block -> createOreDrop(HpCBlocks.MOON_ALUMINIUM_ORE.get(), HpCItems.RAW_ALUMINIUM.get()));

        add(HpCBlocks.RADIOACTIVE_BLOCK.get(),
                block -> createMultipleOreDrops(HpCBlocks.RADIOACTIVE_BLOCK.get(), HpCItems.RADIOACTIVE_CORE.get(), 1,2));
        
        add(HpCBlocks.SILICON_ORE.get(),
                block -> createOreDrop(HpCBlocks.SILICON_ORE.get(), HpCItems.RAW_SILICON.get()));

        add(HpCBlocks.MOON_SILICON_ORE.get(),
                block -> createOreDrop(HpCBlocks.MOON_SILICON_ORE.get(), HpCItems.RAW_SILICON.get()));

        add(HpCBlocks.DEEPSLATE_SILICON_ORE.get(),
                block -> createOreDrop(HpCBlocks.DEEPSLATE_SILICON_ORE.get(), HpCItems.RAW_SILICON.get()));

        add(HpCBlocks.MOON_TIN_ORE.get(),
                block -> createMultipleOreDrops(HpCBlocks.MOON_TIN_ORE.get(), HpCItems.RAW_TIN.get(), 1,5));

        add(HpCBlocks.MOON_COPPER_ORE.get(),
                block -> createMultipleOreDrops(HpCBlocks.MOON_COPPER_ORE.get(), Items.RAW_COPPER,1,5));
        
        add(HpCBlocks.MOON_IRON_ORE.get(),
                block -> createMultipleOreDrops(HpCBlocks.MOON_IRON_ORE.get(), Items.RAW_IRON,1,5));
        

        add(HpCBlocks.TIN_BUILDING_SLAB_WHITE.get(), block -> createSlabItemTable(HpCBlocks.TIN_BUILDING_SLAB_WHITE.get()));
        add(HpCBlocks.TIN_BUILDING_SLAB_BLACK.get(), block -> createSlabItemTable(HpCBlocks.TIN_BUILDING_SLAB_BLACK.get()));
        add(HpCBlocks.MOON_ROCK_SLAB.get(), block -> createSlabItemTable(HpCBlocks.MOON_ROCK_SLAB.get()));
        add(HpCBlocks.MOON_DUNGEON_BRICK_SLAB.get(), block -> createSlabItemTable(HpCBlocks.MOON_DUNGEON_BRICK_SLAB.get()));
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
        return HpCBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
