package net.venera.heliocore.entity.rideable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.heliocore.screen.custom.LanderMenu;
import net.venera.heliocore.screen.custom.RocketMenu;

import javax.annotation.Nullable;

public class Tier1RocketLanderEntity extends Entity implements PlayerRideableJumping {
    public final ItemStackHandler inventory = new ItemStackHandler(28);
    public Tier1RocketLanderEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    private static final EntityDataAccessor<Boolean> IS_LANDING = SynchedEntityData.defineId(Tier1RocketLanderEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FUEL_AMOUNT = SynchedEntityData.defineId(Tier1RocketLanderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ENERGY_AMOUNT = SynchedEntityData.defineId(Tier1RocketLanderEntity.class, EntityDataSerializers.INT);
    public final int MAX_FUEL = 1000;
    public final int MAX_ENERGY = 5000;
    public final int ENERGY_USAGE = 2;
    public final int FUEL_USAGE = 1;
    private double previousYVelocity;
    public boolean isThrusting = false;

    @Override
    public void tick() {
        previousYVelocity = this.getDeltaMovement().y;
        super.tick();
        if (!this.level().isClientSide() && this.isThrusting) {
            this.applyThrust();
        }
        
        Vec3 currentMove = this.getDeltaMovement();

        double newY = currentMove.y - 0.015D;
        if (newY < -3.0D) {
            newY = -3.0D;
        }
        this.setDeltaMovement(0.0D, newY, 0.0D);
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.onGround() && this.previousYVelocity < -0.7D) { 
            explode();
        } 
        else if (this.onGround() && this.getFirstPassenger() != null) {
            this.getFirstPassenger().fallDistance = 0.0F;
        }
        
        if(this.getDeltaMovement().y == 0.0F || this.onGround()){
            entityData.set(IS_LANDING, false);
        }
        else{
            entityData.set(IS_LANDING, true);
        }
        
        if(entityData.get(IS_LANDING)) {
            this.setEnergyAmount(getEnergyAmount() - ENERGY_USAGE);
        }
         
    }

    public void applyThrust() {
        if (getFuelAmount() > 0 &&  getEnergyAmount() > 0) {
            Vec3 currentMove = this.getDeltaMovement();
            
            this.setDeltaMovement(currentMove.add(0.0D, 0.05D, 0.0D));
            this.setFuelAmount(getFuelAmount() - FUEL_USAGE);
        }
    }

    private void explode() {
        this.clearInventory();
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 5.0F, Level.ExplosionInteraction.MOB);
        this.discard();
    }
    
    private void clearInventory() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            if (!this.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (id, playerInv, p) -> new LanderMenu(id, playerInv, this),
                        Component.literal("Lander Cargo Inventory")
                ), buf -> buf.writeInt(this.getId()));

            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        if (!this.level().isClientSide() && !this.entityData.get(IS_LANDING)) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }
        return super.interact(player, hand);
    }
    
    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void onPlayerJump(int i) {
        
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public void handleStartJump(int i) {

    }

    @Override
    public void handleStopJump() {

    }
    
    @Override
    protected void positionRider(Entity passenger, MoveFunction callback) {
        if (this.hasPassenger(passenger)) {
            double yOffset = 0.5;
            callback.accept(passenger, this.getX(), this.getY() + yOffset, this.getZ());
        }
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        Entity passenger = this.getFirstPassenger();
        if (passenger instanceof LivingEntity livingPassenger) {
            return livingPassenger;
        }
        return null;
    }
    
    public int getEnergyAmount(){
        return entityData.get(ENERGY_AMOUNT);
    }
    public int getFuelAmount(){
        return entityData.get(FUEL_AMOUNT);
    }
    public void setEnergyAmount(int value){
        if(value > MAX_ENERGY){value = MAX_ENERGY;}
        entityData.set(ENERGY_AMOUNT, value); 
    }
    public void setFuelAmount(int value){
        if(value > MAX_FUEL){value = MAX_FUEL;}
        entityData.set(FUEL_AMOUNT, value);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(IS_LANDING, false);
        builder.define(FUEL_AMOUNT, 0);
        builder.define(ENERGY_AMOUNT, 0);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.put("LanderInventory", this.inventory.serializeNBT(this.registryAccess()));
        compoundTag.putBoolean("IsLanding", this.entityData.get(IS_LANDING));
        compoundTag.putInt("EnergyAmount", this.getEnergyAmount());
        compoundTag.putInt("FuelAmount", this.getFuelAmount());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("LanderInventory")) {
            this.inventory.deserializeNBT(this.registryAccess(), compoundTag.getCompound("LanderInventory"));
        }
        if (compoundTag.contains("IsLanding")) {
            this.entityData.set(IS_LANDING, compoundTag.getBoolean("IsLanding"));
        }
        if (compoundTag.contains("EnergyAmount")) {
            this.setEnergyAmount(compoundTag.getInt("EnergyAmount"));
        }
        if (compoundTag.contains("FuelAmount")) {
            this.setFuelAmount(compoundTag.getInt("FuelAmount"));
        }
    }
}
