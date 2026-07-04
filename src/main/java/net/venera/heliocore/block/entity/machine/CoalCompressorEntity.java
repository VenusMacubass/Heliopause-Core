package net.venera.heliocore.block.entity.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import net.venera.heliocore.recipe.CoalCompressorRecipe;
import net.venera.heliocore.recipe.HpCRecipes;
import net.venera.heliocore.screen.custom.CoalCompressorMenu;
import org.jetbrains.annotations.Nullable;

public class CoalCompressorEntity extends BaseMachineEntity{
        public final ItemStackHandler inventory = new ItemStackHandler(11) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(!level.isClientSide()){
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }
        };

    private final int[] INPUT_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    private final int FUEL_SLOT = 9;
    private final int OUTPUT_SLOT = 10;
    private int progress = 0;
    private int maxProgress = 200;
    public int burnTime = 0;
    public int maxBurnTime;
    public boolean isActive = false;
    public boolean compressing = false;
    public CoalCompressorEntity(BlockPos pos, BlockState blockState) {
        super(HpCBlockEntities.COAL_COMPRESSOR_ENTITY.get(), pos, blockState, 11);

    }

    @Override
    protected ContainerData initContainerData() {
        return new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> CoalCompressorEntity.this.progress;
                    case 1 -> CoalCompressorEntity.this.maxProgress;
                    case 2 -> CoalCompressorEntity.this.burnTime;
                    case 3 -> CoalCompressorEntity.this.maxBurnTime;
                    case 4 -> CoalCompressorEntity.this.isActive ? 1 : 0;     
                    case 5 -> CoalCompressorEntity.this.compressing ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> CoalCompressorEntity.this.progress = value;
                    case 1 -> CoalCompressorEntity.this.maxProgress = value;
                    case 2 -> CoalCompressorEntity.this.burnTime = value;
                    case 3 -> CoalCompressorEntity.this.maxBurnTime = value;
                }
            }

            @Override
            public int getCount() {
                return 6;
            }
            
            
        };
    }

    public void tick(Level level, BlockPos pos, BlockState state, CoalCompressorEntity entity) {
        if (level.isClientSide()) return;
        
        RecipeHolder<CoalCompressorRecipe> compRecipe = getCompressorRecipe();
        RecipeHolder<BlastingRecipe> blastRecipe = getBlastingRecipe();
        
        Recipe<?> activeRecipe = null;
        if (compRecipe != null) activeRecipe = compRecipe.value();
        else if (blastRecipe != null) activeRecipe = blastRecipe.value();

        boolean dirty = false;

        if (burnTime > 0) {
            burnTime--;
            dirty = true;
        }

        if (burnTime > 0 && activeRecipe != null && canCraft(activeRecipe)) {
            progress++;
            isActive = true;
            compressing = (activeRecipe instanceof CoalCompressorRecipe);

            dirty = true;

            if (progress >= maxProgress) {
                craftItem(activeRecipe);
                progress = 0;
                dirty = true;
            }
        } else {
            if (isActive || compressing) {
                isActive = false;
                compressing = false;
                dirty = true;
            }

            if (burnTime <= 0 && progress > 0 && getFuelStack().getCount() <= 0) {
                progress = Math.max(0, progress - 2);
                dirty = true;
            }
            else if (progress > 0 && activeRecipe == null) {
                progress = 0;
                dirty = true;
            }

            if (burnTime <= 0 && activeRecipe != null && canCraft(activeRecipe)) {
                burnFuel();
                dirty = true;
            }
        }
        if (dirty) setChanged();
    }

    private boolean canCraft(Recipe<?> recipe) {
        if (level == null || recipe == null) return false;

        if (burnTime <= 0 && getFuelStack().getBurnTime(null) <= 0) return false;

        ItemStack output = getOutputStack();
        ItemStack result = recipe.getResultItem(level.registryAccess());

        if (!output.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(output, result)) return false;
            if (output.getCount() + result.getCount() > output.getMaxStackSize()) return false;
        }
        return true;
    }

    private void startCrafting() {
        burnFuel();
        progress = 0;
        setChanged();
    }

    private void burnFuel() {
        ItemStack fuelStack = getFuelStack();
        if (fuelStack.isEmpty()) return;

        int burnTimeValue = fuelStack.getBurnTime(null);
        if (burnTimeValue > 0) {
            burnTime = burnTimeValue;
            maxBurnTime = burnTimeValue;
            fuelStack.shrink(1);
            inventory.setStackInSlot(FUEL_SLOT, fuelStack);
        }
    }

    private void craftItem(Recipe<?> recipe) {
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();

        if (recipe instanceof CoalCompressorRecipe) {
            for (int i : INPUT_SLOTS) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty()) stack.shrink(1);
            }
        } else {
            for (int i : INPUT_SLOTS) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    stack.shrink(1);
                    break;
                }
            }
        }
        ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);
        if (output.isEmpty()) {
            inventory.setStackInSlot(OUTPUT_SLOT, result);
        } else {
            output.grow(result.getCount());
            inventory.setStackInSlot(OUTPUT_SLOT, output);
        }
    }

    private RecipeHolder<CoalCompressorRecipe> getCompressorRecipe() {
        if (level == null) return null;

        NonNullList<ItemStack> craftingGrid = NonNullList.withSize(9, ItemStack.EMPTY);
        for (int i = 0; i < INPUT_SLOTS.length; i++) {
            craftingGrid.set(i, inventory.getStackInSlot(INPUT_SLOTS[i]));
        }

        CraftingInput input = CraftingInput.of(3, 3, craftingGrid);
        
        return level.getRecipeManager().getRecipeFor(HpCRecipes.COAL_COMPRESSOR_TYPE.get(), input, level).orElse(null);
    }

    private RecipeHolder<BlastingRecipe> getBlastingRecipe() {
        if (level == null) return null;

        ItemStack inputItem = ItemStack.EMPTY;
        int count = 0;
        
        for (int i : INPUT_SLOTS) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                inputItem = stack;
                count++;
            }
        }
        if (count != 1) return null;

        SingleRecipeInput input = new SingleRecipeInput(inputItem);

        return level.getRecipeManager().getRecipeFor(RecipeType.BLASTING, input, level).orElse(null);
    }

    private ItemStack getFuelStack() {
        return inventory.getStackInSlot(FUEL_SLOT);
    }

    public ItemStack getOutputStack() {
        return inventory.getStackInSlot(OUTPUT_SLOT);
    }
    
    public void drops(){
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++){
            inv.setItem(i, inventory.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.minecraft.coal_compressor");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CoalCompressorMenu(i, inventory, this, this.data);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public int getArrowScaled(int pixels) {
        if (maxProgress == 0) return 0;
        
        int scaledProgress = (int) ((float) progress / maxProgress * pixels); 
        
        return Math.min(scaledProgress, pixels); 
    }
    public int getFireIconScaled(int pixels) {
        if (burnTime == 0) return 0;
        
        int scaledFire = (int) ((float) burnTime / maxBurnTime * pixels); 
        
        return Math.min(scaledFire, pixels); 
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("Progress", progress);
        tag.putInt("MaxProgress", maxProgress);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("MaxBurnTime", maxBurnTime);
        tag.putBoolean("Compressing", compressing);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        burnTime = tag.getInt("BurnTime");
        maxBurnTime = tag.getInt("MaxBurnTime");
        compressing = tag.getBoolean("Compressing");
    }
}
