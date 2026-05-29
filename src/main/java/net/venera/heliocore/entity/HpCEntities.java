package net.venera.heliocore.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.entity.rideable.Tier1RocketEntity;
import net.venera.heliocore.entity.zombie.SpaceZombieEntity;

import java.util.function.Supplier;

public class HpCEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, HeliopauseCore.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<SpaceZombieEntity>> SPACE_ZOMBIE =
            ENTITY_TYPES.register("space_zombie",
                    () -> EntityType.Builder.of(SpaceZombieEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build("space_zombie"));
    
    public static final Supplier<EntityType<Tier1RocketEntity>> TIER_1_ROCKET = ENTITY_TYPES.register("tier_1_rocket",
            () -> EntityType.Builder.of(Tier1RocketEntity::new, MobCategory.MISC)
                    .sized(1.0F, 2.5F)
                    .build("tier_1_rocket"));
                   

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
