import graph.Graph;
import graph.GraphType;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph(GraphType.UNDIRECTED, false);

        g.addEdge("0", "1");
        g.addEdge("0", "2");
        g.addEdge("1", "3");
        g.addEdge("2", "3");

        System.out.println("V=" + g.vertexCount() + ", E=" + g.edgeCount());
        System.out.println("Neighbors(0): " + g.neighbors("0"));
    }
}
