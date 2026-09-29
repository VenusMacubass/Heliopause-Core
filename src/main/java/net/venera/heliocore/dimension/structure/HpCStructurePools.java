package net.venera.heliocore.dimension.structure;

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
    public static final ResourceKey<StructureTemplatePool> CENTER = createKey("moon_village/center");
    public static final ResourceKey<StructureTemplatePool> PATHWAYS = createKey("moon_village/pathways");
    public static final ResourceKey<StructureTemplatePool> STREETS = createKey("moon_village/streets");
    public static final ResourceKey<StructureTemplatePool> STREET_TERMINATORS = createKey("moon_village/street_terminators");
    public static final ResourceKey<StructureTemplatePool> HOUSES = createKey("moon_village/houses");
    public static final ResourceKey<StructureTemplatePool> ROAD_TURNS = createKey("moon_village/road_turns");
    public static final ResourceKey<StructureTemplatePool> CORNER_BUILDINGS = createKey("moon_village/corner_buildings");
    public static final ResourceKey<StructureTemplatePool> SOLAR_FIELDS = createKey("moon_village/solar_fields");
    public static final ResourceKey<StructureTemplatePool> FARMLANDS = createKey("moon_village/farmlands");
    public static final ResourceKey<StructureTemplatePool> PADDOCKS = createKey("moon_village/paddocks");

    public static final ResourceKey<StructureTemplatePool> BEDS = createKey("moon_village/beds");
    public static final ResourceKey<StructureTemplatePool> BLACKSMITH_JOB_BLOCKS = createKey("moon_village/blacksmith_job_blocks");

    private static ResourceKey<StructureTemplatePool> createKey(String name) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, name));
    }

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        register(context, START, Pools.EMPTY, List.of(Pair.of(HeliopauseCore.MOD_ID + ":moon_village_anchor", 1)));
        register(context, CENTER, Pools.EMPTY, List.of(Pair.of(HeliopauseCore.MOD_ID + ":moon_village_center", 1)));

        register(context, PATHWAYS, Pools.EMPTY, List.of(Pair.of(HeliopauseCore.MOD_ID + ":moon_village_small_pathway", 1)));

        // Added terminator pool
        register(context, STREET_TERMINATORS, Pools.EMPTY, List.of(
                Pair.of("minecraft:empty", 1)
        ));

        // Streets and road turns now use STREET_TERMINATORS as their fallback
        register(context, STREETS, STREET_TERMINATORS, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_street", 2),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_street_long", 2)
        ));

        register(context, HOUSES, Pools.EMPTY, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_blacksmith_house", 4),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_whitesmith_house", 4),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_square", 2),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_librarian_house", 5),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_butcher_house", 3),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_depot_house", 2),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_farmer_house", 6)
        ));

        register(context, ROAD_TURNS, STREET_TERMINATORS, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_road_turn", 2),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_junction", 1)
        ));

        register(context, CORNER_BUILDINGS, Pools.EMPTY, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_farmer_house", 1),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_blacksmith_house", 1),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_whitesmith_house", 1),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_solar_field", 1)
        ));

        register(context, SOLAR_FIELDS, Pools.EMPTY, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_solar_field", 1),
                Pair.of("minecraft:empty", 3)
        ));

        register(context, BEDS, Pools.EMPTY, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":bed_white", 1),
                Pair.of(HeliopauseCore.MOD_ID + ":bed_black", 1)
        ));

        register(context, BLACKSMITH_JOB_BLOCKS, Pools.EMPTY, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":smithing_table", 1),
                Pair.of(HeliopauseCore.MOD_ID + ":blast_furnace", 1),
                Pair.of(HeliopauseCore.MOD_ID + ":grindstone", 1)
        ));

        register(context, FARMLANDS, Pools.EMPTY, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_farmland_1", 2),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_farmland_2", 2),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_farmland_3", 1)
        ));
        register(context, PADDOCKS, Pools.EMPTY, List.of(
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_paddock_1", 1),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_paddock_2", 1),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_paddock_3", 1),
                Pair.of(HeliopauseCore.MOD_ID + ":moon_village_paddock_4", 1)
        ));
    }

    // Signature updated to accept fallbackPool
    private static void register(BootstrapContext<StructureTemplatePool> context, ResourceKey<StructureTemplatePool> key, ResourceKey<StructureTemplatePool> fallbackPool, List<Pair<String, Integer>> structures) {
        HolderGetter<StructureTemplatePool> templatePools = context.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> fallback = templatePools.getOrThrow(fallbackPool);

        List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> elements = new ArrayList<>();

        for (Pair<String, Integer> structure : structures) {
            if (structure.getFirst().equals("minecraft:empty")) {
                elements.add(Pair.of(StructurePoolElement.empty(), structure.getSecond()));
            } else {
                elements.add(Pair.of(StructurePoolElement.single(structure.getFirst()), structure.getSecond()));
            }
        }
        context.register(key, new StructureTemplatePool(fallback, elements, StructureTemplatePool.Projection.RIGID));
    }
}