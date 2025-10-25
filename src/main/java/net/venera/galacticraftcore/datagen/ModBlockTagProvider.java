package net.venera.galacticraftcore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;

import net.venera.galacticraftcore.util.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, GalacticraftCore.MOD_ID, existingFileHelper);
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
                .add(ModBlocks.MARS_DUNGEON_BRICKS.get())
                .add(ModBlocks.MARS_ROCK.get())
                .add(ModBlocks.MARS_COBBLESTONE.get())
                .add(ModBlocks.TIN_BUILDING_SLAB.get())
                .add(ModBlocks.MOON_ROCK_SLAB.get())
                .add(ModBlocks.MOON_DUNGEON_BRICK_SLAB.get())
                .add(ModBlocks.MARS_COBBLESTONE_WALL.get())
                .add(ModBlocks.MARS_DUNGEON_BRICK_WALL.get())
                .add(ModBlocks.MARS_COBBLESTONE_SLAB.get())
                .add(ModBlocks.MARS_DUNGEON_BRICK_SLAB.get())
                .add(ModBlocks.IRIDIUM_BLOCK.get())
                .add(ModBlocks.SAPPHIRE_ORE.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.MOON_DIRT.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.TIN_ORE.get())
                .add(ModBlocks.MARS_ROCK.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.ALUMINIUM_ORE.get())
                .add(ModBlocks.ALUMINIUM_BLOCK.get())
                .add(ModBlocks.SILICON_ORE.get());

        tag(ModTags.Blocks.NEEDS_STEEL_TOOLS)
                .addTag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.SAPPHIRE_ORE.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.RADIOACTIVE_BLOCK.get());

        tag(BlockTags.WALLS)
                .add(ModBlocks.TIN_BUILDING_WALL.get())
                .add(ModBlocks.MOON_ROCK_WALL.get())
                .add(ModBlocks.MOON_DUNGEON_BRICK_WALL.get())
                .add(ModBlocks.MARS_COBBLESTONE_WALL.get())
                .add(ModBlocks.MARS_DUNGEON_BRICK_WALL.get());

        tag(ModTags.Blocks.INCORRECT_FOR_STEEL_TOOL).addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .remove(ModTags.Blocks.NEEDS_STEEL_TOOLS);


    }
}
