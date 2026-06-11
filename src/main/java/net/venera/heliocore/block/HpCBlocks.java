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
import net.venera.heliocore.block.hpc_custom.*;
import net.venera.heliocore.block.hpc_custom.machine.CoalCompressorBlock;
import net.venera.heliocore.block.hpc_custom.machine.electric.*;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import net.venera.heliocore.item.HpCItems;
import java.util.function.Supplier;

public class HpCBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HeliopauseCore.MOD_ID);

    //region Metals & Ores
    public static final DeferredBlock<Block> RAW_TIN_BLOCK = registerBlock("raw_tin_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> TIN_ORE = registerBlock("tin_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE = registerBlock("deepslate_tin_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(8f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> TIN_BLOCK = registerBlock("tin_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MOON_COPPER_ORE = registerBlock("moon_copper_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MOON_TIN_ORE = registerBlock("moon_tin_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    
    
    public static final DeferredBlock<Block> RAW_ALUMINIUM_BLOCK = registerBlock("raw_aluminium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> ALUMINIUM_ORE = registerBlock("aluminium_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    public static final DeferredBlock<Block> MOON_ALUMINIUM_ORE = registerBlock("moon_aluminium_ore",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(HpCBlocks.ALUMINIUM_ORE.get())));
    public static final DeferredBlock<Block> DEEPSLATE_ALUMINIUM_ORE = registerBlock("deepslate_aluminium_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    public static final DeferredBlock<Block> ALUMINIUM_BLOCK = registerBlock("aluminium_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    
    public static final DeferredBlock<Block> SILICON_ORE = registerBlock("silicon_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> MOON_SILICON_ORE = registerBlock("moon_silicon_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> DEEPSLATE_SILICON_ORE = registerBlock("deepslate_silicon_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> SILICON_BLOCK = registerBlock("silicon_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> IRIDIUM_BLOCK = registerBlock("iridium_block",
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
    public static final DeferredBlock<Block> COAL_COMPRESSOR = registerBlock("coal_compressor",
            () -> new CoalCompressorBlock(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.METAL)));
    public static final DeferredBlock<Block> REFINERY = registerBlock("refinery",
            () -> new RefineryBlock(BlockBehaviour.Properties.of().strength(2f).sound(SoundType.HEAVY_CORE)));
    public static final DeferredBlock<Block> ENERGY_STORAGE_UNIT = registerBlock("energy_storage_unit",
            () -> new EnergyStorageBlock(BlockBehaviour.Properties.of().strength(2f), HpCBlockEntities.ENERGY_STORAGE_ENTITY));
    public static final DeferredBlock<Block> BASIC_SOLAR_BLOCK = registerBlock("basic_solar_block",
            () -> new SolarPanelBlock(BlockBehaviour.Properties.of().strength(2f), HpCBlockEntities.BASIC_SOLAR_PANEL_ENTITY));
    public static final DeferredBlock<Block> CARGO_MANAGER_BLOCK = registerBlock("cargo_manager_block",
            () -> new CargoManagerBlock(BlockBehaviour.Properties.of().strength(2f), HpCBlockEntities.CARGO_MANAGER_ENTITY));
    public static final DeferredBlock<Block> FUEL_MANAGER_BLOCK = registerBlock("fuel_manager_block",
            () -> new FuelManagerBlock(BlockBehaviour.Properties.of().strength(2f), HpCBlockEntities.FUEL_MANAGER_ENTITY));
    
    public static final DeferredBlock<Block> OXYGEN_GENERATOR_BLOCK = registerBlock("oxygen_generator_block",
            () -> new OxygenGeneratorBlock(BlockBehaviour.Properties.of().strength(2f), HpCBlockEntities.OXYGEN_GENERATOR_ENTITY));

    public static final DeferredBlock<Block> GAS_COMPRESSOR_BLOCK = registerBlock("gas_compressor_block",
            () -> new GasCompressorBlock(BlockBehaviour.Properties.of().strength(2f), HpCBlockEntities.GAS_COMPRESSOR_ENTITY));
    
    
    
    //endregion
    
    //region Moon
    public static final DeferredBlock<Block> MOON_REGOLITH = registerBlock("moon_regolith",
            () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> MOON_DIRT = registerBlock("moon_dirt",
            () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().sound(SoundType.GRAVEL)));
    public static final DeferredBlock<Block> MOON_ROCK = registerBlock("moon_rock",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.GRAVEL)));
    public static final DeferredBlock<Block> MOON_COBBLESTONE = registerBlock("moon_cobblestone",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> MOON_DUNGEON_BRICKS = registerBlock("moon_dungeon_bricks",
            () -> new Block(BlockBehaviour.Properties.of().requiresCorrectToolForDrops()));
    
    
    public static final DeferredBlock<WallBlock> MOON_ROCK_WALL = registerBlock("moon_rock_wall",
            ()-> new WallBlock(BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<WallBlock> MOON_DUNGEON_BRICK_WALL = registerBlock("moon_dungeon_brick_wall",
            ()-> new WallBlock(BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<StairBlock> MOON_ROCK_STAIRS = registerBlock("moon_rock_stairs",
            ()-> new StairBlock(HpCBlocks.MOON_ROCK.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<StairBlock> MOON_DUNGEON_BRICK_STAIRS = registerBlock("moon_dungeon_brick_stairs",
            ()-> new StairBlock(HpCBlocks.MOON_DUNGEON_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<SlabBlock> MOON_ROCK_SLAB = registerBlock("moon_rock_slab",
            ()-> new SlabBlock(BlockBehaviour.Properties.of().strength(4f)));
    public static final DeferredBlock<SlabBlock> MOON_DUNGEON_BRICK_SLAB = registerBlock("moon_dungeon_brick_slab",
            ()-> new SlabBlock(BlockBehaviour.Properties.of().strength(4f)));
    //endregion
    
    public static final DeferredBlock<Block> RADIOACTIVE_BLOCK = registerBlock("radioactive_block",
            () -> new RadioactiveBlock(BlockBehaviour.Properties.of().strength(8f).requiresCorrectToolForDrops().sound(SoundType.METAL)));
    
    public static final DeferredBlock<Block> TIN_BUILDING_BLOCK = registerBlock("tin_building_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.METAL)));

    public static final DeferredBlock<WallBlock> TIN_BUILDING_WALL = registerBlock("tin_building_wall",
            ()-> new WallBlock(BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<StairBlock> TIN_BUILDING_STAIRS = registerBlock("tin_building_stairs",
            ()-> new StairBlock(HpCBlocks.TIN_BUILDING_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(4f)));

    public static final DeferredBlock<SlabBlock> TIN_BUILDING_SLAB = registerBlock("tin_building_slab",
            ()-> new SlabBlock(BlockBehaviour.Properties.of().strength(5f)));

    public static final DeferredBlock<Block> ARC_LAMP = registerBlock("arc_lamp",
            () -> new ArcLamp(BlockBehaviour.Properties.of().lightLevel(state -> state.getValue(ArcLamp.CLICKED) ? 15:0).noOcclusion().strength(2f)));

    public static final DeferredBlock<Block> CHEESE_BLOCK = registerBlock("cheese_block",
            () -> new CheeseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)));

    public static final DeferredBlock<Block> LAUNCH_PAD = registerBlock("launch_pad",
            () -> new LaunchPadBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.REPEATER).strength(2f)));

    public static final DeferredBlock<Block> FLUID_TANK = registerBlock("fluid_tank",
            () -> new FluidTankBlock(BlockBehaviour.Properties.of().noOcclusion().strength(2f).sound(SoundType.GLASS)));
    
    public static final DeferredBlock<Block> COPPER_WIRE = registerBlock("copper_wire",
            () -> new WireBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f).sound(SoundType.COPPER)));

    public static final DeferredBlock<Block> FLUID_PIPE = registerBlock("fluid_pipe",
            () -> new FluidPipeBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f).sound(SoundType.GLASS)));
    
    public static final DeferredBlock<Block> PRISMATIC_GLASS = registerBlock("prismatic_glass",
            () -> new TransparentBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f).sound(SoundType.GLASS)));

    public static final DeferredBlock<IronBarsBlock> PRISMATIC_GLASS_PANE = registerBlock("prismatic_glass_pane",
            () -> new IronBarsBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f).sound(SoundType.GLASS)));

    public static final DeferredBlock<Block> TINTED_PRISMATIC_GLASS = registerBlock("tinted_prismatic_glass",
            () -> new TintedGlassBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0.5f).sound(SoundType.GLASS)));
    
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
        HpCItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register (IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
    //endregion
}
