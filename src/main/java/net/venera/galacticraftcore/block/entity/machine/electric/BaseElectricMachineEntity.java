package net.venera.galacticraftcore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.venera.galacticraftcore.block.entity.machine.BaseMachineEntity;
import net.venera.galacticraftcore.item.custom.BatteryItem;

public abstract class BaseElectricMachineEntity extends BaseMachineEntity {
    protected final EnergyStorage energyStorage;

    public BaseElectricMachineEntity(BlockEntityType<?> type, BlockPos pos, BlockState state,
                                     int slotCount, int capacity, int transferRate, int usagePerTick) {
        super(type, pos, state, slotCount);
        
        this.energyStorage = new EnergyStorage(capacity, transferRate, usagePerTick, 0) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int retval = super.receiveEnergy(maxReceive, simulate);
                if (retval > 0 && !simulate) {
                    setChanged();
                    if (level != null && !level.isClientSide()) {
                        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                    }
                }
                return retval;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int retval = super.extractEnergy(maxExtract, simulate);
                if (retval > 0 && !simulate) {
                    setChanged();
                    if (level != null && !level.isClientSide()) {
                        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                    }
                }
                return retval;
            }
        };
    }
    
    public EnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    // --- SHARED HELPER METHODS ---

    // Any machine can call this in tick() to drain a battery slot
    protected boolean processBatterySlot(int slotIndex) {
        ItemStack batteryStack = inventory.getStackInSlot(slotIndex);
        if (batteryStack.getItem() instanceof BatteryItem batteryItem) {
            int spaceInMachine = energyStorage.receiveEnergy(Integer.MAX_VALUE, true);
            int toTransfer = batteryItem.extractEnergy(batteryStack, spaceInMachine, true);

            if (toTransfer > 0) {
                batteryItem.extractEnergy(batteryStack, toTransfer, false);
                energyStorage.receiveEnergy(toTransfer, false);
                // Force Update
                inventory.setStackInSlot(slotIndex, batteryStack);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries); // Saves Inventory
        tag.putInt("EnergyLevel", energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries); // Loads Inventory
        // Fix for "Voiding Energy" bug using IntTag
        if (tag.contains("EnergyLevel")) {
            energyStorage.deserializeNBT(registries, IntTag.valueOf(tag.getInt("EnergyLevel")));
        }
    }
}
