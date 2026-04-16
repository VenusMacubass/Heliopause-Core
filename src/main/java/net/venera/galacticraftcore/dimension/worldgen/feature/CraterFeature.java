package net.venera.galacticraftcore.dimension.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.venera.galacticraftcore.block.ModBlocks;

public class CraterFeature extends Feature<NoneFeatureConfiguration> {
    public CraterFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        if (level.isEmptyBlock(origin.below())) {
            return false;
        }

        int radius = random.nextInt(15) + 5; // crater size
        double squish = 2.0; // how flat the crater is
        boolean placedAny = false;

        // We only iterate X and Z (a flat 2D grid covering the crater)
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {

                int worldX = origin.getX() + x;
                int worldZ = origin.getZ() + z;

                // Get the exact height of the natural terrain at this specific column
                // (WORLD_SURFACE_WG returns the block right above the surface, so we subtract 1)
                int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, worldX, worldZ) - 1;

                double distance2D = Math.sqrt(x * x + z * z);

                // If we are outside the circle, skip to the next column
                if (distance2D > radius) {
                    continue;
                }

                // 1. Calculate the mathematical depth of the bowl here
                // Formula: Y = -sqrt(R^2 - X^2 - Z^2) / squish
                double bowlDepth = -Math.sqrt(radius * radius - distance2D * distance2D) / squish;

                // This is where the floor of the crater *wants* to be
                int floorY = origin.getY() + (int) Math.round(bowlDepth);

                // 2. THE CRITICAL FIX: Clamping
                // If floorY is higher than surfaceY (meaning we are on the downhill side
                // and the bowl is about to stick out into the air), we force it to stay 
                // flush with the natural surface terrain.
                int actualFloorY = Math.min(floorY, surfaceY);

                // 3. Paint the crust!
                BlockPos turfPos = new BlockPos(worldX, actualFloorY, worldZ);
                BlockPos dirtPos = new BlockPos(worldX, actualFloorY - 1, worldZ);
                BlockPos rockPos = new BlockPos(worldX, actualFloorY - 2, worldZ);

                // Make sure we don't accidentally delete the bottom of the world
                if (!level.isStateAtPosition(turfPos, state -> state.getBlock() == Blocks.BEDROCK)) {
                    level.setBlock(turfPos, ModBlocks.MOON_TURF.get().defaultBlockState(), 2);
                    level.setBlock(dirtPos, ModBlocks.MOON_DIRT.get().defaultBlockState(), 2);
                    level.setBlock(rockPos, ModBlocks.MOON_ROCK.get().defaultBlockState(), 2);
                    placedAny = true;
                }

                // 4. Fill any hollow caves directly under the new crust so the crater doesn't break into a cavern
                for (int y = actualFloorY - 3; y >= actualFloorY - 5; y--) {
                    BlockPos cavePos = new BlockPos(worldX, y, worldZ);
                    if (level.isEmptyBlock(cavePos)) {
                        level.setBlock(cavePos, ModBlocks.MOON_ROCK.get().defaultBlockState(), 2);
                    }
                }

                // 5. Vaporize the sky! (This fixes the floating mountain bug)
                // We clear everything from just above our new floor, all the way up through the original mountain
                for (int y = actualFloorY + 1; y <= surfaceY + 2; y++) {
                    BlockPos airPos = new BlockPos(worldX, y, worldZ);
                    if (!level.isEmptyBlock(airPos)) {
                        level.setBlock(airPos, Blocks.AIR.defaultBlockState(), 2);
                        placedAny = true;
                    }
                }
            }
        }
        return placedAny;
    }
}
