package net.venera.galacticraftcore.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ArcLamp extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
  //  public static final EnumProperty<AttachFace> BASE = BlockStateProperties.ATTACH_FACE;
    public static final BooleanProperty CLICKED = BooleanProperty.create("clicked");

    public ArcLamp(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
         //       .setValue(BASE, AttachFace.FLOOR)
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
        builder.add(FACING, CLICKED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();

        if (context.getClickedFace().getAxis() .isHorizontal()) {
            Direction facingHorizontal = context.getHorizontalDirection();
            player.displayClientMessage(Component.literal("We are in isHorizontal. Clicked Facing " + facingHorizontal.getOpposite().toString()), true);
            return this.defaultBlockState().setValue(FACING, facingHorizontal);
        }
        else {
            Direction facingHorizontal = context.getHorizontalDirection();
            player.displayClientMessage(Component.literal("We are in isnotHorizontal. Clicked Facing " + facingHorizontal.getOpposite().toString()), true);
            return this.defaultBlockState().setValue(FACING, facingHorizontal);


        }
    }
}
