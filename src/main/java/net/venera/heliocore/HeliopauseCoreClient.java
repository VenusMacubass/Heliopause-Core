package net.venera.heliocore;

import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.network.PacketDistributor;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import net.venera.heliocore.controls.HpCKeybinds;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.data.atmospherics.AtmosphericProperty;
import net.venera.heliocore.data.atmospherics.OxygenVolumeHelper;
import net.venera.heliocore.data.atmospherics.SpaceGearSetupController;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.data.component.HpCDataComponents;
import net.venera.heliocore.entity.HpCEntities;
import net.venera.heliocore.entity.client.*;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;
import net.venera.heliocore.entity.rideable.Tier1RocketLanderEntity;
import net.venera.heliocore.entity.zombie.SpaceZombieRenderer;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.item.HpCTags;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import net.venera.heliocore.item.hpc_custom.GasTankItem;
import net.venera.heliocore.render.FluidTankRenderer;
import net.venera.heliocore.render.MagneticAssemblyPlatformRenderer;
import net.venera.heliocore.render.MagneticCraftingTableRenderer;
import net.venera.heliocore.render.sky.MoonSkyRenderer;
import net.venera.heliocore.screen.HpCMenuTypes;
import net.venera.heliocore.screen.block_entity.*;
import net.venera.heliocore.screen.entity.HpCEquipmentScreen;
import net.venera.heliocore.screen.entity.LanderScreen;
import net.venera.heliocore.screen.entity.RocketScreen;
import net.venera.heliocore.screen.hud.HazardWarningHudOverlay;
import net.venera.heliocore.screen.hud.LanderHudOverlay;
import net.venera.heliocore.screen.hud.SpaceSuitHudOverlay;
import net.venera.heliocore.util.*;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = HeliopauseCore.MOD_ID, value = Dist.CLIENT)
public class HeliopauseCoreClient {

    @SubscribeEvent
    static void onClientSetup(final FMLClientSetupEvent event) {
        HeliopauseCore.LOGGER.info("HELLO FROM CLIENT SETUP");
        HeliopauseCore.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        ItemBlockRenderTypes.setRenderLayer(HpCBlocks.FLUID_TANK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(HpCBlocks.FLUID_PIPE.get(), RenderType.translucent());
        EntityRenderers.register(HpCEntities.TIER_1_ROCKET.get(), Tier1RocketRenderer::new);
        EntityRenderers.register(HpCEntities.TIER_1_ROCKET_LANDER.get(), Tier1RocketLanderRenderer::new);
        

        event.enqueueWork(() -> ItemProperties.register(HpCItems.CANISTER.get(),
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "fill_level"),
                (stack, level, entity, seed) -> {
                    CanisterData data = ((CanisterItem) stack.getItem()).getCanisterData(stack);
                    if (data == null || data.isEmpty()) return 0f;
                    return data.amount() / (float) CanisterItem.MAX_CAPACITY;
                }));
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) { //Block tinter
        event.register((state, getter, pos, tintIndex) -> {
            if (getter != null && pos != null) {
                FluidState fluidState = getter.getFluidState(pos);
                return IClientFluidTypeExtensions.of(fluidState).getTintColor(fluidState, getter, pos);
            } else return 0xFFFFFFFF;
        }, HpCFluids.CRUDE_OIL.getFluidblock(), HpCFluids.REFINED_FUEL.getFluidblock(), HpCFluids.LIQUID_OXYGEN.getFluidblock());

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
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) { //Item tinter
        for (var itemObject : HpCItems.ITEMS.getEntries()) {
            event.register((stack, tintIndex) -> {
                if (tintIndex != 1) return 0xFFFFFFFF;
                return FluidUtil.getFluidContained(stack)
                        .map(fluidStack -> IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack))
                        .orElse(0xFFFFFFFF);
            }, itemObject.get());
        }

        event.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                CanisterData data = stack.getOrDefault(HpCDataComponents.CANISTER_COMPONENT.get(), new CanisterData(null, 0));

