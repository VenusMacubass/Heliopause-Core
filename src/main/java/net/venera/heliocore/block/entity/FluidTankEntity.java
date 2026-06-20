package net.venera.heliocore.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.data.component.GasTankData;
import net.venera.heliocore.fluid.HpCFluids;
import net.venera.heliocore.fluid.IFluidMachine;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import net.venera.heliocore.item.hpc_custom.GasTankItem;


public class FluidTankEntity extends BlockEntity implements IFluidMachine {
    private final int BUCKET_CAPACITY = 1000;
    public static final int FLUID_TANK_CAPACITY = 8000;
    public final FluidTank fluidTank = new FluidTank(FLUID_TANK_CAPACITY){
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        }
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return this.isEmpty() || stack.getFluid().isSame(this.getFluid().getFluid());
        }
    };
    
    public final ContainerData data;

    public FluidTankEntity(BlockPos pos, BlockState blockState) {
        super(HpCBlockEntities.FLUID_TANK_ENTITY.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> fluidTank.getFluidAmount();
                    case 1 -> FLUID_TANK_CAPACITY;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> fluidTank.setFluid(new FluidStack(fluidTank.getFluid().getFluid(), value));
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

        };
    }

    public ItemStack handleBucket(ItemStack container, Player player){
        if (container.getItem() == Items.BUCKET) { 
            if (fluidTank.getFluidAmount() < BUCKET_CAPACITY) return container;                
            if (fluidTank.getFluid().getFluid().isSame(Fluids.EMPTY)) return container;           

            ItemStack filled;
            if (fluidTank.getFluid().getFluid().isSame(Fluids.WATER)) {
                filled = new ItemStack(Items.WATER_BUCKET);
            } else if (fluidTank.getFluid().getFluid().isSame(HpCFluids.CRUDE_OIL.getSource())) {
                filled = new ItemStack(HpCFluids.CRUDE_OIL.getBucket());
            } else if (fluidTank.getFluid().getFluid().isSame(HpCFluids.REFINED_FUEL.getSource())) {
                filled = new ItemStack(HpCFluids.REFINED_FUEL.getBucket());
            } else {
                return container; 
            }
            
            fluidTank.drain(BUCKET_CAPACITY, IFluidHandler.FluidAction.EXECUTE);
            return filled;
        }

        if (container.is(Items.WATER_BUCKET) || container.is(HpCFluids.CRUDE_OIL.getBucket()) || container.is(HpCFluids.REFINED_FUEL.getBucket())) {
            Fluid bucketFluid = Fluids.EMPTY;
            if (container.is(Items.WATER_BUCKET)) bucketFluid = Fluids.WATER;
            else if (container.is(HpCFluids.CRUDE_OIL.getBucket())) bucketFluid = HpCFluids.CRUDE_OIL.getSource();
            else if (container.is(HpCFluids.REFINED_FUEL.getBucket())) bucketFluid = HpCFluids.REFINED_FUEL.getSource();
            
            FluidStack incoming = new FluidStack(bucketFluid, BUCKET_CAPACITY);
            int accepted = fluidTank.fill(incoming, IFluidHandler.FluidAction.SIMULATE);
            
            if (accepted == BUCKET_CAPACITY) {
                fluidTank.fill(incoming, IFluidHandler.FluidAction.EXECUTE);
                return new ItemStack(Items.BUCKET);
            }
            return container; 
        }
        
        return container;
    }

    public ItemStack handleCanister(ItemStack container, Player player){
        if (!(container.getItem() instanceof CanisterItem canisterItem))
            return container;
        CanisterData data = canisterItem.getCanisterData(container);
        if (data == null) return container;

        boolean canisterIsEmpty = data.isEmpty();
        boolean tankHasFluid = fluidTank.getFluidAmount() > 0;
        boolean tankHasSpace = fluidTank.getSpace() > 0;

        if (!canisterIsEmpty && tankHasSpace) {
            Fluid incomingFluid = BuiltInRegistries.FLUID.get(data.fluidId());
            FluidStack incomingStack = new FluidStack(incomingFluid, data.amount());
                
            int transfer = fluidTank.fill(incomingStack, IFluidHandler.FluidAction.SIMULATE);

            if (transfer > 0) {
                canisterItem.drain(container, transfer);
                fluidTank.fill(new FluidStack(incomingFluid, transfer), IFluidHandler.FluidAction.EXECUTE);
                return container;
            }
        }
       
        if (tankHasFluid && data.getSpace() > 0) {
            FluidStack transfer = fluidTank.drain(new FluidStack(fluidTank.getFluid().getFluid(), data.getSpace()), IFluidHandler.FluidAction.SIMULATE);

            if (fluidTank.getFluid().getFluid().isSame(HpCFluids.CRUDE_OIL.getSource()) && (data.getSpace() > 0 && (data.isCrudeOil() || data.isEmpty()))) {
                fluidTank.drain(transfer, IFluidHandler.FluidAction.EXECUTE);
                canisterItem.fill(container, CanisterData.CRUDE_OIL, transfer.getAmount());
                return container;
            }

            if (fluidTank.getFluid().getFluid().isSame(HpCFluids.REFINED_FUEL.getSource()) && (data.getSpace() > 0 && (data.isCrudeOil() || data.isEmpty()))) {
                fluidTank.drain(transfer, IFluidHandler.FluidAction.EXECUTE);
                canisterItem.fill(container, CanisterData.REFINED_FUEL, transfer.getAmount());
                return container;
            }
        }
        return container;
    }

    public ItemStack handleGasTank(ItemStack container, Player player){
        if (!(container.getItem() instanceof GasTankItem gasTankItem))
            return container;
        GasTankData data = gasTankItem.getGasTankData(container);
        if (data == null) return container;

        boolean gasTankIsEmpty = data.isEmpty();
        boolean tankHasFluid = fluidTank.getFluidAmount()> 0;
        boolean tankHasSpace = getTankSpace() > 0;

        if (!gasTankIsEmpty && tankHasSpace) {
            Fluid incomingFluid = BuiltInRegistries.FLUID.get(data.fluidId());
            FluidStack incomingStack = new FluidStack(incomingFluid, data.amount());

            int transfer = fluidTank.fill(incomingStack, IFluidHandler.FluidAction.SIMULATE);

            if (transfer > 0) {
                gasTankItem.drain(container, transfer);
                fluidTank.fill(new FluidStack(incomingFluid, transfer), IFluidHandler.FluidAction.EXECUTE);
                return container;
            }
        }

        if (tankHasFluid && data.getSpace() > 0) {
            FluidStack transfer = fluidTank.drain(new FluidStack(fluidTank.getFluid().getFluid(), fluidTank.getFluidAmount()), IFluidHandler.FluidAction.SIMULATE);

            if (fluidTank.getFluid().getFluid().isSame(HpCFluids.OXYGEN.get()) && (data.getSpace() > 0 && (data.isOxygen() || data.isEmpty()))) {
                fluidTank.drain(new FluidStack(fluidTank.getFluid().getFluid(), fluidTank.getFluidAmount()), IFluidHandler.FluidAction.EXECUTE);
                gasTankItem.fill(container, GasTankData.OXYGEN_GAS, transfer.getAmount());
                return container;
            }
        }

        return container;
    }
    
    @Override
    public IFluidMachine.PortType getFluidPortType(Direction face) {
        return IFluidMachine.PortType.CONTAINER; 
    }

    @Override
    public int insertFluid(String incomingFluidType, int amount, boolean simulate) {
        Fluid incomingFluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(incomingFluidType));
        if (incomingFluid == Fluids.EMPTY) return 0;
        IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
        return fluidTank.fill(new FluidStack(incomingFluid, amount), action);
    }

    @Override
    public int extractFluid(String fluidType, int amount, boolean simulate) {
        Fluid requestedFluid = BuiltInRegistries.FLUID.get(ResourceLocation.parse(fluidType));
        if (!fluidTank.getFluid().getFluid().isSame(requestedFluid)) return 0;
        IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
        return fluidTank.drain(amount, action).getAmount();
    }

    public Fluid getCurrentFluid() {
        return fluidTank.getFluid().getFluid();
    }
    
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }
    
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    public int getTankSpace(){
        return fluidTank.getSpace();
    }

    public int getFluidAmount() {
        return fluidTank.getFluidAmount();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("FluidTank", fluidTank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("FluidTank")) fluidTank.readFromNBT(registries, tag.getCompound("FluidTank"));
    }
}
