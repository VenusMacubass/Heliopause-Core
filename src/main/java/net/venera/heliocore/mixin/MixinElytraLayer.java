package net.venera.heliocore.mixin;

import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.venera.heliocore.data.HpCAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ElytraLayer.class)
public class MixinElytraLayer {
    @Redirect(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;")
    )
    private ItemStack heliocore$renderCustomElytra(LivingEntity entity, EquipmentSlot slot) {
        ItemStack original = entity.getItemBySlot(slot);
        if (slot == EquipmentSlot.CHEST && !original.canElytraFly(entity) && entity instanceof Player player) {

            var inventory = player.getData(HpCAttachments.EQUIPMENT_INVENTORY);
            if (inventory != null) {
                ItemStack customStack = inventory.getStackInSlot(8);
                
                if (!customStack.isEmpty() && customStack.canElytraFly(player)) {
                    return customStack;
                }
            }
        }
        
        return original;
    }
}
