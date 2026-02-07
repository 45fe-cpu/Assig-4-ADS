package io;

import graph.Graph;
import graph.GraphType;

import java.io.*;
import java.util.*;

public class GraphIO {

    /**
     * Edge-list format:
     * first line: n m directed(0/1) weighted(0/1)
     * next m lines: u v [w]
     *
     * Vertices are assumed to be 0..n-1 (ints), stored as strings.
     */
    public static Graph readEdgeList(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);

        String header = nextNonEmptyLine(br);
        if (header == null) throw new IllegalArgumentException("Empty input");

        String[] h = header.trim().split("\\s+");
        if (h.length < 4) throw new IllegalArgumentException("Header must be: n m directed weighted");

        int n = Integer.parseInt(h[0]);
        int m = Integer.parseInt(h[1]);
        int directed = Integer.parseInt(h[2]);
        int weighted = Integer.parseInt(h[3]);

        Graph g = new Graph(directed == 1 ? GraphType.DIRECTED : GraphType.UNDIRECTED, weighted == 1);

        // ensure isolated vertices exist
        for (int i = 0; i < n; i++) g.addVertex(String.valueOf(i));

        for (int i = 0; i < m; i++) {
            String line = nextNonEmptyLine(br);
            if (line == null) throw new IllegalArgumentException("Expected " + m + " edges, got " + i);
            String[] p = line.trim().split("\\s+");
            if ((!g.isWeighted() && p.length < 2) || (g.isWeighted() && p.length < 3)) {
                throw new IllegalArgumentException("Bad edge line: " + line);
            }
            String u = p[0];
            String v = p[1];
            if (g.isWeighted()) {
                int w = Integer.parseInt(p[2]);
                g.addEdge(u, v, w);
            } else {
                g.addEdge(u, v);
            }
        }
        return g;
    }

    private static String nextNonEmptyLine(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty() && !line.startsWith("#")) return line;
        }
        return null;
    }

    public static Graph readEdgeListFromFile(String path) throws IOException {
        try (FileReader fr = new FileReader(path)) {
            return readEdgeList(fr);
        }
    }
}
