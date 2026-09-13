package net.venera.heliocore.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.venera.heliocore.HeliopauseCore;

public record SyncEnergyPayload(BlockPos pos, int energy, int capacity) implements CustomPacketPayload {
    public static final Type<SyncEnergyPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "energy_sync"));

    public static final StreamCodec<ByteBuf, SyncEnergyPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SyncEnergyPayload::pos,
            ByteBufCodecs.INT, SyncEnergyPayload::energy,
            ByteBufCodecs.INT, SyncEnergyPayload::capacity,
            SyncEnergyPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}