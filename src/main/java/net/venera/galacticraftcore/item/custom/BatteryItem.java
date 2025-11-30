package net.venera.galacticraftcore.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.venera.galacticraftcore.data.component.BatteryData;
import net.venera.galacticraftcore.data.component.ModDataComponents;

import java.util.List;

public class BatteryItem extends Item {
    private final int maxCapacity;
    private final int transferRate;

    public BatteryItem(Properties properties, int maxCapacity, int transferRate) {
        super(properties);
        this.maxCapacity = maxCapacity;
        this.transferRate = transferRate;
    }

    // Returns: The amount of energy actually accepted
    public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
        BatteryData current = getBatteryData(stack);

        int energyToAdd = Math.min(maxReceive, transferRate);

        int proposedEnergy = current.energy() + energyToAdd;

        BatteryData newData = current.setEnergy(proposedEnergy);

        int actuallyAdded = newData.energy() - current.energy();

        if (!simulate && actuallyAdded > 0) {
            setBatteryData(stack, newData);
        }

        return actuallyAdded;
    }

    // Returns: The amount of energy actually extracted
    public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
        BatteryData current = getBatteryData(stack);

        int energyToRemove = Math.min(maxExtract, transferRate);
        int proposedEnergy = current.energy() - energyToRemove;

        BatteryData newData = current.setEnergy(proposedEnergy);

        int actuallyRemoved = current.energy() - newData.energy();

        if (!simulate && actuallyRemoved > 0) {
            setBatteryData(stack, newData);
        }

        return actuallyRemoved;
    }

    public BatteryData getBatteryData(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.BATTERY_COMPONENT, new BatteryData(0, maxCapacity));
    }

    public void setBatteryData(ItemStack stack, BatteryData data) {
        stack.set(ModDataComponents.BATTERY_COMPONENT, data);
    }

    public int getTransferRate(){
        return transferRate;
    }

    public ItemStack createFullInstance() {
        ItemStack stack = new ItemStack(this);
        setBatteryData(stack, new BatteryData(this.maxCapacity, this.maxCapacity));
        return stack;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        BatteryData data = getBatteryData(stack);
        // Avoid divide by zero
        if (data.capacity() == 0) return 0;
        return Math.round(13.0F * ((float)data.energy() / data.capacity()));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getBatteryData(stack).getSpace() > 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        BatteryData data = getBatteryData(stack);
        tooltipComponents.add(Component.literal(data.energy() + " / " + data.capacity() + " FE").withStyle(ChatFormatting.GRAY));
    }
}
