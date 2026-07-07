package net.venera.heliocore.data.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.venera.heliocore.block.hpc_custom.machine.electric.WireBlock;
import net.venera.heliocore.block.entity.machine.electric.BaseElectricMachineEntity;

import java.util.*;

public class GridManager extends SavedData {
    private static final String DATA_NAME = "heliopause_energy_grids";

    private final Map<Integer, EnergyGrid> activeGrids = new HashMap<>();
    private final Map<BlockPos, EnergyGrid> positionToGrid = new HashMap<>();
    
    private int nextGridId = 0;
    private final PriorityQueue<Integer> recycledIds = new PriorityQueue<>();
    
    public void tickAll(Level level) {
        for (EnergyGrid grid : activeGrids.values()) {
            grid.tickEnergy(level);
        }
    }
    
    public void onMachineBroken(Level level, BlockPos machinePos) {
        Set<EnergyGrid> gridsToDestroy = new HashSet<>();
        for (EnergyGrid grid : activeGrids.values()) {
            if (grid.containsMachine(machinePos)) {
                gridsToDestroy.add(grid);
            }
        }
        
        for (EnergyGrid grid : gridsToDestroy) {
            destroyGrid(grid);
        }
        
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = machinePos.relative(dir);
            
            if (!positionToGrid.containsKey(neighborPos) && level.getBlockState(neighborPos).getBlock() instanceof WireBlock) {
                buildGridAt(level, neighborPos);
            }
        }
    }

    public static GridManager get(Level level) {
        if (level.isClientSide) {
            throw new IllegalStateException("Attempted to access GridManager on the client side!");
        }

        ServerLevel serverLevel = (ServerLevel) level;
        
        SavedData.Factory<GridManager> factory = new SavedData.Factory<>(
                GridManager::new,  
                GridManager::load, 
                null               
        );
        
        return serverLevel.getDataStorage().computeIfAbsent(factory, DATA_NAME);
    }

    public void onMachinePlaced(Level level, BlockPos machinePos) {

        for (EnergyGrid grid : activeGrids.values()) {
            if (grid.containsMachine(machinePos)) return;
        }
        
        BlockEntity be = level.getBlockEntity(machinePos);
        if (!(be instanceof BaseElectricMachineEntity thisMachine)) return;
        
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = machinePos.relative(dir);
            BlockState neighborState = level.getBlockState(neighborPos);
            
            if (neighborState.getBlock() instanceof WireBlock) {
                onWirePlaced(level, neighborPos);
            }
            
            else {
                BlockEntity neighborBe = level.getBlockEntity(neighborPos);
                if (neighborBe instanceof BaseElectricMachineEntity neighborMachine) {

                    Direction faceToNeighbor = dir;
                    Direction faceFromNeighbor = dir.getOpposite();
                    
                    boolean weAreOutput = thisMachine.isOutputSide(faceToNeighbor);
                    boolean theyAreInput = neighborMachine.isInputSide(faceFromNeighbor);

                    boolean weAreInput = thisMachine.isInputSide(faceToNeighbor);
                    boolean theyAreOutput = neighborMachine.isOutputSide(faceFromNeighbor);

                    if ((weAreOutput && theyAreInput) || (weAreInput && theyAreOutput)) {
                        GridPort port1 = new GridPort(machinePos, faceToNeighbor, weAreOutput ? GridPort.PortType.OUTPUT : GridPort.PortType.INPUT);
                        GridPort port2 = new GridPort(neighborPos, faceFromNeighbor, theyAreOutput ? GridPort.PortType.OUTPUT : GridPort.PortType.INPUT);

                        Set<GridPort> microPorts = Set.of(port1, port2);
                        int newId = recycledIds.isEmpty() ? nextGridId++ : recycledIds.poll();
                        
                        EnergyGrid microGrid = new EnergyGrid(newId, Collections.emptySet(), microPorts, level);
                        activeGrids.put(newId, microGrid);
                        setDirty();
                    }
                }
            }
        }
    }
    
    public void onWirePlaced(Level level, BlockPos pos) {
        Set<EnergyGrid> touchingGrids = findTouchingGrids(pos);
        for (EnergyGrid oldGrid : touchingGrids) {
            destroyGrid(oldGrid);
        }
        
        buildGridAt(level, pos);
    }
    
    public void onWireBroken(Level level, BlockPos brokenPos) {
        EnergyGrid gridToSplit = positionToGrid.get(brokenPos);
        if (gridToSplit == null) return;
        destroyGrid(gridToSplit);
        
        for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
            BlockPos neighbor = brokenPos.relative(dir);
            
            if (!positionToGrid.containsKey(neighbor) && level.getBlockState(neighbor).getBlock() instanceof WireBlock) {
                buildGridAt(level, neighbor);
            }
        }
    }

    private void buildGridAt(Level level, BlockPos startPos) {
        GridScanner.ScanResult result = GridScanner.scanNetwork(level, startPos);

        if (result.wires().isEmpty() && result.ports().isEmpty()) return;
        int newId = recycledIds.isEmpty() ? nextGridId++ : recycledIds.poll();
        
        EnergyGrid newGrid = new EnergyGrid(newId, result.wires(), result.ports(), level);
        
        activeGrids.put(newId, newGrid);
        for (BlockPos wirePos : result.wires()) {
            positionToGrid.put(wirePos, newGrid);
        }

        setDirty();
    }

    private void destroyGrid(EnergyGrid grid) {
        positionToGrid.values().removeIf(g -> g.id == grid.id);
        
        activeGrids.remove(grid.id);
        recycledIds.add(grid.id);
        setDirty();
    }

    private Set<EnergyGrid> findTouchingGrids(BlockPos pos) {
        Set<EnergyGrid> found = new HashSet<>();
        for (Direction dir : Direction.values()) {
            EnergyGrid grid = positionToGrid.get(pos.relative(dir));
            if (grid != null) found.add(grid);
        }
        return found;
    }
    
    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("NextGridId", nextGridId);
        return tag;
    }

    public static GridManager load(CompoundTag tag, HolderLookup.Provider registries) {
        GridManager manager = new GridManager();
        manager.nextGridId = tag.getInt("NextGridId");
        return manager;
    }
}
