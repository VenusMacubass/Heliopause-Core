package net.venera.heliocore.block.custom.machine.electric;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.venera.heliocore.block.entity.machine.electric.BaseElectricMachineEntity;
import net.venera.heliocore.data.energy.GridManager;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

public class WireBlock extends PipeBlock {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = Map.of(
            Direction.NORTH, NORTH,
            Direction.EAST, EAST,
            Direction.SOUTH, SOUTH,
            Direction.WEST, WEST,
            Direction.UP, UP,
            Direction.DOWN, DOWN
    );
    
    public WireBlock(Properties properties) {
        super(0.125f, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false).setValue(EAST, false)
                .setValue(SOUTH, false).setValue(WEST, false)
                .setValue(UP, false).setValue(DOWN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState state = this.defaultBlockState();
        
        for (Direction direction : Direction.values()) {
            BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
            boolean isConnected = connectsTo(level, blockPos, direction);
            state = state.setValue(property, isConnected);
        }
        return state;
    }

    private boolean connectsTo(LevelAccessor level, BlockPos wirePos, Direction wireFacing) {
        BlockPos neighborPos = wirePos.relative(wireFacing);
        BlockState neighborState = level.getBlockState(neighborPos);
        
        if (neighborState.getBlock() instanceof WireBlock) {
            return true;
        }
        
        BlockEntity be = level.getBlockEntity(neighborPos);
        if (be instanceof BaseElectricMachineEntity machine) {
            Direction faceTouchingWire = wireFacing.getOpposite();
            
            return machine.isValidPort(faceTouchingWire);
        }

        return false;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            GridManager.get(level).onWirePlaced(level, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide) {  
                GridManager.get(level).onWireBroken(level, pos);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected MapCodec<? extends PipeBlock> codec() {
        return null;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        BooleanProperty property = PROPERTY_BY_DIRECTION.get(direction);
        return state.setValue(property, connectsTo(level, currentPos, direction));
    }
}
