import algorithms.BFS;
import algorithms.DFS;
import graph.Graph;
import graph.GraphType;

import java.util.*;

public class Benchmark {

    // Simple random undirected unweighted graph generator
    private static Graph randomGraph(int V, int E, long seed) {
        Graph g = new Graph(GraphType.UNDIRECTED, false);
        for (int i = 0; i < V; i++) g.addVertex(String.valueOf(i));

        Random rnd = new Random(seed);
        Set<Long> used = new HashSet<>();

        int added = 0;
        while (added < E) {
            int u = rnd.nextInt(V);
            int v = rnd.nextInt(V);
            if (u == v) continue;
            int a = Math.min(u, v), b = Math.max(u, v);
            long key = (((long) a) << 32) | (b & 0xffffffffL);
            if (used.add(key)) {
                g.addEdge(String.valueOf(u), String.valueOf(v));
                added++;
            }
        }
        return g;
    }

    private static long timeMs(Runnable r) {
        long t0 = System.nanoTime();
        r.run();
        long t1 = System.nanoTime();
        return (t1 - t0) / 1_000_000;
    }

    public static void main(String[] args) {
        int[] sizes = {1000, 3000, 7000}; // can expand
        int[] edges = {3000, 12000, 30000};

        System.out.println("V,E,BFS(ms),DFS-rec(ms),DFS-it(ms)");

        for (int i = 0; i < sizes.length; i++) {
            int V = sizes[i];
            int E = edges[i];

            Graph g = randomGraph(V, E, 42L + i);
            String start = "0";

            // warm-up
            BFS.run(g, start);
            DFS.recursive(g, start);
            DFS.iterative(g, start);

            long bfsMs = timeMs(() -> BFS.run(g, start));
            long dfsRecMs = timeMs(() -> DFS.recursive(g, start));
            long dfsItMs = timeMs(() -> DFS.iterative(g, start));

            System.out.println(V + "," + E + "," + bfsMs + "," + dfsRecMs + "," + dfsItMs);
        }
    }
}
