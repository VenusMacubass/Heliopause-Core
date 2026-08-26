package net.venera.heliocore.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.venera.heliocore.HeliopauseCore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HpCStructurePools {
    public static final ResourceKey<StructureTemplatePool> START = createKey("moon_village/start");
    public static final ResourceKey<StructureTemplatePool> PATHWAYS = createKey("moon_village/pathways");
    public static final ResourceKey<StructureTemplatePool> STREETS = createKey("moon_village/streets");
    public static final ResourceKey<StructureTemplatePool> HOUSES = createKey("moon_village/houses");
    public static final ResourceKey<StructureTemplatePool> ROAD_TURNS = createKey("moon_village/road_turns");
    public static final ResourceKey<StructureTemplatePool> CORNER_BUILDINGS = createKey("moon_village/corner_buildings");
    public static final ResourceKey<StructureTemplatePool> SOLAR_FIELDS = createKey("moon_village/solar_fields");

    private static ResourceKey<StructureTemplatePool> createKey(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, name));
    }

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        register(context, START, List.of(Pair.of(HeliopauseCore.MOD_ID + ":moon_village_center", 1)));
        
        register(context, PATHWAYS, List.of(Pair.of(HeliopauseCore.MOD_ID + ":moon_village_small_pathway", 1)));
        
        register(context, STREETS, List.of(Pair.of(HeliopauseCore.MOD_ID + ":moon_village_street", 1)));

        register(context, HOUSES, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_small_house", 1)
        ));
        
        register(context, ROAD_TURNS, List.of(Pair.of(HeliopauseCore.MOD_ID + ":moon_village_road_turn", 1)));
        
        register(context, CORNER_BUILDINGS, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_small_house", 2),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_solar_field", 1)
        ));
        
        register(context, SOLAR_FIELDS, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_solar_field", 4),
                Pair.of("minecraft:empty", 1)
        ));
    }

    private static void register(BootstrapContext<StructureTemplatePool> context, ResourceKey<StructureTemplatePool> key, List<Pair<String, Integer>> structures) {
        HolderGetter<StructureTemplatePool> templatePools = context.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> empty = templatePools.getOrThrow(Pools.EMPTY);

        List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> elements = new ArrayList<>();

        for (Pair<String, Integer> structure : structures) {
            if (structure.getFirst().equals("minecraft:empty")) {
                elements.add(Pair.of(StructurePoolElement.empty(), structure.getSecond()));
            } else {
                elements.add(Pair.of(StructurePoolElement.single(structure.getFirst()), structure.getSecond()));
            }
        }
        context.register(key, new StructureTemplatePool(empty, elements, StructureTemplatePool.Projection.RIGID));
    }
}