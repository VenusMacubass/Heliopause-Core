package net.venera.heliocore.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.venera.heliocore.HeliopauseCore;

public record EnergySyncPayload(BlockPos pos, int energy, int capacity) implements CustomPacketPayload {
    public static final Type<EnergySyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "energy_sync"));

    public static final StreamCodec<ByteBuf, EnergySyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, EnergySyncPayload::pos,
            ByteBufCodecs.INT, EnergySyncPayload::energy,
            ByteBufCodecs.INT, EnergySyncPayload::capacity,
            EnergySyncPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}