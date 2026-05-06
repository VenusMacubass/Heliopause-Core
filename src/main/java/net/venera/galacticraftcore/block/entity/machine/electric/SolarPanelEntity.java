package net.venera.galacticraftcore.block.entity.machine.electric;

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
import net.venera.galacticraftcore.block.custom.machine.electric.SolarPanelBlock;
import net.venera.galacticraftcore.item.custom.BatteryItem;
import net.venera.galacticraftcore.screen.custom.BasicSolarMenu;
import org.jetbrains.annotations.Nullable;

public class SolarPanelEntity extends BaseElectricMachineEntity{
    private final int OUTPUT_SLOT = 0;
    private final int tierTransferRate;
    private final int generationRate; 
    private boolean isActive;
    
    public SolarPanelEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, int transferRate, int generationRate) {
        super(type, pos, state, 1, capacity, transferRate, transferRate);
        this.tierTransferRate = transferRate;
        this.generationRate = generationRate;
    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> energyStorage.getMaxEnergyStored();
                    case 2 -> tierTransferRate; 
                    case 3 -> generationRate;
                    case 4 -> isActive ? 1 : 0;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {}
            @Override
            public int getCount() { return 5; }
        };
    }
    
    public void tick(Level level, BlockPos pos, BlockState state){
        if(level.isClientSide) return;
        processOutputBattery();
        if(level.getGameTime() % 2 == 0){
        generateEnergy(level, pos);
        updateBlockState(level, pos, state);
        }
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

    private void processOutputBattery() {
        ItemStack stack = inventory.getStackInSlot(OUTPUT_SLOT);
        if (stack.getItem() instanceof BatteryItem batteryItem) {
            int energyAvailable = energyStorage.extractEnergy(Integer.MAX_VALUE, true);

            int acceptedByBattery = batteryItem.receiveEnergy(stack, energyAvailable, true);

            if (acceptedByBattery > 0) {
                energyStorage.extractEnergy(acceptedByBattery, false);
                batteryItem.receiveEnergy(stack, acceptedByBattery, false);

                inventory.setStackInSlot(OUTPUT_SLOT, stack);
            }
        }
    }
    
    private void generateEnergy(Level level, BlockPos pos) {
        float sunFactor = theSunFactor(level, pos);
        int energyToGenerate = Math.round(generationRate * sunFactor);
        if(energyToGenerate > 0){
            isActive = true;
        } else {
            isActive = false;
        }
        energyStorage.receiveEnergy(energyToGenerate, false);
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
    
    private float theSunFactor(Level level, BlockPos pos) { //Returns the total light coefficient
        int dimensionCheck = (!dimensionCheck(level)) ? 0 : 1;
        float lightEfficiency = lightEfficiencyCheck(level, pos);
        if(level.isRaining() || level.isThundering()) {
            lightEfficiency = lightEfficiency * 0.3f;
        }
        if(dimensionCheck == 0) {return 0;}
        float sunFactor = (float)Math.cos(level.getSunAngle(1.0f));
        sunFactor = Math.max(0.0f, sunFactor);
        return 1.0f * lightEfficiency * sunFactor * dimensionCheck;
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
        return Component.literal("Basic Solar Panel");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BasicSolarMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isActive", isActive);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("isActive");
    }
}
