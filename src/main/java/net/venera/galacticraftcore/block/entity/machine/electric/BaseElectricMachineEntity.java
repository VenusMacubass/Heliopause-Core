package net.venera.galacticraftcore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.venera.galacticraftcore.block.entity.machine.BaseMachineEntity;
import net.venera.galacticraftcore.item.custom.BatteryItem;
import net.venera.galacticraftcore.util.MachineConfigHelper;

import java.util.Map;

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

    //--- SHARED HELPER METHODS ---//
    protected boolean processBatterySlot(int slotIndex) { //Any machine can call this in tick() to drain a battery slot
        ItemStack batteryStack = inventory.getStackInSlot(slotIndex);
        if (batteryStack.getItem() instanceof BatteryItem batteryItem) {
            int spaceInMachine = energyStorage.receiveEnergy(Integer.MAX_VALUE, true);
            int toTransfer = batteryItem.extractEnergy(batteryStack, spaceInMachine, true);

            if (toTransfer > 0) {
                batteryItem.extractEnergy(batteryStack, toTransfer, false);
                energyStorage.receiveEnergy(toTransfer, false);
                inventory.setStackInSlot(slotIndex, batteryStack); // Force Update
                return true;
            }
        }
        return false;
    }

    /**
     * checks if a wire should visually connect to this specific side.
     * @param side The side of the machine in the World (e.g., North face, Up face).
     * @return true if this side is either an Input OR an Output.
     */
    public boolean isValidPort(Direction side) {
        // 1. Get the configuration for this machine type
        var config = MachineConfigHelper.getConfigFor(this.getType());
        if (config == null) return false; // This machine isn't in the config file

        // 2. Get the direction the machine is facing
        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);

        // 3. Translate the World Side to the Model Side
        Direction modelSide = MachineConfigHelper.getRelativeSide(side, facing);

        // 4. If the key exists in the map, it's a valid port (Input or Output)
        return config.containsKey(modelSide);
    }

    /**
     * Checks if this side can RECEIVE energy (Input).
     */
    public boolean isInputSide(Direction side) {
        var config = MachineConfigHelper.getConfigFor(this.getType());
        if (config == null) return false;

        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
        Direction modelSide = MachineConfigHelper.getRelativeSide(side, facing);

        // Return true ONLY if the map has the key AND the value is true (Input)
        return config.getOrDefault(modelSide, false);
        // Note: Default is false (Output/None), but since we check containsKey usually, 
        // this is safe for Inputs. If it's null, it returns false.
    }

    /**
     * Checks if this side can EXTRACT energy (Output).
     */
    public boolean isOutputSide(Direction side) {
        var config = MachineConfigHelper.getConfigFor(this.getType());
        if (config == null) return false;

        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
        Direction modelSide = MachineConfigHelper.getRelativeSide(side, facing);

        // We explicitly check if it contains the key first to distinguish "Output" from "Wall"
        if (config.containsKey(modelSide)) {
            return !config.get(modelSide); // If Value is False, it is an Output.
        }
        return false; // Not in map = Wall
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
