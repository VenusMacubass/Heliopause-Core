package net.venera.heliocore.screen.custom;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.venera.heliocore.entity.rideable.Tier1RocketLanderEntity;
import net.venera.heliocore.item.hpc_custom.BatteryItem;
import net.venera.heliocore.screen.HpCMenuTypes;

public class LanderMenu extends AbstractContainerMenu {
    public final Tier1RocketLanderEntity lander;
    
    public LanderMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(containerId, inv, (Tier1RocketLanderEntity) inv.player.level().getEntity(extraData.readInt()));
    }
    
    public LanderMenu(int containerId, Inventory inventory, Tier1RocketLanderEntity lander) {
        super(HpCMenuTypes.LANDER_MENU.get(), containerId);
        this.lander = lander;
        IItemHandler handler = (this.lander != null) ? this.lander.inventory : new ItemStackHandler(28);
        int slotIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new SlotItemHandler(this.lander.inventory, slotIndex++, 8 + col * 18, 64 + row * 18));
            }
        }
        this.addSlot(new SlotItemHandler(this.lander.inventory, 27, 130, 42) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return  stack.getItem() instanceof BatteryItem;
            }

            @Override
            public int getMaxStackSize(ItemStack stack) {
                return 1;
            }
        });
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.lander == null) {
            return player.level().isClientSide();
        }

        return this.lander.isAlive() && player.distanceToSqr(this.lander) < 64.0D;
    }

    public int getEnergyScaled(int pixels) {
        if (this.lander == null) return 0;
        int currentEnergy = this.lander.getEnergyAmount();
        int maxEnergy = this.lander.MAX_ENERGY;
        if (maxEnergy == 0 || currentEnergy <= 0) return 0;

        return (currentEnergy * pixels) / maxEnergy;
    }

    public int getFuelScaled(int pixels) {
        if (this.lander == null) return 0;
        int currentFuel = this.lander.getFuelAmount();
        int maxFuel = this.lander.MAX_FUEL;
        if (maxFuel == 0 || currentFuel <= 0) return 0;

        return (currentFuel * pixels) / maxFuel;
    }
}
