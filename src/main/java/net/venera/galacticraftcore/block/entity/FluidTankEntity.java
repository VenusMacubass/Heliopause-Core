package net.venera.galacticraftcore.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import net.venera.galacticraftcore.data.component.CanisterData;
import net.venera.galacticraftcore.data.component.ModDataComponents;
import net.venera.galacticraftcore.fluid.ModFluids;
import net.venera.galacticraftcore.item.ModItems;
import net.venera.galacticraftcore.item.custom.CanisterItem;

public class FluidTankEntity extends BlockEntity {
    private int currentOil = 0;
    private int currentFuel = 0;
    private final int BUCKET_CAPACITY = 1000;
    public static final int FLUID_TANK_CAPACITY = 8000;
    public final ContainerData data;

    public FluidTankEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FLUID_TANK_ENTITY.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> currentOil;
                    case 1 -> currentFuel;
                    case 2 -> FLUID_TANK_CAPACITY;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> currentOil = value;
                    case 1 -> currentFuel = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

        };
    }

    public ItemStack fillTank(ItemStack container){
        Item containerItem = container.getItem();
        if(containerItem == ModFluids.CRUDE_OIL.getBucket() && getOilSpace() >= BUCKET_CAPACITY && currentFuel <= 0){
            currentOil += BUCKET_CAPACITY;
            return Items.BUCKET.getDefaultInstance();
        }else if(containerItem == ModFluids.REFINED_FUEL.getBucket() && getFuelSpace() >= BUCKET_CAPACITY && currentOil <= 0){
            currentFuel += BUCKET_CAPACITY;
            return Items.BUCKET.getDefaultInstance();
        }else if(containerItem instanceof CanisterItem canisterItem){
            CanisterData data = canisterItem.getCanisterData(container);
            if(data.isCrudeOil() && getOilSpace() > 0 && data.isCrudeOil()){
                int transferAmount = Math.min(data.amount(),  getOilSpace());
                currentOil += transferAmount;
                canisterItem.drain(container, transferAmount);

            }else if(data.isRefinedFuel() && getFuelSpace() > 0 && data.isRefinedFuel()){
                int transferAmount = Math.min(data.amount(),  getFuelSpace());
                currentFuel += transferAmount;
                canisterItem.drain(container, transferAmount);
            }
        }
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        return container;
    }

    public ItemStack drainTank(ItemStack container){
        Item containerItem = container.getItem();
        if(containerItem == Items.BUCKET){
            if(currentOil >= BUCKET_CAPACITY){
                currentOil -= BUCKET_CAPACITY;
                return new ItemStack(ModFluids.CRUDE_OIL.getBucket());
            }else if(currentFuel >= BUCKET_CAPACITY){
                currentFuel -= BUCKET_CAPACITY;
                return new ItemStack(ModFluids.REFINED_FUEL.getBucket());
            }
        }else if(containerItem instanceof CanisterItem canisterItem){
            CanisterData data = canisterItem.getCanisterData(container);
            if(data.isCrudeOil()){
                int transferAmount = Math.min(data.getSpace(), currentOil);
                currentOil -= transferAmount;
                canisterItem.fill(container, CanisterData.CRUDE_OIL,  transferAmount);
            }else if(data.isRefinedFuel()){
                int transferAmount = Math.min(data.getSpace(), currentFuel);
                currentFuel -= transferAmount;
                canisterItem.fill(container, CanisterData.REFINED_FUEL,  transferAmount);
            }else if(data.isEmpty()){
                if(currentOil > 0){
                    canisterItem.fill(container, CanisterData.CRUDE_OIL, Math.min(currentOil, data.getSpace()));
                }else if(currentFuel > 0){
                    canisterItem.fill(container, CanisterData.REFINED_FUEL, Math.min(currentFuel, data.getSpace()));
                }
            }
        }
        setChanged();
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        return new ItemStack(container.getItem());
    }

    public int getOilSpace(){
        return FLUID_TANK_CAPACITY - currentOil;
    }

    public int getFuelSpace(){
        return FLUID_TANK_CAPACITY - currentFuel;
    }

    public int getOilScaled(int pixels){
        return (int) ((float) currentOil / FLUID_TANK_CAPACITY * pixels);
    }

    public int getFuelScaled(int pixels){
        return (int) ((float) currentFuel / FLUID_TANK_CAPACITY * pixels);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("CurrentOil", currentOil);
        tag.putInt("CurrentFuel", currentFuel);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        currentOil = tag.getInt("CurrentOil");
        currentFuel = tag.getInt("CurrentFuel");
    }
}
