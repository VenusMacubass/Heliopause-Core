package net.venera.heliocore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.hpc_custom.FluidPipeBlock;
import net.venera.heliocore.block.hpc_custom.machine.BaseMachineBlock;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.fluid.IFluidMachine;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import net.venera.heliocore.screen.hpc_custom.RefineryMenu;
import net.venera.heliocore.util.MachineConfigHelper;
import net.venera.heliocore.util.PipeNetworkHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class RefineryEntity extends BaseElectricMachineEntity implements IFluidMachine, MachineConfigHelper.IToggleableMachine{
    private final int INPUT_SLOT = 0;
    private final int OUTPUT_SLOT = 1;
    private final int BATTERY_SLOT = 2;
    private final int BUCKET_CAPACITY = 1000;
    private final int CONVERSION_RATE;
    private final int ENERGY_USAGE; 
    private final int maxCapacity = 6000;
    public boolean isActive = false;
    private final int MAX_FLOW_RATE = 10;
    public boolean enabled = true;
    public final FluidTank oilTank = new FluidTank(maxCapacity) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
         @Override
         public boolean isFluidValid(FluidStack stack) {
             return stack.getFluid().isSame(HpCFluids.CRUDE_OIL.getSource()); 
         }
    
    };

    public final FluidTank fuelTank = new FluidTank(maxCapacity) {
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
                    case 0 -> oilTank.getFluidAmount();
                    case 1 -> fuelTank.getFluidAmount();
                    case 2 -> maxCapacity;
                    case 3 -> isActive ? 1 : 0;
                    case 4 -> energyStorage.getEnergyStored();
                    case 5 -> energyStorage.getMaxEnergyStored();
                    case 6 -> enabled ? 1 : 0;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> oilTank.setFluid(new FluidStack(HpCFluids.CRUDE_OIL.getSource(), value));
                    case 1 -> fuelTank.setFluid(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), value));
                    case 3 -> isActive = value == 1;
                }
            }
            @Override
            public int getCount() { return 7; }
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;
        boolean dirty = false;

        if (processBatterySlot(BATTERY_SLOT)) dirty = true;

        if (processInputs()) dirty = true;
        if (processOutputs()) dirty = true;

        if (canRefine() && enabled) {
            refine(level);
            dirty = true;
        } else {
            if (isActive) {
                isActive = false;
                dirty = true;
            }
        }

        if (fuelTank.getFluidAmount() > 0) {
            pumpFluidOut(level, pos);
            dirty = true;
        }
        if (oilTank.getFluidAmount() < this.maxCapacity) {
            pullFluidIn(level, pos);
            dirty = true;
        }
        if (dirty) setChanged();
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

    private boolean processInputs() {
        ItemStack inputStack = inventory.getStackInSlot(INPUT_SLOT);
        int receivedOilBucket = oilTank.fill(new FluidStack(HpCFluids.CRUDE_OIL.getSource(), BUCKET_CAPACITY), IFluidHandler.FluidAction.SIMULATE);
        if(inputStack.getItem() == HpCFluids.CRUDE_OIL.getBucket() && receivedOilBucket >= BUCKET_CAPACITY) {
            oilTank.fill(new FluidStack(HpCFluids.CRUDE_OIL.getSource(), BUCKET_CAPACITY), IFluidHandler.FluidAction.EXECUTE);
            inventory.setStackInSlot(INPUT_SLOT, new ItemStack(Items.BUCKET));
            return true;
        } 
        else if(inputStack.getItem() instanceof CanisterItem canister){
            CanisterData data = canister.getCanisterData(inputStack);
            
            if(data.isCrudeOil()) {
                int receivedOilCanister = oilTank.fill(new FluidStack(HpCFluids.CRUDE_OIL.getSource(), data.amount()), IFluidHandler.FluidAction.SIMULATE);
                
                if(receivedOilCanister > 0) {
                    oilTank.fill(new FluidStack(HpCFluids.CRUDE_OIL.getSource(), receivedOilCanister), IFluidHandler.FluidAction.EXECUTE);
                    int actuallyDrained = canister.drain(inputStack, receivedOilCanister);
                    return actuallyDrained > 0;
                }
            }
        }
        return false;
    }

    private boolean processOutputs() {
        ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);
        FluidStack fuelBucket = fuelTank.drain(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), BUCKET_CAPACITY), IFluidHandler.FluidAction.SIMULATE);
        if(outputStack.getItem() == Items.BUCKET && fuelBucket.getAmount() >= BUCKET_CAPACITY) {
            fuelTank.drain(fuelBucket, IFluidHandler.FluidAction.EXECUTE);
            inventory.setStackInSlot(OUTPUT_SLOT, new ItemStack(HpCFluids.REFINED_FUEL.getBucket()));
            return true;
        } 
        else if(outputStack.getItem() instanceof CanisterItem canister){
            CanisterData data = canister.getCanisterData(outputStack);
            FluidStack fuelCanister = fuelTank.drain(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), data.getSpace()), IFluidHandler.FluidAction.SIMULATE);
            if(data.isEmpty() || data.isRefinedFuel()) {
                if(fuelCanister.getAmount() > 0) {
                    int actuallyFilled = canister.fill(outputStack, CanisterData.REFINED_FUEL, fuelCanister.getAmount());
                    fuelTank.drain(fuelCanister, IFluidHandler.FluidAction.EXECUTE);
                    return actuallyFilled > 0;
                }
            }
        }
        return false;
    }

    private void pumpFluidOut(Level level, BlockPos pos) {
        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        Direction outputFace = machineFacing.getCounterClockWise(); 

        BlockPos pipePos = pos.relative(outputFace);
        if (!(level.getBlockState(pipePos).getBlock() instanceof FluidPipeBlock)) return;

        Set<BlockEntity> connectedMachines = PipeNetworkHelper.findConnectedInventories(level, pipePos, pos);
        int fluidToPush = Math.min(fuelTank.getFluidAmount(), MAX_FLOW_RATE);

        for (BlockEntity entity : connectedMachines) {
            if (entity == this) continue;

            if (fluidToPush <= 0) break;

            if (entity instanceof IFluidMachine targetMachine && !fuelTank.isEmpty()) {
                ResourceLocation liquidId = BuiltInRegistries.FLUID.getKey(fuelTank.getFluid().getFluid());
                String fluidTypeString = liquidId.toString();

                int accepted = targetMachine.insertFluid(fluidTypeString, fluidToPush, false);

                if (accepted > 0) {
                    fuelTank.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
                    fluidToPush -= accepted;
                }
            }
        }
    }

    private void pullFluidIn(Level level, BlockPos pos) {
        if (oilTank.getFluidAmount() >= this.maxCapacity) return;

        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        Direction inputFace = machineFacing.getClockWise();

        BlockPos pipePos = pos.relative(inputFace);
        if (!(level.getBlockState(pipePos).getBlock() instanceof FluidPipeBlock)) return;

        Set<BlockEntity> connectedMachines = PipeNetworkHelper.findConnectedInventories(level, pipePos, pos);

        int spaceAvailable = this.maxCapacity - oilTank.getFluidAmount();
        int fluidToPull = Math.min(spaceAvailable, MAX_FLOW_RATE);

        for (BlockEntity entity : connectedMachines) {
            if (entity == this) continue;

            if (entity instanceof IFluidMachine targetMachine) {
                if (targetMachine.getFluidPortType(inputFace.getOpposite()) == PortType.OUTPUT) {
                    continue;
                }

                int availableToExtract = targetMachine.extractFluid(HeliopauseCore.MOD_ID + ":crude_oil", fluidToPull, true);

                if (availableToExtract > 0) {
                    int actuallyExtracted = targetMachine.extractFluid(HeliopauseCore.MOD_ID + ":crude_oil", availableToExtract, false);
                    oilTank.fill(new FluidStack(HpCFluids.CRUDE_OIL.getSource(), actuallyExtracted), IFluidHandler.FluidAction.EXECUTE);

                    fluidToPull -= actuallyExtracted;
                    if (fluidToPull <= 0) break;
                }
            }
        }
    }

    private boolean canRefine(){
        return oilTank.getFluidAmount() > 2 && fuelTank.getSpace() > 0 && energyStorage.getEnergyStored() >= ENERGY_USAGE;
    }

    @Override
    public PortType getFluidPortType(Direction globalFace) {
        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        if (globalFace == machineFacing.getClockWise()) {
            return PortType.OUTPUT;
        }
        
        if (globalFace == machineFacing.getCounterClockWise()) {
            return PortType.INPUT;
        }
        
        return PortType.NONE;
    }

    @Override
    public @Nullable String peekFluid(Direction face) {
        PortType port = getFluidPortType(face);
        if(port == PortType.OUTPUT &&  !fuelTank.isEmpty()){
            return BuiltInRegistries.FLUID.getKey(fuelTank.getFluid().getFluid()).toString();
        }
        if(port == PortType.INPUT&&  !oilTank.isEmpty()){
            return BuiltInRegistries.FLUID.getKey(oilTank.getFluid().getFluid()).toString();
        }
        return null;
    }

    @Override
    public int insertFluid(String fluidType, int amount, boolean simulate) {
        ResourceLocation fluidLocation = ResourceLocation.parse(fluidType);
        Fluid resolvedFluid = BuiltInRegistries.FLUID.get(fluidLocation);

        if (resolvedFluid != null && resolvedFluid != Fluids.EMPTY) {
            FluidStack newStack = new FluidStack(resolvedFluid, amount);

            if (oilTank.isFluidValid(newStack)) {
                IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
                return oilTank.fill(newStack, action);
            }
        }
        return 0;
    }
    
    @Override
    public int extractFluid(String fluidType, int amount, boolean simulate) {
        if (!fluidType.equals(HeliopauseCore.MOD_ID + ":refined_fuel")) return 0;

        IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;

        return fuelTank.drain(amount, action).getAmount();
    }

    private void refine(Level level){
        if((level.getGameTime() % 5) == 0){
            isActive = true;

            int oilAvailable = oilTank.getFluidAmount();
            int fuelSpace = fuelTank.getSpace();

            int conversionAmount = Math.min(this.CONVERSION_RATE, oilAvailable);
            conversionAmount = Math.min(conversionAmount/2, fuelSpace);

            oilTank.drain((conversionAmount*2), IFluidHandler.FluidAction.EXECUTE);
            fuelTank.fill(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), conversionAmount), IFluidHandler.FluidAction.EXECUTE);

            this.energyStorage.extractEnergy(this.ENERGY_USAGE, false);
        }
    }

    public int getOilAmount() {
        return oilTank.getFluidAmount();
    }

    public int getFuelAmount() {
        return fuelTank.getFluidAmount();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public EnergyStorage getEnergyStorage() {return energyStorage;}
    public FluidTank getOilTank() {return oilTank;}
    public FluidTank getFuelTank() {return fuelTank;}

    @Override
    public Component getDisplayName() {
        return Component.translatable("container."+HeliopauseCore.MOD_ID+".refinery");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new RefineryMenu(i, inventory, this);
    }

    @Override
    public void toggleEnabled(int buttonId) {
        if (buttonId == 0) {
            this.enabled = !this.enabled;
        }
        this.setChanged();
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("OilTank", oilTank.writeToNBT(registries, new CompoundTag()));
        tag.put("FuelTank", fuelTank.writeToNBT(registries, new CompoundTag()));

        tag.putBoolean("IsActive", isActive);
        tag.putBoolean("Enabled", enabled);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("OilTank")) oilTank.readFromNBT(registries, tag.getCompound("OilTank"));
        if (tag.contains("FuelTank")) fuelTank.readFromNBT(registries, tag.getCompound("FuelTank"));

        isActive = tag.getBoolean("IsActive");
        enabled = tag.getBoolean("Enabled");
    }
}
