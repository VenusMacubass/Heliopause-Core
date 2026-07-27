package net.venera.heliocore.data;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.radiation.RadiationData;

import java.util.function.Supplier;

public class HpCAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, HeliopauseCore.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<RadiationData>> RADIATION_DATA = ATTACHMENT_TYPES.register("radiation_data",
                    () -> AttachmentType.serializable(RadiationData::new).build());

    public static final Supplier<AttachmentType<ItemStackHandler>> EQUIPMENT_INVENTORY = ATTACHMENT_TYPES.register("equipment_inventory",
                    () -> AttachmentType.serializable(() -> new ItemStackHandler(10)).build());

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
