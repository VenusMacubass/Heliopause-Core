package net.venera.heliocore.event;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.entity.HpCBlockEntities;
import net.venera.heliocore.block.entity.machine.electric.BaseElectricMachineEntity;
import net.venera.heliocore.data.component.CanisterData;
import net.venera.heliocore.data.temperature.EnvironmentalTemperature;
import net.venera.heliocore.dimension.HpCDimensions;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;
import net.venera.heliocore.entity.rideable.Tier1RocketLanderEntity;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.item.hpc_custom.CanisterItem;
import net.venera.heliocore.util.LanderControlPayload;
import net.venera.heliocore.util.MachineButtonHelper;
import net.venera.heliocore.util.HpCTags;

@EventBusSubscriber
public class HpCEvents {
    
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
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerElectric(event, HpCBlockEntities.ENERGY_STORAGE_ENTITY.get());
        registerElectric(event, HpCBlockEntities.REFINERY_ENTITY.get());
        registerElectric(event, HpCBlockEntities.BASIC_SOLAR_PANEL_ENTITY.get());
        registerElectric(event, HpCBlockEntities.CARGO_MANAGER_ENTITY.get());
        registerElectric(event, HpCBlockEntities.FUEL_MANAGER_ENTITY.get());
        registerElectric(event, HpCBlockEntities.OXYGEN_GENERATOR_ENTITY.get());
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
    
    //region Atmospherics
    @SubscribeEvent
    public static void onFirePlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() == null) return;
        boolean isOnMoon = event.getEntity().level().dimension().location().equals(ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "moon"));

        if (isOnMoon) {
            if (event.getPlacedBlock().is(Blocks.FIRE) ||
                    event.getPlacedBlock().is(Blocks.SOUL_FIRE)) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity living) || living.level().isClientSide) {
            return;
        }
        if (living.tickCount % 40 != 0) {
            return;
        }
        if (living instanceof Player player && player.isCreative()) {
            return;
        }

        Level level = living.level();
        var biome = level.getBiome(living.blockPosition());

        double currentTemp = EnvironmentalTemperature.getEnvironmentalTemperature(level, biome);

        if (currentTemp >= 55.0) {
            living.hurt(level.damageSources().onFire(), 1.0f);

            if (currentTemp >= 60.0 && !living.isOnFire()) {
                living.setRemainingFireTicks(2);
            }
        }
        else if (currentTemp <= -50.0) {
            living.hurt(level.damageSources().freeze(), 1.0f);

            living.setTicksFrozen(150);
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

        boolean hasOxygen = !living.level().dimension().equals(HpCDimensions.MOON_LEVEL_KEY);

        if (!hasOxygen) {
            if (living.getType().is(HpCTags.Entities.DOES_NOT_BREATHE)) return;
            if (living.isInvertedHealAndHarm()) return;

//            //Is the entity wearing an oxygen mask on their head? (Checks your JSON tag!)
//            ItemStack headGear = living.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD);
//            if (headGear.is(OXYGEN_GEAR)) {
//                return;
//            }
            living.hurt(living.damageSources().drown(), 2.0f);
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

            if (isOnMoon) {
                if (entity.isOnFire()) entity.clearFire();

                if (entity instanceof LivingEntity living && living.isFallFlying()) {
                    living.setSharedFlag(7, false);
                }
            }
        }
        
        if (entity instanceof LivingEntity livingEntity) {
            AttributeInstance gravityAttribute = livingEntity.getAttribute(Attributes.GRAVITY);
            AttributeInstance safeFallAttribute = livingEntity.getAttribute(Attributes.SAFE_FALL_DISTANCE);

            if (gravityAttribute != null && safeFallAttribute != null) {
                if (isOnMoon) {
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
    
    // region Solar Panel Helper
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
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(HeliopauseCore.MOD_ID);

        registrar.playToServer(
                LanderControlPayload.TYPE,
                LanderControlPayload.STREAM_CODEC,
                (payload, context) -> {
                    // This runs on the Server!
                    context.enqueueWork(() -> {
                        Player player = context.player();
                        if (player.getVehicle() instanceof Tier1RocketLanderEntity lander) {
                            lander.isThrusting = payload.isThrusting();
                        }
                    });
                }
        );
    }
}
