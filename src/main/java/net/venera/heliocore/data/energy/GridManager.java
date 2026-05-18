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
import net.venera.heliocore.block.custom.machine.electric.WireBlock;
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

        // 1. Find all grids this machine is a part of (Catches both Wire Grids & Micro-Grids)
        for (EnergyGrid grid : activeGrids.values()) {
            if (grid.containsMachine(machinePos)) {
                gridsToDestroy.add(grid);
            }
        }

        // 2. Destroy the compromised grids
        for (EnergyGrid grid : gridsToDestroy) {
            destroyGrid(grid);
        }

        // 3. Rebuild networks for any wires that were touching this machine
        // (The machine is gone, but the wires are still floating there and need a new Grid)
        for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
            BlockPos neighborPos = machinePos.relative(dir);

            // If the neighbor is a wire, and we haven't already assigned it a new grid...
            if (!positionToGrid.containsKey(neighborPos) && level.getBlockState(neighborPos).getBlock() instanceof WireBlock) {
                buildGridAt(level, neighborPos);
            }

            // Note: If the neighbor was another machine (Micro-Grid), we do nothing. 
            // It will just sit there waiting for a new connection.
        }
    }

    public static GridManager get(Level level) {
        if (level.isClientSide) {
            throw new IllegalStateException("Attempted to access GridManager on the client side!");
        }

        ServerLevel serverLevel = (ServerLevel) level;

        // The factory tells the game how to instantiate or deserialize your manager
        SavedData.Factory<GridManager> factory = new SavedData.Factory<>(
                GridManager::new,  // Fallback: creates a new one if the file doesn't exist
                GridManager::load, // Loader: reads from NBT if the file DOES exist
                null               // DataFixTypes (null is perfectly fine for custom mod data)
        );

        // Access the world's data storage folder and grab our specific file
        return serverLevel.getDataStorage().computeIfAbsent(factory, DATA_NAME);
    }

    public void onMachinePlaced(Level level, BlockPos machinePos) {

        for (EnergyGrid grid : activeGrids.values()) {
            if (grid.containsMachine(machinePos)) return;
        }
        
        BlockEntity be = level.getBlockEntity(machinePos);
        if (!(be instanceof BaseElectricMachineEntity thisMachine)) return;

        // Look at all 6 adjacent blocks
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = machinePos.relative(dir);
            BlockState neighborState = level.getBlockState(neighborPos);

            // SCENARIO A: It touches a Wire
            if (neighborState.getBlock() instanceof WireBlock) {
                // Treat it like a wire update: rescan that network to include this machine
                onWirePlaced(level, neighborPos);
            }

            // SCENARIO B: It touches another Machine (The Micro-Grid)
            else {
                BlockEntity neighborBe = level.getBlockEntity(neighborPos);
                if (neighborBe instanceof BaseElectricMachineEntity neighborMachine) {

                    Direction faceToNeighbor = dir;
                    Direction faceFromNeighbor = dir.getOpposite();

                    // Check if our Output is facing their Input, or vice versa
                    boolean weAreOutput = thisMachine.isOutputSide(faceToNeighbor);
                    boolean theyAreInput = neighborMachine.isInputSide(faceFromNeighbor);

                    boolean weAreInput = thisMachine.isInputSide(faceToNeighbor);
                    boolean theyAreOutput = neighborMachine.isOutputSide(faceFromNeighbor);

                    if ((weAreOutput && theyAreInput) || (weAreInput && theyAreOutput)) {
                        GridPort port1 = new GridPort(machinePos, faceToNeighbor, weAreOutput ? GridPort.PortType.OUTPUT : GridPort.PortType.INPUT);
                        GridPort port2 = new GridPort(neighborPos, faceFromNeighbor, theyAreOutput ? GridPort.PortType.OUTPUT : GridPort.PortType.INPUT);

                        Set<GridPort> microPorts = Set.of(port1, port2);
                        int newId = recycledIds.isEmpty() ? nextGridId++ : recycledIds.poll();

                        // Pass an empty set for wires, because there are zero wires here.
                        EnergyGrid microGrid = new EnergyGrid(newId, Collections.emptySet(), microPorts, level);
                        activeGrids.put(newId, microGrid);
                        setDirty();
                    }
                }
            }
        }
    }

    /**
     * Triggered when a player places a wire, or a chunk with wires loads.
     */
    public void onWirePlaced(Level level, BlockPos pos) {
        // Since placing a wire might connect two previously separate grids, 
        // the safest and cleanest approach is to destroy any touching grids and rebuild as one.
        Set<EnergyGrid> touchingGrids = findTouchingGrids(pos);
        for (EnergyGrid oldGrid : touchingGrids) {
            destroyGrid(oldGrid);
        }

        // Run the scanner to build the new, unified network
        buildGridAt(level, pos);
    }

    /**
     * Triggered when a player breaks a wire, or a chunk unloads.
     */
    public void onWireBroken(Level level, BlockPos brokenPos) {
        EnergyGrid gridToSplit = positionToGrid.get(brokenPos);
        if (gridToSplit == null) return;

        // 1. Destroy the old massive grid
        destroyGrid(gridToSplit);

        // 2. The broken wire is gone, so we look at its 6 neighbors.
        // If any neighbor is a wire, we start a new scan from there to form the Sub-Networks.
        for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
            BlockPos neighbor = brokenPos.relative(dir);

            // If we haven't already assigned this neighbor to a new grid, and it's a wire...
            if (!positionToGrid.containsKey(neighbor) && level.getBlockState(neighbor).getBlock() instanceof WireBlock) {
                buildGridAt(level, neighbor);
            }
        }
    }

    // --- INTERNAL LOGIC ---

    private void buildGridAt(Level level, BlockPos startPos) {
        // 1. Use our new Scanner!
        GridScanner.ScanResult result = GridScanner.scanNetwork(level, startPos);

        if (result.wires().isEmpty() && result.ports().isEmpty()) return;

        // 2. Mint a new ID
        int newId = recycledIds.isEmpty() ? nextGridId++ : recycledIds.poll();

        // 3. Create the Grid (This automatically builds the distance cache!)
        EnergyGrid newGrid = new EnergyGrid(newId, result.wires(), result.ports(), level);

        // 4. Register it
        activeGrids.put(newId, newGrid);
        for (BlockPos wirePos : result.wires()) {
            positionToGrid.put(wirePos, newGrid);
        }

        setDirty(); // Tell Minecraft to save the changes
    }

    private void destroyGrid(EnergyGrid grid) {
        // 1. Clear the lookup map so blocks don't point to a dead grid
        // We use an iterator or just clear based on the grid's tracked wires
        positionToGrid.values().removeIf(g -> g.id == grid.id);

        // 2. Remove from active execution
        activeGrids.remove(grid.id);

        // 3. Recycle the ID
        recycledIds.add(grid.id);

        setDirty();
    }

    private Set<EnergyGrid> findTouchingGrids(BlockPos pos) {
        Set<EnergyGrid> found = new HashSet<>();
        for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
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
