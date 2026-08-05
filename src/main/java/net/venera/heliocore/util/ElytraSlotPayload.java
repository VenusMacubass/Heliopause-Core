package net.venera.heliocore.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.HpCAttachments;

public record ElytraSlotPayload() implements CustomPacketPayload {
    public static final Type<ElytraSlotPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "start_elytra"));
    public static final StreamCodec<FriendlyByteBuf, ElytraSlotPayload> CODEC = StreamCodec.unit(new ElytraSlotPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

