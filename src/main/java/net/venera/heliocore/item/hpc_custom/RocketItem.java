package net.venera.heliocore.item.hpc_custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
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
        
        if (level.getBlockState(pos).getBlock() instanceof LaunchPadBlock launchPadBlock) {
            BlockPos newPos = launchPadBlock.getPlatformCenter(level, pos);
            if (!level.isClientSide && newPos != null) {
                Tier1RocketEntity rocket = HpCEntities.TIER_1_ROCKET.get().create(level);
                if (rocket != null) {
                    rocket.moveTo(newPos.getX() + 0.5, newPos.getY() + 0.25f, newPos.getZ() + 0.5);
                    ItemStack itemStack = context.getItemInHand();
                    CustomData customData = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                    CompoundTag tag = customData.copyTag();

                    if (tag.contains("PreloadedEnergy")) {
                        rocket.setEnergyAmount(tag.getInt("PreloadedEnergy"));
                    }
                    if (tag.contains("PreloadedFuel")) {
                        rocket.setFuelAmount(tag.getInt("PreloadedFuel"));
                    }
                    
                    level.addFreshEntity(rocket);
                    context.getItemInHand().shrink(1); 
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
