package net.venera.galacticraftcore.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.data.radiation.RadiationData;

public class ModAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, GalacticraftCore.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<RadiationData>> RADIATION_DATA = ATTACHMENT_TYPES.register("radiation_data",
                    () -> AttachmentType.serializable(RadiationData::new).build());


    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
