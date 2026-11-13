package net.venera.heliocore.item;

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
        public static final TagKey<Item> CIRCUIT_MATERIALS = createTag("circuit_materials");
        
        public static final TagKey<Item> ORES_TIN = commonTag("ores/tin");
        public static final TagKey<Item> ORES_ALUMINUM = commonTag("ores/aluminum");
        public static final TagKey<Item> ORES_IRIDIUM = commonTag("ores/iridium");
        public static final TagKey<Item> ORES_SILICON = commonTag("ores/silicon");
        
        public static final TagKey<Item> INGOTS_TIN = commonTag("ingots/tin");
        public static final TagKey<Item> INGOTS_ALUMINUM = commonTag("ingots/aluminum");
        public static final TagKey<Item> INGOTS_IRIDIUM = commonTag("ingots/iridium");
        
        public static final TagKey<Item> RAW_MATERIALS_TIN = commonTag("raw_materials/tin");
        public static final TagKey<Item> RAW_MATERIALS_ALUMINUM = commonTag("raw_materials/aluminum");
        
        public static final TagKey<Item> SILICON = commonTag("silicon");
        public static final TagKey<Item> GEMS_SILICON = commonTag("gems/silicon");

        public static final TagKey<Item> STORAGE_BLOCKS_TIN = commonTag("storage_blocks/tin");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_TIN = commonTag("storage_blocks/raw_tin");
        public static final TagKey<Item> STORAGE_BLOCKS_ALUMINUM = commonTag("storage_blocks/aluminum");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_ALUMINUM = commonTag("storage_blocks/raw_aluminum");
        public static final TagKey<Item> STORAGE_BLOCKS_IRIDIUM = commonTag("storage_blocks/iridium");
        public static final TagKey<Item> STORAGE_BLOCKS_SILICON = commonTag("storage_blocks/silicon");
        
        public static final TagKey<Item> T1_ROCKET_PARTS = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "t1_rocket_parts"));
        public static final TagKey<Item> T2_ROCKET_PARTS = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "t2_rocket_parts"));
        public static final TagKey<Item> T3_ROCKET_PARTS = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "t3_rocket_parts"));
        
        public static final TagKey<Item> NOSES = ItemTags.create(
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "slots/noses")
        );
        public static final TagKey<Item> BOOSTERS = ItemTags.create(
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "slots/boosters")
        );
        public static final TagKey<Item> HULLS = ItemTags.create(
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "slots/hulls")
        );
        public static final TagKey<Item> FINS = ItemTags.create(
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "slots/fins")
        );
        public static final TagKey<Item> BASE = ItemTags.create(
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "slots/base")
        );
        public static final TagKey<Item> ENGINES = ItemTags.create(
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "slots/engines")
        );

        public static final TagKey<Item> OXYGEN_SETUP_PIECES = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "oxygen_setup"));
        public static final TagKey<Item> OXYGEN_TANK = ItemTags.create(
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "slots/oxygen_tank")
        );
        
        public static final TagKey<Item> THERMAL_GEAR_T1 = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "t1_thermal_gear"));
        public static final TagKey<Item> THERMAL_GEAR_T2 = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "t2_thermal_gear"));
        public static final TagKey<Item> THERMAL_GEAR_T3 = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "t3_thermal_gear"));
        public static final TagKey<Item> THERMAL_GEAR_HEAD = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "thermal_gear_head"));
        public static final TagKey<Item> THERMAL_GEAR_TORSO = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "thermal_gear_torso"));
        public static final TagKey<Item> THERMAL_GEAR_LEGS = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "thermal_gear_legs"));
        public static final TagKey<Item> THERMAL_GEAR_HANDS_AND_FEET = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "thermal_gear_hands_and_feet"));
        
        public static final TagKey<Item> PRESSURE_PROTECTORS_T1 = createTag("t1_pressure_protectors");
        public static final TagKey<Item> PRESSURE_PROTECTORS_T2 = createTag("t2_pressure_protectors");
        public static final TagKey<Item> PRESSURE_PROTECTORS_T3 = createTag("t3_pressure_protectors");
        public static final TagKey<Item> PRESSURE_PROTECTORS_HELMET = createTag("pressure_protectors_helmet");
        public static final TagKey<Item> PRESSURE_PROTECTORS_CHESTPLATE = createTag("pressure_protectors_chestplate");
        public static final TagKey<Item> PRESSURE_PROTECTORS_LEGGINGS = createTag("pressure_protectors_leggings");
        public static final TagKey<Item> PRESSURE_PROTECTORS_BOOTS = createTag("pressure_protectors_boots");
        
        public static final TagKey<Item> RADIATION_PROTECTORS_T1 = createTag("t1_radiation_protectors");
        public static final TagKey<Item> RADIATION_PROTECTORS_T2 = createTag("t2_radiation_protectors");
        public static final TagKey<Item> RADIATION_PROTECTORS_T3 = createTag("t3_radiation_protectors");
        public static final TagKey<Item> RADIATION_PROTECTORS_HELMET = createTag("radiation_protectors_helmet");
        public static final TagKey<Item> RADIATION_PROTECTORS_CHESTPLATE = createTag("radiation_protectors_chestplate");
        public static final TagKey<Item> RADIATION_PROTECTORS_LEGGINGS = createTag("radiation_protectors_leggings");
        public static final TagKey<Item> RADIATION_PROTECTORS_BOOTS = createTag("radiation_protectors_boots");
        
        public static final TagKey<Item> STONES = createTag("stone_variants");
        
        private static TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, name));
        }
        private static TagKey<Item> commonTag(String path) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
        }
    }

    public static class Blocks{
        public static final TagKey<Block> NEEDS_STEEL_TOOLS = createTag("needs_steel_tools");
        public static final TagKey<Block> INCORRECT_FOR_STEEL_TOOL = createTag("incorrect_for_steel_tool");
        public static final TagKey<Block> MACHINERY = createTag("machinery");
        public static final TagKey<Block> MOON_REGOLITH_REPLACEABLES = createTag("moon_regolith_replaceables");
        public static final TagKey<Block> MOON_DIRT_REPLACEABLES = createTag("moon_dirt_replaceables");
        public static final TagKey<Block> MOON_STONE_REPLACEABLES = createTag("moon_stone_replaceables");

        public static final TagKey<Block> ORES_TIN = commonTag("ores/tin");
        public static final TagKey<Block> ORES_ALUMINUM = commonTag("ores/aluminum");
        public static final TagKey<Block> ORES_IRIDIUM = commonTag("ores/iridium");
        public static final TagKey<Block> ORES_SILICON = commonTag("ores/silicon");

        public static final TagKey<Block> STORAGE_BLOCKS_TIN = commonTag("storage_blocks/tin");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_TIN = commonTag("storage_blocks/raw_tin");
        public static final TagKey<Block> STORAGE_BLOCKS_ALUMINUM = commonTag("storage_blocks/aluminum");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_ALUMINUM = commonTag("storage_blocks/raw_aluminum");
        public static final TagKey<Block> STORAGE_BLOCKS_IRIDIUM = commonTag("storage_blocks/iridium");
        public static final TagKey<Block> STORAGE_BLOCKS_SILICON = commonTag("storage_blocks/silicon");
        
        public static final TagKey<Block> RADIOACTIVE = createTag("radioactive");

        private static TagKey<Block> createTag(String name){
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, name));
        }
        private static TagKey<Block> commonTag(String path) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
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
        public static final TagKey<EntityType<?>> HAS_OXYGEN_BLESSING = TagKey.create(Registries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "has_oxygen_blessing"));
        public static final TagKey<EntityType<?>> HAS_THERMAL_BLESSING = TagKey.create(Registries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "entity/has_thermal_blessing"));
        public static final TagKey<EntityType<?>> HAS_RADIATION_BLESSING = TagKey.create(Registries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "has_radiation_blessing"));
        public static final TagKey<EntityType<?>> HAS_PRESSURE_BLESSING = TagKey.create(Registries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "has_pressure_blessing"));
        
    }
}
