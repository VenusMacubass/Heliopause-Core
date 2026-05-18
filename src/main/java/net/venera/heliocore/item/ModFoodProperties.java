package net.venera.heliocore.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {
    public static final FoodProperties DEHYDRATED_FOOD = new FoodProperties.Builder()
            .nutrition(3)
            .saturationModifier(1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 120), 0.5f).build();

    public static final FoodProperties MODERN_FOOD = new FoodProperties.Builder()
            .nutrition(5)
            .saturationModifier(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 240), 1f).build();

    public static final FoodProperties RAW_FOOD = new FoodProperties.Builder()
            .nutrition(5)
            .saturationModifier(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 120), 0.3f).build();

    public static final FoodProperties EDIBLE_INGREDIENT = new FoodProperties.Builder()
            .nutrition(1)
            .saturationModifier(0.5f).build();
}

