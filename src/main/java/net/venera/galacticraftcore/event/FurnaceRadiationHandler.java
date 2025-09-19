package net.venera.galacticraftcore.event;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.venera.galacticraftcore.item.ModItems;

import java.util.List;

public class FurnaceRadiationHandler {
    private static final double RADIUS = 6.0;

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {

    }
}
