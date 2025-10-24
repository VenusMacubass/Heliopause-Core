package net.venera.galacticraftcore.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.entity.villager.MoonVillagerEntity;
import net.venera.galacticraftcore.entity.zombie.SpaceZombieEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, GalacticraftCore.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<SpaceZombieEntity>> SPACE_ZOMBIE =
            ENTITY_TYPES.register("space_zombie",
                    () -> EntityType.Builder.of(SpaceZombieEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build("space_zombie"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
