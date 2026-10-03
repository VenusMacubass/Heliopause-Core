package net.venera.heliocore.entity.villager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.dimension.biome.HpCBiomes;

import java.util.Map;

public class HpCVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, HeliopauseCore.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, HeliopauseCore.MOD_ID);
    public static final DeferredRegister<VillagerType> VILLAGER_TYPES = DeferredRegister.create(BuiltInRegistries.VILLAGER_TYPE, HeliopauseCore.MOD_ID);
    
    public static final Holder<PoiType> WHITESMITH_POI = POI_TYPES.register("whitesmith_poi", 
            () -> new PoiType(ImmutableSet.copyOf(HpCBlocks.COAL_COMPRESSOR.get().getStateDefinition().getPossibleStates()), 1,1));

    public static final Holder<VillagerProfession> WHITESMITH_VILLAGER = VILLAGER_PROFESSIONS.register("whitesmith",
            () -> new VillagerProfession("whitesmith",
                    holder -> holder.value() == WHITESMITH_POI.value(), poiTypeHolder -> poiTypeHolder.value() == WHITESMITH_POI.value(),
                   ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER)); //Placeholder for custom professions

    public static final DeferredHolder<VillagerType, VillagerType> LUNAR_HIGHLANDS =
            VILLAGER_TYPES.register("lunar_highlands", () -> new VillagerType("lunar_highlands"));

    public static final DeferredHolder<VillagerType, VillagerType> LUNAR_MARIA =
            VILLAGER_TYPES.register("lunar_maria", () -> new VillagerType("lunar_maria"));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
        VILLAGER_TYPES.register(eventBus);

        eventBus.addListener((FMLCommonSetupEvent event) -> {
            event.enqueueWork(() -> {
                Map<ResourceKey<Biome>, VillagerType> byBiome = ObfuscationReflectionHelper.getPrivateValue(
                        VillagerType.class, null, "BY_BIOME"
                );
                if (byBiome != null) {
                    byBiome.put(HpCBiomes.LUNAR_HIGHLANDS, LUNAR_HIGHLANDS.get());
                    byBiome.put(HpCBiomes.LUNAR_MARIA, LUNAR_MARIA.get());
                }
            });
        });
    }
}
