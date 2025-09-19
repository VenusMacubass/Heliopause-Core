package net.venera.galacticraftcore.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.venera.galacticraftcore.GalacticraftCore;

public class ModTags {
    public static class Items{

        public static final TagKey<Item> CANISTER = createTag("canister");

        private static TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, name));
        }
    }

    public static class Blocks{

        public static final TagKey<Block> RADIOACTIVE = createTag("radioactive");

        private static TagKey<Block> createTag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, name));
        }
    }
}
