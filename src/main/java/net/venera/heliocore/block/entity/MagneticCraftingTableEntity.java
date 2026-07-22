package net.venera.heliocore.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class MagneticCraftingTableEntity extends BlockEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public MagneticCraftingTableEntity(BlockPos pos, BlockState state) {
        super(HpCBlockEntities.MAGNETIC_CRAFTING_TABLE_ENTITY.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Inventory", inventory.serializeNBT(registries));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
    }
}
