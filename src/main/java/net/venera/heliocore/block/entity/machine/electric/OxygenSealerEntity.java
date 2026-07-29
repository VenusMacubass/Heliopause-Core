package net.venera.heliocore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.hpc_custom.FluidPipeBlock;
import net.venera.heliocore.block.hpc_custom.machine.BaseMachineBlock;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.dimension.HpCDimensions;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.fluid.IFluidMachine;
import net.venera.heliocore.item.hpc_custom.GasTankItem;
import net.venera.heliocore.screen.hpc_custom.GasCompressorMenu;
import net.venera.heliocore.screen.hpc_custom.OxygenSealerMenu;
import net.venera.heliocore.util.MachineConfigHelper;
import net.venera.heliocore.util.OxygenVolumeHelper;
import net.venera.heliocore.util.PipeNetworkHelper;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Set;

public class OxygenSealerEntity extends BaseElectricMachineEntity implements IFluidMachine, MachineConfigHelper.IToggleableMachine{
    private final int OXYGEN_TANK_SLOT = 0;
    private final int THERMAL_EQUIPMENT_SLOT = 1;
    private final int BATTERY_SLOT = 2;
    public final int oxygenUsage;
    public final int energyUsage;
    private final int maxOxygenCapacity = 6000;
    private final int maxFlowRate;
    public boolean isActive = false;
    public boolean enabled = true;
    public boolean seal = false;
    private int currentVolumeCost = 1;
    private final int frequency = 5;
    public boolean isBlocked = false;
    public boolean hasEnoughEnergy = false;
    public boolean hasEnoughOxygen = false;
    public final FluidTank oxygenTank = new FluidTank(maxOxygenCapacity) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid().getFluidType() == HpCFluids.OXYGEN.get().getFluidType();
        }
    };
    private final int maxVolume = 100000;
    
    public OxygenSealerEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int energyCapacity, int energyTransferRate, int oxygenUsage, int energyUsage, int maxFlowRate) {
        super(type, pos, state, 3, energyCapacity, energyTransferRate, 0);
        this.oxygenUsage = oxygenUsage;
        this.energyUsage = energyUsage;
        this.maxFlowRate = maxFlowRate;
    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> oxygenTank.getFluidAmount();
                    case 1 -> oxygenTank.getCapacity();
                    case 2 -> isActive ? 1 : 0;
                    case 3 -> enabled ? 1 : 0;
                    case 4 -> seal ? 1 : 0;
                    case 5 -> isBlocked ? 1 : 0;
                    case 6 -> hasEnoughEnergy ? 1 : 0;
                    case 7 -> hasEnoughOxygen ? 1 : 0;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> oxygenTank.setFluid(new FluidStack(HpCFluids.OXYGEN.get(), value));
                    case 2 -> isActive = value == 1;
                    case 3 -> enabled = value == 1;
                    case 4 -> seal = value == 1;
                    case 5 -> isBlocked = value == 1;
                    case 6 -> hasEnoughEnergy = value == 1;
                    case 7 -> hasEnoughOxygen = value == 1;
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
        if (processOxygenSlot(OXYGEN_TANK_SLOT)) dirty = true;
        if (pullFluidIn(level, pos)) dirty = true;

        boolean hasEnergy = energyStorage.getEnergyStored() >= energyUsage * frequency;
        boolean isUnblocked = !level.getBlockState(pos.above()).isSolidRender(level, pos.above());

        this.isBlocked = !isUnblocked;
        this.hasEnoughEnergy = hasEnergy;
        this.hasEnoughOxygen = oxygenTank.getFluidAmount() >= currentVolumeCost;

        ResourceKey<Level> currentDimension = level.dimension();
        boolean isInSpace = currentDimension.location().toString().equals(HeliopauseCore.MOD_ID + ":moon");

        if (enabled && hasEnergy && isUnblocked) {
            if (!isActive) {
                isActive = true;
                dirty = true;
            }
            if (level.getGameTime() % frequency == 0) {
                if (seal) {
                    if (OxygenVolumeHelper.getExistingRoom(pos) == null) {
                        seal = false;
                        dirty = true;
                    } else if (!spendOxygen(currentVolumeCost)) {
                        seal = false;
                        OxygenVolumeHelper.removeRoom(pos);
                        dirty = true;
                    } else {
                        energyStorage.consumeEnergy(energyUsage * frequency);
                        if (level.getGameTime() % 20 == 0) {
                            if (!OxygenVolumeHelper.isPerimeterSafe(level, pos)) {
                                seal = false;
                                OxygenVolumeHelper.removeRoom(pos);
                                dirty = true;
                            }
                        }
                    }
                }
                if (!seal && isInSpace) {
                    var result = OxygenVolumeHelper.scanAndRegisterRoom(level, pos, maxVolume);

                    if (result != null) {
                        seal = true;
                        int totalCost = oxygenUsage * (1 + result.airBlocks().size() / 1000);
                        currentVolumeCost = totalCost;
                        dirty = true;
                    } else {
                        seal = false;
                    }
                }
            }
        }
        else {
            if (isActive || seal) {
                isActive = false;
                seal = false;
                OxygenVolumeHelper.removeRoom(pos);
                dirty = true;
            }
        }

        if (dirty) setChanged();
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

    protected boolean spendOxygen(int volumeScale){
        if (volumeScale <= 0) return true;

        FluidStack drainable = oxygenTank.drain(volumeScale, IFluidHandler.FluidAction.SIMULATE);
        if (drainable.getAmount() >= volumeScale) {
            oxygenTank.drain(volumeScale, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }
        return false;
    }

    protected boolean processOxygenSlot(int slotIndex) {
        ItemStack inputStack = inventory.getStackInSlot(slotIndex);
        if (inputStack.getItem() instanceof GasTankItem gasTankItem) {
            GasTankData data = gasTankItem.getGasTankData(inputStack);

            if (data != null && !data.isEmpty() && data.getFluid() != null && data.getFluid().getFluidType() == HpCFluids.OXYGEN.get().getFluidType()) {
                
                int amountToTry = Math.min(data.amount(), maxFlowRate);
                int maxAcceptable = oxygenTank.fill(new FluidStack(data.getFluid(), amountToTry), IFluidHandler.FluidAction.SIMULATE);

                if (maxAcceptable > 0) {
                    int actuallyDrained = gasTankItem.drain(inputStack, maxAcceptable);
                    
                    if (actuallyDrained > 0) {
                        oxygenTank.fill(new FluidStack(data.getFluid(), actuallyDrained), IFluidHandler.FluidAction.EXECUTE);
                        inventory.setStackInSlot(slotIndex, inputStack);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable("container." + HeliopauseCore.MOD_ID + ".oxygen_sealer");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new OxygenSealerMenu(i, inventory, this);
    }

    private boolean pullFluidIn(Level level, BlockPos pos) {
        if (oxygenTank.getFluidAmount() >= maxOxygenCapacity) return false;

        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        Direction inputFace = machineFacing.getClockWise();

        BlockPos pipePos = pos.relative(inputFace);
        if (!(level.getBlockState(pipePos).getBlock() instanceof FluidPipeBlock)) return false;

        Set<BlockEntity> connectedMachines = PipeNetworkHelper.findConnectedInventories(level, pipePos, pos);

        int spaceAvailable = oxygenTank.getSpace();
        int fluidToPull = Math.min(spaceAvailable, maxFlowRate);

        String targetFluidString = HpCFluids.OXYGEN.getId().toString();
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
                    oxygenTank.fill(new FluidStack(HpCFluids.OXYGEN.get(), actuallyExtracted), IFluidHandler.FluidAction.EXECUTE);

                    fluidToPull -= actuallyExtracted;
                    actuallyPulledSomething = true;
                    if (fluidToPull <= 0) break;
                }
            }
        }
        return actuallyPulledSomething;
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
        if(port == PortType.INPUT &&  !oxygenTank.isEmpty()){
            return BuiltInRegistries.FLUID.getKey(oxygenTank.getFluid().getFluid()).toString();
        }
        return null;
    }

    @Override
    public int insertFluid(String fluidType, int amount, boolean simulate) {
        ResourceLocation fluidLocation = ResourceLocation.parse(fluidType);
        Fluid resolvedFluid = BuiltInRegistries.FLUID.get(fluidLocation);

        if (resolvedFluid != null && resolvedFluid != Fluids.EMPTY) {
            FluidStack newStack = new FluidStack(resolvedFluid, amount);
            if (oxygenTank.isFluidValid(newStack)) {
                IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
                return oxygenTank.fill(newStack, action);
            }
        }
        return 0;
    }

    @Override
    public int extractFluid(String fluidType, int amount, boolean simulate) {
        return 0;
    }

    @Override
    public void toggleEnabled(int buttonId) {
        if (buttonId == 0) {
            this.enabled = !this.enabled;
        }
        this.setChanged();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        OxygenVolumeHelper.removeRoom(this.getBlockPos());
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("OxygenTank", oxygenTank.writeToNBT(registries, new CompoundTag()));
        tag.putBoolean("IsActive", isActive);
        tag.putBoolean("Enabled", enabled);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("OxygenTank")) oxygenTank.readFromNBT(registries, tag.getCompound("OxygenTank"));
        isActive = tag.getBoolean("IsActive");
        enabled = tag.getBoolean("Enabled");
    }
}
