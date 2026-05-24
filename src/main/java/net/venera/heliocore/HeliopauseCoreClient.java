package net.venera.heliocore;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.venera.heliocore.block.ModBlocks;
import net.venera.heliocore.block.entity.ModBlockEntities;
import net.venera.heliocore.entity.ModEntities;
import net.venera.heliocore.entity.client.Tier1RocketRenderer;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;
import net.venera.heliocore.entity.zombie.SpaceZombieRenderer;
import net.venera.heliocore.fluid.ModFluids;
import net.venera.heliocore.item.ModItems;
import net.venera.heliocore.render.FluidTankRenderer;
import net.venera.heliocore.render.sky.MoonSkyRenderer;
import net.venera.heliocore.screen.ModMenuTypes;
import net.venera.heliocore.screen.custom.*;
import org.joml.Matrix4f;

import java.awt.*;


@EventBusSubscriber(modid = HeliopauseCore.MOD_ID, value = Dist.CLIENT)
public class HeliopauseCoreClient {
    public HeliopauseCoreClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(final FMLClientSetupEvent event) {
        // Some client setup code
        HeliopauseCore.LOGGER.info("HELLO FROM CLIENT SETUP");
        HeliopauseCore.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_TANK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_PIPE.get(), RenderType.translucent());
        EntityRenderers.register(ModEntities.TIER_1_ROCKET.get(), Tier1RocketRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, getter, pos, tintIndex) -> {
            if (getter != null && pos != null) {
                FluidState fluidState = getter.getFluidState(pos);
                return IClientFluidTypeExtensions.of(fluidState).getTintColor(fluidState, getter, pos);
            } else return 0xFFFFFFFF;
        }, ModFluids.CRUDE_OIL.getFluidblock(), ModFluids.REFINED_FUEL.getFluidblock()); 
        event.register((state, level, pos, tintIndex) -> {
            long time = System.currentTimeMillis();

            // 4000 ms
            // Increase the number to slow it down
            float hue = (time % 4000) / 4000.0f;
            
            return Color.HSBtoRGB(hue, 1.0f, 1.0f);

        }, ModBlocks.PRISMATIC_GLASS.get());

        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? 0xFFB4D6ED : -1,
                ModBlocks.DEEPSLATE_ALUMINIUM_ORE.get(), 
                ModBlocks.ALUMINIUM_ORE.get(), 
                ModBlocks.RAW_ALUMINIUM_BLOCK.get(), 
                ModBlocks.ALUMINIUM_BLOCK.get(),
                ModBlocks.MOON_ALUMINIUM_ORE.get());
        
        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? 0xFFFFFAD6: -1,
                ModBlocks.DEEPSLATE_TIN_ORE.get(), 
                ModBlocks.TIN_ORE.get(), 
                ModBlocks.RAW_TIN_BLOCK.get(), 
                ModBlocks.TIN_BLOCK.get(),
                ModBlocks.MOON_TIN_ORE.get());

        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? 0xFFD9AAF2: -1,
                ModBlocks.IRIDIUM_ORE.get(),
                ModBlocks.DEEPSLATE_IRIDIUM_ORE.get(),
                ModBlocks.MOON_IRIDIUM_ORE.get());

        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? 0xFF4B4B4B: -1, 
                ModBlocks.MOON_DUNGEON_BRICKS.get(),
                ModBlocks.MOON_DUNGEON_BRICK_SLAB.get(),
                ModBlocks.MOON_DUNGEON_BRICK_STAIRS.get(),
                ModBlocks.MOON_DUNGEON_BRICK_WALL.get());
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
                ModBlocks.ALUMINIUM_ORE.get(), 
                ModBlocks.DEEPSLATE_ALUMINIUM_ORE.get(), 
                ModItems.ALUMINIUM_INGOT.get(), 
                ModBlocks.RAW_ALUMINIUM_BLOCK.get(), 
                ModItems.COMPRESSED_ALUMINIUM.get(), 
                ModBlocks.ALUMINIUM_BLOCK.get(),
                ModBlocks.MOON_ALUMINIUM_ORE.get());
        
        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFFFFFAD6 : -1,
                ModBlocks.TIN_ORE.get(), 
                ModBlocks.DEEPSLATE_TIN_ORE.get(), 
                ModItems.RAW_TIN.get(), 
                ModItems.TIN_INGOT.get(),  
                ModBlocks.RAW_TIN_BLOCK.get(), 
                ModItems.COMPRESSED_TIN.get(), 
                ModBlocks.TIN_BLOCK.get(),
                ModBlocks.MOON_TIN_ORE.get());

        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFFD9AAF2 : -1,
                ModItems.IRIDIUM_INGOT.get(),
                ModItems.COMPRESSED_IRIDIUM.get(),
                ModBlocks.IRIDIUM_ORE.get(),
                ModBlocks.DEEPSLATE_IRIDIUM_ORE.get(),
                ModBlocks.MOON_IRIDIUM_ORE.get());

        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFF4B4B4B : -1,
                ModBlocks.MOON_DUNGEON_BRICKS.get(),
                ModBlocks.MOON_DUNGEON_BRICK_SLAB.get(),
                ModBlocks.MOON_DUNGEON_BRICK_STAIRS.get(),
                ModBlocks.MOON_DUNGEON_BRICK_WALL.get());
        
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.SPACE_ZOMBIE.get(), SpaceZombieRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FLUID_TANK_ENTITY.get(), FluidTankRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.COAL_COMPRESSOR_MENU.get(), CoalCompressorScreen::new);
        event.register(ModMenuTypes.REFINERY_MENU.get(), RefineryScreen::new);
        event.register(ModMenuTypes.ENERGY_STORAGE_UNIT_MENU.get(), EnergyStorageUnitScreen::new);
        event.register(ModMenuTypes.BASIC_SOLAR_MENU.get(), BasicSolarScreen::new);
        event.register(ModMenuTypes.ROCKET_MENU.get(), RocketScreen::new);
        event.register(ModMenuTypes.CARGO_MANAGER_MENU.get(), CargoManagerScreen::new);
        event.register(ModMenuTypes.FUEL_MANAGER_MENU.get(), FuelManagerScreen::new);
    }

    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) {
        // Target the specific GUI layer responsible for the jump charge bar
        if (event.getName().equals(VanillaGuiLayers.JUMP_METER)) {

            Player player = Minecraft.getInstance().player;
            
            if (player != null && player.getVehicle() instanceof Tier1RocketEntity) {
                event.setCanceled(true);
            }
        }
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
            
            @Override
            public boolean renderSky(ClientLevel level, int ticks, float partialTick, Matrix4f modelViewMatrix, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
                
                MoonSkyRenderer.renderSky(level, partialTick, modelViewMatrix, camera, projectionMatrix, setupFog);
                return true;
            }
        };

        event.register(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon"), moonEffects);
    }
}
