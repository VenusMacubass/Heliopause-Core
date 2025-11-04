package net.venera.galacticraftcore.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE ,GalacticraftCore.MOD_ID);

    public static final Supplier<BlockEntityType<CoalCompressorEntity>> COAL_COMPRESSOR_ENTITY = BLOCK_ENTITIES.register("coal_compressor_entity",
            () -> BlockEntityType.Builder.of(CoalCompressorEntity::new, ModBlocks.COAL_COMPRESSOR.get()).build(null));

    public static final Supplier<BlockEntityType<RefineryEntity>> REFINERY_ENTITY = BLOCK_ENTITIES.register("refinery_entity",
            () -> BlockEntityType.Builder.of(RefineryEntity::new, ModBlocks.REFINERY.get()).build(null));

    public static final Supplier<BlockEntityType<LaunchPlatformEntity>> LAUNCH_PLATFORM_ENTITY = BLOCK_ENTITIES.register("launch_platform_entity",
            () -> BlockEntityType.Builder.of(LaunchPlatformEntity::new, ModBlocks.LAUNCH_PAD.get()).build(null));

    public static final Supplier<BlockEntityType<FluidTankEntity>> FLUID_TANK_ENTITY = BLOCK_ENTITIES.register("fluid_tank_entity",
            () -> BlockEntityType.Builder.of(FluidTankEntity::new, ModBlocks.FLUID_TANK.get()).build(null));


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
