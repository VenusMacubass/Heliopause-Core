package net.venera.heliocore.screen.hpc_custom;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.entity.MagneticCraftingTableEntity;

public class MagneticCraftingTableMenu extends CraftingMenu {
    private final MagneticCraftingTableEntity blockEntity;
    private final ContainerLevelAccess access;
    private boolean isClosing = false;
    private boolean isLoading; 

    public MagneticCraftingTableMenu(int containerId, Inventory playerInv, ContainerLevelAccess access, MagneticCraftingTableEntity blockEntity) {
        super(containerId, playerInv, access);
        this.blockEntity = blockEntity;
        this.access = access;
        
        if (this.blockEntity != null) {
            for (int i = 0; i < 9; i++) {
                this.getSlot(i + 1).set(blockEntity.getInventory().getStackInSlot(i).copy());
            }
        }
        
        this.isLoading = false;
    }

    @Override
    public void slotsChanged(Container inventory) {
        super.slotsChanged(inventory);
        
        if (this.blockEntity != null && !this.isClosing && !this.isLoading) {
            for (int i = 0; i < 9; i++) {
                this.blockEntity.getInventory().setStackInSlot(i, this.getSlot(i + 1).getItem().copy());
            }
            this.blockEntity.setChanged();
        }
    }

    @Override
    public void removed(Player player) {
        this.isClosing = true;
        for (int i = 1; i <= 9; i++) {
            this.getSlot(i).set(ItemStack.EMPTY);
        }
        super.removed(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, HpCBlocks.MAGNETIC_CRAFTING_TABLE.get());
    }
}