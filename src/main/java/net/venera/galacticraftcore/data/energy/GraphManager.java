package net.venera.galacticraftcore.data.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.custom.machine.electric.WireBlock;

import java.util.*;

public class GraphManager extends SavedData {
    private static final String DATA_NAME = "galacticraft_energy_graphs";
    
    public static GraphManager get(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(GraphManager::new, GraphManager::load),
                    DATA_NAME
            );
        }
        return null; 
    }
    
    private final Map<BlockPos, EnergyGraph> positionToGraph = new HashMap<>();
    private final Map<Integer, EnergyGraph> graphs = new HashMap<>();
    
    private int nextGraphId = 0;
    private final PriorityQueue<Integer> recycledIds = new PriorityQueue<>();

    public GraphManager() {}
    
    public void onWirePlaced(Level level, BlockPos pos) {
        rebuildGraphAt(level, pos);
    }
    
    public void onWireBroken(Level level, BlockPos pos) {
        EnergyGraph existingGraph = positionToGraph.get(pos);
        
        if (existingGraph == null) return;
        
        destroyGraph(existingGraph);
        
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            
            if (level.getBlockState(neighborPos).getBlock() instanceof WireBlock) {
                if (!positionToGraph.containsKey(neighborPos)) {
                    rebuildGraphAt(level, neighborPos);
                }
            }
        }
    }

    private void rebuildGraphAt(Level level, BlockPos pos) {
        Set<EnergyGraph> graphsToMerge = new HashSet<>();

        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            EnergyGraph existingGraph = positionToGraph.get(neighborPos);

            if (existingGraph != null) {
                graphsToMerge.add(existingGraph);
            }
        }
        
        for (EnergyGraph oldGraph : graphsToMerge) {
            destroyGraph(oldGraph);
        }

        int newId;
        if (!recycledIds.isEmpty()) {
            newId = recycledIds.poll(); 
        } else {
            newId = nextGraphId++;
        }
        
        EnergyGraph newGraph = GraphBuilder.build(level, pos, newId);
        if (newGraph.getAllPositions().isEmpty()) {
            recycledIds.add(newId); 
            return;
        }
        
        graphs.put(newId, newGraph);

        for (BlockPos wirePos : newGraph.getAllPositions()) {
            positionToGraph.put(wirePos, newGraph);
        }
        setDirty();
    }

    private void destroyGraph(EnergyGraph graph) {
        for (BlockPos wirePos : graph.getAllPositions()) {
            positionToGraph.remove(wirePos);
        }
        graphs.remove(graph.id);
        recycledIds.add(graph.id);
        setDirty();
    }

    public int distributeEnergy(Level level, BlockPos wirePos, int amount) {
        EnergyGraph graph = positionToGraph.get(wirePos);
        
        if (graph == null) {
            if (level.getBlockState(wirePos).getBlock() instanceof WireBlock) {
                rebuildGraphAt(level, wirePos);
                graph = positionToGraph.get(wirePos);
            }
        }
        
        if (graph == null) return 0;
        
        return graph.distribute(level, wirePos, amount);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, net.minecraft.core.HolderLookup.Provider provider) {
        compoundTag.putInt("nextGraphId", nextGraphId);
        return compoundTag;
    }

    public static GraphManager load(CompoundTag compoundTag, net.minecraft.core.HolderLookup.Provider provider) {
        GraphManager manager = new GraphManager();
        manager.nextGraphId = compoundTag.getInt("nextGraphId");
        return manager;
    }
}