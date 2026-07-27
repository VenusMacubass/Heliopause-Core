package net.venera.heliocore.screen.hpc_custom;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.event.HpCEvents;
import net.venera.heliocore.screen.HpCMenuTypes;
import net.venera.heliocore.util.HpCTags;

public class HpCEquipmentMenu extends AbstractContainerMenu {
    public final LivingEntity targetEntity;
    private final ItemStackHandler equipmentInventory;
    
    public HpCEquipmentMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(containerId, inv, (LivingEntity) inv.player.level().getEntity(extraData.readInt()));
    }
    
    public HpCEquipmentMenu(int containerId, Inventory inventory, LivingEntity targetEntity) {
        super(HpCMenuTypes.EQUIPMENT_MENU.get(), containerId);
        this.targetEntity = targetEntity;
        
        this.equipmentInventory = (this.targetEntity != null) ?
                this.targetEntity.getData(HpCAttachments.EQUIPMENT_INVENTORY) :
                new ItemStackHandler(10);

        addCustomSlots();
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
    }

    private void addCustomSlots() {
        this.addSlot(new SlotItemHandler(equipmentInventory, 0, 18, 22) {
            @Override
            public boolean mayPlace(ItemStack stack) { return stack.is(HpCTags.Items.OXYGEN_MASK); }
            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });
        
        this.addSlot(new SlotItemHandler(equipmentInventory, 1, 18, 42) {
            @Override
            public boolean mayPlace(ItemStack stack) { return stack.is(HpCTags.Items.OXYGEN_CONNECTORS); }
            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });
        
        this.addSlot(new SlotItemHandler(equipmentInventory, 2, 8, 62) {
            @Override
            public boolean mayPlace(ItemStack stack) { return stack.is(HpCTags.Items.OXYGEN_TANK); }
            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });

        this.addSlot(new SlotItemHandler(equipmentInventory, 3, 28, 62) {
            @Override
            public boolean mayPlace(ItemStack stack) { return stack.is(HpCTags.Items.OXYGEN_TANK); }
            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });
        
        for (int i = 0; i < 4; i++) {
            this.addSlot(new SlotItemHandler(equipmentInventory, i+4, 97, 8 + (i * 18)){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false; //Reserved for thermal suit items
                }
                @Override
                public void setChanged() {
                    super.setChanged();
                    HpCEvents.syncToAllTracking(targetEntity);
                }
            });
            
        }

        this.addSlot(new SlotItemHandler(equipmentInventory, 8, 118, 26) {
            @Override
            public boolean mayPlace(ItemStack stack) { return stack.is(Items.ELYTRA );}
            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });

        this.addSlot(new SlotItemHandler(equipmentInventory, 9, 118, 44) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false; //Reserved for Mass Belt
            }
            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });
    }
    
    private static final int CUSTOM_SLOT_COUNT = 10;
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

    private static final int CUSTOM_FIRST_SLOT_INDEX = 0;
    private static final int VANILLA_FIRST_SLOT_INDEX = CUSTOM_FIRST_SLOT_INDEX + CUSTOM_SLOT_COUNT;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        
        if (pIndex < CUSTOM_FIRST_SLOT_INDEX + CUSTOM_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        }
        
        else if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, CUSTOM_FIRST_SLOT_INDEX, CUSTOM_FIRST_SLOT_INDEX + CUSTOM_SLOT_COUNT, false)) {
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

    @Override
    public boolean stillValid(Player player) {
        if (this.targetEntity == null) {
            return player.level().isClientSide();
        }
        return this.targetEntity.isAlive() && player.distanceToSqr(this.targetEntity) < 64.0D;
    }
}