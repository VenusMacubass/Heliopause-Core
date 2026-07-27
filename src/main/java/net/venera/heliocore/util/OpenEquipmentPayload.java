package net.venera.heliocore.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.screen.hpc_custom.HpCEquipmentMenu;

public record OpenEquipmentPayload() implements CustomPacketPayload {
    public static final Type<OpenEquipmentPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "open_equipment"));
    public static final StreamCodec<FriendlyByteBuf, OpenEquipmentPayload> CODEC = StreamCodec.unit(new OpenEquipmentPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    public static void handle(OpenEquipmentPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();

            player.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new HpCEquipmentMenu(id, inv, player),
                    Component.translatable("gui."+ HeliopauseCore.MOD_ID +".equipment")
            ), buf -> buf.writeInt(player.getId()));
        });
    }
}