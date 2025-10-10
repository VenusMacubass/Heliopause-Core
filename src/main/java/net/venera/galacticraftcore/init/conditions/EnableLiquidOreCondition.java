package net.venera.galacticraftcore.init.conditions;

import com.mojang.serialization.MapCodec;
import net.venera.galacticraftcore.GalacticraftCore;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.ICondition;

public class EnableLiquidOreCondition implements ICondition {

	public static final EnableLiquidOreCondition INSTANCE = new EnableLiquidOreCondition();

	public static MapCodec<EnableLiquidOreCondition> CODEC = MapCodec.unit(INSTANCE).stable();
	private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "enable_liquid_ore");

	@Override
	public boolean test(IContext context) {
		return  false;//LiquidConfig.COMMON.craftLiquidOre.get();
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}
}