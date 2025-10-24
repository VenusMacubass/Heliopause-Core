package net.venera.galacticraftcore;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.block.entity.ModBlockEntities;
import net.venera.galacticraftcore.component.ModDataComponents;
import net.venera.galacticraftcore.data.ModAttachments;
import net.venera.galacticraftcore.data.radiation.RadiationHandler;
import net.venera.galacticraftcore.entity.ModEntities;
import net.venera.galacticraftcore.entity.villager.ModVillagers;
import net.venera.galacticraftcore.entity.zombie.SpaceZombieEntity;
import net.venera.galacticraftcore.fluid.ModFluids;
import net.venera.galacticraftcore.item.ModCreativeModeTabs;
import net.venera.galacticraftcore.item.ModItems;
import net.venera.galacticraftcore.recipe.ModRecipes;
import net.venera.galacticraftcore.screen.ModMenuTypes;
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

@Mod(GalacticraftCore.MOD_ID)
public class GalacticraftCore {
    public static final String MOD_ID = "galacticraftcore";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GalacticraftCore(IEventBus modEventBus, Dist modDist, ModContainer modContainer) {
        modEventBus.addListener(GalacticraftCoreClient::onClientSetup);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(GalacticraftCoreClient::onRegisterRenderers);
        modEventBus.addListener(this::onEntityCreation);

        if (modDist.isClient()) {
            modEventBus.addListener(GalacticraftCoreClient::registerBlockColors);
            modEventBus.addListener(GalacticraftCoreClient::registerItemColors);
        }

        //NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(RadiationHandler.class);


        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModFluids.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModEntities.register(modEventBus);
        ModVillagers.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
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
        event.put(ModEntities.SPACE_ZOMBIE.get(), SpaceZombieEntity.createAttributes().build());
    }
}
