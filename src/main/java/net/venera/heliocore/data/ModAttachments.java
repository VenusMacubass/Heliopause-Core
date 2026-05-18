package net.venera.heliocore.data;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.radiation.RadiationData;

public class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, HeliopauseCore.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<RadiationData>> RADIATION_DATA = ATTACHMENT_TYPES.register("radiation_data",
                    () -> AttachmentType.serializable(RadiationData::new).build());


    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
