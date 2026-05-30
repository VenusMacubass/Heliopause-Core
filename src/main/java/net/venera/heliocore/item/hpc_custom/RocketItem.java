package net.venera.heliocore.item.hpc_custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.venera.heliocore.block.hpc_custom.LaunchPadBlock;
import net.venera.heliocore.entity.HpCEntities;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;

public class RocketItem extends Item {
    public RocketItem(Properties properties) { super(properties); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        
        if (level.getBlockState(pos).getBlock() instanceof LaunchPadBlock) {
            if (!level.isClientSide) {
                Tier1RocketEntity rocket = HpCEntities.TIER_1_ROCKET.get().create(level);
                if (rocket != null) {
                    // Spawn it sitting ON TOP of the pad
                    rocket.moveTo(pos.getX() + 0.5, pos.getY() + 0.25f, pos.getZ() + 0.5);
                    level.addFreshEntity(rocket);
                    context.getItemInHand().shrink(1); // Consume the item
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
