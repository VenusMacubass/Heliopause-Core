package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends BiomeTagsProvider {

    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, HeliopauseCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Biomes.LUNAR_HIGHLANDS)
                .addOptional(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon_highlands"));
        this.tag(ModTags.Biomes.LUNAR_MARIA)
                .addOptional(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon_maria"));
    }
}
