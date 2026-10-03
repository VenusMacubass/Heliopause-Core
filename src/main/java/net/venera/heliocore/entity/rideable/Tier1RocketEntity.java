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
import net.venera.heliocore.screen.entity.RocketMenu;
import org.jetbrains.annotations.Nullable;

public class Tier1RocketEntity extends Entity implements PlayerRideableJumping {
    public final ItemStackHandler inventory = new ItemStackHandler(27);
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
    public static final int maxFuel = 1000;
    public static final int maxEnergy = 5000;
    private double previousYVelocity;

    @Override
    public void tick() {
        this.previousYVelocity = this.getDeltaMovement().y;
        super.tick();
        double targetAltitude = 2500.0;

        Vec3 currentVelocity = this.getDeltaMovement();
        double newYVelocity = currentVelocity.y();

        // 1. Universal Gravity
        if (!this.onGround()) {
            double gravity = 0.05D;
            newYVelocity -= gravity;
        }

        // 2. Engine Thrust (Overcomes gravity if active)
        if (this.entityData.get(IS_LAUNCHED)) {

            // SERVER ONLY: Drain fuel and cut engine if empty
            if (!this.level().isClientSide()) {
                if (this.getFuelAmount() >= FUEL_USAGE && this.getEnergyAmount() >= ENERGY_USAGE) {
                    this.setFuelAmount(this.getFuelAmount() - FUEL_USAGE);
                    this.setEnergyAmount(this.getEnergyAmount() - ENERGY_USAGE);
                } else {
                    this.entityData.set(IS_LAUNCHED, false); // Out of fuel/energy!
                }
            }

            // BOTH SIDES: Apply physical thrust as long as the engine is on
            if (this.entityData.get(IS_LAUNCHED)) {
                double engineThrust = 0.10D; // Strong enough to beat the 0.05 gravity
                newYVelocity += engineThrust;
            }
        }

        // 3. Clamp speeds to terminal velocity (max 5.0 up, max -5.0 down)
        // (Using Math.max/min for broader Java version compatibility)
        newYVelocity = Math.clamp(newYVelocity, -5.0D, 5.0D);

        // 4. Apply calculated net movement
        this.setDeltaMovement(new Vec3(currentVelocity.x(), newYVelocity, currentVelocity.z()));
        this.move(MoverType.SELF, this.getDeltaMovement());

        // 5. Collision and Crash Logic (SERVER ONLY)
        if (!this.level().isClientSide()) {
            // Did it hit a ceiling during launch? (Vertical collision is true, but it's not on the ground)
            if (this.entityData.get(IS_LAUNCHED) && this.verticalCollision && !this.onGround()) {
                this.explode(Math.abs(this.previousYVelocity * 20D));
                return;
            }

            // Did it fall out of the sky and hit the ground? (Speed was highly negative)
            if (this.onGround() && this.previousYVelocity < -1.0D) {
                this.explode(Math.abs(this.previousYVelocity * 20D));
                return;
            }
        }

        // 6. Dimension Transition Logic
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

        if (this.entityData.get(IS_LAUNCHED) && this.getFirstPassenger() == null && this.getY() >= targetAltitude) {
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

            double dropX = this.getX()/4;
            double dropY = 900.0D;
            double dropZ = this.getZ()/4;

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

                        lander.inventory.setStackInSlot(28, new ItemStack(HpCItems.ROCKET_ITEM.get()));

                        lander.expectedPassenger = teleportedEntity.getUUID();
                        lander.setDeltaMovement(Vec3.ZERO);

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

            double dropX = this.getX()/4;
            double dropY = 900.0D;
            double dropZ = this.getZ()/4;

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
                        lander.inventory.setStackInSlot(28, new ItemStack(HpCItems.ROCKET_ITEM.get()));
                        lander.setFuelAmount(fuel);
                        lander.setEnergyAmount(energy);
                        for (int i = 0; i < this.inventory.getSlots(); i++) {
                            lander.inventory.setStackInSlot(i, this.inventory.getStackInSlot(i).copy());
                        }

                        lander.expectedPassenger = teleportedEntity.getUUID();
                        lander.setDeltaMovement(Vec3.ZERO);

                        moonLevel.addFreshEntity(lander);
                        teleportedEntity.startRiding(lander, true);
                    }
            );

            livingPassenger.changeDimension(transition);
            this.discard();
        }
    }

    private void explode(double speed) {
        this.clearInventory();
        float explosionCoefficient =  1 + (getFuelAmount() / (float)MAX_FUEL) + ((float)speed / 100.0F);
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 4.0F * explosionCoefficient, Level.ExplosionInteraction.MOB);
        this.discard();
    }

    private void clearInventory() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
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
        if(jumpPower > 0) { igniteEngine(); }
    }

    @Override
    public void handleStopJump() { }

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
            return livingPassenger;
        }
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
