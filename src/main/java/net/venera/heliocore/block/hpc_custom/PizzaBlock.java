package net.venera.heliocore.block.hpc_custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.venera.heliocore.block.entity.PizzaEntity;
import net.venera.heliocore.data.component.HpCDataComponents;
import org.jetbrains.annotations.Nullable;

public class PizzaBlock extends Block implements EntityBlock {
    public static final IntegerProperty SLICES = IntegerProperty.create("slices", 0, 3);

    //North-West (Top-Left)
    private static final VoxelShape NW_SLICE = Block.box(0.0, 0.0, 0.0, 8.0, 1.0, 8.0);
    //North-East (Top-Right)
    private static final VoxelShape NE_SLICE = Block.box(8.0, 0.0, 0.0, 16.0, 1.0, 8.0);
    //South-East (Bottom-Right)
    private static final VoxelShape SE_SLICE = Block.box(8.0, 0.0, 8.0, 16.0, 1.0, 16.0);
    //South-West (Bottom-Left)
    private static final VoxelShape SW_SLICE = Block.box(0.0, 0.0, 8.0, 8.0, 1.0, 16.0);
    private static final VoxelShape[] SLICE_SHAPES = new VoxelShape[]{
            //Slice 0: Full pizza (0 bites taken)
            Shapes.or(NW_SLICE, NE_SLICE, SE_SLICE, SW_SLICE),
            //Slice 1: 3/4 pizza (Bite 1 removes NE slice)
            Shapes.or(NW_SLICE, SE_SLICE, SW_SLICE),
            //Slice 2: Half pizza (Bite 2 removes SE slice, continuing clockwise)
            Shapes.or(NW_SLICE, SW_SLICE),
            //Slice 3: 1/4 pizza (Bite 3 removes SW slice. NW is the last piece left)
            NW_SLICE
    };
    
    public PizzaBlock(BlockBehaviour.Properties properties) {
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
        return eat(level, pos, state, player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof PizzaEntity pizzaBE) {

            // Check if the item has our custom data component
            Integer mask = stack.get(HpCDataComponents.PIZZA_TOPPINGS.get());

            if (mask != null) {
                // Apply the toppings from the item to the physical block
                pizzaBE.setToppingsMask(mask);
            }
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    protected static InteractionResult eat(LevelAccessor level, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        } else {
            int nutrition = 2; // Base crust/cheese nutrition
            float saturation = 0.1F;

            // 4. Read the toppings before they eat the slice!
            if (level.getBlockEntity(pos) instanceof PizzaEntity pizzaBE) {
                int mask = pizzaBE.getToppingsMask();

                // Add bonus stats for each topping present
                if ((mask & 1) != 0) { nutrition += 2; saturation += 0.2F; } // Chicken
                if ((mask & 2) != 0) { nutrition += 3; saturation += 0.3F; } // Meat
                if ((mask & 4) != 0) { nutrition += 1; saturation += 0.1F; } // Mushroom
                if ((mask & 8) != 0) { nutrition += 2; saturation += 0.2F; } // Fish
                if ((mask & 16) != 0) { nutrition += 1; saturation += 0.1F; } // Vegetable
            }

            player.getFoodData().eat(nutrition, saturation);
            int i = state.getValue(SLICES);
            level.gameEvent(player, GameEvent.EAT, pos);

            if (i < 3) {
                level.setBlock(pos, state.setValue(SLICES, i + 1), 3);
            } else {
                level.removeBlock(pos, false);
                level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PizzaEntity(blockPos, blockState);
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
