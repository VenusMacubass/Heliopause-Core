package net.venera.galacticraftcore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.fluid.ModFluids;
import net.venera.galacticraftcore.init.CrudeOilBlockReg;
import net.venera.galacticraftcore.registry.ModRegistry;
import net.venera.galacticraftcore.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModFluidTagsProvider extends FluidTagsProvider {
    public ModFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, GalacticraftCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        super.addTags(provider);
        tag(ModTags.Fluids.OIL).add(ModRegistry.CRUDE_OIL.getSource());
        tag(ModTags.Fluids.OIL).add(ModRegistry.CRUDE_OIL.getFlowing().value());



    }
}
