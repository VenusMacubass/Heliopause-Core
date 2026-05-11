package net.venera.galacticraftcore.entity.rideable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.dimension.ModDimensions;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Tier1RocketEntity extends Entity implements PlayerRideableJumping {
    public Tier1RocketEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    private static final EntityDataAccessor<Boolean> IS_LAUNCHED = SynchedEntityData.defineId(Tier1RocketEntity.class, EntityDataSerializers.BOOLEAN);

    public void igniteEngine() {
        if (!this.entityData.get(IS_LAUNCHED)) {
            this.entityData.set(IS_LAUNCHED, true);
        }
    }

    @Override
    public void tick() {
        super.tick();

        double targetAltitude = 1500.0;
        if (!this.level().isClientSide() && this.getY() >= targetAltitude) {
                if (this.level() instanceof ServerLevel serverLevel && this.level().dimension() == Level.OVERWORLD) {
                    ServerLevel moonDimension = serverLevel.getServer().getLevel(ModDimensions.MOON_LEVEL_KEY);
                    if (moonDimension != null) {
                        Entity passenger = this.getFirstPassenger();
                        if(passenger == null){this.discard(); return;}
                        if (passenger instanceof ServerPlayer player) {
                            player.stopRiding();

                            Vec3 landingPos = new Vec3(0.0, 300.0, 0.0);
                            
                            DimensionTransition transition = new DimensionTransition(
                                    moonDimension,             // The destination world
                                    landingPos,               // The exact XYZ landing coordinates
                                    Vec3.ZERO,                // Velocity upon arrival (Zero so they don't go flying off the edge)
                                    player.getYRot(),         // Player's camera yaw
                                    player.getXRot(),         // Player's camera pitch
                                    DimensionTransition.DO_NOTHING // Post-teleport action (like playing a portal sound)
                            );
                            player.changeDimension(transition);
                        }
                    }
                }
                else if(this.level() instanceof ServerLevel serverLevel && this.level().dimension() == ModDimensions.MOON_LEVEL_KEY) {
                    Entity passenger = this.getFirstPassenger();
                    if(passenger == null){this.discard(); return;}
                    if(passenger instanceof LivingEntity entity) {
                        entity.stopRiding();
                        Vec3 landingPos = new Vec3(0.0, 300.0, 0.0);
                        DimensionTransition transition = new DimensionTransition(
                                Objects.requireNonNull(serverLevel.getServer().getLevel(Level.OVERWORLD)), 
                                landingPos,               
                                Vec3.ZERO,               
                                passenger.getYRot(),        
                                passenger.getXRot(),        
                                DimensionTransition.DO_NOTHING 
                        );
                        entity.changeDimension(transition);
                    }
                }
                this.discard();
                
        }
        if (this.entityData.get(IS_LAUNCHED)) {
            Vec3 currentVelocity = this.getDeltaMovement();

            double acceleration = 0.05;
            double maxSpeed = 5.0;

            double newYVelocity = Math.min(currentVelocity.y() + acceleration, maxSpeed);

            this.setDeltaMovement(new Vec3(currentVelocity.x(), newYVelocity, currentVelocity.z()));

            this.move(MoverType.SELF, this.getDeltaMovement());
        }
        
        if(this.entityData.get(IS_LAUNCHED) && this.getFirstPassenger() == null && this.getY() >= targetAltitude){
            this.level().explode(
                    this,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    6.0F,
                    Level.ExplosionInteraction.TNT
            );
            this.discard();
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
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
            this.spawnAtLocation(ModBlocks.T1_ROCKET_BOT.get());
            this.discard();
            return true;
        }
        return false;
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
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}
