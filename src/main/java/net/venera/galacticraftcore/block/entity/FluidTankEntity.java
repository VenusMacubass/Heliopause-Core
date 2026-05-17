package net.venera.galacticraftcore.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
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
        
        if (container.getItem() == Items.BUCKET) { //Empty -> try to drain 1000 mB from the tank and return a filled bucket
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
            
            fluidAmount -= BUCKET_CAPACITY; //remove fluid, sync, and return the filled bucket
            return returnHelper(filled);
        }
        
        if (container.is(Items.WATER_BUCKET) || container.is(ModFluids.CRUDE_OIL.getBucket()) || container.is(ModFluids.REFINED_FUEL.getBucket())) {
            
            Fluid bucketFluid = Fluids.EMPTY;
            if (container.is(Items.WATER_BUCKET)) bucketFluid = Fluids.WATER;
            else if (container.is(ModFluids.CRUDE_OIL.getBucket())) bucketFluid = ModFluids.CRUDE_OIL.getSource();
            else if (container.is(ModFluids.REFINED_FUEL.getBucket())) bucketFluid = ModFluids.REFINED_FUEL.getSource();
            
            if (getTankSpace() < BUCKET_CAPACITY) return container;
            
            if (currentFluid.isSame(Fluids.EMPTY)) {
                currentFluid = bucketFluid;
            } else if (!currentFluid.isSame(bucketFluid)) {
                return container; // incompatible fluid
            }
            
            fluidAmount += BUCKET_CAPACITY; 
            return returnHelper(new ItemStack(Items.BUCKET));
        }
        
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
        
        if (!canisterIsEmpty && tankHasSpace) {
            int transfer = Math.min(data.amount(), getTankSpace());
            
            if (currentFluid.isSame(Fluids.EMPTY) ||
                    currentFluid.isSame(BuiltInRegistries.FLUID.get(data.fluidId()))) {

           
                if (currentFluid.isSame(Fluids.EMPTY)) {
                    if (data.isCrudeOil()) currentFluid = ModFluids.CRUDE_OIL.getSource();
                    else if (data.isRefinedFuel()) currentFluid = ModFluids.REFINED_FUEL.getSource();
                }

                canisterItem.drain(container, transfer);
                fluidAmount += transfer;
                return returnHelper(container);
            }
        }
       
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

    public Fluid getCurrentFluid() {
        return currentFluid;
    }

    // Tells the server to send this NBT data to the client
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    // Packages the data into a network packet
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    public int getTankSpace(){
        return FLUID_TANK_CAPACITY - fluidAmount;
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
