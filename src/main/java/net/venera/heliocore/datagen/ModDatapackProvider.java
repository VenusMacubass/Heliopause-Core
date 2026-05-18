package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.dimension.ModDimensions;
import net.venera.heliocore.dimension.biome.ModBiomes;
import net.venera.heliocore.dimension.worldgen.ModBiomeModifiers;
import net.venera.heliocore.dimension.worldgen.ModConfiguredFeatures;
import net.venera.heliocore.dimension.worldgen.ModNoiseSettings;
import net.venera.heliocore.dimension.worldgen.ModPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
            .add(Registries.BIOME, ModBiomes::bootstrap)
            .add(Registries.DIMENSION_TYPE, ModDimensions::typeBootstrap)
            .add(Registries.NOISE_SETTINGS, ModNoiseSettings::bootstrap)
            .add(Registries.LEVEL_STEM, ModDimensions::bootstrap);
    
    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(HeliopauseCore.MOD_ID));
    }
}
