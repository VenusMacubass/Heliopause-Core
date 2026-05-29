package net.venera.heliocore.item.hpc_custom;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.data.component.HpCDataComponents;

import javax.annotation.Nullable;

public class CanisterItem extends Item {
    public static final int MAX_CAPACITY = 1000;

    public CanisterItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.set(HpCDataComponents.CANISTER_COMPONENT.get(), new CanisterData(null, 0));
        return stack;
    }

    public CanisterData getCanisterData(ItemStack stack) {
        return stack.get(HpCDataComponents.CANISTER_COMPONENT.get());
    }

    public ItemStack setCanisterData(ItemStack stack, @Nullable ResourceLocation fluid, int amount) {
        stack.set(HpCDataComponents.CANISTER_COMPONENT.get(), new CanisterData(fluid, amount));
        return stack;
    }

    public int fill(ItemStack stack, ResourceLocation fluid, int amountToAdd) {
        var data = getCanisterData(stack);
        if (data == null || data.fluidId() == null) {
            int filled = Math.min(MAX_CAPACITY, amountToAdd);
            setCanisterData(stack, fluid, filled);
            return filled;
        }
        if (!data.fluidId().equals(fluid)) return 0;
        int filled = Math.min(MAX_CAPACITY - data.amount(), amountToAdd);
        setCanisterData(stack, fluid, data.amount() + filled);
        return filled;
    }

    public int drain(ItemStack stack, int amountToDrain) {
        var data = getCanisterData(stack);
        if (data == null || data.amount() <= 0) return 0;

        int drained = Math.min(data.amount(), amountToDrain);
        int remaining = data.amount() - drained;
        if (remaining <= 0)
            setCanisterData(stack, null, 0);
        else
            setCanisterData(stack, data.fluidId(), remaining);
        return drained;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        var data = getCanisterData(stack);
        if(data == null || data.isEmpty()){return 64;}
        else{return 1;}
    }

    @Override
    public Component getName(ItemStack stack) {
        var data = getCanisterData(stack);
        if (data != null && data.amount() > 0 && data.fluidId() != null) {
            return Component.translatable("item.heliocore.canister.filled",
                    super.getName(stack),
                    Component.translatable("fluid.heliocore." + data.fluidId().getPath()),
                    (int) ((data.amount() / (float) MAX_CAPACITY) * 100f));
        }
        return super.getName(stack);
    }
}
