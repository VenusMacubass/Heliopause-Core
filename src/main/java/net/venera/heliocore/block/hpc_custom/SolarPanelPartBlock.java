package net.venera.heliocore.block.hpc_custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.venera.heliocore.block.hpc_custom.machine.electric.SolarPanelBlock;

public class SolarPanelPartBlock extends Block {
    private static final VoxelShape PANEL_SHAPE = Block.box(0, 11, 0, 16, 12, 16);

    public SolarPanelPartBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return PANEL_SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos belowPos = pos.below().offset(x, 0, z);
                if (level.getBlockState(belowPos).getBlock() instanceof SolarPanelBlock) {
                    level.destroyBlock(belowPos, !player.isCreative());
                    return state;
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
        return state;
    }
}