                if (data != null && !data.isEmpty() && data.fluidId() != null) {
                    String fluidPath = data.fluidId().getPath();
                    return switch (fluidPath) {
                        case "crude_oil" -> 0xFF1f1f1f;     //Very Dark Grey/Black
                        case "refined_fuel" -> 0xFFC9B000;  //Yellow
                        case "oxygen_liquid" -> 0xFF0C38C7; //Cyan/Blue
                        default -> -1;                      //Fallback White (-1 == 0xFFFFFFFF)
                    };
                }
            }
            return -1;

        }, HpCItems.CANISTER.get());
        
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
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) { //Renderers for entities
        event.registerEntityRenderer(HpCEntities.SPACE_ZOMBIE.get(), SpaceZombieRenderer::new);
        event.registerBlockEntityRenderer(HpCBlockEntities.FLUID_TANK_ENTITY.get(), FluidTankRenderer::new);
        event.registerBlockEntityRenderer(HpCBlockEntities.MAGNETIC_ASSEMBLY_PLATFORM_ENTITY.get(), MagneticAssemblyPlatformRenderer::new);
        event.registerBlockEntityRenderer(HpCBlockEntities.MAGNETIC_CRAFTING_TABLE_ENTITY.get(), MagneticCraftingTableRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) { //Screen Registry
        event.register(HpCMenuTypes.COAL_COMPRESSOR_MENU.get(), CoalCompressorScreen::new);
        event.register(HpCMenuTypes.REFINERY_MENU.get(), RefineryScreen::new);
        event.register(HpCMenuTypes.ENERGY_STORAGE_UNIT_MENU.get(), EnergyStorageUnitScreen::new);
        event.register(HpCMenuTypes.BASIC_SOLAR_MENU.get(), BasicSolarScreen::new);
        event.register(HpCMenuTypes.CARGO_MANAGER_MENU.get(), CargoManagerScreen::new);
        event.register(HpCMenuTypes.FUEL_MANAGER_MENU.get(), FuelManagerScreen::new);
        event.register(HpCMenuTypes.OXYGEN_GENERATOR_MENU.get(), OxygenGeneratorScreen::new);
        event.register(HpCMenuTypes.GAS_COMPRESSOR_MENU.get(), GasCompressorScreen::new);
        event.register(HpCMenuTypes.VAPORIZER_MENU.get(), VaporizerScreen::new);
        event.register(HpCMenuTypes.ENERGY_GENERATOR_MENU.get(), EnergyGeneratorScreen::new);
        event.register(HpCMenuTypes.OXYGEN_SEALER_MENU.get(), OxygenSealerScreen::new);
        event.register(HpCMenuTypes.PCB_FABRICATOR_MENU.get(), PCBFabricatorScreen::new);
        event.register(HpCMenuTypes.MAGNETIC_ASSEMBLY_PLATFORM_MENU.get(), MagneticAssemblyPlatformScreen::new);
        event.register(HpCMenuTypes.ROCKET_MENU.get(), RocketScreen::new);
        event.register(HpCMenuTypes.LANDER_MENU.get(), LanderScreen::new);
        event.register(HpCMenuTypes.EQUIPMENT_MENU.get(), HpCEquipmentScreen::new);
    }

    private static Button creativeGearButton;
    private static Button survivalGearButton;
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            int x = screen.getGuiLeft() + 126;
            int y = screen.getGuiTop() + 61;

            survivalGearButton = Button.builder(Component.literal("Gear"), button -> {
                PacketDistributor.sendToServer(new OpenEquipmentPayload());
            }).bounds(x, y, 40, 20).build();

            event.addListener(survivalGearButton);
        }
        else if (event.getScreen() instanceof CreativeModeInventoryScreen creativeScreen) {
            int x = creativeScreen.getGuiLeft() + 126;
            int y = creativeScreen.getGuiTop() + 20;

            creativeGearButton = Button.builder(Component.literal("Gear"), button -> {
                PacketDistributor.sendToServer(new OpenEquipmentPayload());
            }).bounds(x, y, 40, 20).build();

            event.addListener(creativeGearButton);
        }
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Pre event) {
        if (event.getScreen() instanceof InventoryScreen screen && survivalGearButton != null) {
            survivalGearButton.setX(screen.getGuiLeft() + 126);
            survivalGearButton.setY(screen.getGuiTop() + 61);
        }
        else if (event.getScreen() instanceof CreativeModeInventoryScreen creativeScreen && creativeGearButton != null) {
            boolean isInventoryTab = creativeScreen.isInventoryOpen();
            creativeGearButton.visible = isInventoryTab;
            creativeGearButton.setX(creativeScreen.getGuiLeft() + 126);
            creativeGearButton.setY(creativeScreen.getGuiTop() + 20);
        }
    }
    
    @SubscribeEvent
    public static void onRenderGuiLayer(RenderGuiLayerEvent.Pre event) { //To disable the jump meter on rideable entities
        if (event.getName().equals(VanillaGuiLayers.JUMP_METER)) {
            Player player = Minecraft.getInstance().player;
            
            if (player != null && (player.getVehicle() instanceof Tier1RocketEntity || player.getVehicle() instanceof Tier1RocketLanderEntity)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        DimensionSpecialEffects moonEffects = new DimensionSpecialEffects(Float.NaN, true, DimensionSpecialEffects.SkyType.NORMAL, false, false) {

            @Nullable
            @Override
            public float[] getSunriseColor(float timeOfDay, float partialTicks) {
                return null;
            }
            
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
        event.registerLayerDefinition(Tier1RocketModel.ROCKET_LOCATION, Tier1RocketModel::createBodyLayer);
        event.registerLayerDefinition(Tier1RocketLanderModel.LANDER_LOCATION, Tier1RocketLanderModel::createBodyLayer);
        event.registerLayerDefinition(CatOxygenGear.LAYER_LOCATION, CatOxygenGear::createBodyLayer);
        event.registerLayerDefinition(WolfOxygenGear.LAYER_LOCATION, WolfOxygenGear::createBodyLayer);
        event.registerLayerDefinition(HumanoidOxygenGear.LAYER_LOCATION, HumanoidOxygenGear::createBodyLayer);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        var rawCatRenderer = event.getRenderer(EntityType.CAT);
        if (rawCatRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {

            @SuppressWarnings("unchecked")
            LivingEntityRenderer<Cat, CatModel<Cat>> catRenderer = (LivingEntityRenderer<Cat, CatModel<Cat>>) livingRenderer;
            catRenderer.addLayer(new CatOxygenGearLayer(catRenderer, event.getEntityModels()));
        }
        
        var rawWolfRenderer = event.getRenderer(EntityType.WOLF);
        if (rawWolfRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {
            @SuppressWarnings("unchecked")
            LivingEntityRenderer<Wolf, WolfModel<Wolf>> wolfRenderer = (LivingEntityRenderer<Wolf, WolfModel<Wolf>>) livingRenderer;
            wolfRenderer.addLayer(new WolfOxygenGearLayer(wolfRenderer, event.getEntityModels()));
        }

        PlayerRenderer defaultPlayer = event.getSkin(PlayerSkin.Model.WIDE);
        if (defaultPlayer != null) {
            defaultPlayer.addLayer(new HumanoidOxygenGearLayer<>(defaultPlayer, event.getEntityModels()));
        }
        
        PlayerRenderer slimPlayer = event.getSkin(PlayerSkin.Model.SLIM);
        if (slimPlayer != null) {
            slimPlayer.addLayer(new HumanoidOxygenGearLayer<>(slimPlayer, event.getEntityModels()));
        }

        var rawZombieRenderer = event.getRenderer(EntityType.ZOMBIE);
        if (rawZombieRenderer instanceof LivingEntityRenderer<?, ?> livingRenderer) {
            @SuppressWarnings("unchecked")
            LivingEntityRenderer<Zombie, ZombieModel<Zombie>> zombieRenderer = (LivingEntityRenderer<Zombie, ZombieModel<Zombie>>) livingRenderer;
            zombieRenderer.addLayer(new HumanoidOxygenGearLayer<>(zombieRenderer, event.getEntityModels()));
        }
        
    }

    @SubscribeEvent
    public static void onClientExtensions(RegisterClientExtensionsEvent event) {
        
        event.registerFluidType(new IClientFluidTypeExtensions() {
            private static final ResourceLocation OXYGEN_TEXTURE = ResourceLocation.fromNamespaceAndPath("minecraft", "block/white_concrete");

            @Override
            public int getTintColor() {
                //0x99 = 60% base opacity. 66D8FF = Airy blue color.
                return 0x9966D8FF;
            }

            @Override
            public ResourceLocation getStillTexture() {
                return OXYGEN_TEXTURE;
            }

        }, HpCFluids.OXYGEN_TYPE.get()); 


        /* For future gases:
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override public int getTintColor() { return 0x9900FF00; } // Toxic Green
            @Override public ResourceLocation getStillTexture() { return ResourceLocation.parse("minecraft:block/water_still"); }
        }, HpCFluids.CHLORINE_TYPE.get());
        */
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
    
    private static boolean wasJumping = false;
    private static boolean wasOnGround = true;
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;

        boolean isJumping = mc.options.keyJump.isDown();
        boolean onGround = player.onGround(); 

        if (player.getVehicle() instanceof Tier1RocketLanderEntity lander) {
            lander.isThrusting = isJumping;
            if (isJumping != wasJumping) {
                PacketDistributor.sendToServer(new LanderControlPayload(isJumping));
            }
        } else {
            if (isJumping && !wasJumping && !onGround && !wasOnGround && !player.isFallFlying() && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION)) {
                var inventory = player.getData(HpCAttachments.EQUIPMENT_INVENTORY);
                ItemStack stack = inventory.getStackInSlot(8);

                if (!stack.isEmpty() && stack.canElytraFly(player)) {
                    PacketDistributor.sendToServer(new ElytraSlotPayload());
                }
            }
        }
        wasJumping = isJumping;
        wasOnGround = onGround;

        if (player.tickCount % 20 != 0) return;
        if (mc.level == null || mc.gameMode == null) return;

        long headPos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ()).asLong();
        boolean isSealed = OxygenVolumeHelper.isPositionSealed(headPos);

        var radData = player.getData(HpCAttachments.RADIATION_DATA);
        SpaceSuitHudOverlay.radiationAmount = radData.getRadiation();

        int pressure = AtmosphericProperty.getDimensionalPressure(mc.level);
        if (pressure < 228 && isSealed) pressure = 760;
        SpaceSuitHudOverlay.pressureAmount = pressure;
        
        boolean isCreative = mc.gameMode.getPlayerMode().isCreative() || player.isSpectator();

        if (isCreative) {
            HazardWarningHudOverlay.showRadiationWarning = false;
            HazardWarningHudOverlay.showPressureWarning = false;
            HazardWarningHudOverlay.showOxygenWarning = false;
        } else {
            HazardWarningHudOverlay.showRadiationWarning = radData.getRadiation() > 200;

            boolean pressureProtected = true;
            if (pressure > 7600) {
                pressureProtected = SpaceGearSetupController.checkBaricSetup(player, 2);
            } else if (pressure > 2280 || pressure < 228) {
                pressureProtected = SpaceGearSetupController.checkBaricSetup(player, 1) ||
                        SpaceGearSetupController.checkBaricSetup(player, 2);
            }

            HazardWarningHudOverlay.showPressureWarning = !pressureProtected && !player.getType().is(HpCTags.Entities.DOES_NOT_BREATHE);

            boolean inVacuum = OxygenVolumeHelper.isVacuumDimension(mc.level) && !isSealed;
            HazardWarningHudOverlay.showOxygenWarning = inVacuum
                    && !SpaceGearSetupController.hasSufficientOxygenClientSafe(player)
                    && !player.getType().is(HpCTags.Entities.DOES_NOT_BREATHE);
        }
        
        var inventory = player.getData(HpCAttachments.EQUIPMENT_INVENTORY);

        ItemStack tank1 = inventory.getStackInSlot(2);
        if (!tank1.isEmpty() && tank1.getItem() instanceof GasTankItem gasTankItem1) {
            var data1 = gasTankItem1.getGasTankData(tank1);
            if (data1 != null && data1.isOxygen()) {
                SpaceSuitHudOverlay.oxygenAmount1 = data1.amount();
                SpaceSuitHudOverlay.oxygenCapacity1 = data1.getCapacity();
            } else {
                SpaceSuitHudOverlay.oxygenAmount1 = 0;
            }
        } else {
            SpaceSuitHudOverlay.oxygenAmount1 = 0;
            SpaceSuitHudOverlay.oxygenCapacity1 = 1; 
        }
        ItemStack tank2 = inventory.getStackInSlot(3);
        if (!tank2.isEmpty() && tank2.getItem() instanceof GasTankItem gasTankItem2) {
            var data2 = gasTankItem2.getGasTankData(tank2);
            if (data2 != null && data2.isOxygen()) {
                SpaceSuitHudOverlay.oxygenAmount2 = data2.amount();
                SpaceSuitHudOverlay.oxygenCapacity2 = data2.getCapacity();
            } else {
                SpaceSuitHudOverlay.oxygenAmount2 = 0;
            }
        } else {
            SpaceSuitHudOverlay.oxygenAmount2 = 0;
            SpaceSuitHudOverlay.oxygenCapacity2 = 1;
        }
    }

    private static float currentZoom = 1.0F;
    @SubscribeEvent
    public static void onComputeFov(ComputeFovModifierEvent event) {
        float targetZoom = HpCKeybinds.ZOOM_KEY.isDown() ? 0.1F : 1.0F; //lower value more zoom
        currentZoom = Mth.lerp(0.6F, currentZoom, targetZoom); //delta is zoom speed
        event.setNewFovModifier(event.getNewFovModifier() * currentZoom);
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "lander_hud"),
                LanderHudOverlay.INSTANCE
        );
        event.registerAboveAll(
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "warning_hud"),
                HazardWarningHudOverlay.INSTANCE
        );
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "space_suit_hud"),
                SpaceSuitHudOverlay.INSTANCE
        );
    }
}
