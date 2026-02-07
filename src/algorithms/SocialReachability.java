package algorithms;

import graph.Graph;

import java.util.*;

public class SocialReachability {

    public static class Result {
        public final String source;
        public final List<String> withinDistance2; // all vertices with dist <= 2 (excluding source)
        public final List<String> shortestPathToTarget; // path S->T or empty

        public Result(String source, List<String> withinDistance2, List<String> shortestPathToTarget) {
            this.source = source;
            this.withinDistance2 = withinDistance2;
            this.shortestPathToTarget = shortestPathToTarget;
        }
    }

    /**
     * Undirected friendship graph:
     * (i) all people within distance <= 2 from S
     * (ii) shortest path from S to T
     *
     * If S or T doesn't exist -> returns empty lists.
     */
    public static Result run(Graph g, String s, String t) {
        if (g == null || s == null || t == null) {
            return new Result(s, List.of(), List.of());
        }
        if (!g.containsVertex(s) || !g.containsVertex(t)) {
            return new Result(s, List.of(), List.of());
        }

        BFS.Result bfs = BFS.run(g, s);

        List<String> near = new ArrayList<>();
        for (Map.Entry<String, Integer> e : bfs.distance.entrySet()) {
            String v = e.getKey();
            int d = e.getValue();
            if (!v.equals(s) && d <= 2) near.add(v);
        }
        Collections.sort(near); // stable output

        List<String> path = BFS.shortestPathUnweighted(g, s, t);

        return new Result(s, near, path);
    }
}

