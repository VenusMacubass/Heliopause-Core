package net.venera.galacticraftcore;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.entity.ModEntities;
import net.venera.galacticraftcore.entity.client.Tier1RocketRenderer;
import net.venera.galacticraftcore.entity.zombie.SpaceZombieRenderer;
import net.venera.galacticraftcore.item.ModItems;
import net.venera.galacticraftcore.render.sky.MoonSkyRenderer;
import net.venera.galacticraftcore.screen.ModMenuTypes;
import net.venera.galacticraftcore.screen.custom.BasicSolarScreen;
import net.venera.galacticraftcore.screen.custom.CoalCompressorScreen;
import net.venera.galacticraftcore.screen.custom.EnergyStorageUnitScreen;
import net.venera.galacticraftcore.screen.custom.RefineryScreen;
import org.joml.Matrix4f;

import java.awt.*;


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
        EntityRenderers.register(ModEntities.TIER_1_ROCKET.get(), Tier1RocketRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        for (var blockObject : ModBlocks.BLOCKS.getEntries()) {
            event.register((state, getter, pos, tintIndex) -> {
                if (getter != null && pos != null) {
                    FluidState fluidState = getter.getFluidState(pos);
                    return IClientFluidTypeExtensions.of(fluidState).getTintColor(fluidState, getter, pos);
                } else return 0xFFFFFFFF;
            }, blockObject.get());
        }
        event.register((state, level, pos, tintIndex) -> {
            long time = System.currentTimeMillis();

            // 4000 ms
            // Increase the number to slow it down
            float hue = (time % 4000) / 4000.0f;
            
            return Color.HSBtoRGB(hue, 1.0f, 1.0f);

        }, ModBlocks.PRISMATIC_GLASS.get());

        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? 0xFFB4D6ED : -1,
                ModBlocks.DEEPSLATE_ALUMINIUM_ORE.get(), ModBlocks.ALUMINIUM_ORE.get(), ModBlocks.RAW_ALUMINIUM_BLOCK.get());
        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? 0xFFFFFAD6 : -1,
                ModBlocks.DEEPSLATE_TIN_ORE.get(), ModBlocks.TIN_ORE.get(), ModBlocks.RAW_TIN_BLOCK.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (var itemObject : ModItems.ITEMS.getEntries()) {
            event.register((stack, tintIndex) -> {
                if (tintIndex != 1) return 0xFFFFFFFF;
                return FluidUtil.getFluidContained(stack)
                        .map(fluidStack -> IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack))
                        .orElse(0xFFFFFFFF);
            }, itemObject.get());
        }
        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFFB4D6ED : -1,
                ModBlocks.ALUMINIUM_ORE.get(), ModBlocks.DEEPSLATE_ALUMINIUM_ORE.get(), ModItems.ALUMINIUM_INGOT.get(), 
                ModBlocks.RAW_ALUMINIUM_BLOCK.get(), ModItems.COMPRESSED_ALUMINIUM.get());
        
        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFFD9AAF2 : -1,
                ModItems.IRIDIUM_INGOT.get(), ModItems.COMPRESSED_IRIDIUM.get());
        
        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFFFFFAD6 : -1,
                ModBlocks.TIN_ORE.get(), ModBlocks.DEEPSLATE_TIN_ORE.get(), ModItems.RAW_TIN.get(), ModItems.TIN_INGOT.get(),  
                ModBlocks.RAW_TIN_BLOCK.get(), ModItems.COMPRESSED_TIN.get());
        
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SPACE_ZOMBIE.get(), SpaceZombieRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.COAL_COMPRESSOR_MENU.get(), CoalCompressorScreen::new);
        event.register(ModMenuTypes.REFINERY_MENU.get(), RefineryScreen::new);
        event.register(ModMenuTypes.ENERGY_STORAGE_UNIT_MENU.get(), EnergyStorageUnitScreen::new);
        event.register(ModMenuTypes.BASIC_SOLAR_MENU.get(), BasicSolarScreen::new);
    }

    @SubscribeEvent
    public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {

        DimensionSpecialEffects moonEffects = new DimensionSpecialEffects(Float.NaN, true, DimensionSpecialEffects.SkyType.NORMAL, false, false) {

            @Override
            public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float daylight) {
                return biomeFogColor;
            }

            @Override
            public boolean isFoggyAt(int x, int y) {
                return false;
            }

            // YOUR EXACT SIGNATURE:
            @Override
            public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {

                // Pass the modelViewMatrix to our renderer instead of a PoseStack
                MoonSkyRenderer.renderSky(level, partialTick, modelViewMatrix, camera, projectionMatrix, setupFog);
                return true;
            }
        };

        event.register(ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "moon"), moonEffects);
    }
}
