package net.venera.heliocore.data.atmospherics;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.venera.heliocore.block.HpCBlocks;

public class FireSourceAtmosphericHandler {
    public static void onRoomSealed(Level level, LongOpenHashSet airBlocks) {
        for (long posLong : airBlocks) {
            BlockPos pos = BlockPos.of(posLong);
            BlockState state = level.getBlockState(pos);
            //Extinguished Torch -> Torch
            if (state.is(HpCBlocks.EXTINGUISHED_TORCH.get())) {
                level.setBlockAndUpdate(pos, Blocks.TORCH.defaultBlockState());
            }
            //Extinguished Wall Torch -> Wall Torch
            else if (state.is(HpCBlocks.EXTINGUISHED_WALL_TORCH.get())) {
                level.setBlockAndUpdate(pos, Blocks.WALL_TORCH.defaultBlockState()
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
            }
            //Extinguished Lantern & Extinguished Soul Lantern -> Regular Lantern
            else if (state.is(HpCBlocks.EXTINGUISHED_LANTERN.get()) || state.is(HpCBlocks.EXTINGUISHED_SOUL_LANTERN.get())) {
                level.setBlockAndUpdate(pos, Blocks.LANTERN.defaultBlockState()
                        .setValue(BlockStateProperties.HANGING, state.getValue(BlockStateProperties.HANGING))
                        .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)));
            }
        }
    }
    
    public static void onRoomDepressurized(Level level, LongOpenHashSet airBlocks) {
        for (long posLong : airBlocks) {
            BlockPos pos = BlockPos.of(posLong);
            BlockState state = level.getBlockState(pos);

            //Torch & Soul Torch -> Extinguished Torch
            if (state.is(Blocks.TORCH) || state.is(Blocks.SOUL_TORCH)) {
                level.setBlockAndUpdate(pos, HpCBlocks.EXTINGUISHED_TORCH.get().defaultBlockState());
            }
            //Wall Torch & Soul Wall Torch -> Extinguished Wall Torch
            else if (state.is(Blocks.WALL_TORCH) || state.is(Blocks.SOUL_WALL_TORCH)) {
                level.setBlockAndUpdate(pos, HpCBlocks.EXTINGUISHED_WALL_TORCH.get().defaultBlockState()
                        .setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
            }
            //Lantern -> Extinguished Lantern
            else if (state.is(Blocks.LANTERN)) {
                level.setBlockAndUpdate(pos, HpCBlocks.EXTINGUISHED_LANTERN.get().defaultBlockState()
                        .setValue(BlockStateProperties.HANGING, state.getValue(BlockStateProperties.HANGING))
                        .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)));
            }
            //Soul Lantern -> Extinguished Soul Lantern
            else if (state.is(Blocks.SOUL_LANTERN)) {
                level.setBlockAndUpdate(pos, HpCBlocks.EXTINGUISHED_SOUL_LANTERN.get().defaultBlockState()
                        .setValue(BlockStateProperties.HANGING, state.getValue(BlockStateProperties.HANGING))
                        .setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED)));
            }
            //Campfires -> unlit
            else if (state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE)) {
                if (state.getValue(BlockStateProperties.LIT)) {
                    level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.LIT, false));
                }
            }
        }
    }
}
