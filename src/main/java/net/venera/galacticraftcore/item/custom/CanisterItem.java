package net.venera.galacticraftcore.item.custom;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.data.component.CanisterData;
import net.venera.galacticraftcore.data.component.ModDataComponents;
import net.venera.galacticraftcore.render.CanisterRenderer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CanisterItem extends Item {
    public static final int MAX_CAPACITY = 1000;
    public boolean isEmpty;

    public CanisterItem(Properties properties) {
        super(properties);
    }

    public static final CanisterRenderer CANISTER_RENDERER = new CanisterRenderer();

    @Override
    @SuppressWarnings("removal")
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return CANISTER_RENDERER;
            }
        });
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.set(ModDataComponents.CANISTER_COMPONENT.get(), new CanisterData(null, 0));
        return stack;
    }

    public CanisterData getCanisterData(ItemStack stack) {
        return stack.get(ModDataComponents.CANISTER_COMPONENT.get());
    }

    public void setCanisterData(ItemStack stack, @Nullable ResourceLocation fluid, int amount) {
        stack.set(ModDataComponents.CANISTER_COMPONENT.get(), new CanisterData(fluid, amount));
    }

    public int fill(ItemStack stack, ResourceLocation fluid, int amountToAdd) {
        GalacticraftCore.LOGGER.info("Filling canister item by " + amountToAdd);
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
        GalacticraftCore.LOGGER.info("Draining canister item by " + amountToDrain);
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
    public Component getName(ItemStack stack) {
        var data = getCanisterData(stack);
        if (data != null && data.amount() > 0 && data.fluidId() != null) {
            return Component.translatable("item.galacticraftcore.canister.filled",
                    super.getName(stack),
                    Component.translatable("fluid.galacticraftcore." + data.fluidId().getPath()),
                    (int) ((data.amount() / (float) MAX_CAPACITY) * 100f));
        }
        return super.getName(stack);
    }
}
