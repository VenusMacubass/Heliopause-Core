package net.venera.heliocore.screen.block_entity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.entity.machine.MagneticAssemblyPlatformEntity;
import net.venera.heliocore.item.HpCTags;
import net.venera.heliocore.screen.HpCMenuTypes;

public class MagneticAssemblyPlatformMenu  extends AbstractContainerMenu {
    public final MagneticAssemblyPlatformEntity blockEntity;
    private final Level level;
    public final ContainerData data;

    public MagneticAssemblyPlatformMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(18));
    }

    public MagneticAssemblyPlatformMenu(int containerId, Inventory  inventory, BlockEntity blockEntity, ContainerData data) {
        super(HpCMenuTypes.MAGNETIC_ASSEMBLY_PLATFORM_MENU.get(), containerId);
        this.blockEntity = ((MagneticAssemblyPlatformEntity)blockEntity);
        this.level = inventory.player.level();
        this.data = data;

        addDataSlots(this.data);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 17, 148, 133) { //Rocket
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
            @Override
            public void onTake(Player player, ItemStack stack) {
                for (int i = 0; i < 17; i++) {
                    ((MagneticAssemblyPlatformEntity) blockEntity).inventory.extractItem(i, 1, false);
                }
                super.onTake(player, stack);
            }
        });
        
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 16, 80, 16) { //Nose Cone
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(HpCTags.Items.NOSES);
            }
        });
        
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 15, 44, 46) { //Booster Left
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 14, 116, 46) { //Booster Right
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        
        for(int i = 0; i < 8; i++){
            this.addSlot(new SlotItemHandler(this.blockEntity.inventory, i+6, i < 4 ? 94:66, 94 - ((i%4)*19)) { //Hull
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(HpCTags.Items.HULLS);
                }
            });
        }
        
        for(int i = 0; i < 4; i++){
            this.addSlot(new SlotItemHandler(this.blockEntity.inventory, i+2, 121 - 82 * (i >> 1), 107 - 20 * (i & 1)) { //Fins
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.is(HpCTags.Items.FINS);
                }
            });
        }
        
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 1, 80, 116) { 
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(HpCTags.Items.BASE);
            }
        });
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 80, 137) { 
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(HpCTags.Items.ENGINES); 
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
    private static final int TE_INVENTORY_SLOT_COUNT = 18;  //must be the number of slots

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (sourceStack.is(HpCTags.Items.NOSES)) {
                if (!moveItemStackTo(sourceStack, 37, 38, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (sourceStack.is(HpCTags.Items.HULLS)) {
                if (!moveItemStackTo(sourceStack, 40, 48, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (sourceStack.is(HpCTags.Items.FINS)) {
                if (!moveItemStackTo(sourceStack, 48, 52, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (sourceStack.is(HpCTags.Items.BASE)) {
                if (!moveItemStackTo(sourceStack, 52, 53, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (sourceStack.is(HpCTags.Items.ENGINES)) {
                if (!moveItemStackTo(sourceStack, 53, 54, false)) {
                    return ItemStack.EMPTY;
                }
            }

        }
        else if (pIndex >= TE_INVENTORY_FIRST_SLOT_INDEX && pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
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

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, HpCBlocks.MAGNETIC_ASSEMBLY_PLATFORM.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 158 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 216));
        }
    }

    public boolean getActivation(){
        return this.data.get(4) == 1;
    }
}
