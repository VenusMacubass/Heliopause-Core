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
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.hpc_custom.LaunchPadBlock;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;
import net.venera.heliocore.screen.hpc_custom.CargoManagerMenu;
import net.venera.heliocore.util.MachineConfigHelper;
import org.jetbrains.annotations.Nullable;

public class CargoManagerEntity extends BaseElectricMachineEntity implements MachineConfigHelper.IToggleableMachine {
    private final int[] INPUT_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    private final int[] OUTPUT_SLOTS = {9, 10, 11, 12, 13, 14, 15, 16, 17};
    private final int BATTERY_SLOT = 18;
    private final int tierTransferRate;
    private final int ENERGY_USAGE;
    public boolean isActive = false;
    public boolean isLoading = false;
    public boolean isUnloading = false;
    private final int frequency = 5; 
    public CargoManagerEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int energyCapacity, int energyTransferRate, int energyUsage) {
        super(type, pos, blockState, 19, energyCapacity, energyTransferRate, 0);
        this.tierTransferRate = energyTransferRate;
        this.ENERGY_USAGE = energyUsage;
    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> isActive ? 1 : 0;
                    case 1 -> isLoading ? 1 : 0;
                    case 2 -> isUnloading ? 1 : 0;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {}
            @Override
            public int getCount() { return 3; }
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        if (level.isClientSide()) return;
        boolean dirty = false;
        if(processBatterySlot(BATTERY_SLOT)) dirty = true;

        if(level.getGameTime() % frequency == 0) {
            boolean didWork = false;
            Tier1RocketEntity rocketEntity = findValidRocket(level, pos);

            if (rocketEntity != null && hasEnergy()) {
                if (isLoading && processLoading(rocketEntity)) {
                    this.energyStorage.consumeEnergy(this.ENERGY_USAGE * frequency);
                    didWork = true;
                }
                
                if (isUnloading && hasEnergy() && processUnloading(rocketEntity)) {
                    this.energyStorage.consumeEnergy(this.ENERGY_USAGE * frequency);
                    didWork = true;
                }
            }
            
            if (didWork) {
                if (!isActive) {
                    isActive = true;
                    dirty = true;
                }
            } else {
                if (isActive) {
                    isActive = false;
                    dirty = true;
                }
            }
        }

        if (dirty) setChanged();
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }
    
    public boolean processLoading(Tier1RocketEntity rocketEntity){
        for (int i : INPUT_SLOTS) {
            ItemStack extracted = inventory.extractItem(i, 1, true);
            if (extracted.isEmpty()) continue; 
            
            ItemStack leftover = ItemHandlerHelper.insertItem(rocketEntity.inventory, extracted, true);
            
            if (leftover.isEmpty()) {
                inventory.extractItem(i, 1, false); 
                ItemHandlerHelper.insertItem(rocketEntity.inventory, extracted, false); 
                
                return true;
            }
        }
        return false; 
    }
    
    public boolean processUnloading(Tier1RocketEntity rocketEntity){
        for (int i = 0; i < rocketEntity.inventory.getSlots(); i++) {
            ItemStack extracted = rocketEntity.inventory.extractItem(i, 1, true);
            if (extracted.isEmpty()) continue;
            
            for (int outSlot : OUTPUT_SLOTS) {
                ItemStack leftover = inventory.insertItem(outSlot, extracted, true);
                
                if (leftover.isEmpty()) {
                    rocketEntity.inventory.extractItem(i, 1, false);
                    inventory.insertItem(outSlot, extracted, false); 
                    
                    return true;
                }
            }
        }
        return false; 
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

    private boolean hasEnergy(){
        return energyStorage.getEnergyStored() >= ENERGY_USAGE * frequency;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container." + HeliopauseCore.MOD_ID + ".cargo_manager");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CargoManagerMenu(i, inventory, this, this.data);
    }

    @Override
    public void toggleEnabled(int buttonId) {
        if (buttonId == 0) {
            this.isLoading = !this.isLoading;
        } else if (buttonId == 1) {
            this.isUnloading = !this.isUnloading;
        }
        this.setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isActive", isActive);
        tag.putBoolean("isLoading", isLoading);
        tag.putBoolean("isUnloading", isUnloading);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("isActive");
        isLoading = tag.getBoolean("isLoading");
        isUnloading = tag.getBoolean("isUnloading");
    }
}

