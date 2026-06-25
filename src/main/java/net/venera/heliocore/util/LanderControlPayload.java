package net.venera.heliocore.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.venera.heliocore.HeliopauseCore;

public record LanderControlPayload(boolean isThrusting) implements CustomPacketPayload {
    public static final Type<LanderControlPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "lander_control"));

    public static final StreamCodec<ByteBuf, LanderControlPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, LanderControlPayload::isThrusting,
            LanderControlPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
