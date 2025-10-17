package net.venera.galacticraftcore.data.radiation;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class RadiationData implements INBTSerializable<CompoundTag> {

    public static final double MIN_RADIATION = 0D;
    public static final double MAX_RADIATION = 1000D;
    private double radiation = 0.0;

    public RadiationData() {}

    public double getRadiation() {
        return radiation;
    }

    public void setRadiation(double level) {
        this.radiation = Math.min(Math.max(level, MIN_RADIATION), MAX_RADIATION);
    }

    public void addRadiation(double amount) {
        setRadiation(this.radiation + amount);
    }

    public void subRadiation(double amount) {
        setRadiation(this.radiation - amount);
    }

    public double getRadPercentage(){
        return radiation/MAX_RADIATION;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("radiation_data", radiation);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compoundTag) {
        this.radiation = compoundTag.getDouble("radiation_data");
    }
}