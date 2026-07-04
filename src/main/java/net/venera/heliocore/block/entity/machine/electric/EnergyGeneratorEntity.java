package net.venera.heliocore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.ItemCustomNameToComponentFix;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
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
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.fluid.IFluidMachine;
import net.venera.heliocore.item.hpc_custom.BatteryItem;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import net.venera.heliocore.screen.custom.EnergyGeneratorMenu;
import net.venera.heliocore.util.MachineConfigHelper;
import net.venera.heliocore.util.PipeNetworkHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class EnergyGeneratorEntity extends BaseElectricMachineEntity implements IFluidMachine, MachineConfigHelper.IToggleableMachine{
    private final int SOLID_FUEL_SLOT = 0;
    private final int LIQUID_FUEL_SLOT = 1;
    private final int BATTERY_SLOT = 2;
    private final int BUCKET_CAPACITY = 1000;
    private final int maxLiquidFuelCapacity = 6000;
    private final int LIQUID_CONSUMPTION_RATE;
    private final int TRANSFER_RATE;
    public boolean isActive = false;
    public boolean enabled = true;
    public int burnTime = 0;
    public int maxBurnTime;
    private final int MAX_FLOW_RATE = 10;
    public final FluidTank liquidFuelTank = new FluidTank(maxLiquidFuelCapacity) {
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
    
    public EnergyGeneratorEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, int transferRate, int LIQUID_CONSUMPTION_RATE) {
        super(type, pos, state, 3, capacity, transferRate, transferRate);
        this.LIQUID_CONSUMPTION_RATE = LIQUID_CONSUMPTION_RATE;
        this.TRANSFER_RATE = transferRate;
    }
    
    
    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> liquidFuelTank.getFluidAmount();
                    case 1 -> liquidFuelTank.getCapacity();
                    case 2 -> maxLiquidFuelCapacity;
                    case 3 -> isActive ? 1 : 0;
                    case 4 -> energyStorage.getEnergyStored();
                    case 5 -> energyStorage.getMaxEnergyStored();
                    case 6 -> enabled ? 1 : 0;
                    case 7 -> burnTime;
                    case 8 -> maxBurnTime;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> liquidFuelTank.setFluid(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), value));
                    case 3 -> isActive = value == 1;
                    case 7 -> burnTime = value;
                    case 8 ->  maxBurnTime = value;
                }
            }
            @Override
            public int getCount() { return 9; }
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        if (level.isClientSide()) return;
        boolean dirty = false; 

        int oldBurnTime = burnTime;
        tickBurnTime();
        if (burnTime != oldBurnTime) dirty = true; 

        if(level.getGameTime() % 2 == 0 && canGenerate()){
            generateEnergy();
            dirty = true; 
        }

        if(burnTime <= 0){
            if(processSolidFuels()){
                dirty = true; 
            } else if (processLiquidFuels()) {
                dirty = true;
            }
        }

        if (processLiquidFuelSlot()) dirty = true; 
        if (processOutputBattery()) dirty = true;
        if (pullFluidIn(level, pos)) dirty = true;
        
        if (dirty) {
            setChanged();
        }

        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

    private boolean processOutputBattery() {
        ItemStack stack = inventory.getStackInSlot(BATTERY_SLOT);
        if (stack.getItem() instanceof BatteryItem batteryItem) {
            int energyAvailable = energyStorage.extractEnergy(Integer.MAX_VALUE, true);
            int acceptedByBattery = batteryItem.receiveEnergy(stack, energyAvailable, true);
            if (acceptedByBattery > 0) {
                energyStorage.extractEnergy(acceptedByBattery, false);
                batteryItem.receiveEnergy(stack, acceptedByBattery, false);
                inventory.setStackInSlot(BATTERY_SLOT, stack);
                return true;
            }
            return false;
        }
        return false;
    }
    
    private boolean processSolidFuels(){
        ItemStack solidFuelStack = inventory.getStackInSlot(SOLID_FUEL_SLOT);
        if(solidFuelStack.getBurnTime(RecipeType.SMELTING) > 0){
            burnTime += solidFuelStack.getBurnTime(RecipeType.SMELTING);
            maxBurnTime = solidFuelStack.getBurnTime(RecipeType.SMELTING);
            if(solidFuelStack.getItem() == Items.LAVA_BUCKET){
                inventory.setStackInSlot(SOLID_FUEL_SLOT, new ItemStack(Items.BUCKET));
            }
            else{
                solidFuelStack.shrink(1);
            }
            
            return true;
        }
        return false;
    }   
    
    private boolean processLiquidFuels(){
        if(!liquidFuelTank.isEmpty()){
            int conversionAmount = Math.min(liquidFuelTank.getFluidAmount(), LIQUID_CONSUMPTION_RATE);
            liquidFuelTank.drain(conversionAmount, FluidTank.FluidAction.EXECUTE);
            int burnDuration = conversionAmount;
            burnTime += burnDuration;
            maxBurnTime = burnDuration;
            return true;
        }
        return false;
    }
    
    private boolean processLiquidFuelSlot(){
        ItemStack inputStack = inventory.getStackInSlot(LIQUID_FUEL_SLOT);
        if(inputStack.getItem() == HpCFluids.REFINED_FUEL.getBucket() && liquidFuelTank.getSpace() >= BUCKET_CAPACITY){
            liquidFuelTank.fill(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), BUCKET_CAPACITY), IFluidHandler.FluidAction.EXECUTE);
            inventory.setStackInSlot(LIQUID_FUEL_SLOT, new ItemStack(Items.BUCKET));
            return true;
        }
        else if(inputStack.getItem() instanceof CanisterItem canister){
            CanisterData data = canister.getCanisterData(inputStack);

            if(data.isRefinedFuel()) {
                int receivedFuelCanister = liquidFuelTank.fill(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), data.amount()), IFluidHandler.FluidAction.SIMULATE);

                if(receivedFuelCanister > 0) {
                    liquidFuelTank.fill(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), receivedFuelCanister), IFluidHandler.FluidAction.EXECUTE);
                    canister.drain(inputStack, receivedFuelCanister);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canGenerate(){
        return energyStorage.canReceive() && burnTime > 0 && enabled;
    }
    
    private boolean generateEnergy() {
        int energyToGenerate = energyStorage.receiveEnergy(TRANSFER_RATE/2, true);
        if(energyToGenerate > 0){
            energyStorage.receiveEnergy(energyToGenerate, false);
            return true;
        }
        return false;
    }
    
    private void tickBurnTime() {
        if (burnTime > 0) {
            burnTime--;
            isActive = true;
        } else {
            isActive = false;
        }
        if (burnTime <= 0) {burnTime = 0;}
    }

    private boolean pullFluidIn(Level level, BlockPos pos) {
        if (liquidFuelTank.getFluidAmount() >= maxLiquidFuelCapacity) return false;

        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        Direction inputFace = machineFacing.getClockWise();

        BlockPos pipePos = pos.relative(inputFace);
        if (!(level.getBlockState(pipePos).getBlock() instanceof FluidPipeBlock)) return false;

        Set<BlockEntity> connectedMachines = PipeNetworkHelper.findConnectedInventories(level, pipePos, pos);

        int spaceAvailable = liquidFuelTank.getSpace();
        int fluidToPull = Math.min(spaceAvailable, MAX_FLOW_RATE);

        String targetFluidString = HpCFluids.REFINED_FUEL.getFluidType().getId().toString();
        boolean actuallyPulledSomething = false;

        for (BlockEntity entity : connectedMachines) {
            if (entity == this) continue;

            if (entity instanceof IFluidMachine targetMachine) {
                if (targetMachine.getFluidPortType(inputFace.getOpposite()) == IFluidMachine.PortType.OUTPUT) {
                    continue;
                }

                int availableToExtract = targetMachine.extractFluid(targetFluidString, fluidToPull, true);

                if (availableToExtract > 0) {
                    int actuallyExtracted = targetMachine.extractFluid(targetFluidString, availableToExtract, false);
                    liquidFuelTank.fill(new FluidStack(HpCFluids.REFINED_FUEL.getSource(), actuallyExtracted), IFluidHandler.FluidAction.EXECUTE);

                    fluidToPull -= actuallyExtracted;
                    actuallyPulledSomething = true;
                    if (fluidToPull <= 0) break;
                }
            }
        }
        return actuallyPulledSomething; 
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable("container." + HeliopauseCore.MOD_ID + ".energy_generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new EnergyGeneratorMenu(i, inventory, this);
    }

    public int getFuelAmount() {
        return liquidFuelTank.getFluidAmount();
    }
    public int getMaxCapacity() {
        return maxLiquidFuelCapacity;
    }
    public EnergyStorage getEnergyStorage() {return energyStorage;}

    @Override
    public void toggleEnabled(int buttonId) {
        if (buttonId == 0) {
            this.enabled = !this.enabled;
        }
        this.setChanged();
    }

    @Override
    public PortType getFluidPortType(Direction face) {
        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        if (face == machineFacing.getClockWise()) {
            return PortType.INPUT;
        }

        return PortType.NONE;
    }

    @Override
    public @Nullable String peekFluid(Direction face) {
        PortType port = getFluidPortType(face);
        if(port == PortType.INPUT &&  !liquidFuelTank.isEmpty()){
            return BuiltInRegistries.FLUID.getKey(liquidFuelTank.getFluid().getFluid()).toString();
        }
        return null;
    }

    @Override
    public int insertFluid(String fluidType, int amount, boolean simulate) {
        ResourceLocation fluidLocation = ResourceLocation.parse(fluidType);
        Fluid resolvedFluid = BuiltInRegistries.FLUID.get(fluidLocation);

        if (resolvedFluid != null && resolvedFluid != Fluids.EMPTY) {
            FluidStack newStack = new FluidStack(resolvedFluid, amount);

            if (liquidFuelTank.isFluidValid(newStack)) {
                IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
                return liquidFuelTank.fill(newStack, action);
            }
        }
        return 0;
    }

    @Override
    public int extractFluid(String fluidType, int amount, boolean simulate) {
        return 0;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("FuelTank", liquidFuelTank.writeToNBT(registries, new CompoundTag()));
        tag.putInt("BurnTime", burnTime);
        tag.putInt("MaxBurnTime", maxBurnTime);
        tag.putBoolean("IsActive", isActive);
        tag.putBoolean("Enabled", enabled);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("FuelTank")) liquidFuelTank.readFromNBT(registries, tag.getCompound("FuelTank"));

        burnTime = tag.getInt("BurnTime");
        maxBurnTime = tag.getInt("MaxBurnTime");
        isActive = tag.getBoolean("IsActive");
        enabled = tag.getBoolean("Enabled");
    }
}
