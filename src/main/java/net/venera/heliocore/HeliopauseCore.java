package net.venera.heliocore;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import net.venera.heliocore.data.component.HpCDataComponents;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.data.energy.GridManager;
import net.venera.heliocore.data.radiation.RadiationHandler;
import net.venera.heliocore.dimension.worldgen.feature.HpCFeatures;
import net.venera.heliocore.entity.HpCEntities;
import net.venera.heliocore.entity.villager.HpCVillagers;
import net.venera.heliocore.entity.zombie.SpaceZombieEntity;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.item.HpCCreativeModeTabs;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.recipe.HpCRecipes;
import net.venera.heliocore.screen.HpCMenuTypes;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(HeliopauseCore.MOD_ID)
public class HeliopauseCore {
    public static final String MOD_ID = "heliocore";
    public static final Logger LOGGER = LogUtils.getLogger();

    public HeliopauseCore(IEventBus modEventBus, Dist modDist, ModContainer modContainer) {
        modEventBus.addListener(HeliopauseCoreClient::onClientSetup);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(HeliopauseCoreClient::onRegisterRenderers);
        modEventBus.addListener(this::onEntityCreation);
        NeoForge.EVENT_BUS.addListener(this::onServerTick);

        if (modDist.isClient()) {
            modEventBus.addListener(HeliopauseCoreClient::registerBlockColors);
            modEventBus.addListener(HeliopauseCoreClient::registerItemColors);
        }

        //NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(RadiationHandler.class);


        HpCCreativeModeTabs.register(modEventBus);
        HpCItems.register(modEventBus);
        HpCBlocks.register(modEventBus);
        HpCFluids.register(modEventBus);
        HpCDataComponents.register(modEventBus);
        HpCBlockEntities.register(modEventBus);
        HpCMenuTypes.register(modEventBus);
        HpCRecipes.register(modEventBus);
        HpCAttachments.register(modEventBus);
        HpCEntities.register(modEventBus);
        HpCVillagers.register(modEventBus);
        HpCFeatures.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, HeliopauseConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP"); 

//        if (HeliopauseConfig.LOG_DIRT_BLOCK.getAsBoolean()) {
//            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
//        }
//
//        LOGGER.info("{}{}", HeliopauseConfig.MAGIC_NUMBER_INTRODUCTION.get(), HeliopauseConfig.MAGIC_NUMBER.getAsInt());
//
//        HeliopauseConfig.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
        //event.accept(ModBlocks.SPACE_GLASS);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts

    }

    @SubscribeEvent
    public void onEntityCreation(EntityAttributeCreationEvent event) {
        event.put(HpCEntities.SPACE_ZOMBIE.get(), SpaceZombieEntity.createAttributes().build());
    }

    private void onServerTick(ServerTickEvent.Post event) { 
        for (ServerLevel level : event.getServer().getAllLevels()) {
            GridManager.get(level).tickAll(level);
        }
    }
}
