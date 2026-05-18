package net.venera.heliocore.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.ModBlocks;
import net.venera.heliocore.fluid.ModFluids;
import net.venera.heliocore.item.ModItems;
import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    ResourceLocation dungeonBrickTex = modLoc("block/dungeon_bricks");
    ResourceLocation buildingBlockTex = modLoc("block/tin_building_block");
    ResourceLocation moonRockTex = modLoc("block/moon_rock");

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HeliopauseCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.COPPER_CANISTER.get());
        basicItem(ModItems.TIN_CANISTER.get());
        basicItem(ModItems.DEHYDRATED_APPLE.get());
        basicItem(ModItems.DEHYDRATED_GLOW_BERRIES.get());
        basicItem(ModItems.DEHYDRATED_POTATO.get());
        basicItem(ModItems.DEHYDRATED_BERRIES.get());
        basicItem(ModItems.DEHYDRATED_BEEF.get());
        basicItem(ModItems.DEHYDRATED_RABBIT.get());
        basicItem(ModItems.DEHYDRATED_CHICKEN.get());
        basicItem(ModItems.DEHYDRATED_COD.get());
        basicItem(ModItems.CHEESE_SLICE.get());
        basicItem(ModItems.HAMBURGER.get());

        basicItem(ModFluids.CRUDE_OIL.getBucket());
        basicItem(ModFluids.REFINED_FUEL.getBucket());

        basicItem(ModItems.RADIOACTIVE_CORE.get());
        handheldItem(ModItems.STANDARD_WRENCH.get());

        basicItem(ModItems.RAW_ALUMINIUM.get());
        basicItem(ModItems.RAW_TIN.get());
        basicItem(ModItems.RAW_SILICON.get());
        
        basicItem(ModItems.COMPRESSED_BRONZE.get());
        basicItem(ModItems.COMPRESSED_COPPER.get());
        basicItem(ModItems.COMPRESSED_IRON.get());
        basicItem(ModItems.COMPRESSED_STEEL.get());
        basicItem(ModItems.COMPRESSED_HD_PLATE.get());
        
        basicItem(ModItems.SMALL_BATTERY.get());

       wallItem(ModBlocks.TIN_BUILDING_WALL, buildingBlockTex);
       wallItem(ModBlocks.MOON_ROCK_WALL, moonRockTex);
       wallItem(ModBlocks.MOON_DUNGEON_BRICK_WALL, dungeonBrickTex);
        

        handheldItem(ModItems.STEEL_SWORD.get());
        handheldItem(ModItems.STEEL_AXE.get());
        handheldItem(ModItems.STEEL_PICKAXE.get());
        handheldItem(ModItems.STEEL_SHOVEL.get());
        handheldItem(ModItems.STEEL_HOE.get());

        trimmedArmorItem(ModItems.STEEL_HELMET);
        trimmedArmorItem(ModItems.STEEL_CHESTPLATE);
        trimmedArmorItem(ModItems.STEEL_LEGGINGS);
        trimmedArmorItem(ModItems.STEEL_BOOTS);

        handheldItem(ModItems.GLASS_SWORD.get());

    }

    private void trimmedArmorItem(DeferredItem<ArmorItem> itemDeferredItem) {
        final String MOD_ID = HeliopauseCore.MOD_ID; 

        if(itemDeferredItem.get() instanceof ArmorItem armorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {
                float trimValue = value;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = armorItem.toString();
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = ResourceLocation.parse(armorItemPath);
                ResourceLocation trimResLoc = ResourceLocation.parse(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = ResourceLocation.parse(currentTrimName);
                
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath())
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file 
                this.withExistingParent(itemDeferredItem.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc.getNamespace()  + ":item/" + trimNameResLoc.getPath()))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                ResourceLocation.fromNamespaceAndPath(MOD_ID,
                                        "item/" + itemDeferredItem.getId().getPath()));
            });
        }
    }

    public void wallItem(DeferredBlock<?> block, ResourceLocation texture) {
        this.withExistingParent(block.getId().getPath(), modLoc("block/tinted_wall_inventory"))
                .texture("wall", texture);
    }
}
