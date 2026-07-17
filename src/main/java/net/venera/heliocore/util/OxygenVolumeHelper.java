package net.venera.heliocore.util;

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OxygenVolumeHelper {
    private static final Map<BlockPos, SealedVolumeResult> ACTIVE_ROOMS = new ConcurrentHashMap<>();
    public record SealedVolumeResult(LongOpenHashSet airBlocks, LongOpenHashSet wallBlocks) {}
    public static SealedVolumeResult scanAndRegisterRoom(Level level, BlockPos sealerPos, int maxVolume) {
        LongOpenHashSet visitedAir = new LongOpenHashSet();
        LongOpenHashSet walls = new LongOpenHashSet();
        LongArrayFIFOQueue queue = new LongArrayFIFOQueue();

        long start = sealerPos.above().asLong();
        queue.enqueue(start);
        visitedAir.add(start);

        while (!queue.isEmpty()) {
            long currentLong = queue.dequeueLong();
            BlockPos currentPos = BlockPos.of(currentLong);

            if (visitedAir.size() > maxVolume) {
                ACTIVE_ROOMS.remove(sealerPos);
                return null;
            }

            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(dir);
                long neighborLong = neighborPos.asLong();

                if (!visitedAir.contains(neighborLong)) {
                    BlockState state = level.getBlockState(neighborPos);

                    if (!state.isCollisionShapeFullBlock(level, neighborPos)) {
                        visitedAir.add(neighborLong);
                        queue.enqueue(neighborLong);
                    } else {
                        walls.add(neighborLong);
                    }
                }
            }
        }

        
        ACTIVE_ROOMS.put(sealerPos, new SealedVolumeResult(visitedAir, walls));
        return new SealedVolumeResult(visitedAir, walls);
    }
    
    public static void removeRoom(BlockPos sealerPos) {
        ACTIVE_ROOMS.remove(sealerPos);
    }
    public static boolean isPositionSealed(long targetPosLong) {
        for (SealedVolumeResult room : ACTIVE_ROOMS.values()) {
            if (room.airBlocks().contains(targetPosLong)) {
                return true;
            }
        }
        return false;
    }

    public static BlockPos getSealerForWall(long wallPosLong) {
        for (Map.Entry<BlockPos, SealedVolumeResult> entry : ACTIVE_ROOMS.entrySet()) {
            if (entry.getValue().wallBlocks().contains(wallPosLong)) {
                return entry.getKey(); // Returns the coordinates of the Sealer
            }
        }
        return null;
    }
}
