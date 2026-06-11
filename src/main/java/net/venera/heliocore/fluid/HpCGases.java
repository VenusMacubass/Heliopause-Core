package net.venera.heliocore.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Supplier;

public class HpCGases extends Fluid {
    private final Supplier<FluidType> type;

    public HpCGases(Supplier<FluidType> type) {
        this.type = type;
    }
    
    @Override
    public FluidType getFluidType() {
        return type.get();
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public Item getBucket() {
        return Items.AIR;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    protected Vec3 getFlow(BlockGetter blockGetter, BlockPos blockPos, FluidState fluidState) {
        return Vec3.ZERO;
    }

    @Override
    public int getTickDelay(LevelReader level) {
        return 0;
    }

    @Override
    protected float getExplosionResistance() {
        return 0.0F;
    }

    @Override
    public float getHeight(FluidState fluidState, BlockGetter blockGetter, BlockPos blockPos) {
        return 0;
    }

    @Override
    public float getOwnHeight(FluidState fluidState) {
        return 0;
    }

    @Override
    public boolean isSource(FluidState state) {
        return true;
    }

    @Override
    public int getAmount(FluidState state) {
        return 8;
    }

    @Override
    public VoxelShape getShape(FluidState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }
}
