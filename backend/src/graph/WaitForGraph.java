package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WaitForGraph {

    // Adjacency List
    private Map<Integer, Set<Integer>> graph;

    // Constructor
    public WaitForGraph() {

        graph = new HashMap<>();

    }

    // ==========================
    // Add Dependency
    // Agent A waits for Agent B
    // ==========================

    public void addDependency(int fromAgent, int toAgent) {

        graph.putIfAbsent(fromAgent, new HashSet<>());

        graph.get(fromAgent).add(toAgent);

    }

    // ==========================
    // Remove Dependency
    // ==========================

    public void removeDependency(int fromAgent, int toAgent) {

        if(graph.containsKey(fromAgent)) {

            graph.get(fromAgent).remove(toAgent);

        }

    }

    // ==========================
    // Remove Entire Agent
    // ==========================

    public void removeAgent(int agentId) {

        graph.remove(agentId);

        for(Set<Integer> neighbours : graph.values()) {

            neighbours.remove(agentId);

        }

    }

    // ==========================
    // Get Complete Graph
    // ==========================

    public Map<Integer, Set<Integer>> getGraph() {

        return graph;

    }

    // ==========================
    // Display Graph
    // ==========================

    public void displayGraph() {

        System.out.println("===== Wait For Graph =====");

        for(Integer agent : graph.keySet()) {

            System.out.println(
                    agent +
                    " -> " +
                    graph.get(agent)
            );

        }

    }

}