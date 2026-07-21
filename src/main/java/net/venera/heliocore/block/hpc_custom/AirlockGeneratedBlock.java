package net.venera.heliocore.block.hpc_custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AirlockGeneratedBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    
    private static final VoxelShape SHAPE_AXIS_X = Block.box(6, 0, 0, 10, 16, 16);
    private static final VoxelShape SHAPE_AXIS_Z = Block.box(0, 0, 6, 16, 16, 10);
    private static final VoxelShape SHAPE_AXIS_Y = Block.box(0, 6, 0, 16, 10, 16);

    public AirlockGeneratedBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Z));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction.Axis axis = state.getValue(AXIS);
        if (axis == Direction.Axis.X) return SHAPE_AXIS_X;
        if (axis == Direction.Axis.Y) return SHAPE_AXIS_Y;
        return SHAPE_AXIS_Z;
    }
}
