package net.venera.galacticraftcore.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;
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

        basicItem(ModItems.RAW_ALUMINIUM.get());
        basicItem(ModItems.RAW_TIN.get());
        basicItem(ModItems.RAW_SILICON.get());
        basicItem(ModItems.TIN_INGOT.get());

        basicItem(ModItems.COMPRESSED_ALUMINIUM.get());
        basicItem(ModItems.COMPRESSED_BRONZE.get());
        basicItem(ModItems.COMPRESSED_COPPER.get());
        basicItem(ModItems.COMPRESSED_IRIDIUM.get());
        basicItem(ModItems.COMPRESSED_IRON.get());
        basicItem(ModItems.COMPRESSED_STEEL.get());
        basicItem(ModItems.COMPRESSED_TIN.get());

        basicItem(ModItems.SENSOR_LENS.get());

        wallItem(ModBlocks.TIN_BUILDING_WALL, ModBlocks.TIN_BUILDING_BLOCK);
        wallItem(ModBlocks.MOON_ROCK_WALL, ModBlocks.MOON_ROCK);
        wallItem(ModBlocks.MOON_DUNGEON_BRICK_WALL, ModBlocks.MOON_DUNGEON_BRICKS);
        wallItem(ModBlocks.MARS_COBBLESTONE_WALL, ModBlocks.MARS_COBBLESTONE);
        wallItem(ModBlocks.MARS_DUNGEON_BRICK_WALL, ModBlocks.MARS_DUNGEON_BRICKS);

        handheldItem(ModItems.STEEL_SWORD.get());
        handheldItem(ModItems.STEEL_AXE.get());
        handheldItem(ModItems.STEEL_PICKAXE.get());
        handheldItem(ModItems.STEEL_SHOVEL.get());
        handheldItem(ModItems.STEEL_HOE.get());
    }

    public void wallItem(DeferredBlock<?> block, DeferredBlock<?> baseBlock){
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "block/" + baseBlock.getId().getPath()));
    }
}
