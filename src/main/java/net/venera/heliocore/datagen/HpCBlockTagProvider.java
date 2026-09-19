package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.item.HpCTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

public class HpCBlockTagProvider extends BlockTagsProvider {
    public HpCBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HeliopauseCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(HpCBlocks.MOON_COPPER_ORE.get())
                
                .add(HpCBlocks.MOON_TIN_ORE.get())
                .add(HpCBlocks.TIN_ORE.get())
                .add(HpCBlocks.DEEPSLATE_TIN_ORE.get())
                .add(HpCBlocks.RAW_TIN_BLOCK.get())
                .add(HpCBlocks.TIN_BLOCK.get())

                .add(HpCBlocks.MOON_IRON_ORE.get())
                
                .add(HpCBlocks.MOON_ALUMINIUM_ORE.get())
                .add(HpCBlocks.ALUMINIUM_ORE.get())
                .add(HpCBlocks.DEEPSLATE_ALUMINIUM_ORE.get())
                .add(HpCBlocks.RAW_ALUMINIUM_BLOCK.get())
                .add(HpCBlocks.ALUMINIUM_BLOCK.get())

                .add(HpCBlocks.MOON_SILICON_ORE.get())
                .add(HpCBlocks.SILICON_ORE.get())
                .add(HpCBlocks.DEEPSLATE_SILICON_ORE.get())
                .add(HpCBlocks.SILICON_BLOCK.get())

                .add(HpCBlocks.MOON_IRIDIUM_ORE.get())
                .add(HpCBlocks.IRIDIUM_ORE.get())
                .add(HpCBlocks.DEEPSLATE_IRIDIUM_ORE.get())
                .add(HpCBlocks.IRIDIUM_BLOCK.get())

                .add(HpCBlocks.COAL_COMPRESSOR.get())
                .add(HpCBlocks.REFINERY.get())
                .add(HpCBlocks.ENERGY_STORAGE_UNIT.get())
                .add(HpCBlocks.BASIC_SOLAR_PANEL.get())
                .add(HpCBlocks.CARGO_MANAGER.get())
                .add(HpCBlocks.FUEL_MANAGER.get())
                .add(HpCBlocks.OXYGEN_GENERATOR.get())
                .add(HpCBlocks.GAS_COMPRESSOR.get())
                .add(HpCBlocks.GAS_VAPORIZER.get())
                .add(HpCBlocks.ENERGY_GENERATOR.get())
                .add(HpCBlocks.DECONSTRUCTOR.get())
                .add(HpCBlocks.OXYGEN_SEALER.get())
                .add(HpCBlocks.MAGNETIC_ASSEMBLY_PLATFORM.get())
                
                .add(HpCBlocks.RADIOACTIVE_BLOCK.get())
                .add(HpCBlocks.AIRLOCK_FRAME.get())
                .add(HpCBlocks.AIRLOCK_FRAME_SWITCH.get())
                .add(HpCBlocks.MAGNETIC_CRAFTING_TABLE.get())
                
                .add(HpCBlocks.BASE_BUILDING_WHITE_BLOCK.get())
                .add(HpCBlocks.BASE_BUILDING_SLAB_WHITE.get())
                .add(HpCBlocks.BASE_BUILDING_STAIRS_WHITE.get())
                .add(HpCBlocks.BASE_BUILDING_WALL_WHITE.get())
                .add(HpCBlocks.BASE_BUILDING_BLACK_BLOCK.get())
                .add(HpCBlocks.BASE_BUILDING_SLAB_BLACK.get())
                .add(HpCBlocks.BASE_BUILDING_STAIRS_BLACK.get())
                .add(HpCBlocks.BASE_BUILDING_WALL_BLACK.get())

                .add(HpCBlocks.MOON_COBBLESTONE.get())
                .add(HpCBlocks.MOON_ROCK.get())
                .add(HpCBlocks.MOON_ROCK_SLAB.get())
                .add(HpCBlocks.MOON_ROCK_STAIRS.get())
                .add(HpCBlocks.MOON_ROCK_WALL.get())
                .add(HpCBlocks.MOON_TEKTITES.get())

