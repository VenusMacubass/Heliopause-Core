package net.venera.heliocore.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.level.PistonEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import net.venera.heliocore.block.entity.machine.electric.BaseElectricMachineEntity;
import net.venera.heliocore.block.entity.machine.electric.OxygenSealerEntity;
import net.venera.heliocore.data.HpCAttachments;
import net.venera.heliocore.data.temperature.EnvironmentalTemperature;
import net.venera.heliocore.entity.rideable.Tier1RocketLanderEntity;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.item.HpCTags;
import net.venera.heliocore.util.*;

@EventBusSubscriber
public class HpCEvents {
    private static final ResourceLocation CUSTOM_ELYTRA_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "custom_elytra_flight");
    
    @SubscribeEvent
    public static void onGlassSwordUsage(LivingDamageEvent.Pre event) {
      Entity entity = event.getSource().getEntity();
      if(entity instanceof LivingEntity attacker) {
          ItemStack attackerItem = attacker.getMainHandItem();
          if(attackerItem.getItem() == HpCItems.GLASS_SWORD.get()){
              float damageBuff = (float) ((float) attackerItem.getDamageValue()/(attackerItem.getMaxDamage() + 0.001));
              event.setNewDamage(event.getOriginalDamage() * (1f + damageBuff));
          }
      }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) return;
        BlockPos sealerPos = OxygenVolumeHelper.getSealerForWall(event.getPos().asLong());
        if (sealerPos != null && event.getLevel().getBlockEntity(sealerPos) instanceof OxygenSealerEntity sealer) {
            sealer.seal = false;
            OxygenVolumeHelper.removeRoom(sealerPos);
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        if (event.getLevel().isClientSide()) return;
        for (BlockPos pos : event.getAffectedBlocks()) {
            BlockPos sealerPos = OxygenVolumeHelper.getSealerForWall(pos.asLong());
            if (sealerPos != null && event.getLevel().getBlockEntity(sealerPos) instanceof OxygenSealerEntity sealer) {
                sealer.seal = false;
                OxygenVolumeHelper.removeRoom(sealerPos);
            }
        }
    }

    @SubscribeEvent
    public static void onPistonMove(PistonEvent.Pre event) {
        if (event.getLevel().isClientSide()) return;
        BlockPos sealerPos = OxygenVolumeHelper.getSealerForWall(event.getPos().asLong());
        if (sealerPos != null && event.getLevel().getBlockEntity(sealerPos) instanceof OxygenSealerEntity sealer) {
            sealer.seal = false;
            OxygenVolumeHelper.removeRoom(sealerPos);
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;
        
        if (entity instanceof Player player) {
            if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                return;
            }
        }
        
        var inventory = entity.getData(HpCAttachments.EQUIPMENT_INVENTORY);
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                entity.spawnAtLocation(stack);
                inventory.setStackInSlot(i, ItemStack.EMPTY); 
            }
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity targetEntity && event.getEntity() instanceof ServerPlayer observer) {
            var inventory = targetEntity.getData(HpCAttachments.EQUIPMENT_INVENTORY);
            PacketDistributor.sendToPlayer(observer, new SyncEquipmentPayload(targetEntity.getId(), inventory.serializeNBT(observer.registryAccess())));
        }
    }
    
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var inventory = player.getData(HpCAttachments.EQUIPMENT_INVENTORY);
            PacketDistributor.sendToPlayer(player, new SyncEquipmentPayload(player.getId(), inventory.serializeNBT(player.registryAccess())));
        }
    }
    
    public static void syncToAllTracking(LivingEntity entity) {
        if (entity.level().isClientSide()) return;
        var inventory = entity.getData(HpCAttachments.EQUIPMENT_INVENTORY);
        
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity,
                new SyncEquipmentPayload(entity.getId(), inventory.serializeNBT(entity.registryAccess())));
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        Player newPlayer = event.getEntity();
        if (event.isWasDeath() && !original.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            return; 
        }
        
        var oldInventory = original.getData(HpCAttachments.EQUIPMENT_INVENTORY);
        var newInventory = newPlayer.getData(HpCAttachments.EQUIPMENT_INVENTORY);

        for (int i = 0; i < oldInventory.getSlots(); i++) {
            newInventory.setStackInSlot(i, oldInventory.getStackInSlot(i).copy());
        }
    }

    //region Registries
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerElectric(event, HpCBlockEntities.ENERGY_STORAGE_ENTITY.get());
        registerElectric(event, HpCBlockEntities.REFINERY_ENTITY.get());
        registerElectric(event, HpCBlockEntities.BASIC_SOLAR_PANEL_ENTITY.get());
        registerElectric(event, HpCBlockEntities.CARGO_MANAGER_ENTITY.get());
        registerElectric(event, HpCBlockEntities.FUEL_MANAGER_ENTITY.get());
        registerElectric(event, HpCBlockEntities.OXYGEN_GENERATOR_ENTITY.get());
        registerElectric(event, HpCBlockEntities.ENERGY_GENERATOR_ENTITY.get());
        registerElectric(event, HpCBlockEntities.GAS_COMPRESSOR_ENTITY.get());
        registerElectric(event, HpCBlockEntities.GAS_VAPORIZER_ENTITY.get());
    }
    
    private static void registerElectric(RegisterCapabilitiesEvent event, BlockEntityType<? extends BaseElectricMachineEntity> type) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                type,
                (machine, side) -> {
                    if (side == null) {
                        return machine.getEnergyStorage();
                    }
                    if (machine.isInputSide(side)) {
                        return machine.getEnergyStorage();
                    }
                    if (machine.isOutputSide(side)) {
                        return machine.getEnergyStorage();
                    }
                    return null;
                }
        );
    }
    
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(HeliopauseCore.MOD_ID);

        registrar.playToServer(
                LanderControlPayload.TYPE,
                LanderControlPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        Player player = context.player();
                        if (player.getVehicle() instanceof Tier1RocketLanderEntity lander) {
                            lander.isThrusting = payload.isThrusting();
                        }
                    });
                }
        );

        registrar.playToServer(
                ElytraSlotPayload.TYPE,
                ElytraSlotPayload.CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        Player player = context.player();
                        if (player != null && !player.onGround() && !player.isInWater() && !player.hasEffect(MobEffects.LEVITATION)) {
                            var inventory = player.getData(HpCAttachments.EQUIPMENT_INVENTORY);
                            if (inventory != null) {
                                ItemStack stack = inventory.getStackInSlot(8);
                                
                                if (!stack.isEmpty() && stack.canElytraFly(player)) {
                                    player.startFallFlying();
                                }
                            }
                        }
                    });
                }
        );
    }
    //endregion
    
    //region Atmospherics

    @SubscribeEvent
    public static void onFlintAndSteel(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;

        if (event.getItemStack().is(Items.FLINT_AND_STEEL) || event.getItemStack().is(Items.FIRE_CHARGE)) {
            BlockPos targetPos = event.getPos().relative(event.getFace());
            if (!OxygenVolumeHelper.isPositionSealed(targetPos.asLong())) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public static void onFirePlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel().isClientSide()) return;
        BlockState placedState = event.getPlacedBlock();
        if (placedState.is(Blocks.FIRE) || placedState.is(Blocks.SOUL_FIRE)) {
            if (!OxygenVolumeHelper.isPositionSealed(event.getPos().asLong())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityThermalTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity living) || living.level().isClientSide) {
            return;
        }
        if (living.tickCount % 20 != 0) {
            return;
        }
        if (living instanceof Player player && player.isCreative()) {
            return;
        }

        Level level = living.level();
        
        BlockPos headPos = BlockPos.containing(living.getX(), living.getEyeY(), living.getZ());
        long headLong = headPos.asLong();
        boolean inRegulatedRoom = false;
        BlockPos targetSealedPos = null;
        
        if (OxygenVolumeHelper.isPositionSealed(headLong)) {
            targetSealedPos = headPos;
        }
       
        else {
            BlockState headState = level.getBlockState(headPos);
            if (headState.is(HpCBlocks.AIRLOCK_GENERATED_BLOCK.get())) {
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockPos neighborPos = headPos.relative(dir);
                    if (OxygenVolumeHelper.isPositionSealed(neighborPos.asLong())) {
                        targetSealedPos = neighborPos;
                        break;
                    }
                }
            }
        }
        
        if (targetSealedPos != null) {
            BlockPos sealerPos = OxygenVolumeHelper.getSealerForAir(targetSealedPos.asLong(), level);
            if (sealerPos != null && level.getBlockEntity(sealerPos) instanceof OxygenSealerEntity sealer) {
                if (sealer.isThermallyRegulating()) {
                    inRegulatedRoom = true;
                }
            }
        }
        
        if (inRegulatedRoom) {
            if (living.getTicksFrozen() > 0) {
                living.setTicksFrozen(Math.max(0, living.getTicksFrozen() - 10));
            }
            return;
        }

        Holder<Biome> biome = level.getBiome(living.blockPosition());
        double currentTemp = EnvironmentalTemperature.getEnvironmentalTemperature(level, biome);
        int thermalProtectionScore = SpaceGearSetupHelper.checkThermalSetup(living);
        
        if (thermalProtectionScore == 4) {
            if (living.getTicksFrozen() > 0) {
                living.setTicksFrozen(Math.max(0, living.getTicksFrozen() - 10));
            }
            return;
        }

        float exposureMultiplier = (4 - thermalProtectionScore) / 4.0f;

        if (currentTemp > 40.0) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0, false, false, true));

            if (currentTemp > 400.0) {
                living.setRemainingFireTicks((int)(60 * exposureMultiplier));
                living.hurt(living.damageSources().onFire(), 4.0f * exposureMultiplier);
            } else if (currentTemp > 150.0) {
                living.setRemainingFireTicks((int)(40 * exposureMultiplier));
                living.hurt(living.damageSources().onFire(), 2.0f * exposureMultiplier);
            } else if (currentTemp > 80.0) {
                living.setRemainingFireTicks((int)(40 * exposureMultiplier));
                living.hurt(living.damageSources().onFire(), exposureMultiplier);
            } else if (currentTemp > 60.0) {
                living.hurt(living.damageSources().hotFloor(), 0.5f * exposureMultiplier);
            }
        }

        else if (currentTemp < -15.0) {
            living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, false, false, true));

            if (currentTemp < -60.0) {
                int currentFreeze = living.getTicksFrozen();
                living.setTicksFrozen(currentFreeze + (int)(20 * exposureMultiplier));
            }

            if (currentTemp < -250.0) {
                living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 2, false, false, true));
                living.hurt(living.damageSources().freeze(), 4.0f * exposureMultiplier);
            } else if (currentTemp < -150.0) {
                living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1, false, false, true));
                living.hurt(living.damageSources().freeze(), 2.0f * exposureMultiplier);
            } else if (currentTemp < -80.0) {
                living.hurt(living.damageSources().freeze(), exposureMultiplier);
            }
        } else {
            if (living.getTicksFrozen() > 0) {
                living.setTicksFrozen(Math.max(0, living.getTicksFrozen() - 10));
            }
        }
    }

    @SubscribeEvent
    public static void onOxygenTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity living) || living.level().isClientSide) {
            return;
        }
        if (living.tickCount % 20 != 0) return;
        if (living instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return;
        }
        
        BlockPos headPos = BlockPos.containing(event.getEntity().getX(), event.getEntity().getEyeY(), event.getEntity().getZ());
        long headLong = headPos.asLong();
        ResourceLocation currentDimension = living.level().dimension().location();

        ResourceLocation moonDim = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon");
        boolean inOxygen = !currentDimension.equals(moonDim);
        
        if (!inOxygen) {
            inOxygen = OxygenVolumeHelper.isPositionSealed(headLong);
            if (!inOxygen) {
                BlockState headState = living.level().getBlockState(headPos);
                if (headState.is(HpCBlocks.AIRLOCK_GENERATED_BLOCK.get())) {
                    for (Direction dir : Direction.Plane.HORIZONTAL) {
                        if (OxygenVolumeHelper.isPositionSealed(headPos.relative(dir).asLong())) {
                            inOxygen = true;
                            break;
                        }
                    }
                }
            }
        }
        
        if (!inOxygen) {
            boolean hasOxygenGear = SpaceGearSetupHelper.checkOxygenSetup(living);

            if (!hasOxygenGear) {
                if (living.getType().is(HpCTags.Entities.DOES_NOT_BREATHE)) return;
                if (living.isInvertedHealAndHarm()) return;
                living.hurt(living.damageSources().drown(), 2.0f);
            }
        }
    }
    //endregion

    //region Moon Gravity 
    private static final ResourceLocation MOON_GRAVITY_ID = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon_gravity");
    private static final ResourceLocation MOON_FALL_ID = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon_safe_fall");

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) { //Gravity Manager
        Entity entity = event.getEntity();
        boolean isOnMoon = entity.level().dimension().location().equals(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon"));

        if (isOnMoon) {
            if (entity.isOnFire()) {
                entity.clearFire();
            }
            if (entity.isOnFire()) entity.clearFire();

            if (entity instanceof LivingEntity living && living.isFallFlying()) {
                living.setSharedFlag(7, false);
            }
        }
        
        if (entity instanceof LivingEntity livingEntity) {
            AttributeInstance gravityAttribute = livingEntity.getAttribute(Attributes.GRAVITY);
            AttributeInstance safeFallAttribute = livingEntity.getAttribute(Attributes.SAFE_FALL_DISTANCE);
            ItemStackHandler inventory = livingEntity.getData(HpCAttachments.EQUIPMENT_INVENTORY);
            ItemStack beltItem = inventory.getStackInSlot(9);

            if (gravityAttribute != null && safeFallAttribute != null) {
                if (isOnMoon && !(beltItem.is(HpCItems.MASS_BELT.get()))) {
                    if (!gravityAttribute.hasModifier(MOON_GRAVITY_ID)) {
                        gravityAttribute.addTransientModifier(new AttributeModifier(MOON_GRAVITY_ID, -0.83, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                    }
                    if (!safeFallAttribute.hasModifier(MOON_FALL_ID)) {
                        safeFallAttribute.addTransientModifier(new AttributeModifier(MOON_FALL_ID, 5.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                    }
                } else {
                    gravityAttribute.removeModifier(MOON_GRAVITY_ID);
                    safeFallAttribute.removeModifier(MOON_FALL_ID);
                }
            }
        }
        else if (entity instanceof Projectile projectile) {
            if (isOnMoon && !projectile.isNoGravity()) {
                Vec3 movement = projectile.getDeltaMovement();

                
                if (projectile instanceof AbstractArrow) {
                    projectile.setDeltaMovement(movement.x, movement.y + 0.04D, movement.z);
                }
                else if (projectile instanceof ThrowableItemProjectile) {
                    
                    projectile.setDeltaMovement(movement.x, movement.y + 0.02D, movement.z);
                }
                else {
                    projectile.setDeltaMovement(movement.x, movement.y + 0.02D, movement.z);
                }
            }
        }
        else if (entity instanceof FallingBlockEntity fallingBlock) {
            if (isOnMoon && !fallingBlock.isNoGravity()) {
                net.minecraft.world.phys.Vec3 movement = fallingBlock.getDeltaMovement();
                //Vanilla drops by -0.04 per tick. 
                fallingBlock.setDeltaMovement(movement.x, movement.y + 0.03D, movement.z);
            }
        }
        else if (entity instanceof ItemEntity  itemEntity) {
            if (isOnMoon && !itemEntity.isNoGravity()) {
                Vec3 movement = itemEntity.getDeltaMovement();
                itemEntity.setDeltaMovement(movement.x, movement.y + 0.03D, movement.z);
            }
        }
    }
    //endregion
    
    // region Machine Button Helper
    @SubscribeEvent
    public static void registerNetworking(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(HeliopauseCore.MOD_ID);
        registrar.playToServer(
                MachineButtonHelper.TYPE,
                MachineButtonHelper.STREAM_CODEC,
                MachineButtonHelper::handle
        );
    }
    //endregion
    
    
}
