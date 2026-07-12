package net.venera.heliocore.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.venera.heliocore.HeliopauseCore;

public class HpCTags {
    public static class Items{
        public static final TagKey<Item> CANISTER = createTag("canister");
        public static final TagKey<Item> COMPRESSIBLE_INGOTS = createTag("compressible_ingots");
        public static final TagKey<Item> OXYGEN_GEAR = TagKey.create(Registries.ITEM, 
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "oxygen_gear"));
        public static final TagKey<Item> CURIOS_OXYGEN_MASK = ItemTags.create(
                ResourceLocation.fromNamespaceAndPath("curios", "oxygen_mask")
        );
        public static final TagKey<Item> CURIOS_OXYGEN_CONNECTORS = ItemTags.create(
                ResourceLocation.fromNamespaceAndPath("curios", "oxygen_connectors")
        );
        public static final TagKey<Item> CURIOS_OXYGEN_TANK_1 = ItemTags.create(
                ResourceLocation.fromNamespaceAndPath("curios", "oxygen_tank_1")
        );
        public static final TagKey<Item> CURIOS_OXYGEN_TANK_2 = ItemTags.create(
                ResourceLocation.fromNamespaceAndPath("curios", "oxygen_tank_2")
        );

        private static TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, name));
        }
        
    }

    public static class Blocks{
        public static final TagKey<Block> NEEDS_STEEL_TOOLS = createTag("needs_steel_tools");
        public static final TagKey<Block> INCORRECT_FOR_STEEL_TOOL = createTag("incorrect_for_steel_tool");
        public static final TagKey<Block> MACHINERY = createTag("machinery");
        public static final TagKey<Block> MOON_REGOLITH_REPLACEABLES = createTag("moon_regolith_replaceables");
        public static final TagKey<Block> MOON_DIRT_REPLACEABLES = createTag("moon_dirt_replaceables");
        public static final TagKey<Block> MOON_STONE_REPLACEABLES = createTag("moon_stone_replaceables");
        
        public static final TagKey<Block> RADIOACTIVE = createTag("radioactive");

        private static TagKey<Block> createTag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> HAS_ABUNDANT_OIL = TagKey.create(
                Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "has_abundant_oil")
        );
         
        public static final TagKey<Biome> LUNAR_HIGHLANDS = TagKey.create(
                Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "lunar_highlands")
        );

        public static final TagKey<Biome> LUNAR_MARIA = TagKey.create(
                Registries.BIOME,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "lunar_maria")
        );
    }

    public static class Fluids{
        public static final TagKey<Fluid> OIL = createTag("oil");
        public static final TagKey<Fluid> GASES = createTag("gases");
        public static final TagKey<Fluid> LIQUIDS = createTag("liquids");


        private static TagKey<Fluid> createTag(String name){
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, name));
        }
    }
    
    public static class Entities{
        public static final TagKey<EntityType<?>> DOES_NOT_BREATHE = TagKey.create(Registries.ENTITY_TYPE, 
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "entity/does_not_breathe"));
        public static final TagKey<EntityType<?>> OXYGEN_SLOTS_RECEIVERS = TagKey.create(Registries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "oxygen_slots_receivers"));

        
       
    }
}
