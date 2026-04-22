package net.venera.galacticraftcore.event;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.block.custom.machine.electric.WireBlock;
import net.venera.galacticraftcore.block.entity.ModBlockEntities;
import net.venera.galacticraftcore.block.entity.machine.electric.BaseElectricMachineEntity;
import net.venera.galacticraftcore.data.component.CanisterData;
import net.venera.galacticraftcore.entity.rideable.Tier1RocketEntity;
import net.venera.galacticraftcore.item.ModItems;
import net.venera.galacticraftcore.item.custom.CanisterItem;
import net.venera.galacticraftcore.item.custom.TempSword;

import java.util.HashSet;
import java.util.Set;
@EventBusSubscriber
public class ModEvents {
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();

    @SubscribeEvent
    public static void onShwordUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getMainHandItem();

        if(mainHandItem.getItem() instanceof TempSword shword && player instanceof ServerPlayer serverPlayer) {
            BlockPos initialBlockPos = event.getPos();
            if(HARVESTED_BLOCKS.contains(initialBlockPos)) {
                return;
            }

            for(BlockPos pos : TempSword.getBlocksToBeDestroyed(1, initialBlockPos, serverPlayer)) {
                if(pos == initialBlockPos || !shword.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) {
                    continue;
                }
                HARVESTED_BLOCKS.add(pos);
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }

    @SubscribeEvent
    public static void onModelEvent(ModelEvent.RegisterAdditional event) {
        ItemProperties.register(ModItems.CANISTER.get(),
                ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "fluid_type"),
                (stack, level, entity, seed) -> {
                    CanisterData data = ((CanisterItem) stack.getItem()).getCanisterData(stack);
                    if (data == null || data.isEmpty()) return 0f;
                    return data.isCrudeOil() ? 1f : 2f;
                });

        ItemProperties.register(ModItems.CANISTER.get(),
                ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "fill_level"),
                (stack, level, entity, seed) -> {
                    CanisterData data = ((CanisterItem) stack.getItem()).getCanisterData(stack);
                    if (data == null || data.isEmpty()) return 0f;
                    return data.amount() / (float) CanisterItem.MAX_CAPACITY;
                });
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerElectric(event, ModBlockEntities.ENERGY_STORAGE_ENTITY.get());
        registerElectric(event, ModBlockEntities.REFINERY_ENTITY.get());
        registerElectric(event, ModBlockEntities.BASIC_SOLAR_PANEL_ENTITY.get());
    }
    
    private static void registerElectric(RegisterCapabilitiesEvent event, BlockEntityType<? extends BaseElectricMachineEntity> type) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                type,
                (machine, side) -> {
                    //Internal Access (e.g. GUI or the machine itself) -> ALWAYS ALLOW
                    if (side == null) {
                        return machine.getEnergyStorage();
                    }
                    //Input Side -> ALLOW connection
                    if (machine.isInputSide(side)) {
                        return machine.getEnergyStorage();
                    }
                    //Output Side -> ALLOW connection
                    if (machine.isOutputSide(side)) {
                        return machine.getEnergyStorage();
                    }
                    //Otherwise -> DENY connection (Return null)
                    //This tells the wire/pipe: "I have no energy capability on this side."
                    return null;
                }
        );
    }

    @SubscribeEvent
    public static void onPlayerMountRocket(EntityMountEvent event) {
        if (event.getLevel().isClientSide()) {
            if (event.getEntityMounting() instanceof Player player && player == Minecraft.getInstance().player) {
                if (event.isMounting() && event.getEntityBeingMounted() instanceof Tier1RocketEntity) {
                    Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
                }
                else if (!event.isMounting() && event.getEntityBeingMounted() instanceof Tier1RocketEntity) {
                    Minecraft.getInstance().options.setCameraType(CameraType.FIRST_PERSON);
                }
            }
        }
    }
//-----------------------------------------------------------------
    private static final ResourceLocation MOON_GRAVITY_ID = ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "moon_gravity");
    private static final ResourceLocation MOON_FALL_ID = ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "moon_safe_fall");

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) { //Gravity Manager
        Entity entity = event.getEntity();
        boolean isOnMoon = entity.level().dimension().location().equals(ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "moon"));
        
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
    //-------------------------------------------------
}
