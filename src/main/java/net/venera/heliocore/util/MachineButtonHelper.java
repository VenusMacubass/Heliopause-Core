package net.venera.heliocore.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.venera.heliocore.HeliopauseCore;

public record MachineButtonHelper(BlockPos pos, int buttonId) implements CustomPacketPayload {
    public static final Type<MachineButtonHelper> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "toggle_machine"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MachineButtonHelper> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, MachineButtonHelper::pos,
            ByteBufCodecs.INT, MachineButtonHelper::buttonId,
            MachineButtonHelper::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MachineButtonHelper payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().level().getBlockEntity(payload.pos()) instanceof MachineConfigHelper.IToggleableMachine machine) {
                machine.toggleEnabled(payload.buttonId()); 
            }
        });
    }
}
