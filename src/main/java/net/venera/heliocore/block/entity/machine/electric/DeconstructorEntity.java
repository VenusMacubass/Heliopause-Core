package net.venera.heliocore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.util.MachineConfigHelper;
import org.jetbrains.annotations.Nullable;

public class DeconstructorEntity extends BaseElectricMachineEntity implements MachineConfigHelper.IToggleableMachine{
    public final ItemStackHandler inventory = new ItemStackHandler(11) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    
    private final int INPUT_SLOT = 0;
    private final int[] OUTPUT_SLOTS = {1,2,3,4,5,6,7,8,9};
    private final int BATTERY_SLOT = 10;
    private final int tierTransferRate;
    private final int ENERGY_USAGE;
    public boolean isActive = false;
    public boolean isEnabled = true;
    public DeconstructorEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState,int capacity, int transferRate, int energyUsage) {
        super(type, pos, blockState, 11, capacity, transferRate, transferRate);
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
                    case 2 -> tierTransferRate;
                    case 3 -> isActive ? 1 : 0;
                    case 4 -> isEnabled ? 1 : 0;
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
        if (level.isClientSide()) return;
        
        
        
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }
    
    
    
    
    private boolean hasEnergy(){
        return energyStorage.getEnergyStored() >= ENERGY_USAGE;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container." + HeliopauseCore.MOD_ID + ".deconstructor");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
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
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("isActive");
    }
}
