package net.venera.heliocore.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class HpCFoodProperties {
    public static final FoodProperties COMPLEX_FOOD = new FoodProperties.Builder()
            .nutrition(7)
            .saturationModifier(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 120), 1f).build();

    public static final FoodProperties EDIBLE_INGREDIENT = new FoodProperties.Builder()
            .nutrition(1)
            .saturationModifier(0.5f).build();
}

