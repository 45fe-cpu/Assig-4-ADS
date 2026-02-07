package graph;

import java.util.*;

public class Graph {
    private final GraphType type;
    private final boolean weighted;

    // TreeMap/TreeSet -> детерминированный порядок обхода (важно по заданию)
    private final Map<String, Set<Edge>> adj = new TreeMap<>();
    private int edges = 0;

    public Graph(GraphType type, boolean weighted) {
        this.type = type;
        this.weighted = weighted;
    }

    public boolean isDirected() { return type == GraphType.DIRECTED; }
    public boolean isWeighted() { return weighted; }

    public void addVertex(String v) {
        adj.computeIfAbsent(v, k -> new TreeSet<>(Comparator.comparing(Edge::to)));
    }

    public boolean removeVertex(String v) {
        if (!adj.containsKey(v)) return false;

        // удаляем все входящие ребра
        for (String u : new ArrayList<>(adj.keySet())) {
            if (u.equals(v)) continue;
            if (removeEdge(u, v)) { /* counted inside */ }
        }

        // удаляем исходящие ребра
        edges -= adj.get(v).size();
        adj.remove(v);

        return true;
    }

    public void addEdge(String u, String v) {
        addEdge(u, v, null);
    }

    public void addEdge(String u, String v, Integer w) {
        if (weighted && w == null) throw new IllegalArgumentException("Weighted graph requires weight");
        if (!weighted && w != null) throw new IllegalArgumentException("Unweighted graph must not have weight");

        addVertex(u);
        addVertex(v);

        boolean added = adj.get(u).add(new Edge(v, w));
        if (added) edges++;

        if (!isDirected()) {
            boolean addedBack = adj.get(v).add(new Edge(u, w));
            if (addedBack) edges++;
        }
    }

    public boolean removeEdge(String u, String v) {
        if (!adj.containsKey(u)) return false;

        boolean removed = adj.get(u).removeIf(e -> e.to().equals(v));
        if (removed) edges--;

        if (!isDirected() && adj.containsKey(v)) {
            boolean removedBack = adj.get(v).removeIf(e -> e.to().equals(u));
            if (removedBack) edges--;
        }
        return removed;
    }

    public List<String> neighbors(String u) {
        if (!adj.containsKey(u)) return List.of();
        List<String> res = new ArrayList<>();
        for (Edge e : adj.get(u)) res.add(e.to());
        return res;
    }

    public int vertexCount() { return adj.size(); }
    public int edgeCount() { return edges; }

    public int[][] toAdjMatrix() {
        List<String> verts = new ArrayList<>(adj.keySet());
        int n = verts.size();
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) idx.put(verts.get(i), i);

        int[][] m = new int[n][n];
        for (String u : adj.keySet()) {
            int i = idx.get(u);
            for (Edge e : adj.get(u)) {
                int j = idx.get(e.to());
                m[i][j] = weighted ? e.weight() : 1;
            }
        }
        return m;
    }
}
