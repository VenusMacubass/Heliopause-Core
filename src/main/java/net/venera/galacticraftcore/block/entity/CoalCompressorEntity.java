package net.venera.galacticraftcore.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforgespi.locating.ForgeFeature;
import net.venera.galacticraftcore.item.ModItems;
import net.venera.galacticraftcore.recipe.CoalCompressorRecipe;
import net.venera.galacticraftcore.recipe.ModRecipes;
import net.venera.galacticraftcore.screen.custom.CoalCompressorMenu;
import net.venera.galacticraftcore.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoalCompressorEntity extends BlockEntity implements MenuProvider {
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
    public final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    public int burnTime = 0;
    public int maxBurnTime;
    public boolean isActive = false;
    public CoalCompressorEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.COAL_COMPRESSOR_ENTITY.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> CoalCompressorEntity.this.progress;
                    case 1 -> CoalCompressorEntity.this.maxProgress;
                    case 2 -> CoalCompressorEntity.this.burnTime;
                    case 3 -> CoalCompressorEntity.this.maxBurnTime;
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
                return 4;
            }

        };
    }

    public void tick(Level level, BlockPos pos, BlockState state, CoalCompressorEntity entity) {
        if (level.isClientSide()) return;
        CoalCompressorRecipe recipe = getMatchingRecipe();
        boolean dirty = false;

        // Consume burn time
        if (burnTime > 0) {
            burnTime--;
            dirty = true;
        }

        // If burning and can craft, progress
        if (burnTime > 0 && canCraft()) {
            progress++;
            isActive = true;
            dirty = true;

            if (progress >= maxProgress) {
                if (recipe != null) {
                    craftItem(recipe);
                    progress = 0;
                    dirty = true;
                }
            }
        } else {
            // Not burning or can't craft
            if (isActive) {
                isActive = false;
                dirty = true;
            }

            // Rapid decay ONLY when we have no fuel but had progress
            if (burnTime <= 0 && progress > 0 && getFuelStack().getCount() <= 0) {
                progress = Math.max(0, progress - 2); // Controlled decay
                dirty = true;
            }
            // Reset completely if recipe inputs are gone
            else if (progress > 0 && getMatchingRecipe() == null) {
                progress = 0;
                dirty = true;
            }

            // Try to start crafting if we have fuel
            if (burnTime <= 0 && canCraft()) {
                burnFuel();
                dirty = true;
            }
        }


        if (dirty) setChanged();
    }


   private boolean canCraft(){
       if (level == null) return false;
       if (isActive) return false;

       CoalCompressorRecipe recipe = getMatchingRecipe();
       if (recipe == null) return false;

       // Check fuel - only if we're out of burn time
       if (burnTime <= 0 && getFuelStack().getBurnTime(null) <= 0) return false;

       // Check output
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

    private void craftItem(CoalCompressorRecipe recipe) {
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();

        // Consume each input stack by 1 if not empty
        for (int i : INPUT_SLOTS) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) stack.shrink(1);
        }

        // Output result
        ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);
        if (output.isEmpty()) {
            inventory.setStackInSlot(OUTPUT_SLOT, result);
        } else {
            output.grow(result.getCount());
            inventory.setStackInSlot(OUTPUT_SLOT, output);
        }
    }

    private CoalCompressorRecipe getMatchingRecipe() {
        if (level == null) return null;

        // Create a CraftingInput from our input slots
        NonNullList<ItemStack> craftingGrid = NonNullList.withSize(9, ItemStack.EMPTY);
        for (int i = 0; i < INPUT_SLOTS.length; i++) {
            craftingGrid.set(i, inventory.getStackInSlot(INPUT_SLOTS[i]));
        }

        CraftingInput input = CraftingInput.of(3, 3, craftingGrid);

        // Get all recipes and find the first match
        List<RecipeHolder<CoalCompressorRecipe>> recipeHolders = level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.COAL_COMPRESSOR_TYPE.get());

        for (RecipeHolder<CoalCompressorRecipe> holder : recipeHolders) {
            CoalCompressorRecipe recipe = holder.value();
            if (recipe.matches(input, level)) {
                return recipe;
            }
        }
        return null;
    }

    private ItemStack getFuelStack() {
        return inventory.getStackInSlot(FUEL_SLOT);
    }

    public ItemStack getOutputStack() {
        return inventory.getStackInSlot(OUTPUT_SLOT);
    }

    public void setOutputStack(ItemStack outputStack) {
        inventory.setStackInSlot(OUTPUT_SLOT, outputStack);
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
        return new CoalCompressorMenu(i, inventory, this);
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

        // Calculate the scaled progress based on texture length
        int scaledProgress = (int) ((float) progress / maxProgress * pixels);

        // Ensure we don't exceed the texture bounds
        return Math.min(scaledProgress, pixels);
    }
    public int getFireIconScaled(int pixels) {
        if (burnTime == 0) return 0;

        // Calculate the scaled progress based on texture length
        int scaledFire = (int) ((float) burnTime / maxBurnTime * pixels);

        // Ensure we don't exceed the texture bounds
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
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        burnTime = tag.getInt("BurnTime");
        maxBurnTime = tag.getInt("MaxBurnTime");
    }
    private void sendDebugMessage() {
        if (level == null || level.isClientSide) return;

        // Find the nearest player within 16 blocks
        Player nearestPlayer = level.getNearestPlayer(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 16, false);

        if (nearestPlayer != null) {
            nearestPlayer.sendSystemMessage(Component.literal(
                    "[CoalCompressor] Crafted item! Burn Time: " + burnTime));
        }
    }
}
