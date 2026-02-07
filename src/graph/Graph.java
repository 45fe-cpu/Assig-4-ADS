package graph;

import java.util.*;

public class Graph {
    private final GraphType type;
    private final boolean weighted;

    // TreeMap + TreeSet -> детерминированный порядок вершин/соседей (важно по заданию)
    private final Map<String, Set<Edge>> adj = new TreeMap<>();
    private int edgesStored = 0; // кол-во сохранённых дуг (в UNDIRECTED хранится в обе стороны)

    public Graph(GraphType type, boolean weighted) {
        this.type = type;
        this.weighted = weighted;
    }

    public boolean isDirected() {
        return type == GraphType.DIRECTED;
    }

    public boolean isWeighted() {
        return weighted;
    }

    public void addVertex(String v) {
        if (v == null) throw new IllegalArgumentException("Vertex cannot be null");
        adj.computeIfAbsent(v, k -> new TreeSet<>(Comparator.comparing(Edge::to)));
    }

    public boolean containsVertex(String v) {
        return v != null && adj.containsKey(v);
    }

    public List<String> vertices() {
        return new ArrayList<>(adj.keySet());
    }

    public int vertexCount() {
        return adj.size();
    }

    /**
     * Количество сохранённых дуг.
     * В UNDIRECTED каждое ребро хранится дважды (u->v и v->u).
     */
    public int edgeCountStored() {
        return edgesStored;
    }

    /**
     * Количество логических рёбер.
     * В UNDIRECTED = edgesStored / 2.
     */
    public int edgeCountLogical() {
        return isDirected() ? edgesStored : edgesStored / 2;
    }

    public List<String> neighbors(String u) {
        if (!containsVertex(u)) return List.of();
        List<String> res = new ArrayList<>();
        for (Edge e : adj.get(u)) res.add(e.to());
        return res;
    }

    public void addEdge(String u, String v) {
        addEdge(u, v, null);
    }

    public void addEdge(String u, String v, Integer w) {
        if (u == null || v == null) throw new IllegalArgumentException("Vertices cannot be null");

        if (weighted && w == null)
            throw new IllegalArgumentException("Weighted graph requires weight");
        if (!weighted && w != null)
            throw new IllegalArgumentException("Unweighted graph must not have weight");

        addVertex(u);
        addVertex(v);

        boolean added = adj.get(u).add(new Edge(v, w));
        if (added) edgesStored++;

        if (!isDirected()) {
            boolean addedBack = adj.get(v).add(new Edge(u, w));
            if (addedBack) edgesStored++;
        }
    }

    public boolean removeEdge(String u, String v) {
        if (!containsVertex(u) || v == null) return false;

        boolean removed = adj.get(u).removeIf(e -> e.to().equals(v));
        if (removed) edgesStored--;

        if (!isDirected() && containsVertex(v)) {
            boolean removedBack = adj.get(v).removeIf(e -> e.to().equals(u));
            if (removedBack) edgesStored--;
        }
        return removed;
    }

    public boolean removeVertex(String v) {
        if (!containsVertex(v)) return false;

        // Удаляем входящие дуги (из других вершин)
        for (String u : vertices()) {
            if (!u.equals(v)) {
                removeEdge(u, v);
            }
        }

        // Удаляем исходящие дуги (из v)
        edgesStored -= adj.get(v).size();
        adj.remove(v);

        return true;
    }

    /**
     * Экспорт матрицы смежности.
     * Для unweighted -> 1/0, для weighted -> вес.
     * Порядок вершин = Graph.vertices() (отсортирован TreeMap).
     */
    public int[][] toAdjMatrix() {
        List<String> verts = vertices();
        int n = verts.size();
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) idx.put(verts.get(i), i);

        int[][] m = new int[n][n];
        for (String u : verts) {
            int i = idx.get(u);
            for (Edge e : adj.get(u)) {
                int j = idx.get(e.to());
                m[i][j] = weighted ? e.weight() : 1;
            }
        }
        return m;
    }
}
