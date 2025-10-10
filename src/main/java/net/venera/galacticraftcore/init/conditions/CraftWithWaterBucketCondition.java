package net.venera.galacticraftcore.init.conditions;

import com.mojang.serialization.MapCodec;
import net.venera.galacticraftcore.GalacticraftCore;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

public class CraftWithWaterBucketCondition implements ICondition {

	public static final CraftWithWaterBucketCondition INSTANCE = new CraftWithWaterBucketCondition();

	public static MapCodec<CraftWithWaterBucketCondition> CODEC = MapCodec.unit(INSTANCE).stable();
	private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "craft_with_water_bucket");

	@Override
	public boolean test(IContext context) {
		return true;
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}
}