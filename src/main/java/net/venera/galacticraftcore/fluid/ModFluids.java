package net.venera.galacticraftcore.fluid;

import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.blockentity.LiquidBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector3f;
import java.awt.*;
import java.util.function.Supplier;

public class ModFluids {
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, GalacticraftCore.MOD_ID);
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, GalacticraftCore.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GalacticraftCore.MOD_ID);

    public static final ModLiquidBlockFactory CRUDE_OIL = new ModLiquidBlockFactory.Builder("crude_oil", () -> Blocks.LAVA, 0xFF1f1f1f)
        .modifyFogColor(fogColor(Color.BLACK)).setViscosity(3000).motionScale(0.07D).mapColor(MapColor.COLOR_BLACK).build();

    public static final ModLiquidBlockFactory REFINED_FUEL = new ModLiquidBlockFactory.Builder("refined_fuel", () -> Blocks.WATER, 0xFF1f1f1f)
            .modifyFogColor(fogColor(Color.yellow.darker())).setViscosity(2000).motionScale(0.07D).mapColor(MapColor.COLOR_YELLOW).build();



    public static final Supplier<BlockEntityType<LiquidBlockEntity>> LIQUID_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("liquid_tile",
			() -> BlockEntityType.Builder.of(LiquidBlockEntity::new,
                    CRUDE_OIL.getFluidblock(), REFINED_FUEL.getFluidblock())
                    .build(null));

    public static Vector3f fogColor(Color mcColor){
        return new Vector3f(
            mcColor.getRed() / 255F,
            mcColor.getGreen() / 255F,
            mcColor.getBlue() / 255F
     );}
}
