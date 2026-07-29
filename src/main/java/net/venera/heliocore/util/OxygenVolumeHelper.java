package net.venera.heliocore.util;

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.heliocore.block.HpCBlocks;
import net.venera.heliocore.block.entity.machine.electric.OxygenSealerEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OxygenVolumeHelper {
    private static final Map<BlockPos, SealedVolumeResult> ACTIVE_ROOMS = new ConcurrentHashMap<>();
    public record SealedVolumeResult(LongOpenHashSet airBlocks, LongOpenHashSet wallBlocks, long lastScanTick) {}

    public static SealedVolumeResult scanAndRegisterRoom(Level level, BlockPos sealerPos, int maxVolume) {
        ACTIVE_ROOMS.remove(sealerPos);
        LongOpenHashSet visitedAir = new LongOpenHashSet();
        LongOpenHashSet walls = new LongOpenHashSet();
        LongArrayFIFOQueue queue = new LongArrayFIFOQueue();

        long start = sealerPos.above().asLong();
        queue.enqueue(start);
        visitedAir.add(start);

        while (!queue.isEmpty()) {
            long currentLong = queue.dequeueLong();
            BlockPos currentPos = BlockPos.of(currentLong);

            if (visitedAir.size() > maxVolume) return null;

            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(dir);
                long neighborLong = neighborPos.asLong();

                if (!visitedAir.contains(neighborLong)) {
                    BlockState state = level.getBlockState(neighborPos);

                    boolean isAirtight = state.isCollisionShapeFullBlock(level, neighborPos)
                            || state.getBlock() instanceof net.venera.heliocore.block.hpc_custom.machine.BaseMachineBlock
                            || state.is(HpCBlocks.AIRLOCK_GENERATED_BLOCK.get())
                            || state.is(HpCBlocks.AIRLOCK_FRAME.get())
                            || state.is(HpCBlocks.AIRLOCK_FRAME_SWITCH.get());
                    if (!isAirtight) {
                        if (isPositionSealed(neighborLong)) {
                            walls.add(neighborLong);
                        } else {
                            visitedAir.add(neighborLong);
                            queue.enqueue(neighborLong);
                        }
                    } else {
                        walls.add(neighborLong);
                    }
                }
            }
        }

        SealedVolumeResult result = new SealedVolumeResult(visitedAir, walls, level.getGameTime());
        ACTIVE_ROOMS.put(sealerPos, result);
        return result;
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
    
    public static boolean isPerimeterSafe(Level level, BlockPos sealerPos) {
        SealedVolumeResult room = ACTIVE_ROOMS.get(sealerPos);
        if (room == null) return false;

        for (long wallLong : room.wallBlocks()) {
            BlockPos wallPos = BlockPos.of(wallLong);
            BlockState state = level.getBlockState(wallPos);

            boolean isAirtight = state.isCollisionShapeFullBlock(level, wallPos)
                    || state.getBlock() instanceof net.venera.heliocore.block.hpc_custom.machine.BaseMachineBlock
                    || state.is(HpCBlocks.AIRLOCK_GENERATED_BLOCK.get())
                    || state.is(HpCBlocks.AIRLOCK_FRAME.get())
                    || state.is(HpCBlocks.AIRLOCK_FRAME_SWITCH.get());
            if (!isAirtight && !isPositionSealed(wallLong)) {
                return false; 
            }
        }
        return true;
    }

    public static SealedVolumeResult getExistingRoom(BlockPos pos) {
        return ACTIVE_ROOMS.get(pos);
    }

    public static BlockPos getSealerForWall(long wallPosLong) {
        for (Map.Entry<BlockPos, SealedVolumeResult> entry : ACTIVE_ROOMS.entrySet()) {
            if (entry.getValue().wallBlocks().contains(wallPosLong)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static BlockPos getSealerForAir(long airPosLong) {
        for (Map.Entry<BlockPos, SealedVolumeResult> entry : ACTIVE_ROOMS.entrySet()) {
            if (entry.getValue().airBlocks().contains(airPosLong)) {
                return entry.getKey();
            }
        }
        return null;
    }
}


