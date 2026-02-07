import algorithms.BFS;
import graph.Graph;
import graph.GraphType;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph(GraphType.UNDIRECTED, false);

        // Пример из задания (можно менять)
        g.addEdge("0", "1");
        g.addEdge("0", "2");
        g.addEdge("1", "3");
        g.addEdge("2", "3");
        g.addEdge("3", "4");
        g.addEdge("4", "5");
        g.addEdge("2", "5");

        var res = BFS.run(g, "0");

        System.out.println("V = " + g.vertexCount());
        System.out.println("E(logical) = " + g.edgeCountLogical());
        System.out.println("BFS order: " + res.order);
        System.out.println("distance: " + res.distance);
        System.out.println("parent: " + res.parent);
        System.out.println("Shortest 0->5: " + BFS.shortestPathUnweighted(g, "0", "5"));
    }
}
