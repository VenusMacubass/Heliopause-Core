package net.venera.heliocore.screen.hpc_custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.entity.machine.electric.PCBFabricatorEntity;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.item.HpCTags;
import net.venera.heliocore.item.hpc_custom.BatteryItem;
import net.venera.heliocore.screen.HpCMenuTypes;

public class PCBFabricatorMenu extends AbstractContainerMenu {
    public final PCBFabricatorEntity blockEntity;
    private final Level level;
    public final ContainerData data;
    
    public PCBFabricatorMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public PCBFabricatorMenu(int containerId, Inventory  inventory, BlockEntity blockEntity) {
        super(HpCMenuTypes.PCB_FABRICATOR_MENU.get(), containerId);
        this.blockEntity = ((PCBFabricatorEntity)blockEntity);
        this.level = inventory.player.level();
        this.data = this.blockEntity.data;

        addDataSlots(this.data);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 8, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == Items.DIAMOND;
            }
        });

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 1, 26, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Tags.Items.INGOTS);
            }
        });

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 2, 47, 19) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == Items.REDSTONE;
            }
        });

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 3, 55, 49) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(HpCTags.Items.CIRCUIT_MATERIALS);
            }
        });
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 4, 86, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(HpCItems.RAW_SILICON);
            }
        });
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 5, 104, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(HpCItems.RAW_SILICON);
            }
        });
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 6, 152, 34) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 7, 94, 63) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof BatteryItem;
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
    private static final int TE_INVENTORY_SLOT_COUNT = 8;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
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
                player, HpCBlocks.PCB_FABRICATOR.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public int getEnergy() {
        return this.blockEntity.getEnergyStorage().getEnergyStored();
    }

    public int getMaxEnergy() {
        return this.blockEntity.getEnergyStorage().getMaxEnergyStored();
    }

    public int getEnergyScaled(int pixels) {
        int energy = getEnergy();
        int capacity = getMaxEnergy();
        if (capacity == 0) return 0;
        return (int) ((long) energy * pixels / capacity);
    }

    public boolean isActive() {
        return this.data.get(0) > 0;
    }

    public int getArrowScaled(int pixels) {
        int progress = this.data.get(1);
        int maxProgress = this.data.get(2);
        if (maxProgress == 0 || progress == 0) return 0;
        return Math.min((int) ((float) progress / maxProgress * pixels), pixels);
    }
    
    public boolean hasEnoughEnergy() {
        return this.data.get(0) > 0;
    }
}
