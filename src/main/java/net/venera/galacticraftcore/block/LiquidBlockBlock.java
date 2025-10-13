package net.venera.galacticraftcore.block;

import net.venera.galacticraftcore.block.entity.LiquidBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class LiquidBlockBlock extends LiquidBlock implements EntityBlock {
	private final Supplier<Block> blockSupplier;

	public LiquidBlockBlock(Properties properties, Supplier<? extends FlowingFluid> supplier, Supplier<Block> blockSupplier) {
		super(supplier.get(), properties);
		this.blockSupplier = blockSupplier;
	}

	public Supplier<Block> getLiquifiedBlock() {
		return blockSupplier;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LiquidBlockEntity(pos, state);
	}
}
