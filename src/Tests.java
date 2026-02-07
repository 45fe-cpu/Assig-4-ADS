import algorithms.BFS;
import algorithms.DFS;
import algorithms.SocialReachability;
import graph.Graph;
import graph.GraphType;

import java.util.List;

public class Tests {

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) throw new RuntimeException("TEST FAILED: " + msg);
    }

    public static void main(String[] args) {

        // 1) Empty graph
        Graph g0 = new Graph(GraphType.UNDIRECTED, false);
        assertTrue(BFS.run(g0, "A").order.isEmpty(), "Empty graph BFS");

        // 2) Single vertex
        Graph g1 = new Graph(GraphType.UNDIRECTED, false);
        g1.addVertex("A");
        assertTrue(BFS.run(g1, "A").order.equals(List.of("A")), "Single vertex BFS");

        // 3) Disconnected graph
        Graph g2 = new Graph(GraphType.UNDIRECTED, false);
        g2.addEdge("A", "B");
        g2.addEdge("C", "D");
        assertTrue(DFS.connectedComponentsUndirected(g2).size() == 2, "Disconnected components");

        // 4) Cycle detection
        Graph g3 = new Graph(GraphType.UNDIRECTED, false);
        g3.addEdge("A", "B");
        g3.addEdge("B", "C");
        g3.addEdge("C", "A");
        assertTrue(DFS.hasCycleUndirected(g3), "Cycle detection");

        // 5) BFS shortest path
        Graph g4 = new Graph(GraphType.UNDIRECTED, false);
        g4.addEdge("A", "B");
        g4.addEdge("B", "C");
        g4.addEdge("A", "C");
        assertTrue(BFS.shortestPathUnweighted(g4, "A", "C").equals(List.of("A", "C")),
                "BFS shortest path");

        // 6) DFS determinism
        var dfs1 = DFS.recursive(g4, "A").order;
        var dfs2 = DFS.recursive(g4, "A").order;
        assertTrue(dfs1.equals(dfs2), "DFS deterministic order");

        // 7) Invalid input
        assertTrue(BFS.run(g4, "X").order.isEmpty(), "Invalid start vertex");

        // 8) Social reachability
        Graph g5 = new Graph(GraphType.UNDIRECTED, false);
        g5.addEdge("A", "B");
        g5.addEdge("B", "C");
        g5.addEdge("C", "D");
        var social = SocialReachability.run(g5, "A", "D");
        assertTrue(social.withinDistance2.equals(List.of("B", "C")),
                "Social reachability distance<=2");

        // 9) Stress test (basic)
        Graph big = new Graph(GraphType.UNDIRECTED, false);
        int n = 1000;
        for (int i = 0; i < n - 1; i++) {
            big.addEdge(String.valueOf(i), String.valueOf(i + 1));
        }
        assertTrue(BFS.run(big, "0").distance.get(String.valueOf(n - 1)) == n - 1,
                "Stress test BFS");

        System.out.println("ALL TESTS PASSED");
    }
}
