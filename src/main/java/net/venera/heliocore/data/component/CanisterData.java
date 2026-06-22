package net.venera.heliocore.data.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import javax.annotation.Nullable;

public record CanisterData(@Nullable ResourceLocation fluidId, int amount) {
    public static final ResourceLocation CRUDE_OIL =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "crude_oil");
    public static final ResourceLocation REFINED_FUEL =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "refined_fuel");
    public static final ResourceLocation LIQUID_OXYGEN =
            ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "oxygen_liquid");

    public static final Codec<CanisterData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("fluid", null).forGetter(CanisterData::fluidId),
            Codec.INT.fieldOf("amount").forGetter(CanisterData::amount)
    ).apply(instance, CanisterData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CanisterData> STREAM_CODEC =
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

    public boolean isOxygen() {
        return fluidId != null && fluidId.equals(LIQUID_OXYGEN);
    }
    
    public int getSpace() {
        return CanisterItem.MAX_CAPACITY - amount;
    }

    public Fluid getFluid() {
        if (fluidId == null) return null;
        return BuiltInRegistries.FLUID.get(fluidId);
    }
}
