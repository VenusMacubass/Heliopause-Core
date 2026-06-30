package net.venera.heliocore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.hpc_custom.FluidPipeBlock;
import net.venera.heliocore.block.hpc_custom.machine.BaseMachineBlock;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.fluid.IFluidMachine;
import net.venera.heliocore.item.hpc_custom.BatteryItem;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import net.venera.heliocore.item.hpc_custom.GasTankItem;
import net.venera.heliocore.screen.custom.BasicSolarMenu;
import net.venera.heliocore.screen.custom.OxygenGeneratorMenu;
import net.venera.heliocore.util.MachineConfigHelper;
import net.venera.heliocore.util.PipeNetworkHelper;
import org.jetbrains.annotations.Nullable;

public class OxygenGeneratorEntity extends BaseElectricMachineEntity implements IFluidMachine, MachineConfigHelper.IToggleableMachine {
    private final int BATTERY_SLOT = 0;
    private final int OXYGEN_GAS_SLOT = 1;
    public final int maxCapacity = 5000;
    private final int ENERGY_USAGE = 5;
    private final int oxygenGenerationRate = 3;
    private int oxygenToGenerate = 0;
    public final FluidTank oxygenTank = new FluidTank(maxCapacity){
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
    public boolean isActive;
    public boolean isEnabled = true;
    
    public OxygenGeneratorEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, int transferRate) {
        super(type, pos, state, 2, capacity, transferRate, 2);
    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> energyStorage.getMaxEnergyStored();
                    case 2 -> oxygenTank.getFluidAmount();
                    case 3 -> maxCapacity;
                    case 5 -> isActive ? 1 : 0;
                    case 6 -> isEnabled ? 1 : 0;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {
                switch(i) {
                    case 2 -> oxygenTank.setFluid(new FluidStack(HpCFluids.OXYGEN.get(), value));
                    case 5 -> isActive = value == 1;
                    case 6 -> isEnabled = value == 1;
                }
            }
            @Override
            public int getCount() { return 7; }
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;
        boolean dirty = false;
        boolean batteryProcessed = processBatterySlot(BATTERY_SLOT);
        boolean oxygenProcessed = processOxygenSlot(OXYGEN_GAS_SLOT);
        if (batteryProcessed || oxygenProcessed) dirty = true;
        if (isEnabled && level.getGameTime() % 2 == 0) {
            if (canGenerateOxygen(pos)) {
                if(level.getGameTime() % 40 == 0){
                    oxygenToGenerate = generateableOxygen(level, pos);
                }
                generateOxygen(oxygenToGenerate);

                this.energyStorage.extractEnergy(ENERGY_USAGE, false);
                this.isActive = true;
                dirty = true;
            } else {
                if (this.isActive) {
                    this.isActive = false;
                    dirty = true;
                }
            }
            if (oxygenTank.getFluidAmount() > 0) {
                pumpFluidOut(level, pos);
                dirty = true;
            }
        }

        if (dirty) setChanged();
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

    protected boolean processOxygenSlot(int slotIndex) {
        ItemStack oxygenStack = inventory.getStackInSlot(slotIndex);
        if(oxygenStack.getItem() instanceof GasTankItem gasTankItem){
            GasTankData data = gasTankItem.getGasTankData(oxygenStack);
            if (data == null) return false;
            FluidStack toOxygenTank = oxygenTank.drain(new FluidStack(HpCFluids.OXYGEN.get(), data.getSpace()), IFluidHandler.FluidAction.SIMULATE);
            if(data.isEmpty() || data.isOxygen()) {
                if(toOxygenTank.getAmount() > 0) {
                    int actuallyFilled = gasTankItem.fill(oxygenStack, GasTankData.OXYGEN_GAS, toOxygenTank.getAmount());
                    oxygenTank.drain(toOxygenTank, IFluidHandler.FluidAction.EXECUTE);
                    return actuallyFilled > 0;
                }
            }
        }
        return false;
    }

    private void generateOxygen(int oxyGen){
        int generatedOxygen = oxygenTank.fill(new FluidStack(HpCFluids.OXYGEN.get(), oxyGen), IFluidHandler.FluidAction.SIMULATE);
        if(generatedOxygen > 0){
            oxygenTank.fill(new FluidStack(HpCFluids.OXYGEN.get(), generatedOxygen), IFluidHandler.FluidAction.EXECUTE);
        }
    }
    
    private int generateableOxygen(Level level, BlockPos pos){
        int passiveDimensionGenValue = 0;
        if (this.level != null) {
            if (this.level.dimension() == Level.OVERWORLD) {
                passiveDimensionGenValue = oxygenGenerationRate;
            } 
            else if (this.level.dimension() == Level.NETHER) {
                passiveDimensionGenValue =  oxygenGenerationRate - 1;
            } 
            else if (this.level.dimension() == Level.END) {
                passiveDimensionGenValue = oxygenGenerationRate;
            }
            ResourceLocation currentDim = this.level.dimension().location();
            if (currentDim.getNamespace().equals(HeliopauseCore.MOD_ID) && currentDim.getPath().equals("moon")) {
                passiveDimensionGenValue = 0;
            }
        }
        BlockPos corner1 = pos.offset(-6, -6, -6);
        BlockPos corner2 = pos.offset(6, 6, 6);
        int leafBonus = 0;

        for (BlockPos targetPos : BlockPos.betweenClosed(corner1, corner2)) {
            if (targetPos.distSqr(pos) <= 36) { 

                BlockState state = level.getBlockState(targetPos);
                
                if (state.is(BlockTags.LEAVES)) {
                    leafBonus++;
                }
            }
        }
        return  passiveDimensionGenValue + leafBonus/5;
    }

    private void pumpFluidOut(Level level, BlockPos pos) {
        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        Direction outputFace = machineFacing.getOpposite(); // Based on your getFluidPortType

        BlockPos pipePos = pos.relative(outputFace);
        if (!(level.getBlockState(pipePos).getBlock() instanceof FluidPipeBlock)) return;

        java.util.Set<BlockEntity> connectedMachines = PipeNetworkHelper.findConnectedInventories(level, pipePos, pos);
        int fluidToPush = Math.min(oxygenTank.getFluidAmount(), 10); // Max flow rate of 10

        for (BlockEntity entity : connectedMachines) {
            if (entity == this) continue;

            if (fluidToPush <= 0) break;

            if (entity instanceof IFluidMachine targetMachine && !oxygenTank.isEmpty()) {
                ResourceLocation fluidId = BuiltInRegistries.FLUID.getKey(oxygenTank.getFluid().getFluid());
                String fluidTypeString = fluidId.toString();

                int accepted = targetMachine.insertFluid(fluidTypeString, fluidToPush, false);

                if (accepted > 0) {
                    oxygenTank.drain(accepted, IFluidHandler.FluidAction.EXECUTE);
                    fluidToPush -= accepted;
                }
            }
        }
    }

    private boolean canGenerateOxygen(BlockPos pos){
        boolean spaceCheck = oxygenTank.getSpace() > 0;
        boolean energyCheck = energyStorage.getEnergyStored() >= ENERGY_USAGE;

        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        Direction rightSide = machineFacing.getClockWise();
        Direction leftSide = machineFacing.getCounterClockWise();
        BlockPos rightPos = pos.relative(rightSide);
        BlockPos leftPos = pos.relative(leftSide);
        BlockState rightState = level.getBlockState(rightPos);
        BlockState leftState = level.getBlockState(leftPos);
        
        boolean blockedState = rightState.isFaceSturdy(level, rightPos, rightSide.getOpposite()) ||
                leftState.isFaceSturdy(level, leftPos, leftSide.getOpposite());

        return spaceCheck && energyCheck && !blockedState;
    }
    
    @Override
    public Component getDisplayName() {
        return Component.literal("Oxygen Extractor");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new OxygenGeneratorMenu(i, inventory, this, this.data);
    }

    @Override
    public int insertFluid(String fluidType, int amount, boolean simulate) {
        return 0;
    }

    @Override
    public int extractFluid(String fluidType, int amount, boolean simulate) {
        if (!fluidType.equals(HeliopauseCore.MOD_ID + ":oxygen")) return 0;

        IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
        return oxygenTank.drain(amount, action).getAmount();
    }
    
    @Override
    public PortType getFluidPortType(Direction face) {
        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        if (face == machineFacing.getOpposite()) {
            return PortType.OUTPUT;
        }
        return PortType.NONE;
    }

    @Override
    public @Nullable String peekFluid(Direction face) {
        PortType port = getFluidPortType(face);
        if(port == PortType.OUTPUT && !oxygenTank.isEmpty()){
            return BuiltInRegistries.FLUID.getKey(oxygenTank.getFluid().getFluid()).toString();
        }
        return null;
    }

    @Override
    public void toggleEnabled(int buttonId) {
        if (buttonId == 0) {
            this.isEnabled = !this.isEnabled;
        } 
        this.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isActive", isActive);
        tag.putBoolean("isEnabled", isEnabled);
        tag.put("OxygenTank", oxygenTank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("isActive");
        isEnabled = tag.getBoolean("isEnabled");
        if (tag.contains("OxygenTank")) oxygenTank.readFromNBT(registries, tag.getCompound("OxygenTank"));
    }
}
