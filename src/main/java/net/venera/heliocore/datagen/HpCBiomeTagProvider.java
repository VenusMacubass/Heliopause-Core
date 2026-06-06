package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.util.HpCTags;

import java.util.concurrent.CompletableFuture;

public class HpCBiomeTagProvider extends BiomeTagsProvider {

    public HpCBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, HeliopauseCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(HpCTags.Biomes.HAS_ABUNDANT_OIL)
                .addTag(BiomeTags.IS_OCEAN)
                .addTag(BiomeTags.HAS_SWAMP_HUT)
                .addTag(BiomeTags.HAS_DESERT_PYRAMID);       
        
        this.tag(HpCTags.Biomes.LUNAR_HIGHLANDS)
                .addOptional(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon_highlands"));
        this.tag(HpCTags.Biomes.LUNAR_MARIA)
                .addOptional(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon_maria"));
    }
}
