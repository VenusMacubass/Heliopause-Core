package net.venera.galacticraftcore.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.fluid.ModLiquidBlockFactory;
import net.venera.galacticraftcore.fluid.ModFluids;
import net.venera.galacticraftcore.item.ModItems;
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

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, GalacticraftCore.MOD_ID, existingFileHelper);
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
        basicItem(ModItems.BURGER_BUN.get());
        basicItem(ModItems.BEEF_PATTY_RAW.get());
        basicItem(ModItems.BEEF_PATTY.get());
        basicItem(ModItems.CHEESE_SLICE.get());
        basicItem(ModItems.CHEESEBURGER.get());

        basicItem(ModFluids.CRUDE_OIL.getBucket());
        basicItem(ModFluids.REFINED_FUEL.getBucket());

        basicItem(ModItems.RADIOACTIVE_CORE.get());
        handheldItem(ModItems.STANDARD_WRENCH.get());

        basicItem(ModItems.RAW_ALUMINIUM.get());
        basicItem(ModItems.RAW_TIN.get());
        basicItem(ModItems.RAW_SILICON.get());
        basicItem(ModItems.RAW_IRIDIUM.get());
        basicItem(ModItems.LUNAR_SAPPHIRE.get());

        basicItem(ModItems.COMPRESSED_ALUMINIUM.get());
        basicItem(ModItems.COMPRESSED_BRONZE.get());
        basicItem(ModItems.COMPRESSED_COPPER.get());
        basicItem(ModItems.COMPRESSED_IRIDIUM.get());
        basicItem(ModItems.COMPRESSED_IRON.get());
        basicItem(ModItems.COMPRESSED_STEEL.get());
        basicItem(ModItems.COMPRESSED_TIN.get());
        basicItem(ModItems.COMPRESSED_HD_PLATE.get());

        basicItem(ModItems.SENSOR_LENS.get());
        basicItem(ModItems.SMALL_BATTERY.get());

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

        trimmedArmorItem(ModItems.STEEL_HELMET);
        trimmedArmorItem(ModItems.STEEL_CHESTPLATE);
        trimmedArmorItem(ModItems.STEEL_LEGGINGS);
        trimmedArmorItem(ModItems.STEEL_BOOTS);

        handheldItem(ModItems.GLASS_SWORD.get());

    }

    private void trimmedArmorItem(DeferredItem<ArmorItem> itemDeferredItem) {
        final String MOD_ID = GalacticraftCore.MOD_ID; // Change this to your mod id

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

    public void wallItem(DeferredBlock<?> block, DeferredBlock<?> baseBlock){
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "block/" + baseBlock.getId().getPath()));
    }
}
