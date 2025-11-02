package net.venera.galacticraftcore.data.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.item.custom.CanisterItem;

import javax.annotation.Nullable;
import java.util.List;

public record CanisterData(@Nullable ResourceLocation fluidId, int amount) {
    public static final ResourceLocation CRUDE_OIL =
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "crude_oil");
    public static final ResourceLocation REFINED_FUEL =
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "refined_fuel");

    public static final Codec<CanisterData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("fluid", null).forGetter(CanisterData::fluidId),
            Codec.INT.fieldOf("amount").forGetter(CanisterData::amount)
    ).apply(instance, CanisterData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CanisterData> STREAM_CODEC =
            StreamCodec.of(
                    // encoder: write data into the buffer
                    (buf, data) -> {
                        // write nullable ResourceLocation manually
                        if (data.fluidId() == null) {
                            buf.writeBoolean(false);
                        } else {
                            buf.writeBoolean(true);
                            buf.writeResourceLocation(data.fluidId());
                        }
                        // write amount as varint
                        buf.writeVarInt(data.amount());
                    },
                    // decoder: read data back from buffer
                    buf -> {
                        ResourceLocation fluid = null;
                        if (buf.readBoolean()) {
                            fluid = buf.readResourceLocation();
                        }
                        int amount = buf.readVarInt();
                        return new CanisterData(fluid, amount);
                    }
            );

    public boolean isEmpty() {
        return amount <= 0 || fluidId == null;
    }

    public boolean isCrudeOil() {
        return fluidId != null && fluidId.equals(CRUDE_OIL);
    }

    public boolean isRefinedFuel() {
        return fluidId != null && fluidId.equals(REFINED_FUEL);
    }

    public int getSpace() {
        return CanisterItem.MAX_CAPACITY - amount;
    }

}
