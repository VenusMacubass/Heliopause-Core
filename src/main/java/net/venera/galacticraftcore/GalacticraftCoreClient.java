package net.venera.galacticraftcore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.entity.ModEntities;
import net.venera.galacticraftcore.entity.zombie.SpaceZombieRenderer;
import net.venera.galacticraftcore.item.ModItems;
import net.venera.galacticraftcore.screen.ModMenuTypes;
import net.venera.galacticraftcore.screen.custom.CoalCompressorScreen;
import net.venera.galacticraftcore.screen.custom.RefineryScreen;


//@Mod(value = GalacticraftCore.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = GalacticraftCore.MOD_ID, value = Dist.CLIENT)
public class GalacticraftCoreClient {
    public GalacticraftCoreClient(ModContainer container) {

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(final FMLClientSetupEvent event) {
        // Some client setup code
        GalacticraftCore.LOGGER.info("HELLO FROM CLIENT SETUP");
        GalacticraftCore.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_TANK.get(), RenderType.translucent());
    }

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (var blockObject : ModBlocks.BLOCKS.getEntries()) {
            event.register((state, getter, pos, tintIndex) -> {
                if (getter != null && pos != null) {
                    FluidState fluidState = getter.getFluidState(pos);
                    return IClientFluidTypeExtensions.of(fluidState).getTintColor(fluidState, getter, pos);
                } else return 0xFFFFFFFF;
            }, blockObject.get());
        }
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (var itemObject : ModItems.ITEMS.getEntries()) {
            event.register((stack, tintIndex) -> {
                if (tintIndex != 1) return 0xFFFFFFFF;
                return FluidUtil.getFluidContained(stack)
                        .map(fluidStack -> IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack))
                        .orElse(0xFFFFFFFF);
            }, itemObject.get());
        }
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SPACE_ZOMBIE.get(), SpaceZombieRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.COAL_COMPRESSOR_MENU.get(), CoalCompressorScreen::new);
        event.register(ModMenuTypes.REFINERY_MENU.get(), RefineryScreen::new);
    }

}
