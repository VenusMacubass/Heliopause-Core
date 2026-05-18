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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.fluid.ModFluids;
import net.venera.heliocore.item.custom.CanisterItem;
import net.venera.heliocore.screen.custom.RefineryMenu;
import org.jetbrains.annotations.Nullable;

public class RefineryEntity extends BaseElectricMachineEntity {
    private final int INPUT_SLOT = 0;
    private final int OUTPUT_SLOT = 1;
    private final int BATTERY_SLOT = 2;
    private final int BUCKET_CAPACITY = 1000;
    private int CONVERSION_RATE = 1;
    private int ENERGY_USAGE = 2;
    private int oilAmount = 0;
    private int fuelAmount = 0;
    private int maxCapacity = 6000;
    public boolean isActive = false;

    public RefineryEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState,
                          int energyCapacity, int transferRate, int energyUsage, int conversionRate) {
        super(type, pos, blockState, 3, energyCapacity, transferRate, energyUsage);
        this.CONVERSION_RATE = conversionRate;
        this.ENERGY_USAGE = energyUsage;
    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> oilAmount;
                    case 1 -> fuelAmount;
                    case 2 -> maxCapacity;
                    case 3 -> isActive ? 1 : 0;
                    case 4 -> energyStorage.getEnergyStored();
                    case 5 -> energyStorage.getMaxEnergyStored();
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> oilAmount = value;
                    case 1 -> fuelAmount = value;
                    case 2 -> maxCapacity = value;
                    case 3 -> isActive = value == 1;
                }
            }
            @Override
            public int getCount() { return 6; }
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;
        boolean dirty = false;

        if (processBatterySlot(BATTERY_SLOT)) dirty = true;

        if (processInputs()) dirty = true;
        if (processOutputs()) dirty = true;

        if (canRefine()) {
            refine();
            dirty = true;
        } else {
            if (isActive) {
                isActive = false;
                dirty = true;
            }
        }
        if (dirty) setChanged();
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

    private boolean processInputs() {
        ItemStack inputStack = inventory.getStackInSlot(INPUT_SLOT);

        if(inputStack.getItem() == ModFluids.CRUDE_OIL.getBucket() && oilAmount <= maxCapacity - BUCKET_CAPACITY) {
            oilAmount += BUCKET_CAPACITY;
            inventory.setStackInSlot(INPUT_SLOT, new ItemStack(Items.BUCKET));
            return true;
        } else if(inputStack.getItem() instanceof CanisterItem canister){
            CanisterData data = canister.getCanisterData(inputStack);
            
            if(data.isCrudeOil()) { //Only accept canisters that have crude oil
                int spaceAvailable = maxCapacity - oilAmount;
                int amountToDrain = Math.min(data.amount(), spaceAvailable);

                if(amountToDrain > 0) {
                    int actuallyDrained = canister.drain(inputStack, amountToDrain);
                    oilAmount += actuallyDrained;
                    return actuallyDrained > 0;
                }
            }
        }
        return false;
    }

    private boolean processOutputs() {
        ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);

        if(outputStack.getItem() == Items.BUCKET && fuelAmount >= BUCKET_CAPACITY) {
            fuelAmount -= BUCKET_CAPACITY;
            inventory.setStackInSlot(OUTPUT_SLOT, new ItemStack(ModFluids.REFINED_FUEL.getBucket()));
            return true;
        } else if(outputStack.getItem() instanceof CanisterItem canister){
            CanisterData data = canister.getCanisterData(outputStack);
            
            if(data.isEmpty() || data.isRefinedFuel()) { //Accept empty canisters or canisters with refined fuel
                int amountToFill = Math.min(fuelAmount, data.getSpace());
                if(amountToFill > 0) {
                    int actuallyFilled = canister.fill(outputStack, CanisterData.REFINED_FUEL, amountToFill);
                    fuelAmount -= actuallyFilled;
                    return actuallyFilled > 0;
                }
            }
        }
        return false;
    }

    private boolean canRefine(){
        return oilAmount > 0 && fuelAmount < maxCapacity && energyStorage.getEnergyStored() >= ENERGY_USAGE;
    }

    private void refine(){
        isActive = true;

        int conversionAmount = Math.min(this.CONVERSION_RATE, oilAmount);
        conversionAmount = Math.min(conversionAmount, maxCapacity - fuelAmount);

        oilAmount -= conversionAmount;
        fuelAmount += conversionAmount;
        this.energyStorage.extractEnergy(this.ENERGY_USAGE, false);
    }

    public int getOilAmount() {
        return oilAmount;
    }

    public int getFuelAmount() {
        return fuelAmount;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public EnergyStorage getEnergyStorage() {return energyStorage;}

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.heliocore.refinery");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new RefineryMenu(i, inventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("OilAmount", oilAmount);
        tag.putInt("FuelAmount", fuelAmount);
        tag.putBoolean("IsActive", isActive);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        oilAmount = tag.getInt("OilAmount");
        fuelAmount = tag.getInt("FuelAmount");
        isActive = tag.getBoolean("IsActive");

    }
}
