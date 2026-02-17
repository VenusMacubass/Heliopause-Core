package net.venera.galacticraftcore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.entity.machine.BaseMachineEntity;
import net.venera.galacticraftcore.data.energy.GraphManager;
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

    private void pushEnergyToNetwork(Level level, BlockPos pos) {
        // 1. Snapshot our starting energy
        int energyStored = energyStorage.getEnergyStored();
        if (energyStored <= 0) return;

        // 2. Define our maximum output rate (e.g., 1000 FE/tick)
        int maxOutputRate = 1000;
        int energyRemainingToSend = Math.min(energyStored, maxOutputRate);

        // 3. Iterate all output sides
        for (Direction dir : Direction.values()) {
            // If we ran out of energy mid-loop, stop!
            if (energyRemainingToSend <= 0) break;

            if (isOutputSide(dir)) {
                BlockPos neighborPos = pos.relative(dir);

                // Check for Graph
                GraphManager manager = GraphManager.get(level);
                if (manager != null) {
                    // Try to push whatever is remaining in this tick's budget
                    int consumedByNetwork = manager.distributeEnergy(level, neighborPos, energyRemainingToSend);

                    // 4. Update the budget
                    if (consumedByNetwork > 0) {
                        // Deduct from our "active budget"
                        energyRemainingToSend -= consumedByNetwork;

                        // Deduct from the REAL storage
                        energyStorage.extractEnergy(consumedByNetwork, false);
                    }
                }
            }
        }
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

    public static void tick(Level level, BlockPos pos, BlockState state, BaseElectricMachineEntity entity) {
        if (level.getGameTime() % 20 == 0) {
            GalacticraftCore.LOGGER.debug("DEBUG: Machine Ticking at " + pos + " | Energy: " + entity.energyStorage.getEnergyStored());
        }

        // 2. Push Energy (Server Side Only)
        if (!level.isClientSide) {
            entity.pushEnergyToNetwork(level, pos);
        }
    }
    
    public boolean isValidPort(Direction side) {
        var config = MachineConfigHelper.getConfigFor(this.getType());
        if (config == null) return false; // This machine isn't in the config file
        
        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
        
        Direction modelSide = MachineConfigHelper.getRelativeSide(side, facing);
        
        return config.containsKey(modelSide);
    }
    
    public boolean isInputSide(Direction side) {
        var config = MachineConfigHelper.getConfigFor(this.getType());
        if (config == null) return false;

        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
        Direction modelSide = MachineConfigHelper.getRelativeSide(side, facing);
        
        return config.getOrDefault(modelSide, false);
    }
    
    public boolean isOutputSide(Direction side) {
        var config = MachineConfigHelper.getConfigFor(this.getType());
        if (config == null) return false;

        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
        Direction modelSide = MachineConfigHelper.getRelativeSide(side, facing);
        
        if (config.containsKey(modelSide)) {
            return !config.get(modelSide); // If Value is False, it is an Output.
        }
        return false; // Not in map = Wall
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries); 
        tag.putInt("EnergyLevel", energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("EnergyLevel")) {
            energyStorage.deserializeNBT(registries, IntTag.valueOf(tag.getInt("EnergyLevel")));
        }
    }
}
