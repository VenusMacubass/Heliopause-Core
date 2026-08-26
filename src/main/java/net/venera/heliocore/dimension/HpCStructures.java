package net.venera.heliocore.dimension;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.item.HpCTags;
import net.venera.heliocore.util.HpCStructurePools;

import java.util.Map;

public class HpCStructures {
    public static final ResourceKey<Structure> MOON_VILLAGE = ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon_village"));

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templatePools = context.lookup(Registries.TEMPLATE_POOL);

        context.register(MOON_VILLAGE, new JigsawStructure(
                new Structure.StructureSettings(
                        biomes.getOrThrow(HpCTags.Biomes.LUNAR_HIGHLANDS), // Restricts spawn to your specific biome!
                        Map.of(), // Optional: Add mob spawns here later
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.BEARD_THIN // Blends the terrain nicely around the buildings
                ),
                templatePools.getOrThrow(HpCStructurePools.START), // Starts the puzzle at your Centerpiece
                7, // Max size/depth of the village chain
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                true, // project_start_to_heightmap: Forces it to snap perfectly to the surface ground
                Heightmap.Types.WORLD_SURFACE_WG
        ));
    }
}
