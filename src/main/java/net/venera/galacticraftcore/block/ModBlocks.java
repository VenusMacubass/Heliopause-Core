package net.venera.galacticraftcore.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.custom.ArcLamp;
import net.venera.galacticraftcore.block.custom.RadioactiveBlock;
import net.venera.galacticraftcore.item.ModItems;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GalacticraftCore.MOD_ID);

    public static final DeferredBlock<Block> ALUMINIUM_ORE = registerBlock("gcc_block_aluminium_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> ALUMINIUM_BLOCK = registerBlock("gcc_block_aluminium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> RADIOACTIVE_BLOCK = registerBlock("gcc_block_radioactive_block",
            () -> new RadioactiveBlock(BlockBehaviour.Properties.of().strength(8f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> CHEESE_BLOCK = registerBlock("gcc_block_cheese_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.CROP)));

    public static final DeferredBlock<Block> MOON_TURF = registerBlock("gcc_block_moon_turf",
            () -> new Block(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<Block> MOON_DIRT = registerBlock("gcc_block_moon_dirt",
            () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().sound(SoundType.GRAVEL)));

    public static final DeferredBlock<Block> MOON_ROCK = registerBlock("gcc_block_moon_rock",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL)));

    public static final DeferredBlock<Block> TIN_BUILDING_BLOCK = registerBlock("gcc_block_tin_building_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.METAL)));

    public static final DeferredBlock<Block> TIN_BLOCK = registerBlock("gcc_block_tin_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> TIN_ORE = registerBlock("gcc_block_tin_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> RAW_TIN_BLOCK = registerBlock("gcc_block_raw_tin_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> RAW_ALUMINIUM_BLOCK = registerBlock("gcc_block_raw_aluminium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> SILICON_ORE = registerBlock("gcc_block_silicon_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> SILICON_BLOCK = registerBlock("gcc_block_silicon_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MOON_COPPER_ORE = registerBlock("gcc_block_moon_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MOON_TIN_ORE = registerBlock("gcc_block_moon_tin_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MOON_COBBLESTONE = registerBlock("gcc_block_moon_cobblestone",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MOON_DUNGEON_BRICKS = registerBlock("gcc_block_moon_dungeon_bricks",
            () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MARS_DUNGEON_BRICKS = registerBlock("gcc_block_mars_dungeon_bricks",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MARS_ROCK = registerBlock("gcc_block_mars_rock",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MARS_COBBLESTONE = registerBlock("gcc_block_mars_cobblestone",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<WallBlock> TIN_BUILDING_WALL = registerBlock("gcc_block_tin_building_wall",
            ()-> new WallBlock(BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<WallBlock> MOON_ROCK_WALL = registerBlock("gcc_block_moon_rock_wall",
            ()-> new WallBlock(BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<WallBlock> MOON_DUNGEON_BRICK_WALL = registerBlock("gcc_block_moon_dungeon_brick_wall",
            ()-> new WallBlock(BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<StairBlock> TIN_BUILDING_STAIRS = registerBlock("gcc_block_tin_building_stairs",
            ()-> new StairBlock(ModBlocks.TIN_BUILDING_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<StairBlock> MOON_ROCK_STAIRS = registerBlock("gcc_block_moon_rock_stairs",
            ()-> new StairBlock(ModBlocks.MOON_ROCK.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<StairBlock> MOON_DUNGEON_BRICK_STAIRS = registerBlock("gcc_block_moon_dungeon_brick_stairs",
            ()-> new StairBlock(ModBlocks.MOON_DUNGEON_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<SlabBlock> TIN_BUILDING_SLAB = registerBlock("gcc_block_tin_building_slab",
            ()-> new SlabBlock(BlockBehaviour.Properties.of().strength(5f)));

    public static final DeferredBlock<SlabBlock> MOON_ROCK_SLAB = registerBlock("gcc_block_moon_rock_slab",
            ()-> new SlabBlock(BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<SlabBlock> MOON_DUNGEON_BRICK_SLAB = registerBlock("gcc_block_moon_dungeon_brick_slab",
            ()-> new SlabBlock(BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<WallBlock> MARS_COBBLESTONE_WALL = registerBlock("gcc_block_mars_cobblestone_wall",
            ()-> new WallBlock(BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<WallBlock> MARS_DUNGEON_BRICK_WALL = registerBlock("gcc_block_mars_dungeon_brick_wall",
            ()-> new WallBlock(BlockBehaviour.Properties.of().strength(5f)));
    public static final DeferredBlock<SlabBlock> MARS_COBBLESTONE_SLAB = registerBlock("gcc_block_mars_cobblestone_slab",
            ()-> new SlabBlock(BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<SlabBlock> MARS_DUNGEON_BRICK_SLAB = registerBlock("gcc_block_mars_dungeon_brick_slab",
            ()-> new SlabBlock(BlockBehaviour.Properties.of().strength(5f)));

    public static final DeferredBlock<Block> ARC_LAMP = registerBlock("arclamp",
            () -> new ArcLamp(BlockBehaviour.Properties.of().lightLevel(state -> state.getValue(ArcLamp.CLICKED) ? 15:0).noOcclusion().strength(2f)));

    public static final DeferredBlock<Block> IRIDIUM_BLOCK = registerBlock("gcc_block_iridium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(8f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> SAPPHIRE_ORE = registerBlock("gcc_block_sapphire_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, java.util.function.Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register (IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
