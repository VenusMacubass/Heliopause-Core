package net.venera.galacticraftcore.block.custom.machine;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.entity.machine.BaseMachineEntity;
import net.venera.galacticraftcore.block.entity.machine.electric.BaseElectricMachineEntity;
import net.venera.galacticraftcore.data.energy.EnergyGrid;
import net.venera.galacticraftcore.data.energy.GridManager;
import net.venera.galacticraftcore.data.energy.GridPort;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

public abstract class BaseMachineBlock<T extends BaseMachineEntity> extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    protected final Supplier<BlockEntityType<T>> blockEntityType;

    public BaseMachineBlock(Properties properties, Supplier<BlockEntityType<T>> type) {
        super(properties);
        this.blockEntityType = type;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected abstract MapCodec<? extends BaseEntityBlock> codec();

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            GridManager.get(level).onMachinePlaced(level, pos); // Trigger placed
        }
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (!level.isClientSide) {
                GridManager.get(level).onMachineBroken(level, pos); // Trigger broken
            }

            // ... (keep your existing drops logic here) ...
            if (level.getBlockEntity(pos) instanceof BaseMachineEntity blockEntity) {
                blockEntity.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }
    
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof MenuProvider menuProvider) { //Standard GUI Opener
            player.openMenu(menuProvider, pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) return null;
        
        return (lvl, pos, st, entity) -> {
            
            if (entity instanceof BaseElectricMachineEntity electricMachine) {
                BaseElectricMachineEntity.tick(lvl, pos, st, electricMachine);
            }
        };
    }
}
