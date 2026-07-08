package net.venera.heliocore.screen.hpc_custom;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;
import net.venera.heliocore.item.hpc_custom.BatteryItem;
import net.venera.heliocore.screen.HpCMenuTypes;

public class RocketMenu extends AbstractContainerMenu{
    public final Tier1RocketEntity rocket;
    
    public RocketMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(containerId, inv, (Tier1RocketEntity) inv.player.level().getEntity(extraData.readInt()));
    }
   
    public RocketMenu(int containerId, Inventory inventory, Tier1RocketEntity rocket) {
        super(HpCMenuTypes.ROCKET_MENU.get(), containerId);
        this.rocket = rocket;
        IItemHandler handler = (this.rocket != null) ? this.rocket.inventory : new ItemStackHandler(28);
        int slotIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new SlotItemHandler(this.rocket.inventory, slotIndex++, 8 + col * 18, 64 + row * 18));
            }
        }
        this.addSlot(new SlotItemHandler(this.rocket.inventory, 27, 130, 42) {
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
        if (this.rocket == null) {
            return player.level().isClientSide();
        }
        
        return this.rocket.isAlive() && player.distanceToSqr(this.rocket) < 64.0D;
    }

    public int getEnergyScaled(int pixels) {
        if (this.rocket == null) return 0;
        int currentEnergy = this.rocket.getEnergyAmount();
        int maxEnergy = this.rocket.MAX_ENERGY;
        if (maxEnergy == 0 || currentEnergy <= 0) return 0;

        return (currentEnergy * pixels) / maxEnergy;
    }

    public int getFuelScaled(int pixels) {
        if (this.rocket == null) return 0;
        int currentFuel = this.rocket.getFuelAmount();
        int maxFuel = this.rocket.MAX_FUEL;
        if (maxFuel == 0 || currentFuel <= 0) return 0;

        return (currentFuel * pixels) / maxFuel;
    }
}
