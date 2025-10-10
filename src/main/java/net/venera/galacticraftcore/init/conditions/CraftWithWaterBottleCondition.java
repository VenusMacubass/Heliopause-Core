package net.venera.galacticraftcore.init.conditions;

import com.mojang.serialization.MapCodec;
import net.venera.galacticraftcore.GalacticraftCore;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

public class CraftWithWaterBottleCondition implements ICondition {

	public static final CraftWithWaterBottleCondition INSTANCE = new CraftWithWaterBottleCondition();

	public static MapCodec<CraftWithWaterBottleCondition> CODEC = MapCodec.unit(INSTANCE).stable();
	private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "craft_with_water_bottle");

	@Override
	public boolean test(IContext context) {
		return false; //LiquidConfig.COMMON.craftWithWaterBottle.get();
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}
}