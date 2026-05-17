package net.venera.galacticraftcore.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
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

    ResourceLocation dungeonBrickTex = modLoc("block/dungeon_bricks");
    ResourceLocation buildingBlockTex = modLoc("block/gcc_block_tin_building_block");
    ResourceLocation moonRockTex = modLoc("block/gcc_block_moon_rock");

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.IRIDIUM_BLOCK);
        blockWithItem(ModBlocks.RADIOACTIVE_BLOCK);
        blockWithItem(ModBlocks.MOON_TURF);
        blockWithItem(ModBlocks.MOON_DIRT);
        blockWithItem(ModBlocks.MOON_ROCK);
        blockWithItem(ModBlocks.TIN_BUILDING_BLOCK);
        blockWithItem(ModBlocks.SILICON_BLOCK);
        blockWithItem(ModBlocks.MOON_COBBLESTONE);
        blockWithItem(ModBlocks.COPPER_WIRE_BLOCK);

        

        blockItem(ModBlocks.TIN_BUILDING_SLAB);
        blockItem(ModBlocks.MOON_ROCK_SLAB);
        blockItem(ModBlocks.MOON_DUNGEON_BRICK_SLAB);
        stairItem(ModBlocks.TIN_BUILDING_STAIRS);
        stairItem(ModBlocks.MOON_ROCK_STAIRS);
        stairItem(ModBlocks.MOON_DUNGEON_BRICK_STAIRS);
        
        tintedStairsBlock(ModBlocks.MOON_DUNGEON_BRICK_STAIRS, dungeonBrickTex);
        tintedSlabBlock(ModBlocks.MOON_DUNGEON_BRICK_SLAB, dungeonBrickTex);
        tintedWallBlock(ModBlocks.MOON_DUNGEON_BRICK_WALL, dungeonBrickTex);
        
        tintedStairsBlock(ModBlocks.TIN_BUILDING_STAIRS,  buildingBlockTex);
        tintedSlabBlock(ModBlocks.TIN_BUILDING_SLAB, buildingBlockTex);
        tintedWallBlock(ModBlocks.TIN_BUILDING_WALL, buildingBlockTex);
        
        tintedStairsBlock(ModBlocks.MOON_ROCK_STAIRS,  moonRockTex);
        tintedSlabBlock(ModBlocks.MOON_ROCK_SLAB, moonRockTex);
        tintedWallBlock(ModBlocks.MOON_ROCK_WALL, moonRockTex);
    }

    //region Helpers
    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));

    }
    private void blockItem(DeferredBlock<?> deferredBlock){
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("galacticraftcore:block/" + deferredBlock.getId().getPath()));
    }
    private void blockItem(DeferredBlock<?> deferredBlock, String appendix){
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("galacticraftcore:block/" + deferredBlock.getId().getPath() + appendix));
    }

    public void tintedStairsBlock(DeferredBlock<?> block, ResourceLocation texture) {
        String baseName = block.getId().getPath();

        // Build the 3 required models pointing to YOUR templates
        ModelFile stairs = models().withExistingParent(baseName, modLoc("block/tinted_stairs"))
                .texture("bottom", texture)
                .texture("top", texture)
                .texture("side", texture);

        ModelFile stairsInner = models().withExistingParent(baseName + "_inner", modLoc("block/tinted_stairs_inner"))
                .texture("bottom", texture)
                .texture("top", texture)
                .texture("side", texture);

        ModelFile stairsOuter = models().withExistingParent(baseName + "_outer", modLoc("block/tinted_stairs_outer"))
                .texture("bottom", texture)
                .texture("top", texture)
                .texture("side", texture);

        // Tell DataGen to map the block states to your custom models
        stairsBlock((net.minecraft.world.level.block.StairBlock) block.get(), stairs, stairsInner, stairsOuter);
    }

    public void tintedSlabBlock(DeferredBlock<?> block, ResourceLocation texture) {
        String baseName = block.getId().getPath();

        ModelFile bottom = models().withExistingParent(baseName, modLoc("block/tinted_slab"))
                .texture("bottom", texture)
                .texture("top", texture)
                .texture("side", texture);

        ModelFile top = models().withExistingParent(baseName + "_top", modLoc("block/tinted_slab_top"))
                .texture("bottom", texture)
                .texture("top", texture)
                .texture("side", texture);

        // Slabs use a standard full block for the "double" state
        ModelFile doubleSlab = models().withExistingParent(baseName + "_double", modLoc("block/tinted_cube_all"))
                .texture("all", texture);

        slabBlock((net.minecraft.world.level.block.SlabBlock) block.get(), bottom, top, doubleSlab);
    }

    public void tintedWallBlock(DeferredBlock<?> block, ResourceLocation texture) {
        String baseName = block.getId().getPath();

        ModelFile post = models().withExistingParent(baseName + "_post", modLoc("block/tinted_wall_post"))
                .texture("wall", texture);

        ModelFile side = models().withExistingParent(baseName + "_side", modLoc("block/tinted_wall_side"))
                .texture("wall", texture);

        ModelFile sideTall = models().withExistingParent(baseName + "_side_tall", modLoc("block/tinted_wall_side_tall"))
                .texture("wall", texture);

        wallBlock((net.minecraft.world.level.block.WallBlock) block.get(), post, side, sideTall);
    }

    private void stairItem(DeferredBlock<?> deferredBlock) {
        // Use getBuilder and UncheckedModelFile to bypass the strict file check!
        itemModels().getBuilder(deferredBlock.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + deferredBlock.getId().getPath())))
                .transforms()
                .transform(net.minecraft.world.item.ItemDisplayContext.GUI)
                .rotation(30, 135, 0) // Spun 90 degrees!
                .scale(0.625f)
                .end()
                .end();
    }
    //endregion
}
