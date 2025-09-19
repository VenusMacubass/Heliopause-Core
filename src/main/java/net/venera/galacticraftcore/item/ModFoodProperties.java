package net.venera.galacticraftcore.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {
    public static final FoodProperties DEHYDRATED_FOOD = new FoodProperties.Builder()
            .nutrition(3)
            .saturationModifier(1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 240), 0.5f).build();
}
