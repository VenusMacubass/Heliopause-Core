package net.venera.galacticraftcore.block;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.galacticraftcore.GalacticraftCore;
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
