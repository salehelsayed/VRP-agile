package com.v;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;

public class Simulation {
    public static void main(String[] args) throws IOException {
        System.out.println("Simulation started");
        runSimulation(3, null);
        System.out.println("Simulation ended");
    }

    public static String runSimulation(int nodeCount, int[][] adjacencyMatrix) {
        adjacencyMatrix = initializeAdjacencyMatrix(nodeCount, adjacencyMatrix);
        validateAdjacencyMatrix(nodeCount, adjacencyMatrix);

        Node[] nodes;
        try {
            nodes = createNodes(nodeCount);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // Start of Selection
            e.printStackTrace();
            return "Simulation failed due to IO error";
        }
        Graph<Node, DefaultEdge> graph = buildGraph(nodes, adjacencyMatrix);

        connectNodes(graph, nodes, adjacencyMatrix);

        printNodes(nodes);

        cleanupNodes(nodes);

        System.out.println("Simulation completed");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SimulationData simulationData = new SimulationData(nodeCount, adjacencyMatrix, nodes);
        try (FileWriter writer = new FileWriter("simulation_result.json")) {
            gson.toJson(simulationData, writer);
            System.out.println("Simulation data exported to simulation_result.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Simulation completed";
    }

    private static int[][] initializeAdjacencyMatrix(int nodeCount, int[][] adjacencyMatrix) {
        if (adjacencyMatrix == null || adjacencyMatrix.length != nodeCount) {
            adjacencyMatrix = new int[nodeCount][nodeCount];
            for (int i = 0; i < nodeCount; i++) {
                for (int j = 0; j < nodeCount; j++) {
                    if (i == j) {
                        adjacencyMatrix[i][j] = 0;
                    } else {
                        adjacencyMatrix[i][j] = 1;
                    }
                }
            }
        }
        return adjacencyMatrix;
    }

    private static void validateAdjacencyMatrix(int nodeCount, int[][] adjacencyMatrix) {
        if (adjacencyMatrix.length < 2) {
            throw new IllegalArgumentException("Adjacency matrix must have size of at least 2x2");
        }

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[i].length != adjacencyMatrix.length) {
                throw new IllegalArgumentException("Adjacency matrix must be square");
            }
            if (adjacencyMatrix[i][i] != 0) {
                throw new IllegalArgumentException("Adjacency matrix must not contain self-loops (adjacencyMatrix[" + i
                        + "][" + i + "] must be 0)");
            }
        }

        if (adjacencyMatrix.length != nodeCount) {
            throw new IllegalArgumentException("Adjacency matrix size must match node count");
        }
    }

    private static Node[] createNodes(int nodeCount) throws IOException {
        Node[] nodes = new Node[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            nodes[i] = new Node("localhost", 8080 + i);
            nodes[i].startListeners();
        }
        return nodes;
    }

    private static Graph<Node, DefaultEdge> buildGraph(Node[] nodes, int[][] adjacencyMatrix) {
        Graph<Node, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (Node node : nodes) {
            graph.addVertex(node);
        }
        return graph;
    }

    private static void connectNodes(Graph<Node, DefaultEdge> graph, Node[] nodes, int[][] adjacencyMatrix) {
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                if (adjacencyMatrix[i][j] == 1 && !graph.containsEdge(nodes[i], nodes[j])) {
                    graph.addEdge(nodes[i], nodes[j]);
                    nodes[i].getConnection(nodes[j].getIp(), nodes[j].getPort());
                }
            }
        }
    }

    private static void printNodes(Node[] nodes) {
        for (Node node : nodes) {
            System.out.println(node);
        }
    }

    private static void cleanupNodes(Node[] nodes) {
        for (Node node : nodes) {
            try {
                node.startListeners();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static class SimulationData {
        private int nodeCount;
        private int[][] adjacencyMatrix;
        private NodeInfo[] nodes;

        public SimulationData(int nodeCount, int[][] adjacencyMatrix, Node[] nodes) {
            this.nodeCount = nodeCount;
            this.adjacencyMatrix = adjacencyMatrix;
            this.nodes = new NodeInfo[nodes.length];
            for (int i = 0; i < nodes.length; i++) {
                this.nodes[i] = new NodeInfo(nodes[i]);
            }
        }

        public static class NodeInfo {
            private String ip;
            private int port;
            private String[] connections;

            public NodeInfo(Node node) {
                this.ip = node.getIp();
                this.port = node.getPort();
                this.connections = node.getConnections().keySet().toArray(new String[0]);
            }

            public String getIp() {
                return ip;
            }

            public int getPort() {
                return port;
            }

            public String[] getConnections() {
                return connections;
            }
        }

        public int getNodeCount() {
            return nodeCount;
        }

        public int[][] getAdjacencyMatrix() {
            return adjacencyMatrix;
        }

        public NodeInfo[] getNodes() {
            return nodes;
        }
    }
}
