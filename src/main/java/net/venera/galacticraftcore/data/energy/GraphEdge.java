package net.venera.galacticraftcore.data.energy;


public class GraphEdge {
    public final GraphNode source; //Where is the energy coming from?
    public final GraphNode target; //What are the wires connected to?
    public int distance;     //How many wire blocks are in this line? (Resistance calculation)

    public GraphEdge(GraphNode source, GraphNode target, int distance) {
        this.source = source;
        this.target = target;
        this.distance = distance;
    }
}
