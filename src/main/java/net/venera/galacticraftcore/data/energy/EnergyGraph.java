package net.venera.galacticraftcore.data.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.venera.galacticraftcore.block.entity.machine.electric.BaseElectricMachineEntity;
import java.util.*;

public class EnergyGraph {
    public final int id;
    private final Set<GraphNode> nodes = new HashSet<>();
    private final Set<BlockPos> allPositions = new HashSet<>();
    private double totalConductance = 0;

    public EnergyGraph(int id) {
        this.id = id;
    }

    public void addNode(GraphNode node) {
        nodes.add(node);
        allPositions.add(node.getPos());
    }

    public Set<BlockPos> getAllPositions() { return allPositions; }
    public void addWirePosition(BlockPos pos) { allPositions.add(pos); }

    public void recalculatePhysics() {
        totalConductance = 0;
        for (GraphNode node : nodes) {
            if (node.isSink()) {
                for (GraphEdge edge : node.getConnections()) {
                    if (edge.target.isSource()) {
                        totalConductance += 1.0 / (double) Math.max(1, edge.distance);
                    }
                }
            }
        }
    }

    public int distribute(Level level, BlockPos entryPoint, int maxPush) {
        GraphNode sourceNode = findNodeAt(entryPoint);
        if (sourceNode == null) return distributeRaw(level, entryPoint, maxPush);

        // 1. Identify Local Targets (Direct neighbors to this wire block)
        List<IEnergyStorage> localTargets = getValidLocalTargets(level, sourceNode.getPos());

        // 2. Identify Remote Targets (Network Sinks)
        Map<GraphNode, Integer> remoteTargets = calculatePaths(sourceNode);
        remoteTargets.remove(sourceNode); // Do not count ourselves as remote
        // Remove nodes that aren't sinks (e.g. other Generators or junctions)
        remoteTargets.entrySet().removeIf(e -> !e.getKey().isSink());

        // 3. Calculate Conductance (Demand)
        // Local machines are treated as having distance 1 (Conductance 1.0 each)
        double localConductance = localTargets.size() * 1.0;

        double remoteConductance = 0;
        for (int dist : remoteTargets.values()) {
            remoteConductance += 1.0 / Math.max(1, (double) dist);
        }

        double totalSystemConductance = localConductance + remoteConductance;

        // If nowhere to go, return 0
        if (totalSystemConductance <= 0) return 0;

        int totalConsumed = 0;

        // 4. Distribute to Local Targets (Proportional Share)
        if (!localTargets.isEmpty()) {
            double localShareRatio = localConductance / totalSystemConductance;
            int localBudget = (int) (maxPush * localShareRatio);

            if (localBudget > 0) {
                int perMachine = localBudget / localTargets.size();
                for (IEnergyStorage cap : localTargets) {
                    totalConsumed += cap.receiveEnergy(perMachine, false);
                }
            }
        }

        // 5. Distribute to Remote Targets (Remaining Share)
        if (!remoteTargets.isEmpty()) {
            // We calculate remote budget based on what's left of the theoretical split
            // (Using maxPush - localBudget is safer to ensure we don't create energy due to rounding)
            int localBudgetUsed = (int) (maxPush * (localConductance / totalSystemConductance));
            int remoteBudget = maxPush - localBudgetUsed;

            if (remoteBudget > 0 && remoteConductance > 0) {
                for (Map.Entry<GraphNode, Integer> entry : remoteTargets.entrySet()) {
                    GraphNode sinkNode = entry.getKey();
                    int distance = entry.getValue();

                    // How much of the *remote* budget does this specific node get?
                    double nodeConductance = 1.0 / Math.max(1, (double) distance);
                    double nodeShareOfRemote = nodeConductance / remoteConductance;

                    int energyForNode = (int) (remoteBudget * nodeShareOfRemote);

                    // Distance Loss
                    int loss = distance / 10;
                    int energyToInsert = Math.max(0, energyForNode - loss);

                    if (energyToInsert > 0) {
                        // Push to the machines around that remote node
                        int pushed = pushToMachinesAround(level, sinkNode.getPos(), energyToInsert);
                        if (pushed > 0) {
                            totalConsumed += (pushed + loss);
                        }
                    }
                }
            }
        }

        return totalConsumed;
    }

    // --- Helpers ---

    private List<IEnergyStorage> getValidLocalTargets(Level level, BlockPos pos) {
        List<IEnergyStorage> targets = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            BlockPos targetPos = pos.relative(dir);
            BlockEntity be = level.getBlockEntity(targetPos);
            if (be instanceof BaseElectricMachineEntity machine && machine.isInputSide(dir.getOpposite())) {
                var cap = machine.getEnergyStorage();
                if (cap != null) targets.add(cap);
            }
        }
        return targets;
    }

    private int pushToMachinesAround(Level level, BlockPos pos, int amount) {
        List<IEnergyStorage> targets = getValidLocalTargets(level, pos);
        if (targets.isEmpty()) return 0;

        int perMachine = amount / targets.size();
        int acceptedTotal = 0;
        for (IEnergyStorage cap : targets) {
            acceptedTotal += cap.receiveEnergy(perMachine, false);
        }
        return acceptedTotal;
    }

    private int distributeRaw(Level level, BlockPos pos, int maxPush) {
        return pushToMachinesAround(level, pos, maxPush);
    }

    private Map<GraphNode, Integer> calculatePaths(GraphNode start) {
        Map<GraphNode, Integer> results = new HashMap<>();
        Queue<GraphNode> queue = new LinkedList<>();
        Map<GraphNode, Integer> distances = new HashMap<>();

        queue.add(start);
        distances.put(start, 0);

        while (!queue.isEmpty()) {
            GraphNode current = queue.poll();
            int currentDist = distances.get(current);

            if (current.isSink() && !current.equals(start)) {
                results.put(current, currentDist);
            }

            for (GraphEdge edge : current.getConnections()) {
                if (!distances.containsKey(edge.target)) {
                    int newDist = currentDist + edge.distance;
                    distances.put(edge.target, newDist);
                    queue.add(edge.target);
                }
            }
        }
        return results;
    }

    private GraphNode findNodeAt(BlockPos pos) {
        for (GraphNode node : nodes) {
            if (node.getPos().equals(pos)) return node;
        }
        return null;
    }
}
