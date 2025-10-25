package net.venera.galacticraftcore.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.venera.galacticraftcore.data.ModAttachments;
import net.venera.galacticraftcore.data.radiation.RadiationData;

import java.util.List;

public class RadioactiveBlock extends Block {
    private static final double RADIUS = 9.0;
    public RadioactiveBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        AABB area = new AABB(pos).inflate(RADIUS);
        List<Entity> entities = level.getEntities(null, area);

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                RadiationData radData = entity.getData(ModAttachments.RADIATION_DATA);
                if (!living.hasData(ModAttachments.RADIATION_DATA)) continue;
                double distanceSq = entity.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                if(distanceSq < (RADIUS *RADIUS)){
                    double distance = Math.sqrt(distanceSq);
                    radData.changeRadiation(Math.max(0.3, (RADIUS - distance) * 0.5f), true);
                }
            }
        }
        level.scheduleTick(pos, this, 20);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
            super.onPlace(state, level, pos, oldState, isMoving);
            if (!level.isClientSide) {
                ((ServerLevel) level).scheduleTick(pos, this, 20);
            }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.galacticraftcore.gcc_block_radioactive_block"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
