package com.v;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Simulation {
    public static void main(String[] args) {
        System.out.println("Simulation started");
        runSimulation();
    }

    public static String runSimulation() {
        int nodeCount = 3;
        Node[] nodes = new Node[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            nodes[i] = new Node("localhost", 8080 + i);
        }

        Graph<Node, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (Node node : nodes) {
            graph.addVertex(node);
        }

        int[][] adjacencyMatrix = {
            {0, 1, 0},
            {1, 0, 1},
            {0, 1, 0}
        };

        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    graph.addEdge(nodes[i], nodes[j]);
                    nodes[i].getConnection(nodes[j].getIp(), nodes[j].getPort());
                }
            }
        }

        for (Node node : nodes) {
            System.out.println(node);
        }
        return "Simulation completed";
    }
}
