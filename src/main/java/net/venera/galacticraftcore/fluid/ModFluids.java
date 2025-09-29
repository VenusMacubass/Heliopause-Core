package net.venera.galacticraftcore.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.fluid.custom.CrudeOil;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, GalacticraftCore.MOD_ID);

    public static final DeferredHolder<Fluid, FlowingFluid> CRUDE_OIL =
            FLUIDS.register("oil_still", () -> CrudeOil.SOURCE);

    public static final DeferredHolder<Fluid, FlowingFluid> FLOWING_CRUDE_OIL =
            FLUIDS.register("oil_flow", () -> CrudeOil.FLOWING);







    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}
