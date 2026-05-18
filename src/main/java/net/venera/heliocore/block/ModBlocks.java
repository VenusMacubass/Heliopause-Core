package net.venera.heliocore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.custom.*;
import net.venera.heliocore.block.custom.machine.CoalCompressorBlock;
import net.venera.heliocore.block.custom.machine.electric.EnergyStorageBlock;
import net.venera.heliocore.block.custom.machine.electric.RefineryBlock;
import net.venera.heliocore.block.custom.machine.electric.SolarPanelBlock;
import net.venera.heliocore.block.custom.machine.electric.WireBlock;
import net.venera.heliocore.block.entity.ModBlockEntities;
import net.venera.heliocore.item.ModItems;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HeliopauseCore.MOD_ID);

    //region Metals & Ores
    public static final DeferredBlock<Block> RAW_TIN_BLOCK = registerBlock("gcc_block_raw_tin_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> TIN_ORE = registerBlock("gcc_block_tin_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE = registerBlock("deepslate_tin_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(8f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> TIN_BLOCK = registerBlock("gcc_block_tin_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MOON_COPPER_ORE = registerBlock("gcc_block_moon_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MOON_TIN_ORE = registerBlock("gcc_block_moon_tin_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    
    
    public static final DeferredBlock<Block> RAW_ALUMINIUM_BLOCK = registerBlock("gcc_block_raw_aluminium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> ALUMINIUM_ORE = registerBlock("gcc_block_aluminium_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    public static final DeferredBlock<Block> MOON_ALUMINIUM_ORE = registerBlock("moon_aluminium_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(ModBlocks.ALUMINIUM_ORE.get())));
    public static final DeferredBlock<Block> DEEPSLATE_ALUMINIUM_ORE = registerBlock("deepslate_aluminium_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    public static final DeferredBlock<Block> ALUMINIUM_BLOCK = registerBlock("gcc_block_aluminium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    
    public static final DeferredBlock<Block> SILICON_ORE = registerBlock("gcc_block_silicon_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MOON_SILICON_ORE = registerBlock("moon_silicon_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> DEEPSLATE_SILICON_ORE = registerBlock("deepslate_silicon_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> SILICON_BLOCK = registerBlock("gcc_block_silicon_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> IRIDIUM_BLOCK = registerBlock("gcc_block_iridium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(8f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> IRIDIUM_ORE = registerBlock("iridium_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(8f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> DEEPSLATE_IRIDIUM_ORE = registerBlock("deepslate_iridium_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(8f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MOON_IRIDIUM_ORE = registerBlock("moon_iridium_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(8f).requiresCorrectToolForDrops()));
    
    public static final DeferredBlock<Block> MOON_IRON_ORE = registerBlock("moon_iron_ore",
            ()-> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)));
    //endregion
    
    //region Machines
    public static final DeferredBlock<Block> COAL_COMPRESSOR = registerBlock("gcc_coal_compressor",
            () -> new CoalCompressorBlock(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.METAL)));
    public static final DeferredBlock<Block> REFINERY = registerBlock("gcc_refinery",
            () -> new RefineryBlock(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.HEAVY_CORE)));
    public static final DeferredBlock<Block> ENERGY_STORAGE_UNIT = registerBlock("energy_storage_unit",
            () -> new EnergyStorageBlock(BlockBehaviour.Properties.of().strength(2f), ModBlockEntities.ENERGY_STORAGE_ENTITY));
    public static final DeferredBlock<Block> BASIC_SOLAR_BLOCK = registerBlock("basic_solar_block",
            () -> new SolarPanelBlock(BlockBehaviour.Properties.of().strength(2f), ModBlockEntities.BASIC_SOLAR_PANEL_ENTITY));
    
    //endregion
    
    //region Moon
    public static final DeferredBlock<Block> MOON_TURF = registerBlock("gcc_block_moon_turf",
            () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> MOON_DIRT = registerBlock("gcc_block_moon_dirt",
            () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().sound(SoundType.GRAVEL)));
    public static final DeferredBlock<Block> MOON_ROCK = registerBlock("gcc_block_moon_rock",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL)));
    public static final DeferredBlock<Block> MOON_COBBLESTONE = registerBlock("gcc_block_moon_cobblestone",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MOON_DUNGEON_BRICKS = registerBlock("moon_dungeon_bricks",
            () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()));
    
    
    public static final DeferredBlock<WallBlock> MOON_ROCK_WALL = registerBlock("gcc_block_moon_rock_wall",
            ()-> new WallBlock(BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<WallBlock> MOON_DUNGEON_BRICK_WALL = registerBlock("gcc_block_moon_dungeon_brick_wall",
            ()-> new WallBlock(BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<StairBlock> MOON_ROCK_STAIRS = registerBlock("gcc_block_moon_rock_stairs",
            ()-> new StairBlock(ModBlocks.MOON_ROCK.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<StairBlock> MOON_DUNGEON_BRICK_STAIRS = registerBlock("moon_dungeon_brick_stairs",
            ()-> new StairBlock(ModBlocks.MOON_DUNGEON_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<SlabBlock> MOON_ROCK_SLAB = registerBlock("gcc_block_moon_rock_slab",
            ()-> new SlabBlock(BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<SlabBlock> MOON_DUNGEON_BRICK_SLAB = registerBlock("gcc_block_moon_dungeon_brick_slab",
            ()-> new SlabBlock(BlockBehaviour.Properties.of().strength(4f)));
    //endregion
    
    public static final DeferredBlock<Block> RADIOACTIVE_BLOCK = registerBlock("radioactive_block",
            () -> new RadioactiveBlock(BlockBehaviour.Properties.of().strength(8f).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    
    public static final DeferredBlock<Block> TIN_BUILDING_BLOCK = registerBlock("gcc_block_tin_building_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.METAL)));

    public static final DeferredBlock<WallBlock> TIN_BUILDING_WALL = registerBlock("gcc_block_tin_building_wall",
            ()-> new WallBlock(BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<StairBlock> TIN_BUILDING_STAIRS = registerBlock("gcc_block_tin_building_stairs",
            ()-> new StairBlock(ModBlocks.TIN_BUILDING_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<SlabBlock> TIN_BUILDING_SLAB = registerBlock("gcc_block_tin_building_slab",
            ()-> new SlabBlock(BlockBehaviour.Properties.of().strength(5f)));

    public static final DeferredBlock<Block> ARC_LAMP = registerBlock("gcc_block_arc_lamp",
            () -> new ArcLamp(BlockBehaviour.Properties.of().lightLevel(state -> state.getValue(ArcLamp.CLICKED) ? 15:0).noOcclusion().strength(2f)));

    public static final DeferredBlock<Block> CHEESE_BLOCK = registerBlock("gcc_block_cheese_block",
            () -> new CheeseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)));

    public static final DeferredBlock<Block> LAUNCH_PAD = registerBlock("gcc_launch_pad",
            () -> new LaunchPadBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REPEATER).strength(2f)));

    public static final DeferredBlock<Block> FLUID_TANK = registerBlock("fluid_tank",
            () -> new FluidTankBlock(BlockBehaviour.Properties.of().noOcclusion().strength(2f).sound(SoundType.GLASS)));
    
    public static final DeferredBlock<Block> COPPER_WIRE = registerBlock("copper_wire",
            () -> new WireBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f).sound(SoundType.COPPER)));
    
    public static final DeferredBlock<Block> T1_ROCKET_BOT = registerBlock("rocket_bot",
            () -> new Tier1RocketBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f).noLootTable().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> T1_ROCKET_MID = registerBlock("rocket_mid",
            () -> new Tier1RocketMiddleBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f).noLootTable().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> T1_ROCKET_TOP = registerBlock("rocket_top",
            () -> new Tier1RocketTopBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f).noLootTable().sound(SoundType.METAL)));
    
    public static final DeferredBlock<Block> PRISMATIC_GLASS = registerBlock("prismatic_glass",
            () -> new TransparentBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f).sound(SoundType.GLASS)));
    
    public static final DeferredBlock<Block> COPPER_WIRE_BLOCK = registerBlock("copper_wire_block", 
            () -> new WireBlock(BlockBehaviour.Properties.of().sound(SoundType.COPPER)){
                @Override
                public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
                    return Shapes.block();
                }
            });

    public static final DeferredBlock<Block> PIZZA_BLOCK = registerBlock("pizza_block",
            () -> new PizzaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)));
    
    //region Registry 
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
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
    //endregion
}
