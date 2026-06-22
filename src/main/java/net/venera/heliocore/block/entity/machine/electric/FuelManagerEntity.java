package net.venera.heliocore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.hpc_custom.FluidPipeBlock;
import net.venera.heliocore.block.hpc_custom.LaunchPadBlock;
import net.venera.heliocore.block.hpc_custom.machine.BaseMachineBlock;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;
import net.venera.heliocore.fluid.IFluidMachine;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import net.venera.heliocore.screen.custom.FuelManagerMenu;
import net.venera.heliocore.util.MachineConfigHelper;
import net.venera.heliocore.util.PipeNetworkHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class FuelManagerEntity extends BaseElectricMachineEntity implements IFluidMachine, MachineConfigHelper.IToggleableMachine {
    private final int BATTERY_SLOT = 0;
    private final int FUEL_SLOT = 1;
    private final int BUCKET_CAPACITY = 1000;
    private final int maxFuel = 6000;
    private final int tierTransferRate;
    private int ENERGY_USAGE = 5;
    private int FUEL_LOAD_RATE = 2;
    public boolean isActive;
    public boolean isFueling = false;
    public boolean isCharging = false;
    public final FluidTank fuelTank = new FluidTank(maxFuel) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().isSame(HpCFluids.REFINED_FUEL.getSource());
        }
    };
    public FuelManagerEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, int transferRate, int energyUsage) {
        super(type, pos, blockState, 2, capacity, transferRate, transferRate);
        this.tierTransferRate = transferRate;
        this.ENERGY_USAGE = energyUsage;
    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> energyStorage.getMaxEnergyStored();
                    case 2 -> fuelTank.getFluidAmount();
                    case 3 -> maxFuel;
                    case 4 -> tierTransferRate;
                    case 5 -> isActive ? 1 : 0;
                    case 6 -> isFueling ? 1 : 0;
                    case 7 -> isCharging ? 1 : 0;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {
                switch(i) {
                    case 2 -> fuelTank.setFluid(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), value));
                    case 5 -> isActive = value == 1;
                }
            }
            @Override
            public int getCount() { return 8; }
        };
    }
    
    public void tick(Level level, BlockPos pos, BlockState state){
        if (level.isClientSide()) return;
        boolean dirty = false;

        if (processBatterySlot(BATTERY_SLOT)) dirty = true;
        if (processManualFuel()) dirty = true;
        if (fuelTank.getFluidAmount() < this.maxFuel) {
            if (pullFluidIn(level, pos)) {
                dirty = true; 
            }
        }

        // 2. Find Rocket and Push
        Tier1RocketEntity rocket = findValidRocket(level, pos);
        boolean isCurrentlyWorking = false;

        if (rocket != null) {
            if (isFueling && fuelTank.getFluidAmount() > 0) {
                int acceptedFuel = rocket.fillFuel(Math.min(fuelTank.getFluidAmount(), 5), false);
                FluidStack loadedFuel = fuelTank.drain(acceptedFuel, IFluidHandler.FluidAction.SIMULATE);
                if (loadedFuel.getAmount() > 0) {
                    fuelTank.drain(loadedFuel, IFluidHandler.FluidAction.EXECUTE);
                    isCurrentlyWorking = true;
                }
            }

            // Push Energy
            if (isCharging && this.energyStorage.getEnergyStored() > 0) {
                int acceptedEnergy = rocket.chargeEnergy(Math.min(this.energyStorage.getEnergyStored(), 50), false);
                if (acceptedEnergy > 0) {
                    this.energyStorage.extractEnergy(acceptedEnergy, false);
                    isCurrentlyWorking = true;
                }
            }
        }

        this.isActive = isCurrentlyWorking;
        
        if (dirty || isCurrentlyWorking) {
            setChanged();
        }

        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

    public Tier1RocketEntity findValidRocket(Level level, BlockPos pos) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) <= 1 && Math.abs(z) <= 1) {
                    continue;
                }
                BlockPos padPos = pos.offset(x, 0, z);
                BlockState padState = level.getBlockState(padPos);
                if (padState.getBlock() instanceof LaunchPadBlock && padState.getValue(LaunchPadBlock.IS_CENTER)) {
                    BlockPos rocketPos = padPos.above();
                    AABB searchBox = new AABB(rocketPos);
                    var foundRockets = level.getEntitiesOfClass(Tier1RocketEntity.class, searchBox);
                    if (!foundRockets.isEmpty()) {
                        return foundRockets.getFirst();
                    }
                }
            }
        }
        return null;
    }

    private boolean processManualFuel() {
        ItemStack fuelStack = inventory.getStackInSlot(FUEL_SLOT);

        if(fuelStack.getItem() == HpCFluids.REFINED_FUEL.getBucket() && fuelTank.getSpace() >= BUCKET_CAPACITY) {
            fuelTank.fill(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), BUCKET_CAPACITY), IFluidHandler.FluidAction.EXECUTE);
            inventory.setStackInSlot(FUEL_SLOT, new ItemStack(Items.BUCKET));
            return true;
        } else if(fuelStack.getItem() instanceof CanisterItem canister){
            CanisterData data = canister.getCanisterData(fuelStack);
            if(data.isRefinedFuel()) {
                int amountToDrain = fuelTank.fill(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), data.amount()), IFluidHandler.FluidAction.SIMULATE);
                if(amountToDrain > 0) {
                    canister.drain(fuelStack, amountToDrain);
                    fuelTank.fill(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), amountToDrain), IFluidHandler.FluidAction.EXECUTE);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean pullFluidIn(Level level, BlockPos pos) {
        if (fuelTank.getFluidAmount() >= this.maxFuel) return false;

        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        Direction inputFace = machineFacing.getClockWise();

        BlockPos pipePos = pos.relative(inputFace);
        if (!(level.getBlockState(pipePos).getBlock() instanceof FluidPipeBlock)) return false;

        Set<BlockEntity> connectedMachines = PipeNetworkHelper.findConnectedInventories(level, pipePos, pos);
        
        int fluidToPull = Math.min(fuelTank.getSpace(), FUEL_LOAD_RATE);
        boolean actuallyPulledSomething = false;

        for (BlockEntity entity : connectedMachines) {
            if (entity == this) continue;

            if (entity instanceof IFluidMachine targetMachine) {
                int availableToExtract = targetMachine.extractFluid(HeliopauseCore.MOD_ID + ":refined_fuel", fluidToPull, true);

                if (availableToExtract > 0) {
                    int actuallyExtracted = targetMachine.extractFluid(HeliopauseCore.MOD_ID + ":refined_fuel", availableToExtract, false);
                    fuelTank.fill(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), actuallyExtracted), IFluidHandler.FluidAction.EXECUTE);

                    fluidToPull -= actuallyExtracted;
                    actuallyPulledSomething = true;
                    if (fluidToPull <= 0) break;
                }
            }
        }
        return actuallyPulledSomething;
    }
    
    public boolean canActivate(){
        return fuelTank.getFluidAmount() > 0 && energyStorage.getEnergyStored() >= ENERGY_USAGE;
    }

    @Override
    public PortType getFluidPortType(Direction globalFace) {
        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        if (globalFace == machineFacing.getClockWise()) {
            return PortType.INPUT;
        }
        return PortType.NONE;
    }

    private boolean hasEnergy(){
        return energyStorage.getEnergyStored() >= ENERGY_USAGE;
    }

    @Override
    public int insertFluid(String fluidType, int amount, boolean simulate) {
        if (!fluidType.equals(HeliopauseCore.MOD_ID + ":refined_fuel")) return 0;

        IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
        return fuelTank.fill(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), amount), action);
    }

    @Override
    public int extractFluid(String fluidType, int amount, boolean simulate) {
        return 0;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Rocket Fuel and Energy Manager");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FuelManagerMenu(i, inventory, this, this.data);
    }

    @Override
    public void toggleEnabled(int buttonId) {
        if (buttonId == 0) {
            this.isFueling= !this.isFueling;
        } else if (buttonId == 1) {
            this.isCharging = !this.isCharging;
        }
        this.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isActive", isActive);
        tag.putBoolean("isFueling", isFueling);
        tag.putBoolean("isCharging", isCharging);
        tag.put("FuelTank", fuelTank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("isActive");
        isFueling = tag.getBoolean("isFueling");
        isCharging = tag.getBoolean("isCharging");
        if (tag.contains("FuelTank")) fuelTank.readFromNBT(registries, tag.getCompound("FuelTank"));
    }
}
