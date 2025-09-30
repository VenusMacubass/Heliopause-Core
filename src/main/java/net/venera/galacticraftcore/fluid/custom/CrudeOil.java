package net.venera.galacticraftcore.fluid.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.fluid.ModFluidTypes;
import net.venera.galacticraftcore.fluid.ModFluids;
import net.venera.galacticraftcore.item.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CrudeOil extends FlowingFluid {
    protected CrudeOil() {
        super();
    }

    @Override
    public @NotNull Fluid getFlowing() {return ModFluids.FLOWING_CRUDE_OIL.get();}

    @Override
    public @NotNull Fluid getSource() {return ModFluids.CRUDE_OIL.get();}

    @Override
    protected boolean canConvertToSource(@NotNull Level level) {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(@NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {

    }

    @Override
    public Item getBucket() {
        return ModItems.CRUDE_OIL_BUCKET.get();
    }

    @Override
    protected boolean canBeReplacedWith(@NotNull FluidState fluidState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull Fluid fluid, @NotNull Direction direction) {
        return false;
    }

    @Override
    public int getTickDelay(@NotNull LevelReader levelReader) {
        return 10;
    }

    @Override
    protected float getExplosionResistance() {
        return 0;
    }

    @Override
    protected @NotNull BlockState createLegacyBlock(@NotNull FluidState state) {
        return ModBlocks.CRUDE_OIL_BLOCK.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSource(@NotNull FluidState fluidState) {
        return false;
    }

    // Heavier and slower than water
    @Override
    protected int getSlopeFindDistance(@NotNull LevelReader level) {
        return 2; // shorter spread
    }

    @Override
    protected int getDropOff(@NotNull LevelReader level) {
        return 3;
    }

    @Override
    public int getAmount(@NotNull FluidState fluidState) {
        return 1;
    }

    @Override
    public @NotNull FluidType getFluidType() {
        return ModFluidTypes.CRUDE_OIL_TYPE.get();
    }

    @Override
    protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        builder.add(LEVEL);
    }

    public static class Source extends CrudeOil {
        public Source() { super(); }
        @Override public boolean isSource(@NotNull FluidState state) { return true; }
        @Override public int getAmount(@NotNull FluidState state) { return 8; }

        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return ModFluidTypes.STILL;
                }
                @Override
                public ResourceLocation getFlowingTexture() {
                    return ModFluidTypes.FLOW;
                }
            });}
    }

    public static class Flowing extends CrudeOil {
        public Flowing() { super();}
        @Override public int getAmount(@NotNull FluidState state) { return state.getValue(LEVEL); }
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return ModFluidTypes.STILL;
                }
                @Override
                public ResourceLocation getFlowingTexture() {
                    return ModFluidTypes.FLOW;
                }
            });}
    }
}
