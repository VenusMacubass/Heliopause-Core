package net.venera.galacticraftcore.item.custom;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class CrudeOilBucketItem extends BucketItem {
    public CrudeOilBucketItem(Properties builder, Supplier<? extends Fluid> supplier) {
        super(supplier.get(), builder);
    }
}
