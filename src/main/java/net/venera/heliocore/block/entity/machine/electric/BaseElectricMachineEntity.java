package net.venera.heliocore.block.entity.machine.electric;

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
import net.venera.heliocore.block.entity.machine.BaseMachineEntity;
import net.venera.heliocore.data.energy.GridManager;
import net.venera.heliocore.item.hpc_custom.BatteryItem;
import net.venera.heliocore.util.MachineConfigHelper;

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

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null && !this.level.isClientSide()) {
            GridManager.get(this.level).onMachinePlaced(this.level, this.getBlockPos());
        }
    }
    
    //--- SHARED HELPER METHODS ---//
    protected boolean processBatterySlot(int slotIndex) { 
        ItemStack batteryStack = inventory.getStackInSlot(slotIndex);
        if (batteryStack.getItem() instanceof BatteryItem batteryItem) {
            int spaceInMachine = energyStorage.receiveEnergy(Integer.MAX_VALUE, true);
            int toTransfer = batteryItem.extractEnergy(batteryStack, spaceInMachine, true);

            if (toTransfer > 0) {
                batteryItem.extractEnergy(batteryStack, toTransfer, false);
                energyStorage.receiveEnergy(toTransfer, false);
                inventory.setStackInSlot(slotIndex, batteryStack); 
                return true;
            }
        }
        return false;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BaseElectricMachineEntity entity) {
        
    }

    public boolean isValidPort(Direction side) {
        var config = MachineConfigHelper.getConfigFor(this.getType());
        if (config == null) return false; 
        
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
            return !config.get(modelSide); 
        }
        return false; 
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
