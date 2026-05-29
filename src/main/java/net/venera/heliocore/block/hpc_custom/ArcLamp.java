package net.venera.heliocore.block.hpc_custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ArcLamp extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty CLICKED = BooleanProperty.create("clicked");
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);
    
    private static final VoxelShape UP_SHAPE = Block.box(2, 0, 2, 14, 1, 14);
    private static final VoxelShape DOWN_SHAPE = Block.box(2, 15, 2, 14, 16, 14);
    private static final VoxelShape NORTH_SHAPE = Block.box(2, 2, 15, 14, 14, 16);
    private static final VoxelShape SOUTH_SHAPE = Block.box(2, 2, 0, 14, 14, 1);
    private static final VoxelShape EAST_SHAPE = Block.box(0, 2, 2, 1, 14, 14);
    private static final VoxelShape WEST_SHAPE = Block.box(15, 2, 2, 16, 14, 14);

    public ArcLamp(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.SOUTH)
                .setValue(ROTATION, 3)
                .setValue(CLICKED, false));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        if (!level.isClientSide) {
            boolean currentState = state.getValue(CLICKED);
            level.setBlockAndUpdate(pos, state.setValue(CLICKED, !currentState));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ROTATION, CLICKED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        Direction clickedFace = context.getClickedFace();
        Direction hDirection = context.getHorizontalDirection().getOpposite();
        float pitch = player.getXRot();
        int absRotation = 0;

        if (clickedFace == Direction.UP || clickedFace == Direction.DOWN) {
            switch (hDirection) {
                case NORTH:
                    absRotation = 2;
                    break;
                case EAST:
                    absRotation = 3;
                    break;
                case WEST:
                    absRotation = 1;
                    break;
                case SOUTH:
                    absRotation = 0;
                    break;
            }
        }else if(clickedFace == Direction.SOUTH || clickedFace == Direction.NORTH) {
            if (pitch < -45) {
                absRotation = 0;
            } else if (pitch > 45){
                absRotation = 2;
            }else{
                switch (hDirection) {
                    case EAST:
                        absRotation = 3;
                        break;
                    case WEST:
                        absRotation = 1;
                        break;
                }
            }
        }else {
            if (pitch < -45) {
                absRotation = 0;
            } else if (pitch > 45) {
                absRotation = 2;
            }else{
                switch (hDirection) {
                    case NORTH:
                        absRotation = 3;
                        break;
                    case SOUTH:
                        absRotation = 1;
                        break;
                }
            }
        }
        return this.defaultBlockState()
                .setValue(FACING, clickedFace)
                .setValue(ROTATION, absRotation);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getVoxelShape(state); //This determines the visual outline and block picking overlay
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {  
        return getVoxelShape(state); //This determines interaction area, used the same as shape 
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getVoxelShape(state); //This determines culling, used the same as shape
    }

    private VoxelShape getVoxelShape(BlockState state) {
        return switch (state.getValue(FACING)) {
            case UP -> UP_SHAPE;
            case DOWN -> DOWN_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
        };
    }
}
