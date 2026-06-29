package net.venera.heliocore.datagen;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;

import java.util.Map;

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
    
    ResourceLocation machineTop = modLoc("block/machine/machine");
    ResourceLocation machineBottom = modLoc("block/machine/machine_bottom");
    ResourceLocation machineSide = modLoc("block/machine/machine_side");
    ResourceLocation energyInPort = modLoc("block/machine/machine_energy_input");
    ResourceLocation energyOutPort = modLoc("block/machine/machine_energy_output");
    ResourceLocation fluidInPort = modLoc("block/machine/machine_fluid_input");
    ResourceLocation fluidOutPort = modLoc("block/machine/machine_fluid_output");
    ResourceLocation airVent = modLoc("block/machine/machine_gas_vent");

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

        directionalMachineBlock(HpCBlocks.OXYGEN_GENERATOR_BLOCK.get(), 
                machineSide, 
                Map.of(
                        Direction.NORTH, energyInPort, 
                        Direction.UP, machineTop,    
                        Direction.DOWN, machineBottom,
                        Direction.WEST, airVent,
                        Direction.EAST, airVent,
                        Direction.SOUTH, fluidOutPort
                )
        );
        directionalMachineBlock(HpCBlocks.GAS_COMPRESSOR_BLOCK.get(),
                machineSide, 
                Map.of(
                        Direction.NORTH, energyInPort, 
                        Direction.UP, machineTop,
                        Direction.DOWN, machineBottom,
                        Direction.SOUTH, machineSide,
                        Direction.EAST, fluidInPort,
                        Direction.WEST, fluidOutPort
                )
        );
    }

    //region Helpers
    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));

    }
    private void blockItem(DeferredBlock<?> deferredBlock){
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile(HeliopauseCore.MOD_ID + ":block/" + deferredBlock.getId().getPath()));
    }
    private void blockItem(DeferredBlock<?> deferredBlock, String appendix){
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile(HeliopauseCore.MOD_ID + ":block/" + deferredBlock.getId().getPath() + appendix));
    }

    public void tintedStairsBlock(DeferredBlock<?> block, ResourceLocation texture) {
        String baseName = block.getId().getPath();
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
        stairsBlock((StairBlock) block.get(), stairs, stairsInner, stairsOuter);
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
        ModelFile doubleSlab = models().withExistingParent(baseName + "_double", modLoc("block/tinted_cube_all"))
                .texture("all", texture);
        slabBlock((SlabBlock) block.get(), bottom, top, doubleSlab);
    }

    public void tintedWallBlock(DeferredBlock<?> block, ResourceLocation texture) {
        String baseName = block.getId().getPath();

        ModelFile post = models().withExistingParent(baseName + "_post", modLoc("block/tinted_wall_post"))
                .texture("wall", texture);

        ModelFile side = models().withExistingParent(baseName + "_side", modLoc("block/tinted_wall_side"))
                .texture("wall", texture);

        ModelFile sideTall = models().withExistingParent(baseName + "_side_tall", modLoc("block/tinted_wall_side_tall"))
                .texture("wall", texture);

        wallBlock((WallBlock) block.get(), post, side, sideTall);
    }

    private void stairItem(DeferredBlock<?> deferredBlock) {
        itemModels().getBuilder(deferredBlock.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + deferredBlock.getId().getPath())))
                .transforms()
                .transform(net.minecraft.world.item.ItemDisplayContext.GUI)
                .rotation(30, 135, 0) //Turn 90 degrees
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

    public void directionalMachineBlock(Block block, ResourceLocation defaultTexture, Map<Direction, ResourceLocation> overrides) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();

        BlockModelBuilder model = models().withExistingParent(name, "minecraft:block/cube");

        model.texture("north", overrides.getOrDefault(Direction.NORTH, defaultTexture)); // The Front
        model.texture("south", overrides.getOrDefault(Direction.SOUTH, defaultTexture)); // The Back
        model.texture("up", overrides.getOrDefault(Direction.UP, defaultTexture));       // The Top
        model.texture("down", overrides.getOrDefault(Direction.DOWN, defaultTexture));   // The Bottom
        model.texture("east", overrides.getOrDefault(Direction.EAST, defaultTexture));   // The Right Side
        model.texture("west", overrides.getOrDefault(Direction.WEST, defaultTexture));   // The Left Side

        model.texture("particle", defaultTexture);

        itemModels().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name)))
                .transforms()
                .transform(ItemDisplayContext.GUI)
                .rotation(30, 135, 0) //Turn 90 degrees
                .scale(0.625f)
                .end()
                .end();
        
        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(BlockStateProperties.FACING);
            int x = 0;
            int y = 0;

            switch (dir) {
                case NORTH -> y = 0;
                case EAST -> y = 90;
                case SOUTH -> y = 180;
                case WEST -> y = 270;
                case UP -> x = 270; // Tips the North face up to the sky
                case DOWN -> x = 90;  // Tips the North face down to the floor
            }

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(x)
                    .rotationY(y)
                    .build();
        });
    }
    //endregion
}
