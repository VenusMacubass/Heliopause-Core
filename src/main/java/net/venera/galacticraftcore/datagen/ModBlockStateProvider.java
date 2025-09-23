package net.venera.galacticraftcore.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, GalacticraftCore.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.ALUMINIUM_BLOCK);
        blockWithItem(ModBlocks.RADIOACTIVE_BLOCK);
        blockWithItem(ModBlocks.ALUMINIUM_ORE);
        blockWithItem(ModBlocks.CHEESE_BLOCK);
        blockWithItem(ModBlocks.MOON_TURF);
        blockWithItem(ModBlocks.MOON_DIRT);
        blockWithItem(ModBlocks.MOON_ROCK);
        blockWithItem(ModBlocks.TIN_BUILDING_BLOCK);
        blockWithItem(ModBlocks.TIN_BLOCK);
        blockWithItem(ModBlocks.RAW_TIN_BLOCK);
        blockWithItem(ModBlocks.TIN_ORE);
        blockWithItem(ModBlocks.RAW_ALUMINIUM_BLOCK);
        blockWithItem(ModBlocks.SILICON_ORE);
        blockWithItem(ModBlocks.SILICON_BLOCK);
        blockWithItem(ModBlocks.MOON_COPPER_ORE);
        blockWithItem(ModBlocks.MOON_TIN_ORE);
        blockWithItem(ModBlocks.MOON_COBBLESTONE);
        blockWithItem(ModBlocks.MOON_DUNGEON_BRICKS);
        blockWithItem(ModBlocks.MARS_DUNGEON_BRICKS);
        blockWithItem(ModBlocks.MARS_ROCK);
        blockWithItem(ModBlocks.MARS_COBBLESTONE);

        wallBlock(ModBlocks.TIN_BUILDING_WALL.get(), blockTexture(ModBlocks.TIN_BUILDING_BLOCK.get()));
        wallBlock(ModBlocks.MOON_ROCK_WALL.get(), blockTexture(ModBlocks.MOON_ROCK.get()));
        wallBlock(ModBlocks.MOON_DUNGEON_BRICK_WALL.get(), blockTexture(ModBlocks.MOON_DUNGEON_BRICKS.get()));
        wallBlock(ModBlocks.MARS_COBBLESTONE_WALL.get(), blockTexture(ModBlocks.MARS_COBBLESTONE.get()));
        wallBlock(ModBlocks.MARS_DUNGEON_BRICK_WALL.get(), blockTexture(ModBlocks.MARS_DUNGEON_BRICKS.get()));

        stairsBlock(ModBlocks.TIN_BUILDING_STAIRS.get(),  blockTexture(ModBlocks.TIN_BUILDING_BLOCK.get()));
        stairsBlock(ModBlocks.MOON_ROCK_STAIRS.get(),  blockTexture(ModBlocks.MOON_ROCK.get()));
        stairsBlock(ModBlocks.MOON_DUNGEON_BRICK_STAIRS.get(),  blockTexture(ModBlocks.MOON_DUNGEON_BRICKS.get()));

        blockItem(ModBlocks.TIN_BUILDING_STAIRS);
        blockItem(ModBlocks.MOON_ROCK_STAIRS);
        blockItem(ModBlocks.MOON_DUNGEON_BRICK_STAIRS);

        slabBlock(ModBlocks.TIN_BUILDING_SLAB.get(), blockTexture(ModBlocks.TIN_BUILDING_BLOCK.get()), blockTexture(ModBlocks.TIN_BUILDING_BLOCK.get()));
        slabBlock(ModBlocks.MARS_COBBLESTONE_SLAB.get(), blockTexture(ModBlocks.MARS_COBBLESTONE.get()), blockTexture(ModBlocks.MARS_COBBLESTONE.get()));
        slabBlock(ModBlocks.MARS_DUNGEON_BRICK_SLAB.get(), blockTexture(ModBlocks.MARS_DUNGEON_BRICKS.get()), blockTexture(ModBlocks.MARS_DUNGEON_BRICKS.get()));

        slabBlock(ModBlocks.MOON_ROCK_SLAB.get(), blockTexture(ModBlocks.MOON_ROCK.get()), blockTexture(ModBlocks.MOON_ROCK.get()));
        slabBlock(ModBlocks.MOON_DUNGEON_BRICK_SLAB.get(), blockTexture(ModBlocks.MOON_DUNGEON_BRICKS.get()), blockTexture(ModBlocks.MOON_DUNGEON_BRICKS.get()));

        blockItem(ModBlocks.TIN_BUILDING_SLAB);
        blockItem(ModBlocks.MOON_ROCK_SLAB);
        blockItem(ModBlocks.MOON_DUNGEON_BRICK_SLAB);
        blockItem(ModBlocks.MARS_COBBLESTONE_SLAB);
        blockItem(ModBlocks.MARS_DUNGEON_BRICK_SLAB);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));

    }

    private void blockItem(DeferredBlock<?> deferredBlock){
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("galacticraftcore:block/" + deferredBlock.getId().getPath()));
    }
    private void blockItem(DeferredBlock<?> deferredBlock, String appendix){
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("galacticraftcore:block/" + deferredBlock.getId().getPath() + appendix));
    }
}
