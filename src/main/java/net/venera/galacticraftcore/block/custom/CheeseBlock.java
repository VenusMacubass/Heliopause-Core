package net.venera.galacticraftcore.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.venera.galacticraftcore.item.ModItems;

public class CheeseBlock extends Block {
    public static final IntegerProperty SLICES = IntegerProperty.create("slices", 0, 6);

    private static final VoxelShape[] SLICE_SHAPES = new VoxelShape[]{
            
            Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0), //Slice 0: Full cheese (from first JSON)
            Block.box(3.0, 0.0, 1.0, 15.0, 8.0, 15.0), //Slice 1: Slightly eaten
            Block.box(5.0, 0.0, 1.0, 15.0, 8.0, 15.0), //Slice 2: More eaten
            Block.box(7.0, 0.0, 1.0, 15.0, 8.0, 15.0), //Slice 3: Half eaten
            Block.box(9.0, 0.0, 1.0, 15.0, 8.0, 15.0), //Slice 4: Mostly eaten
            Block.box(11.0, 0.0, 1.0, 15.0, 8.0, 15.0), //Slice 5: Almost gone
            Block.box(13.0, 0.0, 1.0, 15.0, 8.0, 15.0) //Slice 6: Last piece (from second JSON)
    };

    public CheeseBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SLICES, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SLICES);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        int slices = state.getValue(SLICES);
        
        ItemStack cheese = new ItemStack(ModItems.CHEESE_SLICE.get(), 2); // Give player 2 cheese slices
        if (!player.addItem(cheese)) {
            player.drop(cheese, false);
        }
        
        if (slices < 6) {
            level.setBlock(pos, state.setValue(SLICES, slices + 1), 6); // Update bite state
        } else {
            level.removeBlock(pos, false);  // Fully eaten, remove block
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SLICE_SHAPES[state.getValue(SLICES)];
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SLICE_SHAPES[state.getValue(SLICES)];
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return SLICE_SHAPES[state.getValue(SLICES)];
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SLICE_SHAPES[state.getValue(SLICES)];
    }
}
