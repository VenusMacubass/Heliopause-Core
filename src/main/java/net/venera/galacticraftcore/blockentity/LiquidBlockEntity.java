package net.venera.galacticraftcore.blockentity;

import net.venera.galacticraftcore.fluid.ModFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LiquidBlockEntity extends BlockEntity {
	public LiquidBlockEntity(BlockPos pos, BlockState state) {
		super(ModFluids.LIQUID_BLOCK_ENTITY.get(), pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, LiquidBlockEntity blockEntity) {

	}
}
