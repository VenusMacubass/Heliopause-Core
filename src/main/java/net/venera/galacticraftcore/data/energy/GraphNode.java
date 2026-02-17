package net.venera.galacticraftcore.data.energy;

import net.minecraft.core.BlockPos;
import java.util.ArrayList;
import java.util.List;

public class GraphNode {
    private final BlockPos pos;
    private final List<GraphEdge> connections = new ArrayList<>();

    // New Boolean Flags
    private final boolean isSource;   // Can receive energy FROM a machine
    private final boolean isSink;     // Can push energy INTO a machine
    private final boolean isJunction; // Is a complex intersection

    public GraphNode(BlockPos pos, boolean isSource, boolean isSink, boolean isJunction) {
        this.pos = pos;
        this.isSource = isSource;
        this.isSink = isSink;
        this.isJunction = isJunction;
    }

    public void addConnection(GraphNode target, int newDistance) {
        // Merge parallel edges (resistance calculation)
        for (GraphEdge edge : connections) {
            if (edge.target.equals(target)) {
                double gExisting = 1.0 / Math.max(1.0, (double) edge.distance);
                double gNew = 1.0 / Math.max(1.0, (double) newDistance);
                double gTotal = gExisting + gNew;

                int combinedResistance = (int) Math.round(1.0 / gTotal);
                edge.distance = combinedResistance;
                return;
            }
        }
        connections.add(new GraphEdge(this, target, newDistance));
    }

    public BlockPos getPos() { return pos; }

    public boolean isSource() { return isSource; }
    public boolean isSink() { return isSink; }
    public boolean isJunction() { return isJunction; }

    public List<GraphEdge> getConnections() { return connections; }
}
