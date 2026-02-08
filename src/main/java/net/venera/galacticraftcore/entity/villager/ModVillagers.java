package net.venera.galacticraftcore.entity.villager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, GalacticraftCore.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, GalacticraftCore.MOD_ID);

    public static final Holder<PoiType> MOON_POI = POI_TYPES.register("moon_poi", //There can be only 1 block for each chosen block
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.COAL_COMPRESSOR.get().getStateDefinition().getPossibleStates()), 1,1));

    public static final Holder<VillagerProfession> MOON_VILLAGER = VILLAGER_PROFESSIONS.register("moon_villager",
            () -> new VillagerProfession("moon_villager",
                    holder -> holder.value() == MOON_POI.value(), poiTypeHolder -> poiTypeHolder.value() == MOON_POI.value(),
                   ImmutableSet.of(), ImmutableSet.of(), null));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
