package net.venera.galacticraftcore.item.custom;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.venera.galacticraftcore.component.ModDataComponents;
import java.util.List;


public class StandardWrench extends Item {
    public StandardWrench(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);

        context.getItemInHand().set(ModDataComponents.COORDINATES, context.getClickedPos());
        if(context.getItemInHand().get(ModDataComponents.COORDINATES) != null && context.isSecondaryUseActive()) {
            player.displayClientMessage(Component.literal("Last block changed at: " + context.getItemInHand().get(ModDataComponents.COORDINATES)), false);
        }

        if (!level.isClientSide) {
            for (Property<?> property : blockState.getProperties()) {
                if (property instanceof DirectionProperty dirProp) {
                    Direction current = blockState.getValue(dirProp);
                    Direction next = switch (current) {
                        case NORTH -> Direction.EAST;
                        case EAST -> Direction.SOUTH;
                        case SOUTH -> Direction.WEST;
                        case WEST -> Direction.NORTH;
                        default -> current;
                    };
                    level.setBlock(blockPos, blockState.setValue(dirProp, next), Block.UPDATE_ALL);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.galacticraftcore.gcc_item_standard_wrench.shift_down"));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.galacticraftcore.gcc_item_standard_wrench"));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
