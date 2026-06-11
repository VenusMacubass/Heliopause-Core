package net.venera.heliocore.item.hpc_custom;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.venera.heliocore.data.component.BatteryData;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.data.component.HpCDataComponents;

import javax.annotation.Nullable;
import java.awt.*;

public class GasTankItem extends Item {
    public static final int MAX_CAPACITY = 1000;

    public GasTankItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.set(HpCDataComponents.GAS_TANK_COMPONENT.get(), new GasTankData(null, 0));
        return stack;
    }

    public GasTankData getGasTankData(ItemStack stack) {
        return stack.get(HpCDataComponents.GAS_TANK_COMPONENT.get());
    }

    public ItemStack setGasTankData(ItemStack stack, @Nullable ResourceLocation fluid, int amount) {
        stack.set(HpCDataComponents.GAS_TANK_COMPONENT.get(), new GasTankData(fluid, amount));
        return stack;
    }

    public int fill(ItemStack stack, ResourceLocation fluid, int amountToAdd) {
        var data = getGasTankData(stack);
        if (data == null || data.fluidId() == null) {
            int filled = Math.min(MAX_CAPACITY, amountToAdd);
            setGasTankData(stack, fluid, filled);
            return filled;
        }
        if (!data.fluidId().equals(fluid)) return 0;
        int filled = Math.min(MAX_CAPACITY - data.amount(), amountToAdd);
        setGasTankData(stack, fluid, data.amount() + filled);
        return filled;
    }

    public int drain(ItemStack stack, int amountToDrain) {
        var data = getGasTankData(stack);
        if (data == null || data.amount() <= 0) return 0;

        int drained = Math.min(data.amount(), amountToDrain);
        int remaining = data.amount() - drained;
        if (remaining <= 0)
            setGasTankData(stack, null, 0);
        else
            setGasTankData(stack, data.fluidId(), remaining);
        return drained;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        var data = getGasTankData(stack);
        if(data == null || data.isEmpty()){return 64;}
        else{return 1;}
    }

    @Override
    public Component getName(ItemStack stack) {
        var data = getGasTankData(stack);
        if (data != null && data.amount() > 0 && data.fluidId() != null) {
            return Component.translatable("item.heliocore.gas_tank.filled",
                    super.getName(stack),
                    Component.translatable("fluid.heliocore." + data.fluidId().getPath()),
                    (int) ((data.amount() / (float) MAX_CAPACITY) * 100f));
        }
        return super.getName(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        GasTankData data = getGasTankData(stack);
        if (data == null || data.getCapacity() == 0) return 0;
        return Math.round(13.0F * ((float)data.amount() / data.getCapacity()));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        GasTankData data = getGasTankData(stack);
        if (data == null || data.getCapacity() == 0) return Color.RED.getRGB();
        if (data.amount() >= data.getCapacity() * 0.66f) {
            return Color.GREEN.getRGB();
        } else if (data.amount() >= data.getCapacity() * 0.33f) {
            return Color.YELLOW.getRGB();
        } else {
            return Color.RED.getRGB();
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        GasTankData data = getGasTankData(stack);
        return data != null && !data.isEmpty();
    }
}
