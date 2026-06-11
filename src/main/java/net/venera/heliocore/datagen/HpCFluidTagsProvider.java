package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.util.HpCTags;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

public class HpCFluidTagsProvider extends FluidTagsProvider {
    public HpCFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, HeliopauseCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        super.addTags(provider);
        tag(FluidTags.LAVA).add(HpCFluids.CRUDE_OIL.getSource());
        tag(FluidTags.WATER).add(HpCFluids.REFINED_FUEL.getSource());
        tag(HpCTags.Fluids.GASES).add(HpCFluids.OXYGEN.get());
    }
}
