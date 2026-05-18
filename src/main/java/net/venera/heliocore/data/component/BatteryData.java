package net.venera.heliocore.data.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BatteryData(int energy, int capacity) {

    public BatteryData {
        if (capacity < 0) capacity = 0;             // Prevent negative capacity
        if (energy < 0) energy = 0;                 // Prevent negative energy
        if (energy > capacity) energy = capacity;   // Prevent overfill
    }

    public static final Codec<BatteryData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("energy").forGetter(BatteryData::energy),
            Codec.INT.fieldOf("capacity").forGetter(BatteryData::capacity)
    ).apply(instance, BatteryData::new));

    // STREAM_CODEC (Sent as [Int][Int])
    public static final StreamCodec<ByteBuf, BatteryData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, BatteryData::energy,    // First Int
            ByteBufCodecs.INT, BatteryData::capacity,  // Second Int
            BatteryData::new                           // Factory
    );

    public int getEnergy(){return energy;}
    public float getEnergyPercentage(){return (float) (energy * 100) / capacity;}
    public boolean isEmpty(){return energy <= 0;}
    public boolean isFull(){return getSpace() <= 0;}
    public int getSpace(){return capacity - energy;}

    public BatteryData setEnergy(int newAmount) {
        return new BatteryData(newAmount, this.capacity);
    }
}
