package net.venera.galacticraftcore.init.conditions;

import com.mojang.serialization.MapCodec;
import net.venera.galacticraftcore.GalacticraftCore;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

public class CraftWithIceCondition implements ICondition {

	public static final CraftWithIceCondition INSTANCE = new CraftWithIceCondition();

	public static MapCodec<CraftWithIceCondition> CODEC = MapCodec.unit(INSTANCE).stable();

	private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "craft_with_ice");

	@Override
	public boolean test(IContext context) {
		return false; //LiquidConfig.COMMON.craftWithIce.get();
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public String toString() {
		return "craft_with_ice";
	}
}