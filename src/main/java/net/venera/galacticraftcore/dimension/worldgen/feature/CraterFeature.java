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
import net.venera.galacticraftcore.dimension.biome.ModBiomes;

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

        if (level.isEmptyBlock(origin.below())) {
            return false;
        }

        // 3. Replaced the biome checks with the safe config variables
        int radius = random.nextInt(config.maxRadius() - config.minRadius() + 1) + config.minRadius();
        double squish = config.squish();
        boolean placedAny = false;
        
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {

                int worldX = origin.getX() + x;
                int worldZ = origin.getZ() + z;
                
                int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, worldX, worldZ) - 2;

                double distance2D = Math.sqrt(x * x + z * z);
                
                if (distance2D > radius) {
                    continue;
                }
                
                double bowlDepth = -Math.sqrt(radius * radius - distance2D * distance2D) / squish;
                
                int floorY = origin.getY() + (int) Math.round(bowlDepth);
                
                int actualFloorY = Math.min(floorY, surfaceY);
                
                BlockPos turfPos = new BlockPos(worldX, actualFloorY, worldZ);
                BlockPos dirtPos = new BlockPos(worldX, actualFloorY - 1, worldZ);
                BlockPos rockPos = new BlockPos(worldX, actualFloorY - 2, worldZ);
                
                if (!level.isStateAtPosition(turfPos, state -> state.getBlock() == Blocks.BEDROCK)) {
                    level.setBlock(turfPos, ModBlocks.MOON_TURF.get().defaultBlockState(), 2);
                    level.setBlock(dirtPos, ModBlocks.MOON_DIRT.get().defaultBlockState(), 2);
                    level.setBlock(rockPos, ModBlocks.MOON_ROCK.get().defaultBlockState(), 2);
                    placedAny = true;
                }
                
                for (int y = actualFloorY - 3; y >= actualFloorY - 5; y--) {
                    BlockPos cavePos = new BlockPos(worldX, y, worldZ);
                    if (level.isEmptyBlock(cavePos)) {
                        level.setBlock(cavePos, ModBlocks.MOON_ROCK.get().defaultBlockState(), 2);
                    }
                }
                
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
