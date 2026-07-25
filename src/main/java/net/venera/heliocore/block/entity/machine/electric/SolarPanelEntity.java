package net.venera.heliocore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.hpc_custom.machine.electric.SolarPanelBlock;
import net.venera.heliocore.item.hpc_custom.BatteryItem;
import net.venera.heliocore.screen.hpc_custom.BasicSolarMenu;
import net.venera.heliocore.util.MachineConfigHelper;
import org.jetbrains.annotations.Nullable;

public class SolarPanelEntity extends BaseElectricMachineEntity implements MachineConfigHelper.IToggleableMachine {
    private final int OUTPUT_SLOT = 0;
    private final int energyTransferRate;
    private final int generationRate; 
    public boolean isActive = false;
    public boolean isEnabled = true;
    
    public SolarPanelEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, int energyTransferRate, int generationRate) {
        super(type, pos, state, 1, capacity, 0, energyTransferRate);
        this.energyTransferRate = energyTransferRate;
        this.generationRate = generationRate;
    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> energyTransferRate; 
                    case 1 -> generationRate;
                    case 2 -> isActive ? 1 : 0;
                    case 3 -> isEnabled ? 1 : 0;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {}
            @Override
            public int getCount() { return 4; }
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        if(level.isClientSide) return;
        boolean dirty = false;
        
        if (processOutputBattery()) {
            dirty = true;
        }
        
        if (isEnabled) {
            if (level.getGameTime() % 2 == 0) {
                if (generateEnergy(level, pos)) {
                    dirty = true;
                }
                updateBlockState(level, pos, state);
            }
        } else {
            if (isActive) {
                isActive = false;
                dirty = true;
            }
        }

        if (dirty) setChanged();
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

    private boolean processOutputBattery() {
        ItemStack stack = inventory.getStackInSlot(OUTPUT_SLOT);
        if (stack.getItem() instanceof BatteryItem batteryItem) {
            int energyAvailable = energyStorage.extractEnergy(Integer.MAX_VALUE, true);
            int acceptedByBattery = batteryItem.receiveEnergy(stack, energyAvailable, true);

            if (acceptedByBattery > 0) {
                energyStorage.extractEnergy(acceptedByBattery, false);
                batteryItem.receiveEnergy(stack, acceptedByBattery, false);
                inventory.setStackInSlot(OUTPUT_SLOT, stack);
                return true;
            }
        }
        return false;
    }

    private boolean generateEnergy(Level level, BlockPos pos) {
        float sunFactor = theSunFactor(level, pos);
        int energyToGenerate = Math.round(generationRate * sunFactor);
        boolean stateChanged = false;
        if (energyToGenerate > 0) {
            if (!isActive) {
                isActive = true;
                stateChanged = true;
            }
            energyStorage.addEnergy(energyToGenerate);
        } else {
            if (isActive) {
                isActive = false;
                stateChanged = true;
            }
        }
        return stateChanged;
    }

    private void updateBlockState(Level level, BlockPos pos, BlockState state) {
        int current = energyStorage.getEnergyStored();
        int max = energyStorage.getMaxEnergyStored();

        int chargeLevel = 0;
        if (max > 0) {
            chargeLevel = (current * 15) / max;
        }

        if (state.getValue(SolarPanelBlock.CHARGE) != chargeLevel) {
            level.setBlock(pos, state.setValue(SolarPanelBlock.CHARGE, chargeLevel), 3);
        }
    }

    private float theSunFactor(Level level, BlockPos pos) {
        if (!dimensionCheck(level)) {
            return 0.0f;
        }
        float lightEfficiency = lightEfficiencyCheck(level, pos);
        if (level.isThundering()) {
            lightEfficiency *= 0.2f;
        } else if (level.isRaining()) {
            lightEfficiency *= 0.5f;
        }
        float sunFactor = (float) Math.cos(level.getSunAngle(1.0f));
        return Math.max(0.0f, sunFactor * lightEfficiency);
    }
    
    private float lightEfficiencyCheck(Level level, BlockPos pos) { //Returns how much sunlight is available 
        int blockedPanelits = 0;
        for(int x=-1; x<=1; x++) {
            for(int z=-1; z<=1; z++) {
                BlockPos checkPos = pos.offset(x, 1, z);
                if(!level.canSeeSky(checkPos)) {
                    blockedPanelits++;
                }
            }
        }
        return 1.0f - (blockedPanelits / 9.0f);
    }
    
    private boolean dimensionCheck(Level level){
        boolean isNether = level.dimension() == Level.NETHER;
        boolean isEnd = level.dimension() == Level.END;

        return !(isNether || isEnd);
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable("container." + HeliopauseCore.MOD_ID + ".solar_panel");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BasicSolarMenu(i, inventory, this, this.data);
    }

    @Override
    public void toggleEnabled(int buttonId) {
        this.isEnabled = !this.isEnabled;
        this.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isActive", isActive);
        tag.putBoolean("isEnabled", isEnabled);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("isActive");
        isEnabled = tag.getBoolean("isEnabled");
    }
}