                .add(HpCBlocks.MOON_DUNGEON_BRICKS.get())
                .add(HpCBlocks.MOON_DUNGEON_BRICK_SLAB.get())
                .add(HpCBlocks.MOON_DUNGEON_BRICK_STAIRS.get())
                .add(HpCBlocks.MOON_DUNGEON_BRICK_WALL.get());
        
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(HpCBlocks.MOON_REGOLITH.get())
                .add(HpCBlocks.MOON_TEKTITES_REGOLITH.get())
                .add(HpCBlocks.MOON_DIRT.get())
        ;

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(HpCBlocks.MOON_TIN_ORE.get())
                .add(HpCBlocks.TIN_ORE.get())
                .add(HpCBlocks.DEEPSLATE_TIN_ORE.get())
                .add(HpCBlocks.RAW_TIN_BLOCK.get())
                .add(HpCBlocks.TIN_BLOCK.get())
                .add(HpCBlocks.MOON_COPPER_ORE.get())
                .add(HpCBlocks.MOON_IRON_ORE.get())
                .add(HpCBlocks.MOON_TEKTITES.get());
        
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(HpCBlocks.MOON_ALUMINIUM_ORE.get())
                .add(HpCBlocks.ALUMINIUM_ORE.get())
                .add(HpCBlocks.RAW_ALUMINIUM_BLOCK.get())
                .add(HpCBlocks.DEEPSLATE_ALUMINIUM_ORE.get())
                .add(HpCBlocks.ALUMINIUM_BLOCK.get())
                .add(HpCBlocks.MOON_SILICON_ORE.get())
                .add(HpCBlocks.SILICON_ORE.get())
                .add(HpCBlocks.SILICON_BLOCK.get())
                .add(HpCBlocks.DEEPSLATE_SILICON_ORE.get())
                .add(HpCBlocks.MOON_IRIDIUM_ORE.get())
                .add(HpCBlocks.IRIDIUM_ORE.get())
                .add(HpCBlocks.IRIDIUM_BLOCK.get())
                .add(HpCBlocks.DEEPSLATE_IRIDIUM_ORE.get());

        tag(HpCTags.Blocks.NEEDS_STEEL_TOOLS)
                .addTag(BlockTags.NEEDS_IRON_TOOL);

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(HpCBlocks.RADIOACTIVE_BLOCK.get());

        tag(BlockTags.WALLS)
                .add(HpCBlocks.BASE_BUILDING_WALL_WHITE.get())
                .add(HpCBlocks.BASE_BUILDING_WALL_BLACK.get())
                .add(HpCBlocks.MOON_ROCK_WALL.get())
                .add(HpCBlocks.MOON_DUNGEON_BRICK_WALL.get());

        tag(HpCTags.Blocks.INCORRECT_FOR_STEEL_TOOL).addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .remove(HpCTags.Blocks.NEEDS_STEEL_TOOLS);

        tag(HpCTags.Blocks.MACHINERY)
                .add(HpCBlocks.COAL_COMPRESSOR.get())
                .add(HpCBlocks.REFINERY.get())
                .add(HpCBlocks.ENERGY_STORAGE_UNIT.get())
                .add(HpCBlocks.BASIC_SOLAR_PANEL.get())
                .add(HpCBlocks.CARGO_MANAGER.get())
                .add(HpCBlocks.FUEL_MANAGER.get())
                .add(HpCBlocks.OXYGEN_GENERATOR.get())
                .add(HpCBlocks.GAS_COMPRESSOR.get())
                .add(HpCBlocks.GAS_VAPORIZER.get())
                .add(HpCBlocks.ENERGY_GENERATOR.get())
                .add(HpCBlocks.DECONSTRUCTOR.get())
                .add(HpCBlocks.OXYGEN_SEALER.get());


        tag(HpCTags.Blocks.MOON_REGOLITH_REPLACEABLES).add(HpCBlocks.MOON_REGOLITH.get());
        
        tag(HpCTags.Blocks.MOON_DIRT_REPLACEABLES).add(HpCBlocks.MOON_DIRT.get());
        
        tag(HpCTags.Blocks.MOON_STONE_REPLACEABLES).add(HpCBlocks.MOON_ROCK.get());
        
        
        
    }
}
