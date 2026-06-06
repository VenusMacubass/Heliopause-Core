package net.venera.heliocore.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.IronBarsBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;

public class HpCBlockStateProvider extends BlockStateProvider {
    public HpCBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HeliopauseCore.MOD_ID, exFileHelper);
    }

    ResourceLocation dungeonBrickTex = modLoc("block/dungeon_bricks");
    ResourceLocation buildingBlockTex = modLoc("block/tin_building_block");
    ResourceLocation moonRockTex = modLoc("block/moon_rock");
    ResourceLocation prismaticTex = modLoc("block/prismatic_glass");
    ResourceLocation prismaticPaneTex = modLoc("block/prismatic_glass_pane_top");
    ResourceLocation tintedPrismaticTex = modLoc("block/tinted_prismatic_glass");

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(HpCBlocks.IRIDIUM_BLOCK);
        blockWithItem(HpCBlocks.RADIOACTIVE_BLOCK);
        blockWithItem(HpCBlocks.MOON_REGOLITH);
        blockWithItem(HpCBlocks.MOON_DIRT);
        blockWithItem(HpCBlocks.MOON_ROCK);
        blockWithItem(HpCBlocks.TIN_BUILDING_BLOCK);
        blockWithItem(HpCBlocks.SILICON_BLOCK);
        blockWithItem(HpCBlocks.MOON_COBBLESTONE);
        blockWithItem(HpCBlocks.COPPER_WIRE_BLOCK);

        translucentBlock(HpCBlocks.PRISMATIC_GLASS, prismaticTex);
        translucentPaneBlock(HpCBlocks.PRISMATIC_GLASS_PANE.get(), prismaticTex, prismaticPaneTex);
        translucentBlock(HpCBlocks.TINTED_PRISMATIC_GLASS, tintedPrismaticTex);

        blockItem(HpCBlocks.TIN_BUILDING_SLAB);
        blockItem(HpCBlocks.MOON_ROCK_SLAB);
        blockItem(HpCBlocks.MOON_DUNGEON_BRICK_SLAB);
        stairItem(HpCBlocks.TIN_BUILDING_STAIRS);
        stairItem(HpCBlocks.MOON_ROCK_STAIRS);
        stairItem(HpCBlocks.MOON_DUNGEON_BRICK_STAIRS);
        
        tintedStairsBlock(HpCBlocks.MOON_DUNGEON_BRICK_STAIRS, dungeonBrickTex);
        tintedSlabBlock(HpCBlocks.MOON_DUNGEON_BRICK_SLAB, dungeonBrickTex);
        tintedWallBlock(HpCBlocks.MOON_DUNGEON_BRICK_WALL, dungeonBrickTex);
        
        tintedStairsBlock(HpCBlocks.TIN_BUILDING_STAIRS,  buildingBlockTex);
        tintedSlabBlock(HpCBlocks.TIN_BUILDING_SLAB, buildingBlockTex);
        tintedWallBlock(HpCBlocks.TIN_BUILDING_WALL, buildingBlockTex);
        
        tintedStairsBlock(HpCBlocks.MOON_ROCK_STAIRS,  moonRockTex);
        tintedSlabBlock(HpCBlocks.MOON_ROCK_SLAB, moonRockTex);
        tintedWallBlock(HpCBlocks.MOON_ROCK_WALL, moonRockTex);
    }

    //region Helpers
    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));

    }
    private void blockItem(DeferredBlock<?> deferredBlock){
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("heliocore:block/" + deferredBlock.getId().getPath()));
    }
    private void blockItem(DeferredBlock<?> deferredBlock, String appendix){
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("heliocore:block/" + deferredBlock.getId().getPath() + appendix));
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

        // Tell DataGen to map the block states to your hpc_custom models
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
        itemModels().getBuilder(deferredBlock.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + deferredBlock.getId().getPath())))
                .transforms()
                .transform(net.minecraft.world.item.ItemDisplayContext.GUI)
                .rotation(30, 135, 0) // Spun 90 degrees!
                .scale(0.625f)
                .end()
                .end();
    }

    public void translucentBlock(DeferredBlock<?> deferredBlock, ResourceLocation texture) {
        ModelFile model = models().cubeAll(deferredBlock.getId().getPath(), texture)
                .renderType("minecraft:translucent");
        simpleBlockWithItem(deferredBlock.get(), model);
    }

    public void translucentPaneBlock(IronBarsBlock block, ResourceLocation pane, ResourceLocation edge) {
        String baseName = BuiltInRegistries.BLOCK.getKey(block).getPath();
        
        ModelFile post = models().panePost(baseName + "_post", pane, edge).renderType("minecraft:translucent");
        ModelFile side = models().paneSide(baseName + "_side", pane, edge).renderType("minecraft:translucent");
        ModelFile sideAlt = models().paneSideAlt(baseName + "_side_alt", pane, edge).renderType("minecraft:translucent");
        ModelFile noSide = models().paneNoSide(baseName + "_noside", pane).renderType("minecraft:translucent");
        ModelFile noSideAlt = models().paneNoSideAlt(baseName + "_noside_alt", pane).renderType("minecraft:translucent");
        
        paneBlock(block, post, side, sideAlt, noSide, noSideAlt);
        itemModels().getBuilder(baseName)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", pane)
                .renderType("minecraft:translucent"); //There's a cool bug here
    }
    //endregion
}
