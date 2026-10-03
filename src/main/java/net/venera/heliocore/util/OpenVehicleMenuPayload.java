package net.venera.heliocore.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;
import net.venera.heliocore.entity.rideable.Tier1RocketLanderEntity;
import net.venera.heliocore.screen.entity.LanderMenu;
import net.venera.heliocore.screen.entity.RocketMenu;

public record OpenVehicleMenuPayload() implements CustomPacketPayload {
    public static final Type<OpenVehicleMenuPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "open_vehicle_menu"));
    public static final StreamCodec<FriendlyByteBuf, OpenVehicleMenuPayload> CODEC = StreamCodec.unit(new OpenVehicleMenuPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final OpenVehicleMenuPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                Entity vehicle = serverPlayer.getVehicle();

                if (vehicle instanceof Tier1RocketEntity rocket) {
                    serverPlayer.openMenu(new SimpleMenuProvider(
                            (id, playerInv, p) -> new RocketMenu(id, playerInv, rocket),
                            Component.literal("Rocket Cargo Inventory")
                    ), buf -> buf.writeInt(rocket.getId()));
                }
                else if (vehicle instanceof Tier1RocketLanderEntity lander) {
                    serverPlayer.openMenu(new SimpleMenuProvider(
                            (id, playerInv, p) -> new LanderMenu(id, playerInv, lander),
                            Component.literal("Lander Inventory")
                    ), buf -> buf.writeInt(lander.getId()));
                }
            }
        });
    }
}
