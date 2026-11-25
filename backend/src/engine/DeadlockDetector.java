package engine;

import graph.WaitForGraph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeadlockDetector {

    private WaitForGraph graph;

    public DeadlockDetector(WaitForGraph graph) {
        this.graph = graph;
    }

    // Detect Deadlock
    public boolean detectDeadlock() {

        Map<Integer, Set<Integer>> adjacencyList =
                graph.getGraph();

        Set<Integer> visited = new HashSet<>();

        Set<Integer> recursionStack = new HashSet<>();

        for(Integer node : adjacencyList.keySet()) {

            if(dfs(node, adjacencyList, visited, recursionStack)) {

                System.out.println("Deadlock Detected!");

                return true;

            }

        }

        System.out.println("No Deadlock.");

        return false;

    }

    // DFS
    private boolean dfs(Integer current,
                        Map<Integer, Set<Integer>> graph,
                        Set<Integer> visited,
                        Set<Integer> recursionStack) {

        if(recursionStack.contains(current)) {

            return true;

        }

        if(visited.contains(current)) {

            return false;

        }

        visited.add(current);

        recursionStack.add(current);

        for(Integer neighbour :
                graph.getOrDefault(current,new HashSet<>())) {

            if(dfs(neighbour,
                    graph,
                    visited,
                    recursionStack)) {

                return true;

            }

        }

        recursionStack.remove(current);

        return false;

    }

}