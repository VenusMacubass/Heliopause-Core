package net.venera.galacticraftcore.block.entity.machine.electric;


import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.galacticraftcore.block.custom.machine.electric.EnergyStorageUnitBlock;
import net.venera.galacticraftcore.block.entity.ModBlockEntities;
import net.venera.galacticraftcore.item.custom.BatteryItem;
import net.venera.galacticraftcore.screen.custom.EnergyStorageUnitMenu;

import javax.annotation.Nullable;

public class EnergyStorageUnitEntity extends BaseElectricMachineEntity {
    private final int INPUT_SLOT = 0;
    private final int OUTPUT_SLOT = 1;
    private final int tierTransferRate;

    public EnergyStorageUnitEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, int transferRate) {
        super(type, pos, blockState, 2, capacity, transferRate, transferRate);
        this.tierTransferRate = transferRate;
    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> energyStorage.getMaxEnergyStored();
                    case 2 -> tierTransferRate; //Return the specific rate for this tier
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {}
            @Override
            public int getCount() { return 3; }
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        processBatterySlot(INPUT_SLOT);

        processOutputBattery();

        updateBlockState(level, pos, state);
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

    private void updateBlockState(Level level, BlockPos pos, BlockState state) {
        int current = energyStorage.getEnergyStored();
        int max = energyStorage.getMaxEnergyStored();

        int chargeLevel = 0;
        if (max > 0) {
            chargeLevel = (current * 16) / max;
        }

        if (state.getValue(EnergyStorageUnitBlock.CHARGE) != chargeLevel) {
            level.setBlock(pos, state.setValue(EnergyStorageUnitBlock.CHARGE, chargeLevel), 3);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Energy Storage Unit");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new EnergyStorageUnitMenu(i, inventory, this);
    }
}
