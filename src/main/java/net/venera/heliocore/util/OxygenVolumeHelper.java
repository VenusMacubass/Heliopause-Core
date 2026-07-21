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
    
    public record SealedVolumeResult(LongOpenHashSet airBlocks, LongOpenHashSet wallBlocks, Set<BlockPos> activeSealers, long lastScanTick) {}
    
    public static SealedVolumeResult scanAndRegisterRoom(Level level, BlockPos sealerPos, int maxVolumePerSealer) {
        LongOpenHashSet visitedAir = new LongOpenHashSet();
        LongOpenHashSet walls = new LongOpenHashSet();
        LongArrayFIFOQueue queue = new LongArrayFIFOQueue();
        
        Set<BlockPos> connectedSealers = new HashSet<>();
        connectedSealers.add(sealerPos);
        int currentMaxVolume = maxVolumePerSealer;

        long start = sealerPos.above().asLong();
        queue.enqueue(start);
        visitedAir.add(start);

        while (!queue.isEmpty()) {
            long currentLong = queue.dequeueLong();
            BlockPos currentPos = BlockPos.of(currentLong);
            
            
            if (visitedAir.size() > currentMaxVolume) {
                for (BlockPos pos : connectedSealers) ACTIVE_ROOMS.remove(pos);
                return null;
            }

            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(dir);
                long neighborLong = neighborPos.asLong();

                if (!visitedAir.contains(neighborLong)) {
                    BlockState state = level.getBlockState(neighborPos);

                    boolean isAirtight = state.isCollisionShapeFullBlock(level, neighborPos)
                            || state.getBlock() instanceof net.venera.heliocore.block.hpc_custom.machine.BaseMachineBlock
                            || state.is(HpCBlocks.AIRLOCK_GENERATED_BLOCK.get())
                            || state.is(HpCBlocks.AIRLOCK_FRAME_BLOCK.get())
                            || state.is(HpCBlocks.AIRLOCK_FRAME_SWITCH_BLOCK.get());

                    if (!isAirtight) {
                        visitedAir.add(neighborLong);
                        queue.enqueue(neighborLong);
                    } else {
                        walls.add(neighborLong);
                        if (level.getBlockEntity(neighborPos) instanceof OxygenSealerEntity partner) {
                            if (partner.enabled && connectedSealers.add(neighborPos)) {
                                currentMaxVolume += maxVolumePerSealer;
                            }
                        }
                    }
                }
            }
        }

        SealedVolumeResult result = new SealedVolumeResult(visitedAir, walls, connectedSealers, level.getGameTime());
        
        for (BlockPos partnerPos : connectedSealers) {
            ACTIVE_ROOMS.put(partnerPos, result);
        }
        return result;
    }

    public static void removeRoom(BlockPos sealerPos) {
        SealedVolumeResult room = ACTIVE_ROOMS.get(sealerPos);
        if (room != null) {
            for (BlockPos partnerPos : room.activeSealers()) {
                ACTIVE_ROOMS.remove(partnerPos);
            }
        }
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
                return entry.getKey(); //Returns the coordinates of the sealers
            }
        }
        return null;
    }

    public static SealedVolumeResult getExistingRoom(BlockPos pos) {
        return ACTIVE_ROOMS.get(pos);
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


