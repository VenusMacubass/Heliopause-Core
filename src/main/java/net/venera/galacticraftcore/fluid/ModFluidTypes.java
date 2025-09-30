package net.venera.galacticraftcore.fluid;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.WaterFluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.fluid.custom.CrudeOil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, GalacticraftCore.MOD_ID);

    public static final ResourceLocation STILL = ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "block/crude_oil_still");
    public static final ResourceLocation FLOW = ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "block/crude_oil_flow");

    public static final Supplier<FluidType> CRUDE_OIL_TYPE =
            FLUID_TYPES.register("crude_oil", () -> new FluidType(FluidType.Properties.create()
                    .density(1500)
                    .viscosity(3000)
                    .temperature(300)
                    .canDrown(true)
                    .canSwim(true)
                    .canExtinguish(true)
                    .canConvertToSource(false)) {});

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
