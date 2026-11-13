package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.item.HpCTags;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

import static net.venera.heliocore.item.HpCTags.Items.*;

public class HpCItemTagProvider extends ItemTagsProvider {
    public HpCItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, HeliopauseCore.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(Tags.Blocks.ORES_COPPER, Tags.Items.ORES_COPPER);
        copy(Tags.Blocks.ORES_IRON, Tags.Items.ORES_IRON);
        copy(HpCTags.Blocks.ORES_TIN, HpCTags.Items.ORES_TIN);
        copy(HpCTags.Blocks.ORES_ALUMINUM, HpCTags.Items.ORES_ALUMINUM);
        copy(HpCTags.Blocks.ORES_IRIDIUM, HpCTags.Items.ORES_IRIDIUM);
        copy(HpCTags.Blocks.ORES_SILICON, HpCTags.Items.ORES_SILICON);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);

        copy(HpCTags.Blocks.STORAGE_BLOCKS_TIN, HpCTags.Items.STORAGE_BLOCKS_TIN);
        copy(HpCTags.Blocks.STORAGE_BLOCKS_RAW_TIN, HpCTags.Items.STORAGE_BLOCKS_RAW_TIN);
        copy(HpCTags.Blocks.STORAGE_BLOCKS_ALUMINUM, HpCTags.Items.STORAGE_BLOCKS_ALUMINUM);
        copy(HpCTags.Blocks.STORAGE_BLOCKS_RAW_ALUMINUM, HpCTags.Items.STORAGE_BLOCKS_RAW_ALUMINUM);
        copy(HpCTags.Blocks.STORAGE_BLOCKS_IRIDIUM, HpCTags.Items.STORAGE_BLOCKS_IRIDIUM);
        copy(HpCTags.Blocks.STORAGE_BLOCKS_SILICON, HpCTags.Items.STORAGE_BLOCKS_SILICON);
        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
        
        
        
        tag(HpCTags.Items.INGOTS_TIN).add(HpCItems.TIN_INGOT.get());
        tag(HpCTags.Items.INGOTS_ALUMINUM).add(HpCItems.ALUMINIUM_INGOT.get());
        tag(HpCTags.Items.INGOTS_IRIDIUM).add(HpCItems.IRIDIUM_INGOT.get());
        
        tag(Tags.Items.INGOTS)
                .addTag(HpCTags.Items.INGOTS_TIN)
                .addTag(HpCTags.Items.INGOTS_ALUMINUM)
                .addTag(HpCTags.Items.INGOTS_IRIDIUM);
        
        tag(HpCTags.Items.RAW_MATERIALS_TIN).add(HpCItems.RAW_TIN.get());
        tag(HpCTags.Items.RAW_MATERIALS_ALUMINUM).add(HpCItems.RAW_ALUMINIUM.get());

        tag(Tags.Items.RAW_MATERIALS)
                .addTag(HpCTags.Items.RAW_MATERIALS_TIN)
                .addTag(HpCTags.Items.RAW_MATERIALS_ALUMINUM);

        tag(HpCTags.Items.SILICON).add(HpCItems.SILICON.get());
        tag(HpCTags.Items.GEMS_SILICON).add(HpCItems.SILICON.get());
        
        tag(HpCTags.Items.CANISTER)
                .add(HpCItems.COPPER_CANISTER.get())
                .add(HpCItems.TIN_CANISTER.get());

        tag(ItemTags.SWORDS)
                .add(HpCItems.STEEL_SWORD.get());
        tag(ItemTags.AXES)
                .add(HpCItems.STEEL_AXE.get());
        tag(ItemTags.PICKAXES)
                .add(HpCItems.STEEL_PICKAXE.get());
        tag(ItemTags.SHOVELS)
                .add(HpCItems.STEEL_SHOVEL.get());
        tag(ItemTags.HOES)
                .add(HpCItems.STEEL_HOE.get());

        tag(ItemTags.TRIMMABLE_ARMOR)
                .add(HpCItems.STEEL_HELMET.get())
                .add(HpCItems.STEEL_CHESTPLATE.get())
                .add(HpCItems.STEEL_LEGGINGS.get())
                .add(HpCItems.STEEL_BOOTS.get());
        
        tag(HpCTags.Items.CIRCUIT_MATERIALS)
                .add(Items.QUARTZ)
                .add(Items.LAPIS_LAZULI);

        tag(HpCTags.Items.COMPRESSIBLE_INGOTS)
                .addTag(Tags.Items.INGOTS_COPPER)
                .addTag(HpCTags.Items.INGOTS_TIN)
                .addTag(HpCTags.Items.INGOTS_ALUMINUM)
                .addTag(Tags.Items.INGOTS_IRON)
                .addTag(HpCTags.Items.INGOTS_IRIDIUM);

        
        
        //region Gears and Equipment
        tag(OXYGEN_SETUP_PIECES)
                .add(HpCItems.OXYGEN_MASK.get())
                .add(HpCItems.OXYGEN_CONNECTORS.get())
                .add(HpCItems.COMPRESSED_GAS_TANK.get());
        tag(OXYGEN_TANK).add(HpCItems.COMPRESSED_GAS_TANK.get());

        
        
        
        tag(THERMAL_GEAR_T1)
                .add(HpCItems.T1_THERMAL_INSULATION_HEAD.get())
                .add(HpCItems.T1_THERMAL_INSULATION_TORSO.get())
                .add(HpCItems.T1_THERMAL_INSULATION_LEGGINGS.get())
                .add(HpCItems.T1_THERMAL_INSULATION_HANDS_AND_FEET.get());
        
        tag(THERMAL_GEAR_HEAD)
                .add(HpCItems.T1_THERMAL_INSULATION_HEAD.get());
        tag(THERMAL_GEAR_TORSO)
                .add(HpCItems.T1_THERMAL_INSULATION_TORSO.get());
        tag(THERMAL_GEAR_LEGS)
                .add(HpCItems.T1_THERMAL_INSULATION_LEGGINGS.get());
        tag(THERMAL_GEAR_HANDS_AND_FEET)
                .add(HpCItems.T1_THERMAL_INSULATION_HANDS_AND_FEET.get());
        
        
        
        tag(PRESSURE_PROTECTORS_T1)
                .add(HpCItems.T1_SPACE_SUIT_HELMET.get())
                .add(HpCItems.T1_SPACE_SUIT_CHESTPLATE.get())
                .add(HpCItems.T1_SPACE_SUIT_LEGGINGS.get())
                .add(HpCItems.T1_SPACE_SUIT_BOOTS.get());
        
        tag(PRESSURE_PROTECTORS_HELMET)
                .add(HpCItems.T1_SPACE_SUIT_HELMET.get());
        tag(PRESSURE_PROTECTORS_CHESTPLATE)
                .add(HpCItems.T1_SPACE_SUIT_CHESTPLATE.get());
        tag(PRESSURE_PROTECTORS_LEGGINGS)
                .add(HpCItems.T1_SPACE_SUIT_LEGGINGS.get());
        tag(PRESSURE_PROTECTORS_BOOTS)
                .add(HpCItems.T1_SPACE_SUIT_BOOTS.get());
        
        
        
        tag(RADIATION_PROTECTORS_T1)
                .add(HpCItems.T1_SPACE_SUIT_HELMET.get())
                .add(HpCItems.T1_SPACE_SUIT_CHESTPLATE.get())
                .add(HpCItems.T1_SPACE_SUIT_LEGGINGS.get())
                .add(HpCItems.T1_SPACE_SUIT_BOOTS.get());
        
        tag(RADIATION_PROTECTORS_HELMET)
                .add(HpCItems.T1_SPACE_SUIT_HELMET.get());
        tag(RADIATION_PROTECTORS_CHESTPLATE)
                .add(HpCItems.T1_SPACE_SUIT_CHESTPLATE.get());
        tag(RADIATION_PROTECTORS_LEGGINGS)
                .add(HpCItems.T1_SPACE_SUIT_LEGGINGS.get());
        tag(RADIATION_PROTECTORS_BOOTS)
                .add(HpCItems.T1_SPACE_SUIT_BOOTS.get());
        
        
        
        
        
        
        tag(T1_ROCKET_PARTS)
                .add(HpCItems.T1_ROCKET_NOSE_CONE.get())
                .add(HpCItems.COMPRESSED_HD_PLATE.get())
                .add(HpCItems.T1_ROCKET_FIN.get())
                .add(HpCItems.T1_ROCKET_BASE.get())
                .add(HpCItems.T1_ROCKET_ENGINE.get());
        tag(NOSES)
                .add(HpCItems.T1_ROCKET_NOSE_CONE.get());
//        tag(BOOSTERS)
//              .add(HpCItems..get());
        tag(HULLS)
                .add(HpCItems.COMPRESSED_HD_PLATE.get());
        tag(FINS)
                .add(HpCItems.T1_ROCKET_FIN.get());
        tag(BASE)
                .add(HpCItems.T1_ROCKET_BASE.get());
        tag(ENGINES)
                .add(HpCItems.T1_ROCKET_ENGINE.get());
        //endregion
        
        tag(HpCTags.Items.STONES)
                .add(Blocks.STONE.asItem())
                .add(Blocks.SMOOTH_STONE.asItem())
                .add(Blocks.ANDESITE.asItem())
                .add(Blocks.DIORITE.asItem())
                .add(Blocks.GRANITE.asItem())
                .add(Blocks.DEEPSLATE.asItem())
                .add(Blocks.BASALT.asItem())
                .add(Blocks.SANDSTONE.asItem())
                .add(HpCBlocks.MOON_ROCK.get().asItem());
        
    }
}
