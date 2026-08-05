package net.venera.heliocore.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.heliocore.data.HpCAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Inject(method = "updateFallFlying", at = @At("HEAD"), cancellable = true)
    private void heliocore$tickCustomElytra(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Player player) {
            if (player.isFallFlying() && !player.onGround() && !player.isPassenger() && !player.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION)) {

                var inventory = player.getData(HpCAttachments.EQUIPMENT_INVENTORY);
                if (inventory != null) {
                    ItemStack stack = inventory.getStackInSlot(8);

                    if (!stack.isEmpty() && stack.canElytraFly(player)) {
                        if (!player.level().isClientSide) {
                            if (!stack.getItem().elytraFlightTick(stack, player, player.getFallFlyingTicks())) {
                                player.stopFallFlying();
                            }
                        }
                        ci.cancel();
                    }
                }
            }
        }
    }
}
