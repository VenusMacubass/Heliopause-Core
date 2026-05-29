package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.dimension.HpCDimensions;
import net.venera.heliocore.dimension.biome.HpCBiomes;
import net.venera.heliocore.dimension.worldgen.HpCBiomeModifiers;
import net.venera.heliocore.dimension.worldgen.HpCConfiguredFeatures;
import net.venera.heliocore.dimension.worldgen.HpCNoiseSettings;
import net.venera.heliocore.dimension.worldgen.HpCPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class HpCDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, HpCConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, HpCPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, HpCBiomeModifiers::bootstrap)
            .add(Registries.BIOME, HpCBiomes::bootstrap)
            .add(Registries.DIMENSION_TYPE, HpCDimensions::typeBootstrap)
            .add(Registries.NOISE_SETTINGS, HpCNoiseSettings::bootstrap)
            .add(Registries.LEVEL_STEM, HpCDimensions::bootstrap);
    
    public HpCDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(HeliopauseCore.MOD_ID));
    }
}
