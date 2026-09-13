package net.venera.heliocore.util;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.venera.heliocore.HeliopauseCore;

public record SyncRadiationPayload(double radiation) implements CustomPacketPayload {
    public static final Type<SyncRadiationPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "radiation_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRadiationPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, SyncRadiationPayload::radiation,
            SyncRadiationPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
