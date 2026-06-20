package net.venera.heliocore;

import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
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
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.entity.HpCEntities;
import net.venera.heliocore.entity.client.Tier1RocketModel;
import net.venera.heliocore.entity.client.Tier1RocketRenderer;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;
import net.venera.heliocore.entity.zombie.SpaceZombieRenderer;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import net.venera.heliocore.render.FluidTankRenderer;
import net.venera.heliocore.render.sky.MoonSkyRenderer;
import net.venera.heliocore.screen.HpCMenuTypes;
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
        ItemBlockRenderTypes.setRenderLayer(HpCBlocks.FLUID_TANK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(HpCBlocks.FLUID_PIPE.get(), RenderType.translucent());
        EntityRenderers.register(HpCEntities.TIER_1_ROCKET.get(), Tier1RocketRenderer::new);
        
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, getter, pos, tintIndex) -> {
            if (getter != null && pos != null) {
                FluidState fluidState = getter.getFluidState(pos);
                return IClientFluidTypeExtensions.of(fluidState).getTintColor(fluidState, getter, pos);
            } else return 0xFFFFFFFF;
        }, HpCFluids.CRUDE_OIL.getFluidblock(), HpCFluids.REFINED_FUEL.getFluidblock());

        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? 0xFFB4D6ED : -1,
                HpCBlocks.DEEPSLATE_ALUMINIUM_ORE.get(), 
                HpCBlocks.ALUMINIUM_ORE.get(), 
                HpCBlocks.RAW_ALUMINIUM_BLOCK.get(), 
                HpCBlocks.ALUMINIUM_BLOCK.get(),
                HpCBlocks.MOON_ALUMINIUM_ORE.get());
        
        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? 0xFFFFFAD6: -1,
                HpCBlocks.DEEPSLATE_TIN_ORE.get(), 
                HpCBlocks.TIN_ORE.get(), 
                HpCBlocks.RAW_TIN_BLOCK.get(), 
                HpCBlocks.TIN_BLOCK.get(),
                HpCBlocks.MOON_TIN_ORE.get());

        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? 0xFFD9AAF2: -1,
                HpCBlocks.IRIDIUM_ORE.get(),
                HpCBlocks.DEEPSLATE_IRIDIUM_ORE.get(),
                HpCBlocks.MOON_IRIDIUM_ORE.get());

        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? 0xFF4B4B4B: -1, 
                HpCBlocks.MOON_DUNGEON_BRICKS.get(),
                HpCBlocks.MOON_DUNGEON_BRICK_SLAB.get(),
                HpCBlocks.MOON_DUNGEON_BRICK_STAIRS.get(),
                HpCBlocks.MOON_DUNGEON_BRICK_WALL.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (var itemObject : HpCItems.ITEMS.getEntries()) {
            event.register((stack, tintIndex) -> {
                if (tintIndex != 1) return 0xFFFFFFFF;
                return FluidUtil.getFluidContained(stack)
                        .map(fluidStack -> IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack))
                        .orElse(0xFFFFFFFF);
            }, itemObject.get());
        }
        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFFB4D6ED : -1,
                HpCBlocks.ALUMINIUM_ORE.get(), 
                HpCBlocks.DEEPSLATE_ALUMINIUM_ORE.get(), 
                HpCItems.ALUMINIUM_INGOT.get(), 
                HpCBlocks.RAW_ALUMINIUM_BLOCK.get(), 
                HpCItems.COMPRESSED_ALUMINIUM.get(), 
                HpCBlocks.ALUMINIUM_BLOCK.get(),
                HpCBlocks.MOON_ALUMINIUM_ORE.get());
        
        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFFFFFAD6 : -1,
                HpCBlocks.TIN_ORE.get(), 
                HpCBlocks.DEEPSLATE_TIN_ORE.get(), 
                HpCItems.RAW_TIN.get(), 
                HpCItems.TIN_INGOT.get(),  
                HpCBlocks.RAW_TIN_BLOCK.get(), 
                HpCItems.COMPRESSED_TIN.get(), 
                HpCBlocks.TIN_BLOCK.get(),
                HpCBlocks.MOON_TIN_ORE.get());

        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFFD9AAF2 : -1,
                HpCItems.IRIDIUM_INGOT.get(),
                HpCItems.COMPRESSED_IRIDIUM.get(),
                HpCBlocks.IRIDIUM_ORE.get(),
                HpCBlocks.DEEPSLATE_IRIDIUM_ORE.get(),
                HpCBlocks.MOON_IRIDIUM_ORE.get());

        event.register((stack, tintIndex) -> tintIndex == 0 ? 0xFF4B4B4B : -1,
                HpCBlocks.MOON_DUNGEON_BRICKS.get(),
                HpCBlocks.MOON_DUNGEON_BRICK_SLAB.get(),
                HpCBlocks.MOON_DUNGEON_BRICK_STAIRS.get(),
                HpCBlocks.MOON_DUNGEON_BRICK_WALL.get());
        
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(HpCEntities.SPACE_ZOMBIE.get(), SpaceZombieRenderer::new);
        event.registerBlockEntityRenderer(HpCBlockEntities.FLUID_TANK_ENTITY.get(), FluidTankRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(HpCMenuTypes.COAL_COMPRESSOR_MENU.get(), CoalCompressorScreen::new);
        event.register(HpCMenuTypes.REFINERY_MENU.get(), RefineryScreen::new);
        event.register(HpCMenuTypes.ENERGY_STORAGE_UNIT_MENU.get(), EnergyStorageUnitScreen::new);
        event.register(HpCMenuTypes.BASIC_SOLAR_MENU.get(), BasicSolarScreen::new);
        event.register(HpCMenuTypes.ROCKET_MENU.get(), RocketScreen::new);
        event.register(HpCMenuTypes.CARGO_MANAGER_MENU.get(), CargoManagerScreen::new);
        event.register(HpCMenuTypes.FUEL_MANAGER_MENU.get(), FuelManagerScreen::new);
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

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(Tier1RocketModel.LAYER_LOCATION, Tier1RocketModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onClientExtensions(RegisterClientExtensionsEvent event) {
        
        event.registerFluidType(new IClientFluidTypeExtensions() {
            private static final ResourceLocation OXYGEN_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "block/white_concrete");

            @Override
            public int getTintColor() {
                // 0x99 = 60% base opacity. 66D8FF = Airy blue color.
                return 0x9966D8FF;
            }

            @Override
            public ResourceLocation getStillTexture() {
                return OXYGEN_TEXTURE;
            }

        }, HpCFluids.OXYGEN_TYPE.get()); // <-- Tie it to your registered Oxygen Type here!


        /* Example for future gases:
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override public int getTintColor() { return 0x9900FF00; } // Toxic Green
            @Override public ResourceLocation getStillTexture() { return ResourceLocation.parse("minecraft:block/water_still"); }
        }, HpCFluids.CHLORINE_TYPE.get());
        */
    }

    @SubscribeEvent
    public static void onModelEvent(ModelEvent.RegisterAdditional event) {
        ItemProperties.register(HpCItems.CANISTER.get(),
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "fluid_type"),
                (stack, level, entity, seed) -> {
                    CanisterData data = ((CanisterItem) stack.getItem()).getCanisterData(stack);
                    if (data == null || data.isEmpty()) return 0f;
                    return data.isCrudeOil() ? 1f : 2f;
                });

        ItemProperties.register(HpCItems.CANISTER.get(),
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "fill_level"),
                (stack, level, entity, seed) -> {
                    CanisterData data = ((CanisterItem) stack.getItem()).getCanisterData(stack);
                    if (data == null || data.isEmpty()) return 0f;
                    return data.amount() / (float) CanisterItem.MAX_CAPACITY;
                });
    }
    
    @SubscribeEvent
    public static void onPlayerMountRocket(EntityMountEvent event) {
        if (event.getLevel().isClientSide()) {
            if (event.getEntityMounting() instanceof Player player && player == Minecraft.getInstance().player) {
                if (event.isMounting() && event.getEntityBeingMounted() instanceof Tier1RocketEntity) {
                    Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
                }
                else if (!event.isMounting() && event.getEntityBeingMounted() instanceof Tier1RocketEntity) {
                    Minecraft.getInstance().options.setCameraType(CameraType.FIRST_PERSON);
                }
            }
        }
    }
    
    
}
