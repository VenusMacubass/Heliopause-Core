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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.hpc_custom.machine.BaseMachineBlock;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.fluid.IFluidMachine;
import net.venera.heliocore.item.hpc_custom.GasTankItem;
import net.venera.heliocore.util.MachineConfigHelper;
import net.venera.heliocore.util.OxygenVolumeHelper;
import org.jetbrains.annotations.Nullable;

public class OxygenSealerEntity extends BaseElectricMachineEntity implements IFluidMachine, MachineConfigHelper.IToggleableMachine{
    private final int OXYGEN_TANK_SLOT = 0;
    private final int THERMAL_EQUIPMENT_SLOT = 1;
    private final int BATTERY_SLOT = 2;
    public final int oxygenUsage;
    private final int maxOxygenCapacity = 6000;
    public boolean isActive = false;
    public boolean enabled = true;
    public boolean seal = false;
    private int currentVolumeCost = 1;
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
            return stack.getFluid().isSame(HpCFluids.OXYGEN.get());
        }
    };
    private final int maxVolume = 100000;
    
    public OxygenSealerEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, int transferRate, int usagePerTick, int oxygenUsage) {
        super(type, pos, state, 3, capacity, transferRate, usagePerTick);
        this.oxygenUsage = oxygenUsage;
    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> oxygenTank.getFluidAmount();
                    case 1 -> oxygenTank.getCapacity();
                    case 2 -> energyStorage.getEnergyStored();
                    case 3 -> energyStorage.getMaxEnergyStored();
                    case 4 -> isActive ? 1 : 0;
                    case 5 -> enabled ? 1 : 0;
                    case 6 -> seal ? 1 : 0;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> oxygenTank.setFluid(new FluidStack(HpCFluids.OXYGEN.get(), value));
                    case 4 -> isActive = value == 1;
                    case 5 -> enabled = value == 1;
                    case 6 -> seal = value == 1;
                }
            }
            @Override
            public int getCount() { return 7; }
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        if (level.isClientSide()) return;
        boolean dirty = false;
        boolean batteryProcessed = processBatterySlot(BATTERY_SLOT);
        boolean oxygenProcessed = processOxygenSlot(OXYGEN_TANK_SLOT);
        if (batteryProcessed || oxygenProcessed) dirty = true;
        if (enabled && level.getGameTime() % 5 == 0 && level.getBlockState(pos.above()).isAir()) {
            if(!seal){
                var result = OxygenVolumeHelper.scanAndRegisterRoom(level, pos, maxVolume);
                if (result != null) {
                    seal = true;
                    currentVolumeCost = oxygenUsage * (1 + result.airBlocks().size() / 1000);
                } else {
                    seal = false;
                }
            }
            if(seal) {
                if(!spendOxygen(currentVolumeCost)){
                    seal = false;
                    OxygenVolumeHelper.removeRoom(pos);
                }
            }
        }
        if (dirty) setChanged();
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }
    
    protected boolean spendOxygen(int volumeScale){
        FluidStack drainable = oxygenTank.drain(Math.min(volumeScale, oxygenTank.getFluidAmount()), IFluidHandler.FluidAction.SIMULATE);
        if (drainable.getAmount() > 0) {
            oxygenTank.drain(drainable, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }
        return false;
    }

    protected boolean processOxygenSlot(int slotIndex) {
        ItemStack inputStack = inventory.getStackInSlot(slotIndex);
        if (inputStack.getItem() instanceof GasTankItem gasTankItem) {
            GasTankData data = gasTankItem.getGasTankData(inputStack);

            if (data != null && !data.isEmpty() && data.getFluid() != null && data.getFluid().getFluidType() == HpCFluids.OXYGEN.get().getFluidType()) {
                int receivedGas = oxygenTank.fill(new FluidStack(data.getFluid(), data.amount()), IFluidHandler.FluidAction.SIMULATE);

                if (receivedGas > 0) {
                    oxygenTank.fill(new FluidStack(data.getFluid(), receivedGas), IFluidHandler.FluidAction.EXECUTE);
                    int actuallyDrained = gasTankItem.drain(inputStack, receivedGas);
                    return actuallyDrained > 0;
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
        return null;
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
