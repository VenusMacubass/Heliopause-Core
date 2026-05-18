package net.venera.heliocore.data.radiation;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IRadiation extends INBTSerializable<CompoundTag> {
    RadiationData getData();
    void setData(RadiationData data);

    default double getRadiationLevel() {
        return getData().getRadiation();
    }

    default void setRadiationLevel(double radiation) {
        getData().setRadiation(radiation);
    }

    default void changeRadiation(double amount, boolean vector) {
        getData().changeRadiation(amount, vector);
    }

    default double getRadiationPercent() {
        return getData().getRadPercentage();
    }
}
