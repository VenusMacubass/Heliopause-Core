package net.venera.heliocore.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.entity.machine.electric.SolarPanelEntity;

public record SolarPanelHelper(BlockPos pos) implements CustomPacketPayload {
    public static final Type<SolarPanelHelper> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "solar_panel_activitiy"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SolarPanelHelper> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SolarPanelHelper::pos,
            SolarPanelHelper::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    public static void handle(SolarPanelHelper payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().level().getBlockEntity(payload.pos()) instanceof SolarPanelEntity panel) {
                panel.isEnabled = !panel.isEnabled;
                panel.setChanged(); 
            }
        });
    }
}
