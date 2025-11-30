package net.venera.galacticraftcore.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.galacticraftcore.block.custom.EnergyStorageUnitBlock;
import net.venera.galacticraftcore.item.custom.BatteryItem;
import net.venera.galacticraftcore.screen.custom.EnergyStorageUnitMenu;
import org.jetbrains.annotations.Nullable;

public class EnergyStorageUnitEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
            }
        }
    };

    private final int INPUT_SLOT = 0;
    private final int OUTPUT_SLOT = 1;
    private final EnergyStorage energyUnitStorage;
    public final ContainerData data;
    public final int energyTransferRate = 10;
    public final int energyCapacity = 10000;

    public EnergyStorageUnitEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ENERGY_STORAGE_UNIT_ENTITY.get(), pos, blockState);
        this.energyUnitStorage = new EnergyStorage(energyCapacity, energyTransferRate, energyTransferRate, 0) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int retval = super.receiveEnergy(maxReceive, simulate);
                // If energy was actually added (and we aren't just simulating), save and sync
                if(retval > 0 && !simulate) {
                    setChanged();
                    if(level != null && !level.isClientSide()) {
                        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                    }
                }
                return retval;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int retval = super.extractEnergy(maxExtract, simulate);
                // If energy was actually removed, save and sync
                if(retval > 0 && !simulate) {
                    setChanged();
                    if(level != null && !level.isClientSide()) {
                        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                    }
                }
                return retval;
            }
        };

        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> energyUnitStorage.getEnergyStored();
                    case 1 -> energyUnitStorage.getMaxEnergyStored();
                    case 2 -> energyTransferRate;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {

                }
            }

            @Override
            public int getCount() {
                return 3;
            }

        };
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide) return;

        processInputBattery();
        processOutputBattery();

        updateBlockState(level, blockPos, blockState);
    }

    private void processInputBattery() { // DRAIN Battery -> FILL Machine
        ItemStack stack = inventory.getStackInSlot(INPUT_SLOT);
        if (stack.getItem() instanceof BatteryItem batteryItem) {

            int machineLimit = energyUnitStorage.receiveEnergy(Integer.MAX_VALUE, true);

            int toTransfer = batteryItem.extractEnergy(stack, machineLimit, true);

            if (toTransfer > 0) {
                batteryItem.extractEnergy(stack, toTransfer, false);
                energyUnitStorage.receiveEnergy(toTransfer, false);
            }
        }
    }

    private void processOutputBattery() { // DRAIN Machine -> FILL Battery
        ItemStack stack = inventory.getStackInSlot(OUTPUT_SLOT);
        if (stack.getItem() instanceof BatteryItem batteryItem) {

            int machineLimit = energyUnitStorage.extractEnergy(Integer.MAX_VALUE, true);

            int toTransfer = batteryItem.receiveEnergy(stack, machineLimit, true);

            // 3. EXECUTE
            if (toTransfer > 0) {
                energyUnitStorage.extractEnergy(toTransfer, false);
                batteryItem.receiveEnergy(stack, toTransfer, false);
            }
        }
    }

    private void updateBlockState(Level level, BlockPos pos, BlockState state) {
        int current = energyUnitStorage.getEnergyStored();
        int max = energyUnitStorage.getMaxEnergyStored();

        int chargeLevel = 0;
        if (max > 0) {
            chargeLevel = (current * 16) / max;
        }
        // Optimization: Only update block if the value actually changed
        if (state.getValue(EnergyStorageUnitBlock.CHARGE) != chargeLevel) {
            level.setBlock(pos, state.setValue(EnergyStorageUnitBlock.CHARGE, chargeLevel), 3);
        }
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public EnergyStorage getEnergyStorage() {
        return this.energyUnitStorage;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Energy Storage Unit");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new EnergyStorageUnitMenu(i,inventory, this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        // Save the energy storage manually
        tag.putInt("EnergyLevel", energyUnitStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));

       energyUnitStorage.deserializeNBT(registries, IntTag.valueOf(tag.getInt("EnergyLevel")));
    }
}
