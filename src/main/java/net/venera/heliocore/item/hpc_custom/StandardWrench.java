package net.venera.heliocore.item.hpc_custom;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
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
import net.venera.heliocore.data.component.HpCDataComponents;
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

        context.getItemInHand().set(HpCDataComponents.COORDINATES, context.getClickedPos());
        if(context.getItemInHand().get(HpCDataComponents.COORDINATES) != null && context.isSecondaryUseActive()) {
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
                    level.setBlock(blockPos, blockState.setValue(dirProp, next), Block.UPDATE_ALL | Block.UPDATE_KNOWN_SHAPE);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.heliocore.standard_wrench.shift_down"));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.heliocore.standard_wrench"));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        DamageSource damageSource = attacker.damageSources().generic();
        return target.hurt(damageSource, 5f);
    }
}
