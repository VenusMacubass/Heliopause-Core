package net.venera.heliocore.util;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.venera.heliocore.HeliopauseCore;

public record SyncPressurePayload(double pressure) implements CustomPacketPayload {
    public static final Type<SyncPressurePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "pressure_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncPressurePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, SyncPressurePayload::pressure,
            SyncPressurePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
