package net.venera.heliocore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.heliocore.item.hpc_custom.BatteryItem;
import net.venera.heliocore.util.MachineConfigHelper;
import org.jetbrains.annotations.Nullable;

public class ElectricGeneratorEntity extends BaseElectricMachineEntity implements MachineConfigHelper.IToggleableMachine{
    public ElectricGeneratorEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int slotCount, int capacity, int transferRate) {
        super(type, pos, state, slotCount, capacity, transferRate, 0);
    }
    
    @Override
    protected ContainerData initContainerData() {
        return null;
    }

    public void tick(Level level, BlockPos pos, BlockState state){
       
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

//    private void processOutputBattery() {
//        ItemStack stack = inventory.getStackInSlot(OUTPUT_SLOT);
//        if (stack.getItem() instanceof BatteryItem batteryItem) {
//            int energyAvailable = energyStorage.extractEnergy(Integer.MAX_VALUE, true);
//
//            int acceptedByBattery = batteryItem.receiveEnergy(stack, energyAvailable, true);
//
//            if (acceptedByBattery > 0) {
//                energyStorage.extractEnergy(acceptedByBattery, false);
//                batteryItem.receiveEnergy(stack, acceptedByBattery, false);
//
//                inventory.setStackInSlot(OUTPUT_SLOT, stack);
//            }
//        }
//    }

//    private void generateEnergy(Level level, BlockPos pos) {
//        float sunFactor = theSunFactor(level, pos);
//        int energyToGenerate = Math.round(generationRate * sunFactor);
//        if(energyToGenerate > 0){
//            isActive = true;
//        } else {
//            isActive = false;
//        }
//        energyStorage.receiveEnergy(energyToGenerate, false);
//    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }

    @Override
    public void toggleEnabled(int buttonId) {

    }
}
