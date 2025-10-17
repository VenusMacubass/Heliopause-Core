package net.venera.galacticraftcore.data.radiation;

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

    default void addRadiation(double amount) {
        getData().addRadiation(amount);
    }

    default double getRadiationPercent() {
        return getData().getRadPercentage();
    }
}
