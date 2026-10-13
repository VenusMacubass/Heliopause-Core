package net.venera.heliocore.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

public class PizzaEntity extends BlockEntity {
    private int toppingsMask = 0; //1:Chicken, 2: Meat, 4: Mushroom, 8: Fish, 16: Vegetable

    public PizzaEntity(BlockPos pos, BlockState state) {
        super(HpCBlockEntities.PIZZA_ENTITY.get(), pos, state);
    }
    
    public void setToppingsMask(int mask) {
        this.toppingsMask = mask;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public int getToppingsMask() {
        return toppingsMask;
    }

    public static final ModelProperty<Integer> TOPPINGS_PROPERTY = new ModelProperty<>();
    
    @Override
    public ModelData getModelData() {
        return ModelData.builder().with(TOPPINGS_PROPERTY, this.toppingsMask).build();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("Toppings", this.toppingsMask);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.toppingsMask = tag.getInt("Toppings");
    }
}