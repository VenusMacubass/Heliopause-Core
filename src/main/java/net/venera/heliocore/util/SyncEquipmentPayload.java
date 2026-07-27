package net.venera.heliocore.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.HpCAttachments;

public record SyncEquipmentPayload(int entityId, CompoundTag inventoryData) implements CustomPacketPayload {
    public static final Type<SyncEquipmentPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "sync_equipment"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEquipmentPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncEquipmentPayload::entityId,
            ByteBufCodecs.COMPOUND_TAG, SyncEquipmentPayload::inventoryData,
            SyncEquipmentPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SyncEquipmentPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Entity entity = context.player().level().getEntity(payload.entityId());
            if (entity instanceof LivingEntity livingEntity) {
                var inventory = livingEntity.getData(HpCAttachments.EQUIPMENT_INVENTORY);
                inventory.deserializeNBT(context.player().registryAccess(), payload.inventoryData());
            }
        });
    }
}
