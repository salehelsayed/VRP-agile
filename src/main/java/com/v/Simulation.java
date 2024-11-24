package com.v;

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
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                if (i != j) {
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
