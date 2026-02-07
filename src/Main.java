import algorithms.BFS;
import algorithms.DFS;
import algorithms.SocialReachability;
import graph.Graph;
import graph.GraphType;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph(GraphType.UNDIRECTED, false);

        // Friendship graph example
        g.addEdge("A", "B");
        g.addEdge("A", "C");
        g.addEdge("B", "D");
        g.addEdge("C", "D");
        g.addEdge("D", "E");
        g.addEdge("E", "F");
        g.addEdge("C", "F");

        System.out.println("V = " + g.vertexCount());
        System.out.println("E(logical) = " + g.edgeCountLogical());

        // Part B: BFS demo
        var bfs = BFS.run(g, "A");
        System.out.println("BFS order from A: " + bfs.order);
        System.out.println("Shortest A->F: " + BFS.shortestPathUnweighted(g, "A", "F"));

        // Part C: DFS demo
        var dfsRec = DFS.recursive(g, "A");
        System.out.println("DFS recursive from A: " + dfsRec.order);

        var dfsIt = DFS.iterative(g, "A");
        System.out.println("DFS iterative from A: " + dfsIt.order);

        System.out.println("Connected components: " + DFS.connectedComponentsUndirected(g));
        System.out.println("Has cycle (undirected): " + DFS.hasCycleUndirected(g));

        // Part D: Social reachability
        var social = SocialReachability.run(g, "A", "F");
        System.out.println("People within distance <= 2 from A: " + social.withinDistance2);
        System.out.println("Shortest path A->F: " + social.shortestPathToTarget);
    }
}
