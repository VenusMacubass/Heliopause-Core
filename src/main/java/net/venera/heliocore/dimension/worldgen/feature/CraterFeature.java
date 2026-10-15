package net.venera.heliocore.dimension.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.venera.heliocore.block.HpCBlocks;

public class CraterFeature extends Feature<CraterConfig> {
    public CraterFeature(Codec<CraterConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<CraterConfig> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        CraterConfig config = context.config();
        
        StructureManager structureManager = level.getLevel().structureManager();
        int maxRadius = config.maxRadius();
        
        BlockPos[] checkPoints = {
                origin,
                origin.offset(maxRadius, 0, 0),
                origin.offset(-maxRadius, 0, 0),
                origin.offset(0, 0, maxRadius),
                origin.offset(0, 0, -maxRadius)
        };

        for (BlockPos pos : checkPoints) {
            if (structureManager.hasAnyStructureAt(pos)) {
                return false;
            }
        }

        if (level.isEmptyBlock(origin.below())) {
            return false;
        }

        int radius = random.nextInt(config.maxRadius() - config.minRadius() + 1) + config.minRadius();
        double squish = config.squish();
        boolean placedAny = false;

        int originChunkX = origin.getX() >> 4;
        int originChunkZ = origin.getZ() >> 4;

        // 1. True solid surface Y at the center (-1 instead of -2)
        int centerY = origin.getY() - 1;

        // 2. Cavity & Cliff Rejection Check:
        // Sample 8 points around the crater perimeter (N, S, E, W, and diagonals)
        int diag = (int) Math.round(radius * 0.707D);
        int[][] sampleOffsets = {
                {radius, 0}, {-radius, 0}, {0, radius}, {0, -radius},
                {diag, diag}, {diag, -diag}, {-diag, diag}, {-diag, -diag}
        };

        int minSampleY = centerY;
        int maxSampleY = centerY;
        int sumRimY = 0;
        int validSamples = 0;

        for (int[] offset : sampleOffsets) {
            int sx = origin.getX() + offset[0];
            int sz = origin.getZ() + offset[1];
            if (Math.abs(originChunkX - (sx >> 4)) > 1 || Math.abs(originChunkZ - (sz >> 4)) > 1) {
                continue;
            }
            int sy = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, sx, sz) - 1;
            minSampleY = Math.min(minSampleY, sy);
            maxSampleY = Math.max(maxSampleY, sy);
            sumRimY += sy;
            validSamples++;
        }

        if (validSamples > 0) {
            int avgRimY = sumRimY / validSamples;
            if (centerY < avgRimY - 4) {
                return false;
            }
        }

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {

                int worldX = origin.getX() + x;
                int worldZ = origin.getZ() + z;

                int targetChunkX = worldX >> 4;
                int targetChunkZ = worldZ >> 4;

                if (Math.abs(originChunkX - targetChunkX) > 1 || Math.abs(originChunkZ - targetChunkZ) > 1) {
                    continue;
                }

                double distance2D = Math.sqrt(x * x + z * z);
                if (distance2D > radius) {
                    continue;
                }

                // 3. Get true top solid block (-1)
                int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, worldX, worldZ) - 1;
                int maxCliffSlice = 4;
                int minRefY = centerY - 3;
                int maxRefY = Math.max(centerY, surfaceY - maxCliffSlice);
                int clampedSurfaceY = Math.clamp(centerY, minRefY, maxRefY);
                // Clamp reference surfaceY so a small 1-block cave hole inside the radius doesn't pull the bowl down
                double normalizedDist = distance2D / (double) radius;
                double referenceY = (centerY * (1.0D - normalizedDist)) + (clampedSurfaceY * normalizedDist);
                
                double bowlDepth = -Math.sqrt(radius * radius - distance2D * distance2D) / squish;
                int floorY = (int) Math.round(referenceY + bowlDepth);
                int actualFloorY = Math.min(floorY, surfaceY);

                // Skip outer rim columns that don't actually carve below the surface
                if (actualFloorY >= surfaceY) {
                    continue;
                }

                BlockPos turfPos = new BlockPos(worldX, actualFloorY, worldZ);
                BlockPos dirtPos = new BlockPos(worldX, actualFloorY - 1, worldZ);
                BlockPos rockPos = new BlockPos(worldX, actualFloorY - 2, worldZ);

                if (!level.isStateAtPosition(turfPos, state -> state.getBlock() == Blocks.BEDROCK)) {
                    if (random.nextFloat() < 0.2F) {
                        if (random.nextFloat() < 0.15F) {
                            level.setBlock(turfPos, HpCBlocks.MOON_TEKTITES_REGOLITH.get().defaultBlockState(), 2);
                        } else {
                            level.setBlock(turfPos, HpCBlocks.MOON_REGOLITH.get().defaultBlockState(), 2);
                        }
                        level.setBlock(dirtPos, HpCBlocks.MOON_TEKTITES.get().defaultBlockState(), 2);

                    } else {
                        level.setBlock(turfPos, HpCBlocks.MOON_REGOLITH.get().defaultBlockState(), 2);
                        level.setBlock(dirtPos, HpCBlocks.MOON_DIRT.get().defaultBlockState(), 2);
                    }

                    level.setBlock(rockPos, HpCBlocks.MOON_ROCK.get().defaultBlockState(), 2);
                    placedAny = true;
                }

                for (int y = actualFloorY - 3; y >= actualFloorY - 5; y--) {
                    BlockPos cavePos = new BlockPos(worldX, y, worldZ);
                    if (level.isEmptyBlock(cavePos)) {
                        level.setBlock(cavePos, HpCBlocks.MOON_ROCK.get().defaultBlockState(), 2);
                    }
                }

                for (int y = actualFloorY + 1; y <= surfaceY; y++) {
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
