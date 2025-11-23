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

    public ItemStack handleBucket(ItemStack container, Player player){
        // 1) EMPTY BUCKET -> try to drain 1000 mB from the tank and return a filled bucket
        if (container.getItem() == Items.BUCKET) {
            if (fluidAmount < BUCKET_CAPACITY) return container;                // not enough fluid
            if (currentFluid.isSame(Fluids.EMPTY)) return container;            // no known fluid type

            ItemStack filled;
            if (currentFluid.isSame(Fluids.WATER)) {
                filled = new ItemStack(Items.WATER_BUCKET);
            } else if (currentFluid.isSame(ModFluids.CRUDE_OIL.getSource())) {
                filled = new ItemStack(ModFluids.CRUDE_OIL.getBucket());
            } else if (currentFluid.isSame(ModFluids.REFINED_FUEL.getSource())) {
                filled = new ItemStack(ModFluids.REFINED_FUEL.getBucket());
            } else {
                return container; // unsupported fluid for buckets
            }

            // remove fluid, sync, and return the filled bucket
            fluidAmount -= BUCKET_CAPACITY;
            return returnHelper(filled);
        }

        // 2) FULL BUCKET -> try to add 1000 mB to the tank and return an empty bucket
        //    Use ItemStack#is(...) checks for mod buckets and vanilla checks for vanilla buckets.
        if (container.is(Items.WATER_BUCKET) || container.is(ModFluids.CRUDE_OIL.getBucket()) || container.is(ModFluids.REFINED_FUEL.getBucket())) {

            // determine fluid type of this bucket
            Fluid bucketFluid = Fluids.EMPTY;
            if (container.is(Items.WATER_BUCKET)) bucketFluid = Fluids.WATER;
            else if (container.is(ModFluids.CRUDE_OIL.getBucket())) bucketFluid = ModFluids.CRUDE_OIL.getSource();
            else if (container.is(ModFluids.REFINED_FUEL.getBucket())) bucketFluid = ModFluids.REFINED_FUEL.getSource();

            // need space
            if (getTankSpace() < BUCKET_CAPACITY) return container;

            // if tank empty, adopt bucket fluid; otherwise must match
            if (currentFluid.isSame(Fluids.EMPTY)) {
                currentFluid = bucketFluid;
            } else if (!currentFluid.isSame(bucketFluid)) {
                return container; // incompatible fluid
            }

            // add fluid, sync, and return an empty bucket
            fluidAmount += BUCKET_CAPACITY;
            return returnHelper(new ItemStack(Items.BUCKET));
        }

        // 3) Not a bucket the tank understands
        return container;
    }

    public ItemStack handleCanister(ItemStack container, Player player){
        if (!(container.getItem() instanceof CanisterItem canisterItem))
            return container;
        CanisterData data = canisterItem.getCanisterData(container);
        if (data == null) return container; // NEVER allow null to continue

        boolean canisterIsEmpty = data.isEmpty();
        boolean tankHasFluid = fluidAmount > 0;
        boolean tankHasSpace = getTankSpace() > 0;

        // -----------------------------
        // 1. CANISTER → TANK (drain canister)
        // -----------------------------
        if (!canisterIsEmpty && tankHasSpace) {
            int transfer = Math.min(data.amount(), getTankSpace());

            // Match fluid types or tank empty
            if (currentFluid.isSame(Fluids.EMPTY) ||
                    currentFluid.isSame(BuiltInRegistries.FLUID.get(data.fluidId()))) {

                // Set tank fluid type if empty
                if (currentFluid.isSame(Fluids.EMPTY)) {
                    if (data.isCrudeOil()) currentFluid = ModFluids.CRUDE_OIL.getSource();
                    else if (data.isRefinedFuel()) currentFluid = ModFluids.REFINED_FUEL.getSource();
                }

                canisterItem.drain(container, transfer);
                fluidAmount += transfer;
                return returnHelper(container);
            }
        }

        // -----------------------------
        // 2. TANK → CANISTER (fill canister)
        // -----------------------------
        if (tankHasFluid && data.getSpace() > 0) {

            int transfer = Math.min(fluidAmount, data.getSpace());

            if (currentFluid.isSame(ModFluids.CRUDE_OIL.getSource()) && (data.getSpace() > 0 && (data.isCrudeOil() || data.isEmpty()))) {
                fluidAmount -= transfer;
                canisterItem.fill(container, CanisterData.CRUDE_OIL, transfer);
                return returnHelper(container);
            }

            if (currentFluid.isSame(ModFluids.REFINED_FUEL.getSource()) && (data.getSpace() > 0 && (data.isCrudeOil() || data.isEmpty()))) {
                fluidAmount -= transfer;
                canisterItem.fill(container, CanisterData.REFINED_FUEL, transfer);
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
