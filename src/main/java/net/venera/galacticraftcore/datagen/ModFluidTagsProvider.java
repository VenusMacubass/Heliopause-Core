package net.venera.galacticraftcore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.fluid.ModFluids;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModFluidTagsProvider extends FluidTagsProvider {
    public ModFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, GalacticraftCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        super.addTags(provider);
        tag(FluidTags.LAVA).add(ModFluids.CRUDE_OIL.getSource());
        tag(FluidTags.WATER).add(ModFluids.REFINED_FUEL.getSource());

    }
}
