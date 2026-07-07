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
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.fluid.IFluidMachine;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import net.venera.heliocore.item.hpc_custom.GasTankItem;
import net.venera.heliocore.screen.custom.VaporizerMenu;
import net.venera.heliocore.util.MachineConfigHelper;
import net.venera.heliocore.util.PipeNetworkHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class GasVaporizerEntity extends BaseElectricMachineEntity implements IFluidMachine, MachineConfigHelper.IToggleableMachine{
    private final int INPUT_SLOT = 0;
    private final int OUTPUT_SLOT = 1;
    private final int BATTERY_SLOT = 2;
    private final int CONVERSION_RATE;
    private final int ENERGY_USAGE;
    private final int maxCapacity = 6000;
    public boolean isActive = false;
    private final int MAX_FLOW_RATE = 10;
    public boolean enabled = true;
    private int conversionScore = 0;
    private final int conversionThreshold = 100;
    private final int conversionMaximum = 2400;
    public final FluidTank gasTank = new FluidTank(maxCapacity) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
        @Override
        public boolean isFluidValid(FluidStack stack) {
            ResourceLocation id = BuiltInRegistries.FLUID.getKey(stack.getFluid());
            return stack.getFluid().getFluidType().getDensity() < 0 && id.getPath().endsWith("_gas");
        }

    };

    public final FluidTank liquidTank = new FluidTank(maxCapacity/10) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
        @Override
        public boolean isFluidValid(FluidStack stack) {
            ResourceLocation id = BuiltInRegistries.FLUID.getKey(stack.getFluid());
            return stack.getFluid().getFluidType().getDensity() > 0 && id.getPath().endsWith("_liquid"); 
        }
    };

    public GasVaporizerEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState,
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
                    case 0 -> gasTank.getFluidAmount();
                    case 1 -> BuiltInRegistries.FLUID.getId(gasTank.getFluid().getFluid());
                    case 2 -> liquidTank.getFluidAmount();
                    case 3 -> BuiltInRegistries.FLUID.getId(liquidTank.getFluid().getFluid());
                    case 4 -> maxCapacity;
                    case 5 -> isActive ? 1 : 0;
                    case 6 -> energyStorage.getEnergyStored();
                    case 7 -> energyStorage.getMaxEnergyStored();
                    case 8 -> enabled ? 1 : 0;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> gasTank.setFluid(new FluidStack(gasTank.getFluid().getFluid(), value));
                    case 1 -> {
                        Fluid fluid = BuiltInRegistries.FLUID.byId(value);
                        gasTank.setFluid(new FluidStack(fluid == null ? Fluids.EMPTY : fluid, gasTank.getFluidAmount()));
                    }
                    case 2 -> liquidTank.setFluid(new FluidStack(liquidTank.getFluid().getFluid(), value));
                    case 3 -> {
                        Fluid fluid = BuiltInRegistries.FLUID.byId(value);
                        liquidTank.setFluid(new FluidStack(fluid == null ? Fluids.EMPTY : fluid, liquidTank.getFluidAmount()));
                    }
                    case 5 -> isActive = value == 1;
                    case 8 -> enabled = value == 1;
                }
            }
            @Override
            public int getCount() { return 9; } // Updated total count
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;
        boolean dirty = false;

        if (processBatterySlot(BATTERY_SLOT)) dirty = true;

        if (processInputs()) dirty = true;
        if (processOutputs()) dirty = true;
        
        if (enabled) {
            if (processPhaseChange()) {
                dirty = true;
            }
        } else if (isActive) {
            isActive = false;
            dirty = true;
        }

        if (gasTank.getFluidAmount() > 0) {
            pumpFluidOut(level, pos);
            dirty = true;
        }
        if (liquidTank.getFluidAmount() < this.maxCapacity) {
            pullFluidIn(level, pos);
            dirty = true;
        }
        if (dirty) setChanged();
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

    private boolean processPhaseChange() {
        boolean didWork = false;
        
        if (liquidTank.getFluidAmount() > 0 && energyStorage.getEnergyStored() >= ENERGY_USAGE) {
            if (conversionScore <= conversionMaximum - conversionThreshold) {
                FluidStack drained = liquidTank.drain(1, IFluidHandler.FluidAction.EXECUTE);

                if (drained.getAmount() == 1) {
                    energyStorage.extractEnergy(ENERGY_USAGE, false);
                    conversionScore += conversionThreshold;
                    isActive = true;
                    didWork = true;
                }
            }
        } else if (conversionScore <= 0) {
            isActive = false;
        }
        
        if (conversionScore > 0) {
            Fluid targetGas = net.minecraft.world.level.material.Fluids.EMPTY;
            
            if (!gasTank.isEmpty()) {
                targetGas = gasTank.getFluid().getFluid();
            } else if (!liquidTank.isEmpty()) {
                ResourceLocation liquidId = BuiltInRegistries.FLUID.getKey(liquidTank.getFluid().getFluid());
                String gasName = liquidId.getPath().replace("_liquid", "_gas");

                if (!gasName.equals(liquidId.getPath())) {
                    ResourceLocation gasId = ResourceLocation.fromNamespaceAndPath(liquidId.getNamespace(), gasName);
                    Fluid mappedGas = BuiltInRegistries.FLUID.get(gasId);

                    if (mappedGas != null && mappedGas != net.minecraft.world.level.material.Fluids.EMPTY) {
                        targetGas = mappedGas;
                    }
                }
            }
            
            if (targetGas != net.minecraft.world.level.material.Fluids.EMPTY) {
                int gasToPush = Math.min(conversionScore, CONVERSION_RATE);
                int accepted = gasTank.fill(new FluidStack(targetGas, gasToPush), IFluidHandler.FluidAction.EXECUTE);

                if (accepted > 0) {
                    conversionScore -= accepted;
                    didWork = true;
                }
            } else {
                conversionScore = 0;
                didWork = true;
            }
        }

        return didWork;
    }

    private boolean processInputs() {
        ItemStack inputStack = inventory.getStackInSlot(INPUT_SLOT);

        if (inputStack.getItem() instanceof CanisterItem canisterItem) {
            CanisterData data = canisterItem.getCanisterData(inputStack);

            if (data != null && !data.isEmpty() && data.getFluid() != null && data.getFluid().getFluidType().getDensity() > 0) {
                ResourceLocation fluidId = BuiltInRegistries.FLUID.getKey(data.getFluid());
                if (fluidId.getPath().endsWith("_liquid")) {
                    int receivedLiquid = liquidTank.fill(new FluidStack(data.getFluid(), data.amount()), IFluidHandler.FluidAction.SIMULATE);

                    if (receivedLiquid > 0) {
                        liquidTank.fill(new FluidStack(data.getFluid(), receivedLiquid), IFluidHandler.FluidAction.EXECUTE);
                        int actuallyDrained = canisterItem.drain(inputStack, receivedLiquid);
                        return actuallyDrained > 0;
                    }
                }
            }
        }
        return false;
    }

    private boolean processOutputs() {
        ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);

        if (outputStack.getItem() instanceof GasTankItem gastankItem && gasTank.getFluidAmount() > 0) {
            GasTankData data = gastankItem.getGasTankData(outputStack);
            Fluid currentGas = gasTank.getFluid().getFluid();

            if (data == null || data.isEmpty() || data.getFluid().isSame(currentGas)) {
                int space = data == null ? CanisterItem.MAX_CAPACITY : data.getSpace();
                FluidStack simulatedDrain = gasTank.drain(space, IFluidHandler.FluidAction.SIMULATE);

                if (simulatedDrain.getAmount() > 0) {
                    ResourceLocation gasId = BuiltInRegistries.FLUID.getKey(currentGas);
                    int actuallyFilled = gastankItem.fill(outputStack, gasId, simulatedDrain.getAmount());
                    gasTank.drain(actuallyFilled, IFluidHandler.FluidAction.EXECUTE);
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
        int fluidToPush = Math.min(gasTank.getFluidAmount(), MAX_FLOW_RATE);

        for (BlockEntity entity : connectedMachines) {
            if (entity == this) continue;

            if (fluidToPush <= 0) break;
            if (entity instanceof IFluidMachine targetMachine) {
                ResourceLocation gasId = BuiltInRegistries.FLUID.getKey(gasTank.getFluid().getFluid());
                String fluidTypeString = gasId.toString();
                int accepted = targetMachine.insertFluid(fluidTypeString, fluidToPush, false);
                if (accepted > 0) {
                    gasTank.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
                    fluidToPush -= accepted;
                }
            }
        }
    }

    private void pullFluidIn(Level level, BlockPos pos) {
        if (liquidTank.getFluidAmount() >= this.maxCapacity) return;

        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        Direction inputFace = machineFacing.getClockWise();

        BlockPos pipePos = pos.relative(inputFace);
        if (!(level.getBlockState(pipePos).getBlock() instanceof FluidPipeBlock)) return;

        Set<BlockEntity> connectedMachines = PipeNetworkHelper.findConnectedInventories(level, pipePos, pos);
        int fluidToPull = Math.min(liquidTank.getSpace(), MAX_FLOW_RATE);

        for (BlockEntity entity : connectedMachines) {
            if (entity == this) continue;

            if (entity instanceof IFluidMachine targetMachine) {
                if (targetMachine.getFluidPortType(inputFace.getOpposite()) == IFluidMachine.PortType.OUTPUT) {
                    continue;
                }
                
                String fluidToAskFor = null;

                if (!liquidTank.isEmpty()) {
                    fluidToAskFor = BuiltInRegistries.FLUID.getKey(liquidTank.getFluid().getFluid()).toString();
                } else {
                    String peekedFluid = targetMachine.peekFluid(inputFace.getOpposite());

                    if (peekedFluid != null) {
                        Fluid resolvedFluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(peekedFluid));
                        if (resolvedFluid != null && resolvedFluid.getFluidType().getDensity() > 0 && peekedFluid.endsWith("_liquid")) {
                            fluidToAskFor = peekedFluid;
                        }
                    }
                }
                
                if (fluidToAskFor != null) {
                    int availableToExtract = targetMachine.extractFluid(fluidToAskFor, fluidToPull, true);

                    if (availableToExtract > 0) {
                        int actuallyExtracted = targetMachine.extractFluid(fluidToAskFor, availableToExtract, false);
                        Fluid incomingFluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(fluidToAskFor));

                        if (incomingFluid != null) {
                            liquidTank.fill(new FluidStack(incomingFluid, actuallyExtracted), IFluidHandler.FluidAction.EXECUTE);
                        }

                        fluidToPull -= actuallyExtracted;
                        if (fluidToPull <= 0) break;
                    }
                }
            }
        }
    }

    @Override
    public IFluidMachine.PortType getFluidPortType(Direction globalFace) {
        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        
        if (globalFace == machineFacing.getClockWise()) {
            return IFluidMachine.PortType.INPUT;
        }
        
        if (globalFace == machineFacing.getCounterClockWise()) {
            return IFluidMachine.PortType.OUTPUT;
        }

        return IFluidMachine.PortType.NONE;
    }

    @Override
    public @Nullable String peekFluid(Direction face) {
        IFluidMachine.PortType port = getFluidPortType(face);
        if(port == IFluidMachine.PortType.OUTPUT){
            return BuiltInRegistries.FLUID.getKey(gasTank.getFluid().getFluid()).toString();
        }
        if(port == IFluidMachine.PortType.INPUT){
            return BuiltInRegistries.FLUID.getKey(liquidTank.getFluid().getFluid()).toString();
        }
        return "";
    }

    @Override
    public int insertFluid(String fluidType, int amount, boolean simulate) {
        ResourceLocation fluidLocation = ResourceLocation.parse(fluidType);
        Fluid resolvedFluid = BuiltInRegistries.FLUID.get(fluidLocation);

        if (resolvedFluid != null && resolvedFluid != Fluids.EMPTY) {
            FluidStack newStack = new FluidStack(resolvedFluid, amount);
            if (liquidTank.isFluidValid(newStack)) {
                IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
                return liquidTank.fill(newStack, action);
            }
        }
        return 0;
    }

    @Override
    public int extractFluid(String fluidType, int amount, boolean simulate) {
        ResourceLocation fluidLocation = ResourceLocation.parse(fluidType);
        Fluid resolvedFluid = BuiltInRegistries.FLUID.get(fluidLocation);

        if (resolvedFluid != null && resolvedFluid.isSame(gasTank.getFluid().getFluid())) {
            IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
            return gasTank.drain(amount, action).getAmount();
        }
        return 0;
    }

    public EnergyStorage getEnergyStorage() {return energyStorage;}
    public FluidTank getGasTank() {return gasTank;}
    public FluidTank getLiquidTank() {return liquidTank;}

    @Override
    public Component getDisplayName() {
        return Component.translatable("container." + HeliopauseCore.MOD_ID + ".gas_vaporizer");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new VaporizerMenu(i, inventory, this);
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
        tag.put("GasTank", gasTank.writeToNBT(registries, new CompoundTag()));
        tag.put("LiquidTank", liquidTank.writeToNBT(registries, new CompoundTag()));
        tag.putBoolean("IsActive", isActive);
        tag.putBoolean("Enabled", enabled);
        tag.putInt("ConversionScore", conversionScore);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("GasTank")) gasTank.readFromNBT(registries, tag.getCompound("GasTank"));
        if (tag.contains("LiquidTank")) liquidTank.readFromNBT(registries, tag.getCompound("LiquidTank"));
        isActive = tag.getBoolean("IsActive");
        enabled = tag.getBoolean("Enabled");
        conversionScore = tag.getInt("ConversionScore");
    }
}
