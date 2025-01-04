import java.util.ArrayList;
import java.util.List;

public class Graph {
    // A class to represent a vertex in the graph
    static class Vertex {
        int data;
        boolean visited;

        Vertex(int data) {
            this.data = data;
        }
    }

    // A class to represent an edge in the graph
    static class Edge {
        Vertex src;
        Vertex dest;

        Edge(Vertex src, Vertex dest) {
            this.src = src;
            this.dest = dest;
        }
    }

    // A class to represent a graph
    static class Graph {
        // A list of vertices
        List<Vertex> vertices;

        // A list of edges
        List<Edge> edges;

        Graph(List<Vertex> vertices, List<Edge> edges) {
            this.vertices = vertices;
            this.edges = edges;
        }
    }

    public static void main(String[] args) {
        // Create a list of vertices
        List<Vertex> vertices = new ArrayList<>();
        vertices.add(new Vertex(1));
        vertices.add(new Vertex(2));
        vertices.add(new Vertex(3));

        // Create a list of edges
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(vertices.get(0), vertices.get(1)));
        edges.add(new Edge(vertices.get(1), vertices.get(2)));
        edges.add(new Edge(vertices.get(2), vertices.get(0)));

        // Create a graph
        Graph graph = new Graph(vertices, edges);

        // Do something with the graph
        // ...
    }
}
