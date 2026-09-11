package net.venera.heliocore.item.hpc_custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.venera.heliocore.screen.entity.HpCEquipmentMenu;

public class HpCDebugStickItem extends Item {
    public HpCDebugStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {

            serverPlayer.openMenu(new SimpleMenuProvider(
                    (id, playerInv, p) -> new HpCEquipmentMenu(id, playerInv, interactionTarget),
                    Component.literal("Entity Gear Debug")
            ), buf -> buf.writeInt(interactionTarget.getId())); // Pass the target's ID to the client buffer

        }
        // Return success so the hand swings and the game knows an action occurred
        return InteractionResult.sidedSuccess(player.level().isClientSide());
    }
}