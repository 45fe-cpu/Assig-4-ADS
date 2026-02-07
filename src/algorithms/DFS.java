package algorithms;

import graph.Graph;

import java.util.*;

public class DFS {

    public static class Result {
        public final List<String> order;           // traversal order
        public final Map<String, String> parent;   // parent for each visited vertex (start -> null)

        public Result(List<String> order, Map<String, String> parent) {
            this.order = order;
            this.parent = parent;
        }
    }

    /**
     * DFS recursive from start.
     * Deterministic because Graph.neighbors(u) is sorted.
     */
    public static Result recursive(Graph g, String start) {
        if (g == null || start == null || !g.containsVertex(start)) {
            return new Result(List.of(), Map.of());
        }

        List<String> order = new ArrayList<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();

        parent.put(start, null);
        dfsRec(g, start, visited, parent, order);

        return new Result(order, parent);
    }

    private static void dfsRec(Graph g, String u, Set<String> visited,
                               Map<String, String> parent, List<String> order) {
        visited.add(u);
        order.add(u);

        for (String v : g.neighbors(u)) {
            if (!visited.contains(v)) {
                parent.put(v, u);
                dfsRec(g, v, visited, parent, order);
            }
        }
    }

    /**
     * DFS iterative from start (explicit stack).
     * To match recursive order deterministically, we push neighbors in reverse order.
     */
    public static Result iterative(Graph g, String start) {
        if (g == null || start == null || !g.containsVertex(start)) {
            return new Result(List.of(), Map.of());
        }

        List<String> order = new ArrayList<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();

        Deque<String> stack = new ArrayDeque<>();
        stack.push(start);
        parent.put(start, null);

        while (!stack.isEmpty()) {
            String u = stack.pop();
            if (visited.contains(u)) continue;

            visited.add(u);
            order.add(u);

            List<String> neigh = g.neighbors(u);
            // reverse push so that smallest neighbor processed first
            for (int i = neigh.size() - 1; i >= 0; i--) {
                String v = neigh.get(i);
                if (!visited.contains(v)) {
                    // set parent only first time we see v (avoid overwriting)
                    parent.putIfAbsent(v, u);
                    stack.push(v);
                }
            }
        }

        return new Result(order, parent);
    }

    /**
     * Connected components for UNDIRECTED graphs.
     * Returns list of components, each component is a sorted list of vertices (deterministic).
     */
    public static List<List<String>> connectedComponentsUndirected(Graph g) {
        if (g == null) return List.of();

        List<List<String>> comps = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        for (String start : g.vertices()) { // vertices are sorted
            if (visited.contains(start)) continue;

            List<String> comp = new ArrayList<>();
            // BFS/DFS for component - use stack DFS
            Deque<String> st = new ArrayDeque<>();
            st.push(start);

            while (!st.isEmpty()) {
                String u = st.pop();
                if (visited.contains(u)) continue;

                visited.add(u);
                comp.add(u);

                List<String> neigh = g.neighbors(u);
                for (int i = neigh.size() - 1; i >= 0; i--) {
                    String v = neigh.get(i);
                    if (!visited.contains(v)) st.push(v);
                }
            }

            Collections.sort(comp); // make component output stable
            comps.add(comp);
        }

        return comps;
    }

    /**
     * Cycle detection for UNDIRECTED graphs using DFS + parent check.
     * Returns true if a cycle exists.
     */
    public static boolean hasCycleUndirected(Graph g) {
        if (g == null) return false;

        Set<String> visited = new HashSet<>();
        for (String start : g.vertices()) {
            if (!visited.contains(start)) {
                if (cycleDfsUndirected(g, start, null, visited)) return true;
            }
        }
        return false;
    }

    private static boolean cycleDfsUndirected(Graph g, String u, String parent,
                                              Set<String> visited) {
        visited.add(u);

        for (String v : g.neighbors(u)) {
            if (!visited.contains(v)) {
                if (cycleDfsUndirected(g, v, u, visited)) return true;
            } else {
                // visited neighbor that is not parent => cycle
                if (parent == null || !v.equals(parent)) return true;
            }
        }
        return false;
    }
}
