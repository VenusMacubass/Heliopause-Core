package net.venera.galacticraftcore.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
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
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));

    }
}
