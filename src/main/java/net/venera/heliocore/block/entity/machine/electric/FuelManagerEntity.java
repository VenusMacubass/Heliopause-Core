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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.heliocore.block.custom.FluidPipeBlock;
import net.venera.heliocore.block.custom.LaunchPadBlock;
import net.venera.heliocore.block.custom.machine.BaseMachineBlock;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;
import net.venera.heliocore.fluid.IFluidMachine;
import net.venera.heliocore.screen.custom.FuelManagerMenu;
import net.venera.heliocore.util.MachineConfigHelper;
import net.venera.heliocore.util.PipeNetworkHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class FuelManagerEntity extends BaseElectricMachineEntity implements IFluidMachine, MachineConfigHelper.IToggleableMachine {
    private final int FUEL_SLOT = 0;
    private final int BATTERY_SLOT = 1;
    private int fuelAmount = 0;
    private final int maxFuel = 6000;
    private final int tierTransferRate;
    private int ENERGY_USAGE = 2;
    private int FUEL_LOAD_RATE = 1;
    public boolean isActive;
    public boolean isFueling = false;
    public boolean isCharging = false;
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
                    case 2 -> fuelAmount;
                    case 3 -> maxFuel;
                    case 4 -> tierTransferRate;
                    case 5 -> isActive ? 1 : 0;
                    case 6 -> isFueling ? 1 : 0;
                    case 7 -> isCharging ? 1 : 0;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {}
            @Override
            public int getCount() { return 8; }
        };
    }
    
    

    public void tick(Level level, BlockPos pos, BlockState state){



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

    private void pullFluidIn(Level level, BlockPos pos) {
        if (this.fuelAmount >= this.maxFuel) return;

        Direction machineFacing = this.getBlockState().getValue(BaseMachineBlock.FACING);
        Direction inputFace = machineFacing.getClockWise();

        BlockPos pipePos = pos.relative(inputFace);
        if (!(level.getBlockState(pipePos).getBlock() instanceof FluidPipeBlock)) return;

        Set<BlockEntity> connectedMachines = PipeNetworkHelper.findConnectedInventories(level, pipePos, pos);

        int spaceAvailable = this.maxFuel - this.fuelAmount;
        int fluidToPull = Math.min(spaceAvailable, FUEL_LOAD_RATE);

        for (BlockEntity entity : connectedMachines) {
            if (entity == this) continue;

            if (entity instanceof IFluidMachine targetMachine) {
                int availableToExtract = targetMachine.extractFluid("heliocore:refined_fuel", fluidToPull, true);

                if (availableToExtract > 0) {
                    int actuallyExtracted = targetMachine.extractFluid("heliocore:refined_fuel", availableToExtract, false);

                    this.fuelAmount += actuallyExtracted;

                    fluidToPull -= actuallyExtracted;
                    if (fluidToPull <= 0) break;
                }
            }
        }
    }

    private boolean hasEnergy(){
        return energyStorage.getEnergyStored() >= ENERGY_USAGE;
    }
    
    @Override
    public PortType getFluidPortType(Direction face) {
        return null;
    }

    @Override
    public int insertFluid(String fluidType, int amount, boolean simulate) {
        return 0;
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
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("isActive");
        isFueling = tag.getBoolean("isFueling");
        isCharging = tag.getBoolean("isCharging");
    }
}
