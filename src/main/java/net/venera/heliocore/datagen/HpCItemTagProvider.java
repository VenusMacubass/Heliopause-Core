package net.venera.heliocore.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.util.HpCTags;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

import static net.venera.heliocore.util.HpCTags.Items.*;

public class HpCItemTagProvider extends ItemTagsProvider {
    public HpCItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, HeliopauseCore.MOD_ID, existingFileHelper);
    }
    
//    private static final TagKey<Item> CURIOS_BACK = ItemTags.create(
//            ResourceLocation.fromNamespaceAndPath("curios", "back")
//    );
    

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(HpCTags.Items.CANISTER)
                .add(HpCItems.COPPER_CANISTER.get())
                .add(HpCItems.TIN_CANISTER.get());

        tag(ItemTags.SWORDS).add(HpCItems.STEEL_SWORD.get());
        tag(ItemTags.AXES).add(HpCItems.STEEL_AXE.get());
        tag(ItemTags.PICKAXES).add(HpCItems.STEEL_PICKAXE.get());
        tag(ItemTags.SHOVELS).add(HpCItems.STEEL_SHOVEL.get());
        tag(ItemTags.HOES).add(HpCItems.STEEL_HOE.get());

        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(HpCItems.STEEL_HELMET.get())
                .add(HpCItems.STEEL_CHESTPLATE.get())
                .add(HpCItems.STEEL_LEGGINGS.get())
                .add(HpCItems.STEEL_BOOTS.get());

        tag(HpCTags.Items.COMPRESSIBLE_INGOTS)
                .add(Items.COPPER_INGOT)
                .add(HpCItems.TIN_INGOT.get())
                .add(HpCItems.ALUMINIUM_INGOT.get())
                .add(Items.IRON_INGOT)
                .add(HpCItems.IRIDIUM_INGOT.get());
        
        
//        tag(CURIOS_BACK)
//                .add(Items.ELYTRA);
        
        tag(CURIOS_OXYGEN_MASK).add(HpCItems.OXYGEN_MASK.get());
        tag(CURIOS_OXYGEN_CONNECTORS).add(HpCItems.OXYGEN_CONNECTORS.get());
        tag(CURIOS_OXYGEN_TANK_1).add(HpCItems.COMPRESSED_GAS_TANK.get());
        tag(CURIOS_OXYGEN_TANK_2).add(HpCItems.COMPRESSED_GAS_TANK.get());

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
