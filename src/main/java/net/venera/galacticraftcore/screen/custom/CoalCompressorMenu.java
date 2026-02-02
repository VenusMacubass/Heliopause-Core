package net.venera.galacticraftcore.screen.custom;

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
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.block.entity.machine.CoalCompressorEntity;
import net.venera.galacticraftcore.screen.ModMenuTypes;

public class CoalCompressorMenu extends AbstractContainerMenu {
    public final CoalCompressorEntity blockEntity;
    private final Level level;
    public final ContainerData data;

    public CoalCompressorMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public CoalCompressorMenu(int containerId, Inventory  inventory, BlockEntity blockEntity) {
        super(ModMenuTypes.COAL_COMPRESSOR_MENU.get(), containerId);
        this.blockEntity = ((CoalCompressorEntity)blockEntity);
        this.level = inventory.player.level();
        this.data = this.blockEntity.data;

        addDataSlots(this.data);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        final int GRID_START_X = 19;
        final int GRID_START_Y = 18;
        final int SLOT_SPACING = 18;

// Create the 3x3 grid (slots 0-8)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int slotIndex = row * 3 + col;
                int x = GRID_START_X + col * SLOT_SPACING;
                int y = GRID_START_Y + row * SLOT_SPACING;
                this.addSlot(new SlotItemHandler(this.blockEntity.inventory, slotIndex, x, y));
            }
        }
        // FUEL SLOT with restriction (Step 3)
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 9, 55, 75) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getBurnTime(null) > 0; // Only accept fuel items
            }
        });

        // OUTPUT SLOT with restriction (Step 4)
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 10, 138, 38) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false; // Prevent inserting into output slot
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

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 11;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
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
                player, ModBlocks.COAL_COMPRESSOR.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 110 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 168));
        }
    }
    public int getProgress() {
        return data.get(0); // progress
    }

    public int getMaxProgress() {
        return data.get(1); // maxProgress
    }
    public int getBurnTime() {
        return data.get(2); // burnTime
    }
    public int getMaxBurnTime() {
        return data.get(3); // maxBurnTime
    }
}
