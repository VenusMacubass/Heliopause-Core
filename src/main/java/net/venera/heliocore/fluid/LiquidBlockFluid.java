package net.venera.heliocore.fluid;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class LiquidBlockFluid {
	public static class Flowing extends BaseFlowingFluid {
		public Flowing(Properties properties) {
			super(properties);
			registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
		}

		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}

        @Override
		public boolean isSource(FluidState state) {
			return false;
		}

		@Override
		public int getAmount(FluidState state) {
			return state.getValue(LEVEL);
		}
	}

	public static class Source extends BaseFlowingFluid {
		public Source(Properties properties) {
			super(properties);
		}

        @Override
        public int getTickDelay(LevelReader level) {
            return Math.max(1, getFluidType().getViscosity() / 200);
        }

		public int getAmount(FluidState state) {
			return 8;
		}

		public boolean isSource(FluidState state) {
			return true;
		}

		@Override
		protected boolean canConvertToSource(Level level) {
			return false;
		}
	}
}
