package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.ModBlocks;
import net.venera.heliocore.util.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HeliopauseCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ALUMINIUM_ORE.get())
                .add(ModBlocks.ALUMINIUM_BLOCK.get())
                .add(ModBlocks.RADIOACTIVE_BLOCK.get())
                .add(ModBlocks.MOON_ROCK.get())
                .add(ModBlocks.TIN_BUILDING_BLOCK.get())
                .add(ModBlocks.TIN_BLOCK.get())
                .add(ModBlocks.TIN_ORE.get())
                .add(ModBlocks.RAW_TIN_BLOCK.get())
                .add(ModBlocks.RAW_ALUMINIUM_BLOCK.get())
                .add(ModBlocks.SILICON_BLOCK.get())
                .add(ModBlocks.SILICON_ORE.get())
                .add(ModBlocks.MOON_COPPER_ORE.get())
                .add(ModBlocks.MOON_TIN_ORE.get())
                .add(ModBlocks.MOON_COBBLESTONE.get())
                .add(ModBlocks.MOON_DUNGEON_BRICKS.get())
                .add(ModBlocks.TIN_BUILDING_SLAB.get())
                .add(ModBlocks.MOON_ROCK_SLAB.get())
                .add(ModBlocks.MOON_DUNGEON_BRICK_SLAB.get())
                
                .add(ModBlocks.IRIDIUM_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.MOON_DIRT.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.TIN_ORE.get());
        
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.ALUMINIUM_ORE.get())
                .add(ModBlocks.ALUMINIUM_BLOCK.get())
                .add(ModBlocks.SILICON_ORE.get());

        tag(ModTags.Blocks.NEEDS_STEEL_TOOLS)
                .addTag(BlockTags.NEEDS_IRON_TOOL);

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.RADIOACTIVE_BLOCK.get());

        tag(BlockTags.WALLS)
                .add(ModBlocks.TIN_BUILDING_WALL.get())
                .add(ModBlocks.MOON_ROCK_WALL.get())
                .add(ModBlocks.MOON_DUNGEON_BRICK_WALL.get());

        tag(ModTags.Blocks.INCORRECT_FOR_STEEL_TOOL).addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .remove(ModTags.Blocks.NEEDS_STEEL_TOOLS);

        tag(ModTags.Blocks.MACHINERY)
                .add(ModBlocks.COAL_COMPRESSOR.get())
                .add(ModBlocks.REFINERY.get())
                .add(ModBlocks.ENERGY_STORAGE_UNIT.get())
                .add(ModBlocks.BASIC_SOLAR_BLOCK.get());
        
        tag(ModTags.Blocks.MOON_STONE_REPLACEABLES)
                .add(ModBlocks.MOON_ROCK.get())
                .add(ModBlocks.MOON_TURF.get());
        
    }
}
