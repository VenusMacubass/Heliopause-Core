package net.venera.heliocore.entity.rideable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.venera.heliocore.dimension.HpCDimensions;
import net.venera.heliocore.entity.HpCEntities;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.screen.hpc_custom.RocketMenu;
import org.jetbrains.annotations.Nullable;

public class Tier1RocketEntity extends Entity implements PlayerRideableJumping {
    public final ItemStackHandler inventory = new ItemStackHandler(28);
    public Tier1RocketEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    private static final EntityDataAccessor<Boolean> IS_LAUNCHED = SynchedEntityData.defineId(Tier1RocketEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> FUEL_AMOUNT = SynchedEntityData.defineId(Tier1RocketEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ENERGY_AMOUNT = SynchedEntityData.defineId(Tier1RocketEntity.class, EntityDataSerializers.INT);
    public final int MAX_FUEL = 1000; 
    public final int MAX_ENERGY = 5000;
    public final int ENERGY_USAGE = 2;
    public final int FUEL_USAGE = 1;
    public static int maxFuel = 1000;
    public static int maxEnergy = 5000;

    @Override
    public void tick() {
        super.tick();
        double targetAltitude = 1500.0;
        
        if (this.entityData.get(IS_LAUNCHED)) {
            if (this.getFuelAmount() > 0 && this.getEnergyAmount() > 0) {
                this.setFuelAmount(this.getFuelAmount() - FUEL_USAGE);
                this.setEnergyAmount(this.getEnergyAmount() - ENERGY_USAGE);

                Vec3 currentVelocity = this.getDeltaMovement();
                double acceleration = 0.05;
                double maxSpeed = 5.0;
                double newYVelocity = Math.min(currentVelocity.y() + acceleration, maxSpeed);
                this.setDeltaMovement(new Vec3(currentVelocity.x(), newYVelocity, currentVelocity.z()));
            } else {
                this.entityData.set(IS_LAUNCHED, false);
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
        }
        
        if (this.getY() >= targetAltitude) {
            
            if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
                Entity passenger = this.getFirstPassenger();

                if (passenger instanceof LivingEntity) {
                    
                    if (serverLevel.dimension().equals(Level.OVERWORLD)) {
                        transitionToMoon();
                    } else if (serverLevel.dimension().equals(HpCDimensions.MOON_LEVEL_KEY)) {
                        transitionToEarth();
                    } else {
                        this.discard();
                    }
                } else {
                    
                    this.level().explode(this, this.getX(), this.getY(), this.getZ(), 6.0F, Level.ExplosionInteraction.TNT);
                    this.discard();
                }
            }
        }
        if(this.entityData.get(IS_LAUNCHED) && this.getFirstPassenger() == null && this.getY() >= targetAltitude){
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 6.0F, Level.ExplosionInteraction.TNT);
            this.discard();
        }
    }

    public void igniteEngine() {
        if (!this.entityData.get(IS_LAUNCHED) && this.getEnergyAmount() > 0 && this.getFuelAmount() > 0 ) {
            this.entityData.set(IS_LAUNCHED, true);
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            if (!this.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (id, playerInv, p) -> new RocketMenu(id, playerInv, this),
                        Component.literal("Rocket Cargo Inventory")
                ), buf -> buf.writeInt(this.getId()));

            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        if (!this.level().isClientSide() && !this.entityData.get(IS_LAUNCHED)) {
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }
        return super.interact(player, hand);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (!this.level().isClientSide() && !this.isRemoved()) {
            SimpleContainer inv = new SimpleContainer(inventory.getSlots());
            for(int i = 0; i < inventory.getSlots(); i++){
                inv.setItem(i, inventory.getStackInSlot(i));
            }
            Containers.dropContents(this.level(), this, inv);
            this.discard();
            return true;
        }
        return false;
    }
    public void transitionToEarth() {
        if (!(this.level() instanceof ServerLevel currentLevel)) return;
        ServerLevel earthLevel = currentLevel.getServer().overworld();

        Entity passenger = this.getFirstPassenger();
        if (passenger instanceof LivingEntity livingPassenger) {
            livingPassenger.stopRiding();

            double dropX = this.getX();
            double dropY = 600.0D;
            double dropZ = this.getZ();
            
            int fuel = this.getFuelAmount();
            int energy = this.getEnergyAmount();
            CompoundTag invTag = this.inventory.serializeNBT(this.registryAccess());

            DimensionTransition transition = new DimensionTransition(
                    earthLevel,
                    new Vec3(dropX, dropY, dropZ),
                    Vec3.ZERO,
                    livingPassenger.getYRot(),
                    livingPassenger.getXRot(),
                    
                    (teleportedEntity) -> {
                        Tier1RocketLanderEntity lander = new Tier1RocketLanderEntity(HpCEntities.TIER_1_ROCKET_LANDER.get(), earthLevel);
                        lander.setPos(dropX, dropY, dropZ);

                        lander.setFuelAmount(fuel);
                        lander.setEnergyAmount(energy);
                        for (int i = 0; i < this.inventory.getSlots(); i++) {
                            lander.inventory.setStackInSlot(i, this.inventory.getStackInSlot(i).copy());
                        }
                        lander.inventory.setStackInSlot(29, new ItemStack(HpCItems.ROCKET_ITEM.get()));
                        lander.setDeltaMovement(new Vec3(0.0D, -2.5D, 0.0D));
                        earthLevel.addFreshEntity(lander);

                        teleportedEntity.startRiding(lander, true);
                    }
            );

            livingPassenger.changeDimension(transition);
            this.discard();
        }
    }

    public void transitionToMoon() {
        if (!(this.level() instanceof ServerLevel currentLevel)) return;
        ServerLevel moonLevel = currentLevel.getServer().getLevel(HpCDimensions.MOON_LEVEL_KEY);

        if (moonLevel == null) {
            this.discard(); 
            return;
        }

        Entity passenger = this.getFirstPassenger();
        if (passenger instanceof LivingEntity livingPassenger) {
            livingPassenger.stopRiding();

            double dropX = this.getX();
            double dropY = 600.0D;
            double dropZ = this.getZ();

            int fuel = this.getFuelAmount();
            int energy = this.getEnergyAmount();
            CompoundTag invTag = this.inventory.serializeNBT(this.registryAccess());

            DimensionTransition transition = new DimensionTransition(
                    moonLevel,
                    new Vec3(dropX, dropY, dropZ),
                    Vec3.ZERO,
                    livingPassenger.getYRot(),
                    livingPassenger.getXRot(),

                    (teleportedEntity) -> {
                        Tier1RocketLanderEntity lander = new Tier1RocketLanderEntity(HpCEntities.TIER_1_ROCKET_LANDER.get(), moonLevel);
                        lander.setPos(dropX, dropY, dropZ);
                        lander.inventory.setStackInSlot(29, new ItemStack(HpCItems.ROCKET_ITEM.get()));
                        lander.setFuelAmount(fuel);
                        lander.setEnergyAmount(energy);
                        for (int i = 0; i < this.inventory.getSlots(); i++) {
                            lander.inventory.setStackInSlot(i, this.inventory.getStackInSlot(i).copy());
                        }

                        lander.setDeltaMovement(new Vec3(0.0D, -2.5D, 0.0D));
                        moonLevel.addFreshEntity(lander);

                        teleportedEntity.startRiding(lander, true);
                    }
            );

            livingPassenger.changeDimension(transition);
            this.discard();
        }
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public void onPlayerJump(int jumpPower) {
        if(jumpPower > 0){igniteEngine();}
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int jumpPower) {
        
    }

    @Override
    public void handleStopJump() {
        
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
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
            return livingPassenger;}
        return null;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(IS_LAUNCHED, false);
        builder.define(FUEL_AMOUNT, 0);
        builder.define(ENERGY_AMOUNT, 0);
    }

    public int getFuelAmount() {
        return this.entityData.get(FUEL_AMOUNT);
    }

    public void setFuelAmount(int amount) {
        this.entityData.set(FUEL_AMOUNT, Math.max(0, Math.min(amount, MAX_FUEL)));
    }

    public int getEnergyAmount() {
        return this.entityData.get(ENERGY_AMOUNT);
    }

    public void setEnergyAmount(int amount) {
        this.entityData.set(ENERGY_AMOUNT, Math.max(0, Math.min(amount, MAX_ENERGY)));
    }
    public int getMaxFuelAmount() {
        maxFuel = this.MAX_FUEL;
        return maxFuel;
    }
    public int getMaxEnergyAmount() {
        maxEnergy = this.MAX_ENERGY;
        return maxEnergy;
    }
    
    public int chargeEnergy(int amount, boolean simulate) {
        int space = MAX_ENERGY - this.getEnergyAmount();
        int accepted = Math.min(space, amount);
        if (!simulate && accepted > 0) {
            this.setEnergyAmount(this.getEnergyAmount() + accepted);
        }
        return accepted;
    }
    
    public int fillFuel(int amount, boolean simulate) {
        int space = MAX_FUEL - this.getFuelAmount();
        int filled = Math.min(space, amount);
        if (!simulate && filled > 0) {
            this.setFuelAmount(this.getFuelAmount() + filled);
        }
        return filled;
    }
    
    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.put("RocketInventory", this.inventory.serializeNBT(this.registryAccess()));
        compoundTag.putBoolean("IsLaunched", this.entityData.get(IS_LAUNCHED));
        compoundTag.putInt("EnergyAmount", this.getEnergyAmount());
        compoundTag.putInt("FuelAmount", this.getFuelAmount());
    }
    
    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("RocketInventory")) {
            this.inventory.deserializeNBT(this.registryAccess(), compoundTag.getCompound("RocketInventory"));
        }
        if (compoundTag.contains("IsLaunched")) {
            this.entityData.set(IS_LAUNCHED, compoundTag.getBoolean("IsLaunched"));
        }
        if (compoundTag.contains("EnergyAmount")) {
            this.setEnergyAmount(compoundTag.getInt("EnergyAmount"));
        }
        if (compoundTag.contains("FuelAmount")) {
            this.setFuelAmount(compoundTag.getInt("FuelAmount")); 
        }
    }
}
