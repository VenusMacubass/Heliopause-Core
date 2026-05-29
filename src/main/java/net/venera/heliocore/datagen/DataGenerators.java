package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.venera.heliocore.HeliopauseCore;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = HeliopauseCore.MOD_ID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(HpCBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));
        BlockTagsProvider blockTagsProvider = new HpCBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        FluidTagsProvider fluidTagsProvider = new HpCFluidTagsProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), fluidTagsProvider);
        generator.addProvider(event.includeServer(), new HpCItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new HpCMobTagProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new HpCRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new HpCDataMapProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeClient(), new HpCBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new HpCItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new HpCDatapackProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new HpCBiomeTagProvider(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new HpCCuriosDataProvider(packOutput, existingFileHelper, lookupProvider));

    }
}
