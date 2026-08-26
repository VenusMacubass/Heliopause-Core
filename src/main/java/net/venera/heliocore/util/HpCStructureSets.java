package net.venera.heliocore.util;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.dimension.HpCStructures;

public class HpCStructureSets {

    public static final ResourceKey<StructureSet> MOON_VILLAGE_SET = ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon_village"));

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(MOON_VILLAGE_SET, new StructureSet(
                structures.getOrThrow(HpCStructures.MOON_VILLAGE),
                new RandomSpreadStructurePlacement(
                        32, // Average distance in chunks between villages
                        8,  // Minimum chunk distance (so they don't overlap)
                        RandomSpreadType.LINEAR,
                        1234567890 // A random salt number to randomize placement math
                )
        ));
    }
}