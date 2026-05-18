package net.venera.heliocore.block.entity;

import net.venera.heliocore.fluid.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LiquidBlockEntity extends BlockEntity {
	public LiquidBlockEntity(BlockPos pos, BlockState state) {
		super(ModFluids.LIQUID_BLOCK_ENTITY.get(), pos, state);
	}
}
