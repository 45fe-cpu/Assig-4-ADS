import algorithms.BFS;
import algorithms.DFS;
import algorithms.SocialReachability;
import graph.Graph;
import io.GraphIO;

import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {

    /**
     * Run modes:
     * 1) Edge-list from file:
     *    java -cp out Main edge file.txt [start] [target]
     *
     * 2) Edge-list from stdin:
     *    cat file.txt | java -cp out Main edge - [start] [target]
     *
     * Output:
     * - Graph stats
     * - BFS order, distance, parent
     * - shortest path start->target
     * - DFS recursive+iterative orders
     * - connected components (if undirected)
     * - cycle detection (if undirected)
     * - Applied: Social reachability (dist<=2 + shortest path)
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            printUsage();
            return;
        }

        String mode = args[0];
        String source = args[1];

        if (!mode.equalsIgnoreCase("edge")) {
            System.out.println("Unsupported mode: " + mode);
            printUsage();
            return;
        }

        Graph g;
        if (source.equals("-")) {
            g = GraphIO.readEdgeList(new InputStreamReader(System.in));
        } else {
            g = GraphIO.readEdgeListFromFile(source);
        }

        String start = (args.length >= 3) ? args[2] : "0";
        String target = (args.length >= 4) ? args[3] : start;

        System.out.println("=== INPUT ===");
        System.out.println("Args: " + Arrays.toString(args));

        System.out.println("\n=== GRAPH STATS ===");
        System.out.println("V = " + g.vertexCount());
        System.out.println("E(logical) = " + g.edgeCountLogical());
        System.out.println("Directed = " + g.isDirected());
        System.out.println("Weighted = " + g.isWeighted());

        System.out.println("\n=== BFS ===");
        var bfs = BFS.run(g, start);
        System.out.println("BFS from " + start + ": order = " + bfs.order);
        System.out.println("distance = " + bfs.distance);
        System.out.println("parent   = " + bfs.parent);

        System.out.println("\n=== Shortest path (unweighted) ===");
        System.out.println("Path " + start + " -> " + target + ": " + BFS.shortestPathUnweighted(g, start, target));

        System.out.println("\n=== DFS ===");
        var dfsRec = DFS.recursive(g, start);
        var dfsIt = DFS.iterative(g, start);
        System.out.println("DFS recursive from " + start + ": " + dfsRec.order);
        System.out.println("DFS iterative  from " + start + ": " + dfsIt.order);

        if (!g.isDirected()) {
            System.out.println("\n=== Connected components (undirected) ===");
            System.out.println(DFS.connectedComponentsUndirected(g));

            System.out.println("\n=== Cycle detection (undirected) ===");
            System.out.println("Has cycle: " + DFS.hasCycleUndirected(g));

            System.out.println("\n=== Applied: Social reachability ===");
            var social = SocialReachability.run(g, start, target);
            System.out.println("Within distance <= 2 from " + start + ": " + social.withinDistance2);
            System.out.println("Shortest path " + start + "->" + target + ": " + social.shortestPathToTarget);
        } else {
            System.out.println("\nNOTE: components/cycle/social part configured for UNDIRECTED in this implementation.");
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java -cp out Main edge <file> [start] [target]");
        System.out.println("  cat <file> | java -cp out Main edge - [start] [target]");
        System.out.println("\nEdge-list file format:");
        System.out.println("  n m directed(0/1) weighted(0/1)");
        System.out.println("  u v [w]");
    }
}
