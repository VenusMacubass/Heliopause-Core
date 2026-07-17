package net.venera.heliocore.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.entity.machine.CoalCompressorEntity;
import net.venera.heliocore.block.entity.machine.electric.*;

import java.util.concurrent.Flow;
import java.util.function.Supplier;

public class HpCBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE , HeliopauseCore.MOD_ID);

    //----------------- Compressor Entities ----------------//
    public static final Supplier<BlockEntityType<CoalCompressorEntity>> COAL_COMPRESSOR_ENTITY = BLOCK_ENTITIES.register("coal_compressor_entity",
            () -> BlockEntityType.Builder.of(CoalCompressorEntity::new, HpCBlocks.COAL_COMPRESSOR.get()).build(null));

    //----------------- Refinery Entity ----------------//
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RefineryEntity>> REFINERY_ENTITY =
            BLOCK_ENTITIES.register("refinery", () ->
                    BlockEntityType.Builder.of(
                            (pos, state) -> new RefineryEntity(HpCBlockEntities.REFINERY_ENTITY.get(), pos, state,
                                    5000, 100, 2, 10
                            ),
                            HpCBlocks.REFINERY.get()
                    ).build(null));
    
    //----------------- Launch Platform Entities ----------------//
    public static final Supplier<BlockEntityType<LaunchPlatformEntity>> LAUNCH_PLATFORM_ENTITY = BLOCK_ENTITIES.register("launch_platform_entity",
            () -> BlockEntityType.Builder.of(LaunchPlatformEntity::new, HpCBlocks.LAUNCH_PAD.get()).build(null));
    
    //----------------- Fluid Tank Entity ----------------//
    public static final Supplier<BlockEntityType<FluidTankEntity>> FLUID_TANK_ENTITY = BLOCK_ENTITIES.register("fluid_tank_entity",
            () -> BlockEntityType.Builder.of(FluidTankEntity::new, HpCBlocks.FLUID_TANK.get()).build(null));

    //----------------- Energy Storage Entities ----------------//
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyStorageEntity>> ENERGY_STORAGE_ENTITY = BLOCK_ENTITIES.register("basic_energy_storage",
            () -> BlockEntityType.Builder.of((pos, state)
                            -> new EnergyStorageEntity(HpCBlockEntities.ENERGY_STORAGE_ENTITY.get(), pos, state, 20000, 10),
                            HpCBlocks.ENERGY_STORAGE_UNIT.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyGeneratorEntity>> ENERGY_GENERATOR_ENTITY = BLOCK_ENTITIES.register("energy_generator_entity",
            ()-> BlockEntityType.Builder.of((blockPos, blockState) ->
                            new EnergyGeneratorEntity(HpCBlockEntities.ENERGY_GENERATOR_ENTITY.get(), blockPos, blockState,
                                    5000, 100, 50),
                    HpCBlocks.ENERGY_GENERATOR_BLOCK.get()).build(null));

    //----------------- Solar Panel Entities ----------------//
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarPanelEntity>> BASIC_SOLAR_PANEL_ENTITY = BLOCK_ENTITIES.register("basic_solar_panel_entity",
            ()-> BlockEntityType.Builder.of((blockPos, blockState) ->
                    new SolarPanelEntity(HpCBlockEntities.BASIC_SOLAR_PANEL_ENTITY.get(), blockPos, blockState, 
                            10000, 10, 12), 
                    HpCBlocks.BASIC_SOLAR_BLOCK.get()).build(null));
    //----------------- Manager Entities ---------------------//
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CargoManagerEntity>> CARGO_MANAGER_ENTITY = BLOCK_ENTITIES.register("cargo_manager_entity",
            ()-> BlockEntityType.Builder.of((blockPos, blockState) ->
                    new CargoManagerEntity(HpCBlockEntities.CARGO_MANAGER_ENTITY.get(), blockPos, blockState,
                            5000, 10, 2),
                    HpCBlocks.CARGO_MANAGER_BLOCK.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FuelManagerEntity>> FUEL_MANAGER_ENTITY = BLOCK_ENTITIES.register("fuel_manager_entity",
            ()-> BlockEntityType.Builder.of((blockPos, blockState) ->
                    new FuelManagerEntity(HpCBlockEntities.FUEL_MANAGER_ENTITY.get(), blockPos, blockState,
                                    5000, 10, 2),
                    HpCBlocks.FUEL_MANAGER_BLOCK.get()).build(null));
    
    //region Gas Machines
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OxygenGeneratorEntity>> OXYGEN_GENERATOR_ENTITY = BLOCK_ENTITIES.register("oxygen_generator_entity",
            ()-> BlockEntityType.Builder.of((blockPos, blockState) ->
                    new OxygenGeneratorEntity(HpCBlockEntities.OXYGEN_GENERATOR_ENTITY.get(), blockPos, blockState,
                            5000, 2),
                    HpCBlocks.OXYGEN_GENERATOR_BLOCK.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GasCompressorEntity>> GAS_COMPRESSOR_ENTITY = BLOCK_ENTITIES.register("gas_compressor_entity",
            ()-> BlockEntityType.Builder.of((blockPos, blockState) ->
                            new GasCompressorEntity(HpCBlockEntities.GAS_COMPRESSOR_ENTITY.get(), blockPos, blockState,
                                    5000, 100, 3, 5),
                    HpCBlocks.GAS_COMPRESSOR_BLOCK.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GasVaporizerEntity>> GAS_VAPORIZER_ENTITY = BLOCK_ENTITIES.register("gas_vaporizer_entity",
            ()-> BlockEntityType.Builder.of((blockPos, blockState) ->
                            new GasVaporizerEntity(HpCBlockEntities.GAS_VAPORIZER_ENTITY.get(), blockPos, blockState,
                                    5000, 100, 3, 5),
                    HpCBlocks.GAS_VAPORIZER_BLOCK.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OxygenSealerEntity>> OXYGEN_SEALER_ENTITY = BLOCK_ENTITIES.register("oxygen_sealer_entity",
            ()-> BlockEntityType.Builder.of((blockPos, blockState) ->
                            new OxygenSealerEntity(HpCBlockEntities.OXYGEN_SEALER_ENTITY.get(), blockPos, blockState,
                                    5000, 100, 3, 1),
                    HpCBlocks.OXYGEN_SEALER_BLOCK.get()).build(null));
    
    //endregion

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DeconstructorEntity>> DECONSTRUCTOR_ENTITY = BLOCK_ENTITIES.register("deconstructor_entity",
            ()-> BlockEntityType.Builder.of((blockPos, blockState) ->
                            new DeconstructorEntity(HpCBlockEntities.DECONSTRUCTOR_ENTITY.get(), blockPos, blockState,
                                    5000, 50, 50),
                    HpCBlocks.DECONSTRUCTOR_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
