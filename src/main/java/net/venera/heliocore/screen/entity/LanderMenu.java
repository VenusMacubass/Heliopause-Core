package net.venera.heliocore.screen.entity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.venera.heliocore.entity.rideable.Tier1RocketLanderEntity;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import net.venera.heliocore.item.hpc_custom.RocketItem;
import net.venera.heliocore.screen.HpCMenuTypes;

public class LanderMenu extends AbstractContainerMenu {
    public final Tier1RocketLanderEntity lander;
    
    public LanderMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(containerId, inv, (Tier1RocketLanderEntity) inv.player.level().getEntity(extraData.readInt()));
    }
    

    public LanderMenu(int containerId, Inventory inventory, Tier1RocketLanderEntity lander) {
        super(HpCMenuTypes.LANDER_MENU.get(), containerId);
        this.lander = lander;
        IItemHandler handler = (this.lander != null) ? this.lander.inventory : new ItemStackHandler(29); // Changed to 29
        
        int slotIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new SlotItemHandler(this.lander.inventory, slotIndex++, 8 + col * 18, 63 + row * 18)); // Removed offsetY
            }
        }
        
        this.addSlot(new SlotItemHandler(this.lander.inventory, 27, 132, 40) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof CanisterItem;
            }
            @Override
            public int getMaxStackSize(ItemStack stack) { return 1; }
        });

        this.addSlot(new SlotItemHandler(this.lander.inventory, 28, 43, 19) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof RocketItem;
            }
            @Override
            public int getMaxStackSize(ItemStack stack) { return 1; }
        });
        
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
    }

    private static final int TE_INVENTORY_SLOT_COUNT = 29;
    private static final int VANILLA_SLOT_COUNT = 36;
    private static final int VANILLA_FIRST_SLOT_INDEX = TE_INVENTORY_SLOT_COUNT; // Index 29

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        
        if (pIndex < VANILLA_FIRST_SLOT_INDEX) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        }
        else if (pIndex >= VANILLA_FIRST_SLOT_INDEX && pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, 0, TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 128 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 186));
        }
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
