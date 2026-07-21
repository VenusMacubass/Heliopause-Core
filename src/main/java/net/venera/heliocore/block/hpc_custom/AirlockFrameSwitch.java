package net.venera.heliocore.block.hpc_custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.venera.heliocore.block.entity.machine.electric.OxygenSealerEntity;
import net.venera.heliocore.util.AirlockGateHelper;
import net.venera.heliocore.util.OxygenVolumeHelper;

public class AirlockFrameSwitch extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final IntegerProperty ROTATION = IntegerProperty.create("rotation", 0, 3);
    
    public AirlockFrameSwitch(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.SOUTH)
                .setValue(ACTIVE, false)
                .setValue(ROTATION, 3));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            boolean isActive = state.getValue(ACTIVE);
            Direction doorFacing = state.getValue(FACING);
            
            boolean isOpening = !isActive;
            boolean success = AirlockGateHelper.toggleAirlockBlocks(level, pos, doorFacing, isOpening);

            if (success) {
                if (isOpening) {
                    BlockPos front = pos.relative(doorFacing);
                    BlockPos back = pos.relative(doorFacing.getOpposite());

                    boolean frontSealed = OxygenVolumeHelper.isPositionSealed(front.asLong());
                    boolean backSealed = OxygenVolumeHelper.isPositionSealed(back.asLong());

                    if (frontSealed != backSealed) {
                        BlockPos sealedAirPos = frontSealed ? front : back;
                        BlockPos sealerToBreak = OxygenVolumeHelper.getSealerForAir(sealedAirPos.asLong());
                        if (sealerToBreak != null && level.getBlockEntity(sealerToBreak) instanceof OxygenSealerEntity sealer) {
                            sealer.seal = false;
                            OxygenVolumeHelper.removeRoom(sealerToBreak);
                        }
                    }
                }
                level.setBlockAndUpdate(pos, state.setValue(ACTIVE, !isActive));
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE, ROTATION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction pistonFacing = context.getNearestLookingDirection().getOpposite();
        
        float pitch = context.getPlayer() != null ? context.getPlayer().getXRot() : 0;
        Direction hDirection = context.getHorizontalDirection().getOpposite();
        int absRotation = 0;

        if (pistonFacing.getAxis() == Direction.Axis.Z) {
            if (pitch < -45) absRotation = 0;
            else if (pitch > 45) absRotation = 2;
            else absRotation = (hDirection == Direction.EAST) ? 3 : 1;
        } else {
            if (pitch < -45) absRotation = 0;
            else if (pitch > 45) absRotation = 2;
            else absRotation = (hDirection == Direction.NORTH) ? 3 : 1;
        }

        return this.defaultBlockState()
                .setValue(FACING, pistonFacing)
                .setValue(ROTATION, absRotation);
    }
    
    
}
