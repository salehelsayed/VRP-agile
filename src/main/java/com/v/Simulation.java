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

        int nodeCount = 3; // Set the number of nodes

        // Generate upper triangular matrices filled with ones
        int[][] p2pMatrix = generateRandomUpperTriangularMatrix(nodeCount);
        int[][] demandMatrix = generateFullUpperTriangularMatrix(nodeCount);

        runSimulation(nodeCount, p2pMatrix, demandMatrix);

        System.out.println("Simulation ended");
    }

    private static int[][] generateRandomUpperTriangularMatrix(int nodeCount) {
        int[][] matrix = new int[nodeCount][nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            for (int j = i + 1; j < nodeCount; j++) {
                    // Start Generation Here
                    if (Math.random() < 0.5) {
                        matrix[i][j] = 1;
                        matrix[j][i] = 1;
                    } else {
                        matrix[i][j] = 0;
                        matrix[j][i] = 0;
                    }
                matrix[i][j] = 1; // Set upper triangle to 1
                matrix[j][i] = matrix[i][j]; // Ensure symmetry if needed
            }
            matrix[i][i] = 0; // No self-loops
        }
        return matrix;

    }

    // Update the generateFullUpperTriangularMatrix method
    private static int[][] generateFullUpperTriangularMatrix(int nodeCount) {
        int[][] matrix = new int[nodeCount][nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            for (int j = i + 1; j < nodeCount; j++) {
                matrix[i][j] = 1; // Set upper triangle to 1
                matrix[j][i] = matrix[i][j]; // Ensure symmetry if needed
            }
            matrix[i][i] = 0; // No self-loops
        }
        return matrix;
    }

    public static String runSimulation(int nodeCount, int[][] p2pMatrix, int[][] demandMatrix) {
        p2pMatrix = initializeMatrix(nodeCount, p2pMatrix, true);
        demandMatrix = initializeMatrix(nodeCount, demandMatrix, false);

        validateMatrix(nodeCount, p2pMatrix);
        validateMatrix(nodeCount, demandMatrix);

        Node[] nodes;
        try {
            nodes = createNodes(nodeCount);
        } catch (IOException e) {
            e.printStackTrace();
            return "Simulation failed due to IO error";
        }

        Graph<Node, DefaultEdge> graph = buildGraph(nodes, p2pMatrix);

        connectNodesP2P(graph, nodes, p2pMatrix);

        processDemands(nodes, demandMatrix);

        printNodes(nodes);

        cleanupNodes(nodes);

        System.out.println("Simulation completed");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        SimulationData simulationData = new SimulationData(nodeCount, p2pMatrix, demandMatrix, nodes);
        try (FileWriter writer = new FileWriter("simulation_result.json")) {
            gson.toJson(simulationData, writer);
            System.out.println("Simulation data exported to simulation_result.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Simulation completed";
    }

    private static int[][] initializeMatrix(int nodeCount, int[][] matrix, boolean isP2P) {
        if (matrix == null || matrix.length != nodeCount) {
            matrix = new int[nodeCount][nodeCount];
            for (int i = 0; i < nodeCount; i++) {
                for (int j = 0; j < nodeCount; j++) {
                    if (i == j) {
                        matrix[i][j] = 0;
                    } else {
                        if (isP2P) {
                            matrix[i][j] = (Math.random() > 0.5) ? 1 : 0;
                        } else {
                            matrix[i][j] = (Math.random() > 0.5) ? 1 : 0;
                        }
                    }
                }
            }
        }
        return matrix;
    }

    private static void validateMatrix(int nodeCount, int[][] matrix) {
        if (matrix.length != nodeCount) {
            throw new IllegalArgumentException("Matrix size must match node count");
        }
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].length != nodeCount) {
                throw new IllegalArgumentException("Matrix must be square");
            }
            if (matrix[i][i] != 0) {
                throw new IllegalArgumentException("Matrix must not contain self-loops (matrix[" + i
                        + "][" + i + "] must be 0)");
            }
        }
    }

    // Modify the createNodes method to pass nodeId
    private static Node[] createNodes(int nodeCount) throws IOException {
        Node[] nodes = new Node[nodeCount];
        int portStart = 8080;
        int portEnd = 8100;
        for (int i = 0; i < nodeCount; i++) {
            nodes[i] = new Node(i, "127.0.0.1", portStart+((i+1)*5), portEnd+((i+2)*5)); // Pass nodeId
            nodes[i].startListeners();
        }
        return nodes;
    }

    private static Graph<Node, DefaultEdge> buildGraph(Node[] nodes, int[][] p2pMatrix) {
        Graph<Node, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (Node node : nodes) {
            graph.addVertex(node);
        }
        return graph;
    }

    // Update logs to include nodeId
    private static void connectNodesP2P(Graph<Node, DefaultEdge> graph, Node[] nodes, int[][] p2pMatrix) {
        for (int i = 0; i < p2pMatrix.length; i++) {
            for (int j = i + 1; j < p2pMatrix[i].length; j++) {
                if (p2pMatrix[i][j] == 1) {
                    graph.addEdge(nodes[i], nodes[j]);
                    nodes[i].addNeighbor(nodes[j]);
                    nodes[j].addNeighbor(nodes[i]);

                    // Establish P2P connections via sockets
                    try {
                        System.out.println("Establishing P2P connection between Node " + nodes[i].getNodeId()
                                + " and Node " + nodes[j].getNodeId());
                        nodes[i].establishP2PConnection(nodes[j]);

                        System.out.println("P2P connection successful between Node " + nodes[i].getNodeId()
                                + " and Node " + nodes[j].getNodeId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void processDemands(Node[] nodes, int[][] demandMatrix) {
        for (int i = 0; i < demandMatrix.length; i++) {
            for (int j = 0; j < demandMatrix[i].length; j++) {
                if (demandMatrix[i][j] == 1) {
                    System.out.println("Processing demand from Node " + i + " to Node " + j);
                    nodes[i].processDemand(nodes[j], "Test Message");
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
                node.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class SimulationData {
        private int nodeCount;
        private int[][] p2pMatrix;
        private int[][] demandMatrix;
        private NodeInfo[] nodes;

        public SimulationData(int nodeCount, int[][] p2pMatrix, int[][] demandMatrix, Node[] nodes) {
            this.nodeCount = nodeCount;
            this.p2pMatrix = p2pMatrix;
            this.demandMatrix = demandMatrix;
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

        public int[][] getP2PMatrix() {
            return p2pMatrix;
        }

        public int[][] getDemandMatrix() {
            return demandMatrix;
        }

        public NodeInfo[] getNodes() {
            return nodes;
        }
    }
}
