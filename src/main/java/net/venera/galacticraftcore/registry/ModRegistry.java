package net.venera.galacticraftcore.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.registries.*;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.custom.CrudeOilBlock;
import net.venera.galacticraftcore.fluid.CrudeOilFluid;
import net.venera.galacticraftcore.fluid.ModFluids;
import net.venera.galacticraftcore.init.CrudeOilBlockReg;
import net.venera.galacticraftcore.item.custom.CrudeOilBucketItem;

public class ModRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GalacticraftCore.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GalacticraftCore.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, GalacticraftCore.MOD_ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, GalacticraftCore.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GalacticraftCore.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GalacticraftCore.MOD_ID);

    public static final CrudeOilBlockReg CRUDE_OIL = new CrudeOilBlockReg.Builder("crude_oil", () -> Blocks.COAL_BLOCK, 0xFF392C20).mapColor(MapColor.COLOR_BLACK).build();



//    public static final Supplier<BlockEntityType<LiquidBlockEntity>> LIQUID_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("liquid_tile",
//            () -> BlockEntityType.Builder.of(LiquidBlockEntity::new,
//                    CRUDE_OIL.getFluidblock()).build(null));

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (DeferredHolder<Item, ? extends Item> itemDeferredHolder : ModRegistry.ITEMS.getEntries()) {
            if (itemDeferredHolder.get() instanceof CrudeOilBucketItem) {
                event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidBucketWrapper(stack), itemDeferredHolder.get());
            }
        }
    }
}
