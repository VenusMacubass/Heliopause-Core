package net.venera.galacticraftcore.util;

import io.netty.util.Constant;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.venera.galacticraftcore.GalacticraftCore;

public class ModTags {

    public static class Items{
        public static final TagKey<Item> CANISTER = createTag("canister");
        public static final TagKey<Item> COMPRESSIBLE_INGOTS = createTag("compressible_ingots");


        private static TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, name));
        }
    }

    public static class Blocks{
        public static final TagKey<Block> NEEDS_STEEL_TOOLS = createTag("needs_steel_tools");
        public static final TagKey<Block> INCORRECT_FOR_STEEL_TOOL = createTag("incorrect_for_steel_tool");


        public static final TagKey<Block> RADIOACTIVE = createTag("radioactive");

        private static TagKey<Block> createTag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, name));
        }
    }

    public static class Fluids{
        public static final TagKey<Fluid> OIL = createTag("oil");



        private static TagKey<Fluid> createTag(String name){
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, name));
        }
    }
}
