package net.venera.heliocore.block.entity.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import net.venera.heliocore.recipe.*;
import net.venera.heliocore.screen.hpc_custom.MagneticAssemblyPlatformMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MagneticAssemblyPlatformEntity extends BaseMachineEntity{
    public final ItemStackHandler inventory = new ItemStackHandler(18) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide() && slot != ROCKET_SLOT){
                craftRocket();
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final int ROCKET_SLOT = 17;
    private final int NOSE_CONE_SLOT = 16;
    private final int[] BOOSTER_SLOTS = {14,15};
    private final int[] HULL_SLOTS = {6,7,8,9,10,11,12,13};
    private final int[] FIN_SLOTS = {2,3,4,5};
    private final int BASE_SLOT = 1;
    private final int ENGINE_SLOT = 0;
   
    public MagneticAssemblyPlatformEntity(BlockPos pos, BlockState blockState) {
        super(HpCBlockEntities.MAGNETIC_ASSEMBLY_PLATFORM_ENTITY.get(), pos, blockState, 18);
    }
    
    private void craftRocket() {
        Optional<RecipeHolder<MagneticAssemblyPlatformRecipe>> recipeHolder = getCurrentRecipe();
        if (recipeHolder.isEmpty()) {
            inventory.setStackInSlot(ROCKET_SLOT, ItemStack.EMPTY);
            return;
        }
        MagneticAssemblyPlatformRecipe recipe = recipeHolder.get().value();
        ItemStack result = recipe.getResultItem(level.registryAccess());
        inventory.insertItem(ROCKET_SLOT, result.copy(), false);
    }

    private Optional<RecipeHolder<MagneticAssemblyPlatformRecipe>> getCurrentRecipe() {
        if (this.level == null) return Optional.empty();

        MagneticAssemblyPlatformInput currentInput = new MagneticAssemblyPlatformInput(
                this.inventory.getStackInSlot(ENGINE_SLOT),
                this.inventory.getStackInSlot(BASE_SLOT),
                this.inventory.getStackInSlot(FIN_SLOTS[0]),
                this.inventory.getStackInSlot(FIN_SLOTS[1]),
                this.inventory.getStackInSlot(FIN_SLOTS[2]),
                this.inventory.getStackInSlot(FIN_SLOTS[3]),
                this.inventory.getStackInSlot(HULL_SLOTS[0]),
                this.inventory.getStackInSlot(HULL_SLOTS[1]),
                this.inventory.getStackInSlot(HULL_SLOTS[2]),
                this.inventory.getStackInSlot(HULL_SLOTS[3]),
                this.inventory.getStackInSlot(HULL_SLOTS[4]),
                this.inventory.getStackInSlot(HULL_SLOTS[5]),
                this.inventory.getStackInSlot(HULL_SLOTS[6]),
                this.inventory.getStackInSlot(HULL_SLOTS[7]),
                this.inventory.getStackInSlot(BOOSTER_SLOTS[0]),
                this.inventory.getStackInSlot(BOOSTER_SLOTS[1]),
                this.inventory.getStackInSlot(NOSE_CONE_SLOT)
        );

        return this.level.getRecipeManager().getRecipeFor(HpCRecipes.MAGNETIC_ASSEMBLY_TYPE.get(), currentInput, this.level);
    }

    @Override
    protected ContainerData initContainerData() {
        return new SimpleContainerData(18);
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
        return Component.translatable("block.minecraft.magnetic_assembly_platform");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new MagneticAssemblyPlatformMenu(i, inventory, this, this.data);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
    }
}
