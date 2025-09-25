package net.venera.galacticraftcore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.item.ModItems;
import net.venera.galacticraftcore.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, GalacticraftCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.CANISTER)
                .add(ModItems.COPPER_CANISTER.get())
                .add(ModItems.TIN_CANISTER.get());

        tag(ItemTags.SWORDS).add(ModItems.STEEL_SWORD.get());
        tag(ItemTags.AXES).add(ModItems.STEEL_AXE.get());
        tag(ItemTags.PICKAXES).add(ModItems.STEEL_PICKAXE.get());
        tag(ItemTags.SHOVELS).add(ModItems.STEEL_SHOVEL.get());
        tag(ItemTags.HOES).add(ModItems.STEEL_HOE.get());
    }
}
