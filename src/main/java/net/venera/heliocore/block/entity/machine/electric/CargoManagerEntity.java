package net.venera.heliocore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class CargoManagerEntity extends BaseElectricMachineEntity {
    public final ItemStackHandler inventory = new ItemStackHandler(18) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final int[] INPUT_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    private final int[] OUTPUT_SLOTS = {9, 10, 11, 12, 13, 14, 15, 16, 17};
    private final int BATTERY_SLOT = 18;
    private final int tierTransferRate;
    public boolean isActive;
    public CargoManagerEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int capacity, int transferRate) {
        super(type, pos, blockState, 2, capacity, transferRate, transferRate);
        this.tierTransferRate = transferRate;
    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> energyStorage.getEnergyStored();
                    case 1 -> energyStorage.getMaxEnergyStored();
                    case 2 -> tierTransferRate;
                    case 4 -> isActive ? 1 : 0;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {}
            @Override
            public int getCount() { return 5; }
        };
    }
    
    public void tick(Level level, BlockPos pos, BlockState state){



        BaseElectricMachineEntity.tick(level, pos, state, this);
    }
    
    public void processLoading(Level level, BlockPos pos, BlockState state){
        
    }

    @Override
    public Component getDisplayName() {
        return null;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }
}

