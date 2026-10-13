package net.venera.heliocore.util;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.venera.heliocore.data.HpCAttachments;

public class ElytraStartHandler {
    public static void handle(ElytraSlotPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null && !player.onGround() && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION)) {

                var inventory = player.getData(HpCAttachments.EQUIPMENT_INVENTORY);
                if (inventory != null) {
                    ItemStack stack = inventory.getStackInSlot(8);
                    
                    if (!stack.isEmpty() && stack.canElytraFly(player)) {
                        player.startFallFlying();
                    }
                }
            }
        });
    }
}
