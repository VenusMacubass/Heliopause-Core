package net.venera.heliocore.datagen;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.hpc_custom.AirlockFrameSwitch;
import net.venera.heliocore.block.hpc_custom.machine.electric.EnergyStorageBlock;
import net.venera.heliocore.block.hpc_custom.machine.electric.SolarPanelBlock;
import net.venera.heliocore.fluid.HpCFluids;

import java.util.Map;

public class HpCBlockStateProvider extends BlockStateProvider {
    public HpCBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, HeliopauseCore.MOD_ID, exFileHelper);
    }
    //region Resource Locations
    ResourceLocation crudeOilStill = modLoc("block/crude_oil_still");
    ResourceLocation refinedFuelStill = modLoc("block/refined_fuel_still");
    ResourceLocation liquidOxygenStill = modLoc("block/oxygen_liquid_still");
    
    ResourceLocation moonRegolith = modLoc("block/moon_regolith");
    ResourceLocation moonDirt = modLoc("block/moon_dirt");
    ResourceLocation moonStone = modLoc("block/moon_rock");
    ResourceLocation stone = mcLoc("block/stone");
    ResourceLocation deepslate = mcLoc("block/deepslate");

    ResourceLocation copper = modLoc("block/copper_ore");
    ResourceLocation iron =  modLoc("block/iron_ore");
    ResourceLocation tintedOreTex = modLoc("block/ore");
    ResourceLocation siliconOreTex = modLoc("block/silicon_ore");
    ResourceLocation iridiumOreTex = modLoc("block/iridium_ore");
    ResourceLocation tektitesOreTex = modLoc("block/raw_tektites");
    
    ResourceLocation dungeonBrickTex = modLoc("block/dungeon_bricks");
    ResourceLocation buildingBlockWhiteTex = modLoc("block/base_building_block_white");
    ResourceLocation buildingBlockBlackTex = modLoc("block/base_building_block_black");
    ResourceLocation moonRockTex = modLoc("block/moon_rock");
    ResourceLocation prismaticTex = modLoc("block/prismatic_glass");
    ResourceLocation prismaticPaneTex = modLoc("block/prismatic_glass_pane_top");
    ResourceLocation tintedPrismaticTex = modLoc("block/tinted_prismatic_glass");
    ResourceLocation airlockBlockTex = modLoc("block/airlock_gate_frame");
    ResourceLocation magneticCraftingTopTex = modLoc("block/magnetic_crafting_table_top");
    
    ResourceLocation machineTop = modLoc("block/machine/machine");
    ResourceLocation machineBottom = modLoc("block/machine/machine_bottom");
    ResourceLocation machineSide = modLoc("block/machine/machine_side");
    ResourceLocation energyInPort = modLoc("block/machine/machine_energy_input");
    ResourceLocation energyOutPort = modLoc("block/machine/machine_energy_output");
    ResourceLocation fluidInPort = modLoc("block/machine/machine_fluid_input");
    ResourceLocation fluidOutPort = modLoc("block/machine/machine_fluid_output");
    ResourceLocation airVent = modLoc("block/machine/machine_gas_vent");
    ResourceLocation filterVent = modLoc("block/machine/oxygen_sealer_top");
    
    ResourceLocation coalCompressor = modLoc("block/machine/coal_compressor");
    ResourceLocation refineryFront =  modLoc("block/machine/refinery_front");
    ResourceLocation refineryTop =  modLoc("block/machine/refinery_energy_input");
    ResourceLocation refinerySide =  modLoc("block/machine/refinery_side");
    ResourceLocation solarPanelSide = modLoc("block/machine/machine_basic_solar_panel");
    ResourceLocation solarPanelTop = modLoc("block/machine/machine_solar_top");
    ResourceLocation cargoManagerSide = modLoc("block/machine/cargo_manager_side");
    ResourceLocation fuelManagerLoader = modLoc("block/machine/fuel_loader_side");
    ResourceLocation gasCompressor = modLoc("block/machine/gas_compressor");
    ResourceLocation gasVaporizer = modLoc("block/machine/gas_vaporizer");
    //endregion
    
    @Override
    protected void registerStatesAndModels() {
        blockWithItem(HpCBlocks.IRIDIUM_BLOCK);
        blockWithItem(HpCBlocks.RADIOACTIVE_BLOCK);
        blockWithItem(HpCBlocks.MOON_REGOLITH);
        blockWithItem(HpCBlocks.MOON_DIRT);
        blockWithItem(HpCBlocks.MOON_ROCK);
        blockWithItem(HpCBlocks.BASE_BUILDING_WHITE_BLOCK);
        blockWithItem(HpCBlocks.BASE_BUILDING_BLACK_BLOCK);
        blockWithItem(HpCBlocks.SILICON_BLOCK);
        blockWithItem(HpCBlocks.MOON_COBBLESTONE);
        blockWithItem(HpCBlocks.COPPER_WIRE_BLOCK);
        blockWithItem(HpCBlocks.FLUID_PIPE_BLOCK);

        fluidBlock(HpCFluids.CRUDE_OIL.getFluidBlockRegistry(), crudeOilStill);
        fluidBlock(HpCFluids.REFINED_FUEL.getFluidBlockRegistry(), refinedFuelStill);
        fluidBlock(HpCFluids.LIQUID_OXYGEN.getFluidBlockRegistry(), liquidOxygenStill);

        translucentBlock(HpCBlocks.PRISMATIC_GLASS, prismaticTex);
        translucentPaneBlock(HpCBlocks.PRISMATIC_GLASS_PANE.get(), prismaticTex, prismaticPaneTex);
        translucentBlock(HpCBlocks.TINTED_PRISMATIC_GLASS, tintedPrismaticTex);
        
        //region Non-Full Blocks
        blockItem(HpCBlocks.BASE_BUILDING_SLAB_WHITE);
        blockItem(HpCBlocks.BASE_BUILDING_SLAB_BLACK);
        blockItem(HpCBlocks.MOON_ROCK_SLAB);
        blockItem(HpCBlocks.MOON_DUNGEON_BRICK_SLAB);
        stairItem(HpCBlocks.BASE_BUILDING_STAIRS_WHITE);
        stairItem(HpCBlocks.BASE_BUILDING_STAIRS_BLACK);
        stairItem(HpCBlocks.MOON_ROCK_STAIRS);
        stairItem(HpCBlocks.MOON_DUNGEON_BRICK_STAIRS);
        
        tintedStairsBlock(HpCBlocks.MOON_DUNGEON_BRICK_STAIRS, dungeonBrickTex);
        tintedSlabBlock(HpCBlocks.MOON_DUNGEON_BRICK_SLAB, dungeonBrickTex);
        tintedWallBlock(HpCBlocks.MOON_DUNGEON_BRICK_WALL, dungeonBrickTex);
        
        tintedStairsBlock(HpCBlocks.BASE_BUILDING_STAIRS_WHITE, buildingBlockWhiteTex);
        tintedSlabBlock(HpCBlocks.BASE_BUILDING_SLAB_WHITE, buildingBlockWhiteTex);
        tintedWallBlock(HpCBlocks.BASE_BUILDING_WALL_WHITE, buildingBlockWhiteTex);

        tintedStairsBlock(HpCBlocks.BASE_BUILDING_STAIRS_BLACK, buildingBlockBlackTex);
        tintedSlabBlock(HpCBlocks.BASE_BUILDING_SLAB_BLACK, buildingBlockBlackTex);
        tintedWallBlock(HpCBlocks.BASE_BUILDING_WALL_BLACK, buildingBlockBlackTex);
        
        tintedStairsBlock(HpCBlocks.MOON_ROCK_STAIRS,  moonRockTex);
        tintedSlabBlock(HpCBlocks.MOON_ROCK_SLAB, moonRockTex);
        tintedWallBlock(HpCBlocks.MOON_ROCK_WALL, moonRockTex);

        blockWithItem(HpCBlocks.AIRLOCK_FRAME_BLOCK);
        booleanBlock(HpCBlocks.AIRLOCK_FRAME_SWITCH_BLOCK.get(),
                AirlockFrameSwitch.ACTIVE,
                airlockBlockTex,
                Map.of(),
                "block/airlock_gate_switch",
                "_on","_off",
                Direction.NORTH, Direction.SOUTH
        );
        airlockGeneratedBlock(HpCBlocks.AIRLOCK_GENERATED_BLOCK, buildingBlockBlackTex);

        customFacesBlock(HpCBlocks.MAGNETIC_CRAFTING_TABLE.get(),
                mcLoc("block/crafting_table_side"), 
                Map.of(
                        Direction.UP, magneticCraftingTopTex,
                        Direction.DOWN, mcLoc("block/oak_planks"),
                        Direction.WEST, mcLoc("block/crafting_table_front"),
                        Direction.NORTH, mcLoc("block/crafting_table_front")
                )
        );
        //endregion

        //region Machines
        directionalMachineBlock(HpCBlocks.COAL_COMPRESSOR.get(),
                machineSide,
                Map.of(
                        Direction.NORTH, coalCompressor,
                        Direction.UP, machineTop,
                        Direction.DOWN, machineBottom
                )
        );
        
        directionalMachineBlock(HpCBlocks.REFINERY.get(), 
                machineSide, 
                Map.of(
                        Direction.NORTH, refineryFront, 
                        Direction.UP, refineryTop,    
                        Direction.DOWN, machineBottom,
                        Direction.EAST, fluidInPort,
                        Direction.WEST, fluidOutPort,
                        Direction.SOUTH, refinerySide
                )
        );

        chargeableMachineBlock(
                HpCBlocks.ENERGY_STORAGE_UNIT.get(),
                EnergyStorageBlock.CHARGE,
                machineSide,
                Map.of(
                        Direction.EAST, energyInPort,   
                        Direction.WEST, energyOutPort,  
                        Direction.UP, machineTop,        
                        Direction.DOWN, machineBottom    
                ),
                "block/machine/energy_storage_unit/energy_storage_unit_",
                Direction.NORTH, Direction.SOUTH 
        );

        chargeableMachineBlock(
                HpCBlocks.BASIC_SOLAR_BLOCK.get(),
                SolarPanelBlock.CHARGE,
                machineSide,
                Map.of(
                        Direction.NORTH, energyOutPort,
                        Direction.SOUTH, solarPanelSide,
                        Direction.UP, solarPanelTop,
                        Direction.DOWN, machineBottom
                ),
                "block/machine/energy_storage_unit/energy_storage_unit_",
                Direction.EAST, Direction.WEST
        );
        
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
                        Direction.SOUTH, gasCompressor,
                        Direction.EAST, fluidInPort,
                        Direction.WEST, fluidOutPort
                )
        );

        directionalMachineBlock(HpCBlocks.GAS_VAPORIZER_BLOCK.get(),
                machineSide,
                Map.of(
                        Direction.NORTH, energyInPort,
                        Direction.UP, machineTop,
                        Direction.DOWN, machineBottom,
                        Direction.SOUTH, gasVaporizer,
                        Direction.EAST, fluidInPort,
                        Direction.WEST, fluidOutPort
                )
        );

        directionalMachineBlock(HpCBlocks.FUEL_MANAGER_BLOCK.get(),
                machineSide,
                Map.of(
                        Direction.NORTH, energyInPort,
                        Direction.SOUTH, fuelManagerLoader,
                        Direction.UP, machineTop,
                        Direction.DOWN, machineBottom,
                        Direction.EAST, fluidInPort
                )
        );

        directionalMachineBlock(HpCBlocks.CARGO_MANAGER_BLOCK.get(),
                machineSide,
                Map.of(
                        Direction.NORTH, energyInPort,
                        Direction.EAST, cargoManagerSide,
                        Direction.UP, machineTop,
                        Direction.DOWN, machineBottom
                )
        );
        
        directionalMachineBlock(HpCBlocks.ENERGY_GENERATOR_BLOCK.get(),
                machineSide,
                Map.of(
                        Direction.NORTH, energyOutPort,
                        Direction.UP, machineTop,
                        Direction.DOWN, machineBottom,
                        Direction.EAST, fluidInPort
                )
        );

        directionalMachineBlock(HpCBlocks.OXYGEN_SEALER_BLOCK.get(),
                machineSide,
                Map.of(
                        Direction.NORTH, energyInPort,
                        Direction.UP, filterVent,
                        Direction.DOWN, machineBottom,
                        Direction.EAST, fluidInPort
                )
        );
        //endregion

        //region Ores
        tintedOreBlock(HpCBlocks.MOON_COPPER_ORE.get(), moonStone, copper);
        tintedOreBlock(HpCBlocks.MOON_TIN_ORE.get(), moonStone, tintedOreTex);
        tintedOreBlock(HpCBlocks.TIN_ORE.get(), stone, tintedOreTex);
        tintedOreBlock(HpCBlocks.DEEPSLATE_TIN_ORE.get(), deepslate, tintedOreTex);
        tintedOreBlock(HpCBlocks.MOON_IRON_ORE.get(), moonStone, iron);
        tintedOreBlock(HpCBlocks.MOON_ALUMINIUM_ORE.get(), moonStone, tintedOreTex);
        tintedOreBlock(HpCBlocks.ALUMINIUM_ORE.get(), stone, tintedOreTex);
        tintedOreBlock(HpCBlocks.DEEPSLATE_ALUMINIUM_ORE.get(), deepslate, tintedOreTex);
        tintedOreBlock(HpCBlocks.MOON_SILICON_ORE.get(), moonStone, siliconOreTex);
        tintedOreBlock(HpCBlocks.SILICON_ORE.get(), stone, siliconOreTex);
        tintedOreBlock(HpCBlocks.DEEPSLATE_SILICON_ORE.get(), deepslate, siliconOreTex);
        tintedOreBlock(HpCBlocks.MOON_IRIDIUM_ORE.get(), moonStone, iridiumOreTex);
        tintedOreBlock(HpCBlocks.IRIDIUM_ORE.get(), stone, iridiumOreTex);
        tintedOreBlock(HpCBlocks.DEEPSLATE_IRIDIUM_ORE.get(), deepslate, iridiumOreTex);
        tintedOreBlock(HpCBlocks.MOON_TEKTITES_REGOLITH.get(), moonRegolith, tektitesOreTex);
        tintedOreBlock(HpCBlocks.MOON_TEKTITES.get(), moonDirt, tektitesOreTex);
        //endregion
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

    public void customFacesBlock(Block block, ResourceLocation defaultTexture, Map<Direction, ResourceLocation> overrides) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();

        BlockModelBuilder model = models().withExistingParent(name, "minecraft:block/cube");
        
        model.texture("north", overrides.getOrDefault(Direction.NORTH, defaultTexture));
        model.texture("south", overrides.getOrDefault(Direction.SOUTH, defaultTexture));
        model.texture("up", overrides.getOrDefault(Direction.UP, defaultTexture));
        model.texture("down", overrides.getOrDefault(Direction.DOWN, defaultTexture));
        model.texture("east", overrides.getOrDefault(Direction.EAST, defaultTexture));
        model.texture("west", overrides.getOrDefault(Direction.WEST, defaultTexture));
        
        model.texture("particle", overrides.getOrDefault(Direction.UP, defaultTexture));
        simpleBlock(block, model);
        
        itemModels().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + name)))
                .transforms()
                .transform(ItemDisplayContext.GUI)
                .rotation(30, 135, 0)
                .scale(0.625f)
                .end()
                .end();
    }

    public void tintedOreBlock(Block block, ResourceLocation baseTexture, ResourceLocation overlayTexture) {
        String baseName = BuiltInRegistries.BLOCK.getKey(block).getPath();
        
        ModelFile oreModel = models().withExistingParent(baseName, modLoc("block/tinted_ore_template"))
                .texture("base", baseTexture)
                .texture("overlay", overlayTexture);
        
        simpleBlock(block, oreModel);
        itemModels().getBuilder(baseName)
                .parent(oreModel);
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

    public void fluidBlock(DeferredBlock<?> fluidBlock, ResourceLocation particleTexture) {
        String baseName = fluidBlock.getId().getPath();
        
        ModelFile model = models().getBuilder(baseName)
                .texture("particle", particleTexture);
        
        getVariantBuilder(fluidBlock.get()).forAllStates(state ->
                ConfiguredModel.builder()
                        .modelFile(model)
                        .build()
        );
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

        model.texture("north", overrides.getOrDefault(Direction.NORTH, defaultTexture)); //Front
        model.texture("south", overrides.getOrDefault(Direction.SOUTH, defaultTexture)); //Back
        model.texture("up", overrides.getOrDefault(Direction.UP, defaultTexture));       //Top
        model.texture("down", overrides.getOrDefault(Direction.DOWN, defaultTexture));   //Bottom
        model.texture("east", overrides.getOrDefault(Direction.EAST, defaultTexture));   //Right Side
        model.texture("west", overrides.getOrDefault(Direction.WEST, defaultTexture));   //Left Side

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
                case UP -> x = 270; 
                case DOWN -> x = 90;  
            }

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(x)
                    .rotationY(y)
                    .build();
        });
    }

    public void chargeableMachineBlock(Block block, IntegerProperty chargeProperty, ResourceLocation defaultTexture, Map<Direction, ResourceLocation> staticOverrides, String chargeTexturePrefix, Direction... chargeFaces) {
        String baseName = BuiltInRegistries.BLOCK.getKey(block).getPath();
        
        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(BlockStateProperties.FACING);
            int charge = state.getValue(chargeProperty);

            String modelName = baseName + "_charge_" + charge;

            BlockModelBuilder model = models().withExistingParent(modelName, "minecraft:block/cube");
            
            model.texture("north", staticOverrides.getOrDefault(Direction.NORTH, defaultTexture));
            model.texture("south", staticOverrides.getOrDefault(Direction.SOUTH, defaultTexture));
            model.texture("up", staticOverrides.getOrDefault(Direction.UP, defaultTexture));
            model.texture("down", staticOverrides.getOrDefault(Direction.DOWN, defaultTexture));
            model.texture("east", staticOverrides.getOrDefault(Direction.EAST, defaultTexture));
            model.texture("west", staticOverrides.getOrDefault(Direction.WEST, defaultTexture));
            model.texture("particle", defaultTexture);
            
            for (Direction face : chargeFaces) {
                model.texture(face.getSerializedName(), modLoc(chargeTexturePrefix + charge));
            }

            int x = 0;
            int y = 0;

            switch (dir) {
                case NORTH -> y = 0;
                case EAST -> y = 90;
                case SOUTH -> y = 180;
                case WEST -> y = 270;
                case UP -> x = 270;
                case DOWN -> x = 90;
            }

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(x)
                    .rotationY(y)
                    .build();
        });

        var itemBuilder = itemModels().getBuilder(baseName)
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + baseName + "_charge_0")))
                .transforms()
                .transform(ItemDisplayContext.GUI)
                .rotation(30, 135, 0)
                .scale(0.625f)
                .end()
                .end();

        int maxCharge = chargeProperty.getPossibleValues().stream().max(Integer::compareTo).orElse(15);

        for (int i = 1; i <= maxCharge; i++) {
            itemBuilder.override()
                    .predicate(modLoc("charge"), (float) i / maxCharge)
                    .model(new ModelFile.UncheckedModelFile(modLoc("block/" + baseName + "_charge_" + i)))
                    .end();
        }
    }

    public void booleanBlock(Block block, BooleanProperty activityProperty, ResourceLocation defaultTexture, Map<Direction, ResourceLocation> staticOverrides, String activityTextureName, String positiveTexLoc, String negativeTexLoc, Direction... activeFaces) {
        String registryName = BuiltInRegistries.BLOCK.getKey(block).getPath();
        String defaultModelName = activityTextureName + negativeTexLoc;

        itemModels().getBuilder(registryName)
                .parent(new ModelFile.UncheckedModelFile(modLoc(defaultModelName)))
                .transforms()
                .transform(ItemDisplayContext.GUI)
                .rotation(30, 135, 0)
                .scale(0.625f)
                .end();
        
        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(BlockStateProperties.FACING);
            boolean active = state.getValue(activityProperty);

            String modelName = activityTextureName + (active ? positiveTexLoc : negativeTexLoc);
            BlockModelBuilder model = models().withExistingParent(modelName, "minecraft:block/cube");

            model.texture("north", staticOverrides.getOrDefault(Direction.NORTH, defaultTexture));
            model.texture("south", staticOverrides.getOrDefault(Direction.SOUTH, defaultTexture));
            model.texture("up", staticOverrides.getOrDefault(Direction.UP, defaultTexture));
            model.texture("down", staticOverrides.getOrDefault(Direction.DOWN, defaultTexture));
            model.texture("east", staticOverrides.getOrDefault(Direction.EAST, defaultTexture));
            model.texture("west", staticOverrides.getOrDefault(Direction.WEST, defaultTexture));
            model.texture("particle", defaultTexture);

            for (Direction face : activeFaces) {
                model.texture(face.getSerializedName(), modLoc(modelName));
            }

            int x = 0;
            int y = 0;

            switch (dir) {
                case NORTH -> y = 0;
                case EAST -> y = 90;
                case SOUTH -> y = 180;
                case WEST -> y = 270;
                case UP -> x = 270;
                case DOWN -> x = 90;
            }
            
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(x)
                    .rotationY(y)
                    .build();
        });
    }
    //endregion
    
    //region Niche Helpers
    public void airlockGeneratedBlock(DeferredBlock<?> block, ResourceLocation texture) {
        String baseName = block.getId().getPath();

        ModelFile sideTall = models().withExistingParent(baseName + "_side_tall", modLoc("block/tinted_wall_side_tall"))
                .texture("wall", texture);
        
        ModelFile flat = models().getBuilder(baseName + "_flat")
                .texture("particle", texture)
                .texture("wall", texture)
                .element()
                .from(0, 6, 0).to(16, 10, 16)
                .allFaces((dir, face) -> face.texture("#wall"))
                .end();

        var builder = getMultipartBuilder(block.get());
        
        builder.part().modelFile(sideTall).rotationY(0).addModel().condition(BlockStateProperties.AXIS, Direction.Axis.X).end()
                .part().modelFile(sideTall).rotationY(180).addModel().condition(BlockStateProperties.AXIS, Direction.Axis.X).end();

        builder.part().modelFile(sideTall).rotationY(90).addModel().condition(BlockStateProperties.AXIS, Direction.Axis.Z).end()
                .part().modelFile(sideTall).rotationY(270).addModel().condition(BlockStateProperties.AXIS, Direction.Axis.Z).end();
        
        builder.part().modelFile(flat).addModel().condition(BlockStateProperties.AXIS, Direction.Axis.Y).end();
    }
    //endregion
}
