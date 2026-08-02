package net.venera.heliocore.block.entity.machine.electric;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.item.hpc_custom.BatteryItem;
import net.venera.heliocore.recipe.CoalCompressorRecipe;
import net.venera.heliocore.recipe.HpCRecipes;
import net.venera.heliocore.recipe.PCBFabricatorInput;
import net.venera.heliocore.recipe.PCBFabricatorRecipe;
import net.venera.heliocore.screen.hpc_custom.PCBFabricatorMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PCBFabricatorEntity extends BaseElectricMachineEntity{
    public final ItemStackHandler inventory = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final int[] INPUT_SLOTS = {0,1,2,3,4,5};
    private final int OUTPUT_SLOT = 6;
    private final int BATTERY_SLOT = 7;
    private final int energyTransferRate;
    private final int ENERGY_USAGE;
    public boolean isActive = false;
    private int progress = 0;
    private int maxProgress = 400;
    public PCBFabricatorEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, int energyCapacity, int energyTransferRate, int energyUsage) {
        super(type, pos, blockState, 11, energyCapacity, energyTransferRate, 0);
        this.energyTransferRate = energyTransferRate;
        this.ENERGY_USAGE = energyUsage;
    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> isActive ? 1 : 0;
                    case 1 -> progress;
                    case 2 -> maxProgress;
                    default -> 0;
                };
            }
            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 1 -> progress = value;
                    case 2 -> maxProgress = value;
                }
            }
            @Override
            public int getCount() { return 3; }
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        if (level.isClientSide()) return;
        boolean dirty = false;

        if (processBatterySlot(BATTERY_SLOT)) dirty = true;

        boolean wasActive = this.isActive;
        this.isActive = false;

        if (hasEnergy()) {
            Optional<RecipeHolder<PCBFabricatorRecipe>> recipeHolder = getCurrentRecipe();

            if (recipeHolder.isPresent()) {
                PCBFabricatorRecipe recipe = recipeHolder.get().value();
                ItemStack result = recipe.getResultItem(level.registryAccess());
                
                ItemStack simulatedRemainder = this.inventory.insertItem(OUTPUT_SLOT, result.copy(), true);

                if (simulatedRemainder.isEmpty()) {
                    this.isActive = true;
                    this.progress++;
                    this.energyStorage.consumeEnergy(ENERGY_USAGE);

                    if (this.progress >= this.maxProgress) {
                        // Actually insert the item (simulate = false)
                        this.inventory.insertItem(OUTPUT_SLOT, result.copy(), false);

                        for (int i = 0; i < INPUT_SLOTS.length; i++) {
                            this.inventory.extractItem(INPUT_SLOTS[i], 1, false);
                        }

                        this.progress = 0;
                    }
                    dirty = true;
                } else {
                    // Recipe is valid, but the output slot is too full to accept the items
                    this.progress = 0;
                }
            } else {
                // No valid recipe exists for the current inputs
                this.progress = 0;
            }
        } else {
            if (this.progress > 0) {
                this.progress = Math.max(0, this.progress - 2);
                dirty = true;
            }
        }

        if (wasActive != this.isActive) {
            dirty = true;
        }

        if (dirty) setChanged();
        BaseElectricMachineEntity.tick(level, pos, state, this);
    }

    private Optional<RecipeHolder<PCBFabricatorRecipe>> getCurrentRecipe() {
        if (this.level == null) return Optional.empty();

        PCBFabricatorInput currentInput = new PCBFabricatorInput(
                this.inventory.getStackInSlot(0),
                this.inventory.getStackInSlot(1),
                this.inventory.getStackInSlot(2),
                this.inventory.getStackInSlot(3),
                this.inventory.getStackInSlot(4),
                this.inventory.getStackInSlot(5)
        );

        return this.level.getRecipeManager().getRecipeFor(HpCRecipes.PCB_FABRICATOR_TYPE.get(), currentInput, this.level);
    }

    protected boolean processBatterySlot(int slotIndex) {
        ItemStack batteryStack = inventory.getStackInSlot(slotIndex);
        if (batteryStack.getItem() instanceof BatteryItem batteryItem) {
            int spaceInMachine = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
            int toTransfer = batteryItem.extractEnergy(batteryStack, spaceInMachine, true);
            if (toTransfer > 0) {
                batteryItem.extractEnergy(batteryStack, toTransfer, false);
                energyStorage.addEnergy(toTransfer); 
                inventory.setStackInSlot(slotIndex, batteryStack);
                return true;
            }
        }
        return false;
    }

    private boolean hasEnergy(){
        return energyStorage.getEnergyStored() >= ENERGY_USAGE;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container." + HeliopauseCore.MOD_ID + ".pcb_fabricator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new PCBFabricatorMenu(i, inventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", this.inventory.serializeNBT(registries));
        tag.putInt("Progress", this.progress);
        tag.putBoolean("isActive", this.isActive);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        this.progress = tag.getInt("Progress");
        this.isActive = tag.getBoolean("isActive");
    }
}
