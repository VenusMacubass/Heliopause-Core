package net.venera.heliocore.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.entity.machine.electric.EnergyStorageEntity;
import net.venera.heliocore.item.hpc_custom.BatteryItem;
import net.venera.heliocore.screen.HpCMenuTypes;

public class EnergyStorageUnitMenu extends AbstractContainerMenu {
    public final EnergyStorageEntity blockEntity;
    private final Level level;
    public final ContainerData data;

    public EnergyStorageUnitMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public EnergyStorageUnitMenu(int containerId, Inventory  inventory, BlockEntity blockEntity) {
        super(HpCMenuTypes.ENERGY_STORAGE_UNIT_MENU.get(), containerId);
        this.blockEntity = ((EnergyStorageEntity)blockEntity);
        this.level = inventory.player.level();
        this.data = this.blockEntity.data;

        addDataSlots(this.data);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        //Input Slot
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 15, 19) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof BatteryItem;
            }
            @Override
            public int getMaxStackSize(ItemStack stack) {
                return 1;
            }
        });
        //Output slot
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 1, 145, 19) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof BatteryItem;
            }

            @Override
            public int getMaxStackSize(ItemStack stack) {
                return 1;
            }
        });
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 2;  //Number of slots of this specific menu (inventory and hotbar excluded)
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) { // Check if the slot clicked is one of the vanilla container slots
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX // This is a vanilla container slot so merge the stack into the tile inventory
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) { // This is a TE slot so merge the stack into the players inventory
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) { // If stack size == 0 (the entire stack was moved) set slot contents to null
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, HpCBlocks.ENERGY_STORAGE_UNIT.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 51 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
        }
    }

    public int getEnergyScaled(int pixels){
        int energy = this.data.get(0);
        int capacity = this.data.get(1);
        if(capacity == 0) {
            return 0;
        }
        return energy * pixels / capacity;
    }
}
