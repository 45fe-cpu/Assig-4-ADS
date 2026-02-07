package algorithms;

import graph.Graph;

import java.util.*;

public class BFS {

    public static class Result {
        public final List<String> order;
        public final Map<String, Integer> distance; // number of edges from start
        public final Map<String, String> parent;    // for path reconstruction

        public Result(List<String> order, Map<String, Integer> distance, Map<String, String> parent) {
            this.order = order;
            this.distance = distance;
            this.parent = parent;
        }
    }

    /**
     * BFS(start) -> traversal order + distance + parent
     * If start vertex doesn't exist -> returns empty result.
     */
    public static Result run(Graph g, String start) {
        if (g == null || start == null || !g.containsVertex(start)) {
            return new Result(List.of(), Map.of(), Map.of());
        }

        Queue<String> q = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        List<String> order = new ArrayList<>();
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();

        visited.add(start);
        dist.put(start, 0);
        parent.put(start, null);
        q.add(start);

        while (!q.isEmpty()) {
            String u = q.poll();
            order.add(u);

            // neighbors already deterministic (Graph uses TreeSet ordering)
            for (String v : g.neighbors(u)) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    dist.put(v, dist.get(u) + 1);
                    parent.put(v, u);
                    q.add(v);
                }
            }
        }

        return new Result(order, dist, parent);
    }

    /**
     * Shortest path in unweighted graph using BFS parents.
     * Returns vertices from s to t (inclusive). If unreachable -> empty list.
     */
    public static List<String> shortestPathUnweighted(Graph g, String s, String t) {
        if (g == null || s == null || t == null) return List.of();
        if (!g.containsVertex(s) || !g.containsVertex(t)) return List.of();

        Result r = run(g, s);
        if (!r.distance.containsKey(t)) return List.of(); // unreachable

        LinkedList<String> path = new LinkedList<>();
        String cur = t;
        while (cur != null) {
            path.addFirst(cur);
            cur = r.parent.get(cur);
        }
        return (!path.isEmpty() && path.getFirst().equals(s)) ? path : List.of();
    }
}
