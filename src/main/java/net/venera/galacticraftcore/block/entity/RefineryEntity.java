package net.venera.galacticraftcore.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.data.component.CanisterData;
import net.venera.galacticraftcore.data.component.ModDataComponents;
import net.venera.galacticraftcore.fluid.ModFluids;
import net.venera.galacticraftcore.item.ModItems;
import net.venera.galacticraftcore.item.custom.CanisterItem;
import net.venera.galacticraftcore.screen.custom.CoalCompressorMenu;
import net.venera.galacticraftcore.screen.custom.RefineryMenu;
import org.jetbrains.annotations.Nullable;

public class RefineryEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()){
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final int INPUT_SLOT = 0;
    private final int OUTPUT_SLOT = 1;
    private final int BATTERY_SLOT = 2;
    private final int BUCKET_CAPACITY = 1000;
    public final ContainerData data;
    private static final int CONVERSION_RATE = 1;
    private int oilAmount = 0;
    private int fuelAmount = 0;
    private int maxCapacity = 6000;
    private int ElectricCapacity = 1000; //TODO Implement this when the time comes.
    public boolean isActive = true; //TODO: Change this when the time comes.

    public RefineryEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.REFINERY_ENTITY.get(), pos, blockState);
        data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> oilAmount;
                    case 1 -> fuelAmount;
                    case 2 -> maxCapacity;
                    case 3 -> isActive ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> oilAmount = value;
                    case 1 -> fuelAmount = value;
                    case 2 -> maxCapacity = value;
                    case 3 -> isActive = value == 1;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

        };
    }

    public void tick(Level level, BlockPos pos, BlockState state, RefineryEntity entity){
        if (level.isClientSide()) return;
        boolean dirty = false;

        if (processInputs()) {
            dirty = true;
        }

        if (processOutputs()) {
            dirty = true;
        }

        if (canRefine()) {
            startRefining();
            dirty = true;
        } else {
            if (isActive) {
                isActive = false;
                dirty = true;
            }
        }
        if (dirty) setChanged();
    }

    private boolean processInputs() {
        ItemStack inputStack = inventory.getStackInSlot(INPUT_SLOT);

        if(inputStack.getItem() == ModFluids.CRUDE_OIL.getBucket() && oilAmount <= maxCapacity - BUCKET_CAPACITY) {
            oilAmount += BUCKET_CAPACITY;
            inventory.setStackInSlot(INPUT_SLOT, new ItemStack(Items.BUCKET));
            return true;
        } else if(inputStack.getItem() instanceof CanisterItem canister){
            CanisterData data = canister.getCanisterData(inputStack);

            // ONLY accept canisters that have CRUDE OIL
            if(data.isCrudeOil()) {
                int spaceAvailable = maxCapacity - oilAmount;
                int amountToDrain = Math.min(data.amount(), spaceAvailable);

                if(amountToDrain > 0) {
                    int actuallyDrained = canister.drain(inputStack, amountToDrain);
                    GalacticraftCore.LOGGER.info("Draining the canister by " + actuallyDrained + " of " + amountToDrain);
                    oilAmount += actuallyDrained;
                    return actuallyDrained > 0;
                }
            }
        }
        return false;
    }

    private boolean processOutputs() {
        ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);

        if(outputStack.getItem() == Items.BUCKET && fuelAmount >= BUCKET_CAPACITY) {
            fuelAmount -= BUCKET_CAPACITY;
            inventory.setStackInSlot(OUTPUT_SLOT, new ItemStack(ModFluids.REFINED_FUEL.getBucket()));
            return true;
        } else if(outputStack.getItem() instanceof CanisterItem canister){
            CanisterData data = canister.getCanisterData(outputStack);

            // Accept empty canisters OR canisters with refined fuel
            if(data.isEmpty() || data.isRefinedFuel()) {
                int amountToFill = Math.min(fuelAmount, data.getSpace());
                GalacticraftCore.LOGGER.info("Filling the canister by " + amountToFill);
                if(amountToFill > 0) {
                    int actuallyFilled = canister.fill(outputStack, CanisterData.REFINED_FUEL, amountToFill);
                    fuelAmount -= actuallyFilled;
                    return actuallyFilled > 0;
                }
            }
        }
        return false;
    }

    private boolean canRefine(){
        return oilAmount > 0 && fuelAmount < maxCapacity && inventory.getStackInSlot(BATTERY_SLOT).getItem() == Items.NETHER_STAR;
    }

    private void startRefining(){
        isActive = true;

        int conversionAmount = Math.min(CONVERSION_RATE, oilAmount);
        conversionAmount = Math.min(conversionAmount, maxCapacity - fuelAmount);

        oilAmount -= conversionAmount;
        fuelAmount += conversionAmount;
    }

    public int getOilAmount() {
        return oilAmount;
    }

    public int getFuelAmount() {
        return fuelAmount;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.galacticraftcore.refinery");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new RefineryMenu(i, inventory, this);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public int getOilScaled(int pixels) {
        if (oilAmount == 0) return 0;
        int scaled = (int) ((float) oilAmount / maxCapacity * pixels);
        return Math.min(scaled, pixels);
    }

    public int getFuelScaled(int pixels) {
        if (fuelAmount == 0) return 0;
        int scaled = (int) ((float) fuelAmount / maxCapacity * pixels);
        return Math.min(scaled, pixels);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("OilAmount", oilAmount);
        tag.putInt("FuelAmount", fuelAmount);
        tag.putBoolean("IsActive", isActive);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        oilAmount = tag.getInt("OilAmount");
        fuelAmount = tag.getInt("FuelAmount");
        isActive = tag.getBoolean("IsActive");
    }
}
