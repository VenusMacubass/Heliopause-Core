package net.venera.galacticraftcore.data.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.venera.galacticraftcore.GalacticraftCore;
import net.venera.galacticraftcore.block.custom.machine.electric.WireBlock;
import net.venera.galacticraftcore.block.entity.machine.electric.BaseElectricMachineEntity;
import java.util.*;

public class GraphBuilder {

    public static EnergyGraph build(Level level, BlockPos startNode, int graphId) {
        EnergyGraph graph = new EnergyGraph(graphId);

        Set<BlockPos> allWires = findAllWires(level, startNode);

        if (allWires.isEmpty()) return graph;

        for (BlockPos pos : allWires) {
            graph.addWirePosition(pos);
        }

        Map<BlockPos, GraphNode> nodeMap = new HashMap<>();

        // 1. Create Nodes
        for (BlockPos pos : allWires) {
            GraphNode node = analyzeAndCreateNode(level, pos);
            if (node != null) {
                nodeMap.put(pos, node);
                graph.addNode(node);
            }
        }

        // 2. Connect Nodes
        for (GraphNode node : nodeMap.values()) {
            findConnections(level, node, allWires, nodeMap);
        }

        graph.recalculatePhysics();
        return graph;
    }

    private static GraphNode analyzeAndCreateNode(Level level, BlockPos pos) {
        int wireNeighbors = 0;
        boolean isSource = false;
        boolean isSink = false;

        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.relative(dir);
            BlockState state = level.getBlockState(neighbor);

            if (state.getBlock() instanceof WireBlock) { // Check your specific WireBlock class
                wireNeighbors++;
            } else {
                BlockEntity be = level.getBlockEntity(neighbor);
                if (be instanceof BaseElectricMachineEntity machine) {
                    // Check if machine actually connects to this side
                    if (machine.isValidPort(dir.getOpposite())) {

                        // If machine OUTPUTS to us, we are a Source
                        if (machine.isOutputSide(dir.getOpposite())) isSource = true;

                        // If machine INPUTS from us, we are a Sink
                        if (machine.isInputSide(dir.getOpposite())) isSink = true;
                    }
                }
            }
        }

        // A node is created if it interacts with machines OR is a complex junction
        // This includes straight wires if they touch a machine (solving your issue)
        boolean isJunction = (wireNeighbors != 2);

        if (isSource || isSink || isJunction) {
            return new GraphNode(pos, isSource, isSink, isJunction);
        }

        return null;
    }

    private static Set<BlockPos> findAllWires(Level level, BlockPos start) {
        // (Your existing implementation - no changes needed here)
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        if (level.getBlockState(start).getBlock() instanceof WireBlock) {
            queue.add(start);
            visited.add(start);
        }

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.relative(dir);
                if (!visited.contains(neighbor)) {
                    if (level.getBlockState(neighbor).getBlock() instanceof WireBlock) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }
        return visited;
    }

    private static void findConnections(Level level, GraphNode startNode, Set<BlockPos> scope, Map<BlockPos, GraphNode> allNodes) {
        // (Your existing implementation - no changes needed here)
        BlockPos startPos = startNode.getPos();
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = startPos.relative(dir);
            if (scope.contains(neighbor)) {
                walkPath(level, startNode, neighbor, dir, 1, scope, allNodes);
            }
        }
    }

    private static void walkPath(Level level, GraphNode sourceNode, BlockPos currentPos, Direction fromDir, int distance, Set<BlockPos> scope, Map<BlockPos, GraphNode> allNodes) {
        // (Your existing implementation - no changes needed here)
        if (allNodes.containsKey(currentPos)) {
            GraphNode targetNode = allNodes.get(currentPos);
            if (!targetNode.equals(sourceNode)) {
                sourceNode.addConnection(targetNode, distance);
            }
            return;
        }

        for (Direction dir : Direction.values()) {
            if (dir == fromDir.getOpposite()) continue;
            BlockPos nextPos = currentPos.relative(dir);
            if (scope.contains(nextPos)) {
                walkPath(level, sourceNode, nextPos, dir, distance + 1, scope, allNodes);
                return;
            }
        }
    }
}
