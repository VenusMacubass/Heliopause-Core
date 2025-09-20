package net.venera.galacticraftcore.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, GalacticraftCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.IRIDIUM_INGOT.get());
        basicItem(ModItems.ALUMINIUM_INGOT.get());
        basicItem(ModItems.COPPER_CANISTER.get());
        basicItem(ModItems.TIN_CANISTER.get());
        basicItem(ModItems.DEHYDRATED_APPLE.get());
        basicItem(ModItems.DEHYDRATED_CARROT.get());
        basicItem(ModItems.DEHYDRATED_POTATO.get());
        basicItem(ModItems.DEHYDRATED_MELON.get());
        basicItem(ModItems.RADIOACTIVE_CORE.get());
        handheldItem(ModItems.STANDARD_WRENCH.get());
    }
}
