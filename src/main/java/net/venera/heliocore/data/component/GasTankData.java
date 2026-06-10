package net.venera.heliocore.data.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.item.hpc_custom.GasTankItem;

import javax.annotation.Nullable;

public record GasTankData(@Nullable ResourceLocation fluidId, int amount) {
    public static final ResourceLocation OXYGEN_GAS =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "oxygen_gas");

    public static final Codec<GasTankData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("fluid", null).forGetter(GasTankData::fluidId),
            Codec.INT.fieldOf("amount").forGetter(GasTankData::amount)
    ).apply(instance, GasTankData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, GasTankData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, data) -> { // encoder: write data into the buffer
                        if (data.fluidId() == null) { // write nullable ResourceLocation manually
                            buf.writeBoolean(false);
                        } else {
                            buf.writeBoolean(true);
                            buf.writeResourceLocation(data.fluidId());
                        }
                        buf.writeVarInt(data.amount()); //write amount as variant
                    },

                    buf -> { // decoder: read data back from buffer
                        ResourceLocation fluid = null;
                        if (buf.readBoolean()) {
                            fluid = buf.readResourceLocation();
                        }
                        int amount = buf.readVarInt();
                        return new GasTankData(fluid, amount);
                    }
            );

    public boolean isEmpty() {
        return amount <= 0 || fluidId == null;
    }

    public boolean isOxygen() {
        return fluidId != null && fluidId.equals(OXYGEN_GAS);
    }

    public int getCapacity() {
        return GasTankItem.MAX_CAPACITY;
    }

    public int getSpace() {
        return GasTankItem.MAX_CAPACITY - amount;
    }
}
