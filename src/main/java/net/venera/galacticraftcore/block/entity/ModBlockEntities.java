package net.venera.galacticraftcore.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.block.entity.machine.CoalCompressorEntity;
import net.venera.galacticraftcore.block.entity.machine.electric.EnergyStorageUnitEntity;
import net.venera.galacticraftcore.block.entity.machine.electric.RefineryEntity;
import net.venera.galacticraftcore.block.entity.machine.electric.SolarPanelEntity;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE ,GalacticraftCore.MOD_ID);

    public static final Supplier<BlockEntityType<CoalCompressorEntity>> COAL_COMPRESSOR_ENTITY = BLOCK_ENTITIES.register("coal_compressor_entity",
            () -> BlockEntityType.Builder.of(CoalCompressorEntity::new, ModBlocks.COAL_COMPRESSOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RefineryEntity>> REFINERY_ENTITY =
            BLOCK_ENTITIES.register("refinery", () ->
                    BlockEntityType.Builder.of(
                            (pos, state) -> new RefineryEntity(ModBlockEntities.REFINERY_ENTITY.get(), pos, state,
                                    10000, 100, 2, 1
                            ),
                            ModBlocks.REFINERY.get()
                    ).build(null));

    public static final Supplier<BlockEntityType<LaunchPlatformEntity>> LAUNCH_PLATFORM_ENTITY = BLOCK_ENTITIES.register("launch_platform_entity",
            () -> BlockEntityType.Builder.of(LaunchPlatformEntity::new, ModBlocks.LAUNCH_PAD.get()).build(null));

    public static final Supplier<BlockEntityType<FluidTankEntity>> FLUID_TANK_ENTITY = BLOCK_ENTITIES.register("fluid_tank_entity",
            () -> BlockEntityType.Builder.of(FluidTankEntity::new, ModBlocks.FLUID_TANK.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EnergyStorageUnitEntity>> ENERGY_STORAGE_UNIT_ENTITY = BLOCK_ENTITIES.register("basic_energy_storage",
            () -> BlockEntityType.Builder.of((pos, state)
                            -> new EnergyStorageUnitEntity(ModBlockEntities.ENERGY_STORAGE_UNIT_ENTITY.get(), pos, state, 10000, 10),
                            ModBlocks.ENERGY_STORAGE_UNIT.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolarPanelEntity>> BASIC_SOLAR_PANEL_ENTITY = BLOCK_ENTITIES.register("basic_solar_panel_entity",
            ()-> BlockEntityType.Builder.of((blockPos, blockState) ->
                    new SolarPanelEntity(ModBlockEntities.BASIC_SOLAR_PANEL_ENTITY.get(), blockPos, blockState, 
                            12000, 10, 12), 
                    ModBlocks.BASIC_SOLAR_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
