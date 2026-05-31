package net.venera.heliocore.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.*;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.entity.machine.electric.RefineryEntity;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.item.hpc_custom.BatteryItem;
import net.venera.heliocore.screen.HpCMenuTypes;

public class RefineryMenu extends AbstractContainerMenu {
    public final RefineryEntity blockEntity;
    private final Level level;
    public final ContainerData data;

    public RefineryMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public RefineryMenu(int containerId, Inventory  inventory, BlockEntity blockEntity) {
        super(HpCMenuTypes.REFINERY_MENU.get(), containerId);
        this.blockEntity = ((RefineryEntity)blockEntity);
        this.level = inventory.player.level();
        this.data = this.blockEntity.data;

        addDataSlots(this.data);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 28, 13) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == HpCFluids.CRUDE_OIL.getBucket() || stack.getItem() == Items.BUCKET || stack.getItem() == HpCItems.CANISTER.get();
            }

            @Override
            public int getMaxStackSize(ItemStack stack) {
                return 1;
            }
        });

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 1, 132, 13) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == Items.BUCKET || stack.getItem() == HpCItems.CANISTER.get();
            }
            @Override
            public int getMaxStackSize(ItemStack stack) {
                return 1;
            }
        });

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 2, 61, 24) {
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
    private static final int TE_INVENTORY_SLOT_COUNT = 3;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, HpCBlocks.REFINERY.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 65 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 123));
        }
    }

    public boolean isActive() {
        return data.get(3) == 1;
    }

    public int getOilScaled(int pixels) {
        int max = data.get(2);
        return max == 0 ? 0 : data.get(0) * pixels / max;
    }

    public int getFuelScaled(int pixels) {
        int max = data.get(2);
        return max == 0 ? 0 : data.get(1) * pixels / max;
    }
    
    public int getEnergyScaled(int pixels) {
        int max = data.get(5);
        return max == 0 ? 0 : data.get(4) * pixels / max;
    }
}
