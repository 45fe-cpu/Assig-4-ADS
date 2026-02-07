import algorithms.BFS;
import algorithms.DFS;
import graph.Graph;
import graph.GraphType;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph(GraphType.UNDIRECTED, false);

        g.addEdge("0", "1");
        g.addEdge("0", "2");
        g.addEdge("1", "3");
        g.addEdge("2", "3");
        g.addEdge("3", "4");
        g.addEdge("4", "5");
        g.addEdge("2", "5"); // цикл есть

        var bfs = BFS.run(g, "0");
        System.out.println("V = " + g.vertexCount());
        System.out.println("E(logical) = " + g.edgeCountLogical());
        System.out.println("BFS order: " + bfs.order);
        System.out.println("Shortest 0->5: " + BFS.shortestPathUnweighted(g, "0", "5"));

        var dfsRec = DFS.recursive(g, "0");
        System.out.println("DFS recursive: " + dfsRec.order);

        var dfsIt = DFS.iterative(g, "0");
        System.out.println("DFS iterative: " + dfsIt.order);

        System.out.println("Connected components: " + DFS.connectedComponentsUndirected(g));
        System.out.println("Has cycle (undirected): " + DFS.hasCycleUndirected(g));
    }
}
