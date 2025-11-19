package net.venera.galacticraftcore.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.venera.galacticraftcore.data.component.CanisterData;
import net.venera.galacticraftcore.fluid.ModFluids;
import net.venera.galacticraftcore.item.custom.CanisterItem;

import javax.annotation.Nullable;

public class FluidTankEntity extends BlockEntity {
    private int fluidAmount = 0;
    private Fluid currentFluid = Fluids.EMPTY;
    private final int BUCKET_CAPACITY = 1000;
    public static final int FLUID_TANK_CAPACITY = 8000;
    public final ContainerData data;

    public FluidTankEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FLUID_TANK_ENTITY.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> fluidAmount;
                    case 1 -> FLUID_TANK_CAPACITY;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> fluidAmount = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

        };
    }

    public ItemStack handleInteractions(ItemStack container, Player player){
        if(container.getItem() instanceof BucketItem){return handleBucket(container, player);}
        else if(container.getItem() instanceof CanisterItem){return handleCanister(container, player);}
        return container;
    }

    public ItemStack handleBucket(ItemStack container, Player player){
        if(container.getItem() == Items.BUCKET){
            return drainTank(container, player);
        }else if(container.is(ModFluids.CRUDE_OIL.getBucket()) || container.is(ModFluids.REFINED_FUEL.getBucket())){
            return fillTank(container, player);
        }
        return container;
    }

    public ItemStack handleCanister(ItemStack container, Player player){
        if(container.getItem() instanceof CanisterItem canisterItem){
            CanisterData data = canisterItem.getCanisterData(container);
            if(data.getSpace() > data.amount()){
                return drainTank(container, player);
            }else{
                return fillTank(container, player);
            }
        }
        return container;
    }

    public ItemStack fillTank(ItemStack container, Player player){
        if(container.getItem() instanceof BucketItem){
            if(BUCKET_CAPACITY > getTankSpace()){return container;}
            if(container.is(ModFluids.CRUDE_OIL.getBucket()) && (currentFluid.isSame(ModFluids.CRUDE_OIL.getSource()) || currentFluid == Fluids.EMPTY)){
                currentFluid = ModFluids.CRUDE_OIL.getSource();
                fluidAmount += BUCKET_CAPACITY;
                container.shrink(1);
                if(!player.addItem(new ItemStack(Items.BUCKET))){
                    player.drop(new ItemStack(Items.BUCKET), false);
                }
                return returnHelper(container);
            }
            else if(container.is(ModFluids.REFINED_FUEL.getBucket()) && (currentFluid.isSame(ModFluids.REFINED_FUEL.getSource()) || currentFluid == Fluids.EMPTY)){
                currentFluid = ModFluids.REFINED_FUEL.getSource();
                fluidAmount += BUCKET_CAPACITY;
                container.shrink(1);
                if(!player.addItem(new ItemStack(Items.BUCKET))){
                    player.drop(new ItemStack(Items.BUCKET), false);
                }
                return returnHelper(container);
            }
        }
        else if(container.getItem() instanceof CanisterItem canisterItem){
            CanisterData data = canisterItem.getCanisterData(container);
            if(data.isEmpty()){return container;}
            if(data.isCrudeOil() && (currentFluid.isSame(ModFluids.CRUDE_OIL.getSource()) || currentFluid == Fluids.EMPTY)){
                currentFluid = ModFluids.CRUDE_OIL.getSource();
                int transferAmount = Math.min(data.amount(), getTankSpace());
                canisterItem.drain(container, transferAmount);
                fluidAmount += transferAmount;
                return returnHelper(container);
            }else if(data.isRefinedFuel() && (currentFluid.isSame(ModFluids.REFINED_FUEL.getSource()) || currentFluid == Fluids.EMPTY)){
                currentFluid = ModFluids.REFINED_FUEL.getSource();
                int transferAmount = Math.min(data.amount(), getTankSpace());
                canisterItem.drain(container, transferAmount);
                fluidAmount += transferAmount;
                return returnHelper(container);
            }
        }
        return container;
    }

    public ItemStack drainTank(ItemStack container, Player player){
        if(container.getItem() instanceof BucketItem){
            if(fluidAmount < BUCKET_CAPACITY){return container;}
            if(currentFluid.isSame(ModFluids.CRUDE_OIL.getSource())){
                fluidAmount -= BUCKET_CAPACITY;
                container.shrink(1);
                if(!player.addItem(new ItemStack(ModFluids.CRUDE_OIL.getBucket()))){
                    player.drop(new ItemStack(ModFluids.CRUDE_OIL.getBucket()), false);
                }
                returnHelper(container);
            }else if(currentFluid.isSame(ModFluids.REFINED_FUEL.getSource())){
                fluidAmount -= BUCKET_CAPACITY;
                container.shrink(1);
                if(!player.addItem(new ItemStack(ModFluids.REFINED_FUEL.getBucket()))){
                    player.drop(new ItemStack(ModFluids.REFINED_FUEL.getBucket()), false);
                }
                returnHelper(container);
            }
        }else if(container.getItem() instanceof CanisterItem canisterItem){
            CanisterData data = canisterItem.getCanisterData(container);
            if(data.getSpace() <= 0 || fluidAmount <= 0){return container;}
            if(data.isCrudeOil() && currentFluid.isSame(ModFluids.CRUDE_OIL.getSource())){
                int transferAmount = Math.min(fluidAmount, data.getSpace());
                fluidAmount -= transferAmount;
                canisterItem.fill(container, CanisterData.CRUDE_OIL, transferAmount);
                return returnHelper(container);
            }else if(data.isRefinedFuel() && currentFluid.isSame(ModFluids.REFINED_FUEL.getSource())){
                int transferAmount = Math.min(fluidAmount, data.getSpace());
                fluidAmount -= transferAmount;
                canisterItem.fill(container, CanisterData.REFINED_FUEL, transferAmount);
                return returnHelper(container);
            }else if(data.isEmpty()){
                int transferAmount = Math.min(fluidAmount, data.getSpace());
                if(currentFluid.isSame(ModFluids.CRUDE_OIL.getSource())){
                    fluidAmount -= transferAmount;
                    canisterItem.fill(container, CanisterData.CRUDE_OIL, transferAmount);
                }
                else if(currentFluid.isSame(ModFluids.REFINED_FUEL.getSource())){
                    fluidAmount -= transferAmount;
                    canisterItem.fill(container, CanisterData.REFINED_FUEL, transferAmount);
                }
                return returnHelper(container);
            }
        }
        return container;
    }

    public int getTankSpace(){
        return FLUID_TANK_CAPACITY - fluidAmount;
    }

    public Fluid getCurrentFluid() {
        return currentFluid;
    }

    public int getFluidAmount() {
        return fluidAmount;
    }

    private ItemStack returnHelper(ItemStack itemStack){
        if (fluidAmount <= 0) {
            fluidAmount = 0;
            currentFluid = Fluids.EMPTY;
        }
        setChanged();
        if(level != null){level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);}
        return itemStack;
    }

    public int getFluidScaled(int pixels){
        return (int) ((float) fluidAmount / FLUID_TANK_CAPACITY * pixels);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("FluidAmount", fluidAmount);
        if (currentFluid != null && currentFluid != Fluids.EMPTY) {
            tag.putString("FluidType", BuiltInRegistries.FLUID.getKey(currentFluid).toString());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        fluidAmount = tag.getInt("FluidAmount");
        if (tag.contains("FluidType")) {
            currentFluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(tag.getString("FluidType")));
        } else {
            currentFluid = Fluids.EMPTY;
        }
    }
}
