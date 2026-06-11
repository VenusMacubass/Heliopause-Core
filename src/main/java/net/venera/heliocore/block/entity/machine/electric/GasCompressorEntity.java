package net.venera.heliocore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.heliocore.fluid.IFluidMachine;
import net.venera.heliocore.util.MachineConfigHelper;
import org.jetbrains.annotations.Nullable;

public class GasCompressorEntity extends BaseElectricMachineEntity implements IFluidMachine, MachineConfigHelper.IToggleableMachine{
    public GasCompressorEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int slotCount, int capacity, int transferRate) {
        super(type, pos, state, slotCount, capacity, transferRate, 0);
    }

    @Override
    protected ContainerData initContainerData() {
        return null;
    }


    public void tick(Level level, BlockPos pos, BlockState state) {

        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
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
    public void toggleEnabled(int buttonId) {

    }
}
