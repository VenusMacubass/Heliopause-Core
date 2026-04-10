package net.venera.galacticraftcore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends BiomeTagsProvider {

    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, GalacticraftCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Biomes.IS_MOON)
                .addOptional(ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "moon_plains"));
    }
}
