package test;

import graph.WaitForGraph;

public class WaitForGraphTest {

    public static void main(String[] args) {

        // Create Wait-For Graph
        WaitForGraph graph = new WaitForGraph();

        // Add Dependencies
        graph.addDependency(101, 102);
        graph.addDependency(102, 103);
        graph.addDependency(103, 101);

        System.out.println("\n===== INITIAL GRAPH =====");
        graph.displayGraph();

        // Remove One Dependency
        graph.removeDependency(103, 101);

        System.out.println("\n===== AFTER REMOVING DEPENDENCY =====");
        graph.displayGraph();

        // Add Dependency Again
        graph.addDependency(103, 101);

        System.out.println("\n===== AFTER ADDING DEPENDENCY AGAIN =====");
        graph.displayGraph();

        // Remove Agent
        graph.removeAgent(102);

        System.out.println("\n===== AFTER REMOVING AGENT 102 =====");
        graph.displayGraph();
    }
}