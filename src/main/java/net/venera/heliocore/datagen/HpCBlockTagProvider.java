package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.util.HpCTags;
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
                .add(HpCBlocks.ALUMINIUM_ORE.get())
                .add(HpCBlocks.ALUMINIUM_BLOCK.get())
                .add(HpCBlocks.RADIOACTIVE_BLOCK.get())
                .add(HpCBlocks.MOON_ROCK.get())
                .add(HpCBlocks.BASE_BUILDING_WHITE_BLOCK.get())
                .add(HpCBlocks.TIN_BLOCK.get())
                .add(HpCBlocks.TIN_ORE.get())
                .add(HpCBlocks.RAW_TIN_BLOCK.get())
                .add(HpCBlocks.RAW_ALUMINIUM_BLOCK.get())
                .add(HpCBlocks.SILICON_BLOCK.get())
                .add(HpCBlocks.SILICON_ORE.get())
                .add(HpCBlocks.MOON_COPPER_ORE.get())
                .add(HpCBlocks.MOON_TIN_ORE.get())
                .add(HpCBlocks.MOON_COBBLESTONE.get())
                .add(HpCBlocks.MOON_DUNGEON_BRICKS.get())
                .add(HpCBlocks.BASE_BUILDING_SLAB_WHITE.get())
                .add(HpCBlocks.MOON_ROCK_SLAB.get())
                .add(HpCBlocks.MOON_DUNGEON_BRICK_SLAB.get())
                .add(HpCBlocks.MOON_TEKTITES.get())
                
                .add(HpCBlocks.IRIDIUM_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(HpCBlocks.MOON_DIRT.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(HpCBlocks.TIN_ORE.get())
                .add(HpCBlocks.MOON_TEKTITES.get());
        
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(HpCBlocks.ALUMINIUM_ORE.get())
                .add(HpCBlocks.ALUMINIUM_BLOCK.get())
                .add(HpCBlocks.SILICON_ORE.get());

        tag(HpCTags.Blocks.NEEDS_STEEL_TOOLS)
                .addTag(BlockTags.NEEDS_IRON_TOOL);

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(HpCBlocks.RADIOACTIVE_BLOCK.get());

        tag(BlockTags.WALLS)
                .add(HpCBlocks.BASE_BUILDING_WALL_WHITE.get())
                .add(HpCBlocks.MOON_ROCK_WALL.get())
                .add(HpCBlocks.MOON_DUNGEON_BRICK_WALL.get());

        tag(HpCTags.Blocks.INCORRECT_FOR_STEEL_TOOL).addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .remove(HpCTags.Blocks.NEEDS_STEEL_TOOLS);

        tag(HpCTags.Blocks.MACHINERY)
                .add(HpCBlocks.COAL_COMPRESSOR.get())
                .add(HpCBlocks.REFINERY.get())
                .add(HpCBlocks.ENERGY_STORAGE_UNIT.get())
                .add(HpCBlocks.BASIC_SOLAR_BLOCK.get())
                .add(HpCBlocks.CARGO_MANAGER_BLOCK.get())
                .add(HpCBlocks.FUEL_MANAGER_BLOCK.get())
                .add(HpCBlocks.OXYGEN_GENERATOR_BLOCK.get())
                .add(HpCBlocks.GAS_COMPRESSOR_BLOCK.get())
                .add(HpCBlocks.GAS_VAPORIZER_BLOCK.get())
                .add(HpCBlocks.ENERGY_GENERATOR_BLOCK.get())
                .add(HpCBlocks.DECONSTRUCTOR_BLOCK.get())
                .add(HpCBlocks.OXYGEN_SEALER_BLOCK.get());


        tag(HpCTags.Blocks.MOON_REGOLITH_REPLACEABLES).add(HpCBlocks.MOON_REGOLITH.get());
        
        tag(HpCTags.Blocks.MOON_DIRT_REPLACEABLES).add(HpCBlocks.MOON_DIRT.get());
        
        tag(HpCTags.Blocks.MOON_STONE_REPLACEABLES)
                .add(HpCBlocks.MOON_ROCK.get());
        
        
        
    }
}
