package net.venera.galacticraftcore.fluid.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.ModBlocks;
import net.venera.galacticraftcore.item.ModItems;

public class CrudeOil {
    public static final ResourceLocation STILL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "oil_still");
    public static final ResourceLocation FLOW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GalacticraftCore.MOD_ID, "oil_flow");

    public static final FluidType FLUID_TYPE = new CrudeOilFluidType(
            FluidType.Properties.create()
                    .canConvertToSource(false)
                    .canDrown(true)
                    .canExtinguish(false)
                    .canHydrate(false)
                    .canPushEntity(true)
                    .canSwim(false)
                    .fallDistanceModifier(0.5F)
                    .motionScale(0.002D)
                    .viscosity(3000)
                    .density(1200),
            STILL_TEXTURE,
            FLOW_TEXTURE,
            0xFF2F2F2F
    );

    public static final FlowingFluid SOURCE = new Source();
    public static final FlowingFluid FLOWING = new Flowing();

    public static class Source extends FlowingFluid{

        public FluidType getFluidType(){

            return FLUID_TYPE;
        }

        @Override
        public Fluid getFlowing() {
            return FLOWING;
        }

        @Override
        public Fluid getSource() {
            return SOURCE;
        }

        @Override
        protected boolean canConvertToSource(Level level) {
            return false;
        }

        @Override
        protected void beforeDestroyingBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {

        }

        @Override
        protected int getSlopeFindDistance(LevelReader levelReader) {
            return 3;
        }

        @Override
        protected int getDropOff(LevelReader levelReader) {
            return 3;
        }

        @Override
        public Item getBucket() {
            return ModItems.CRUDE_OIL_BUCKET.get();
        }

        @Override
        protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
            return false;
        }

        @Override
        public int getTickDelay(LevelReader levelReader) {
            return 20;
        }

        @Override
        protected float getExplosionResistance() {
            return 2;
        }

        @Override
        protected BlockState createLegacyBlock(FluidState fluidState) {
            return ModBlocks.CRUDE_OIL.get().defaultBlockState();
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return true;
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return FLUID_STATE_REGISTRY.getId(fluidState);
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
    }


    public static class Flowing extends FlowingFluid{

        public FluidType getFluidType(){
            return FLUID_TYPE;
        }

        @Override
        public Fluid getFlowing() {
            return null;
        }

        @Override
        public Fluid getSource() {
            return null;
        }

        @Override
        protected boolean canConvertToSource(Level level) {
            return false;
        }

        @Override
        protected void beforeDestroyingBlock(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {

        }

        @Override
        protected int getSlopeFindDistance(LevelReader levelReader) {
            return 0;
        }

        @Override
        protected int getDropOff(LevelReader levelReader) {
            return 0;
        }

        @Override
        public Item getBucket() {
            return null;
        }

        @Override
        protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos, Fluid fluid, Direction direction) {
            return false;
        }

        @Override
        public int getTickDelay(LevelReader levelReader) {
            return 0;
        }

        @Override
        protected float getExplosionResistance() {
            return 0;
        }

        @Override
        protected BlockState createLegacyBlock(FluidState fluidState) {
            return null;
        }

        @Override
        public boolean isSource(FluidState fluidState) {
            return false;
        }

        @Override
        public int getAmount(FluidState fluidState) {
            return 0;
        }
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
    }
}
