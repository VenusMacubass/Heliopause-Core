package net.venera.heliocore.screen.entity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.event.HpCEvents;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.screen.HpCMenuTypes;
import net.venera.heliocore.item.HpCTags;

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

        this.addSlot(new Slot(inventory, 40, 139, 62) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
    }

    private void addCustomSlots() {
        this.addSlot(new SlotItemHandler(equipmentInventory, 0, 18, 22) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(HpCTags.Items.OXYGEN_MASK);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public int getMaxStackSize(ItemStack stack) { return 1; }

            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });

        this.addSlot(new SlotItemHandler(equipmentInventory, 1, 18, 42) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(HpCTags.Items.OXYGEN_CONNECTORS);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public int getMaxStackSize(ItemStack stack) { return 1; }
            
            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });

        this.addSlot(new SlotItemHandler(equipmentInventory, 2, 8, 62) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(HpCTags.Items.OXYGEN_TANK);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public int getMaxStackSize(ItemStack stack) { return 1; }
            
            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });

        this.addSlot(new SlotItemHandler(equipmentInventory, 3, 28, 62) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(HpCTags.Items.OXYGEN_TANK);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public int getMaxStackSize(ItemStack stack) { return 1; }
            
            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });

        for (int i = 0; i < 4; i++) {
            int j = i;
            this.addSlot(new SlotItemHandler(equipmentInventory, j + 4, 97, 8 + (j * 18)) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return switch(j){
                        case 0-> stack.is(HpCTags.Items.THERMAL_GEAR_HEAD);
                        case 1-> stack.is(HpCTags.Items.THERMAL_GEAR_TORSO);
                        case 2-> stack.is(HpCTags.Items.THERMAL_GEAR_LEGS);
                        case 3-> stack.is(HpCTags.Items.THERMAL_GEAR_HANDS_AND_FEET);
                        default -> false;
                    };
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public int getMaxStackSize(ItemStack stack) { return 1; }

                @Override
                public void setChanged() {
                    super.setChanged();
                    HpCEvents.syncToAllTracking(targetEntity);
                }
            });

        }

        this.addSlot(new SlotItemHandler(equipmentInventory, 8, 118, 26) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.ELYTRA);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
            
            @Override
            public int getMaxStackSize(ItemStack stack) { return 1; }
            
            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });

        this.addSlot(new SlotItemHandler(equipmentInventory, 9, 118, 44) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(HpCItems.MASS_BELT.get());
            }

            @Override
            public int getMaxStackSize() {return 1;}
            
            @Override
            public int getMaxStackSize(ItemStack stack) { return 1; }
            
            @Override
            public void setChanged() {
                super.setChanged();
                HpCEvents.syncToAllTracking(targetEntity);
            }
        });
    }

    private static final int CUSTOM_SLOT_COUNT = 10;
    private static final int VANILLA_MAIN_FIRST = CUSTOM_SLOT_COUNT; // 10
    private static final int VANILLA_MAIN_COUNT = 27;
    private static final int VANILLA_HOTBAR_FIRST = VANILLA_MAIN_FIRST + VANILLA_MAIN_COUNT; // 37
    private static final int VANILLA_HOTBAR_COUNT = 9;
    private static final int VANILLA_END = VANILLA_HOTBAR_FIRST + VANILLA_HOTBAR_COUNT; // 46
    private static final int OFFHAND_INDEX = 46;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // 1. Completely disable quick moving if clicking the offhand slot
        if (pIndex == OFFHAND_INDEX) {
            return ItemStack.EMPTY;
        }

        // 2. Custom Slots (0-9) -> Player Inventory + Hotbar (10-45)
        if (pIndex < CUSTOM_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_MAIN_FIRST, VANILLA_END, false)) {
                return ItemStack.EMPTY;
            }
        }
        // 3. Player Inventory + Hotbar (10-45) -> Custom Slots OR internal swapping
        else if (pIndex >= VANILLA_MAIN_FIRST && pIndex < VANILLA_END) {
            // First, try to send it to our custom equipment slots
            if (!moveItemStackTo(sourceStack, 0, CUSTOM_SLOT_COUNT, false)) {

                // If it can't go to equipment slots, swap it between main inventory and hotbar
                if (pIndex < VANILLA_HOTBAR_FIRST) {
                    // Main Inventory -> Hotbar
                    if (!moveItemStackTo(sourceStack, VANILLA_HOTBAR_FIRST, VANILLA_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // Hotbar -> Main Inventory
                    if (!moveItemStackTo(sourceStack, VANILLA_MAIN_FIRST, VANILLA_HOTBAR_FIRST, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        // If the stack size didn't change at all, the move failed entirely
        if (sourceStack.getCount() == copyOfSourceStack.getCount()) {
            return ItemStack.EMPTY;
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