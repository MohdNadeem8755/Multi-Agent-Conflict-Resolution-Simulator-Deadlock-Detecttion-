package test;

import engine.DeadlockResolver;
import graph.WaitForGraph;
import manager.AgentManager;
import manager.ResourceManager;
import model.Agent;
import model.Resource;

import java.util.Arrays;

public class DeadlockResolverTest {

    public static void main(String[] args) {

        // =====================================
        // Create Managers
        // =====================================

        AgentManager agentManager = new AgentManager();
        ResourceManager resourceManager = new ResourceManager();

        // =====================================
        // Create Agents
        // =====================================

        Agent agent1 = new Agent(
                101,
                "Agent-A",
                10,
                5,
                Arrays.asList("Printer")
        );

        Agent agent2 = new Agent(
                102,
                "Agent-B",
                8,
                4,
                Arrays.asList("GPU")
        );

        agentManager.addAgent(agent1);
        agentManager.addAgent(agent2);

        // =====================================
        // Create Resource
        // =====================================

        Resource printer = new Resource(1, "Printer");

        resourceManager.addResource(printer);

        // Allocate resource to Agent-B
        resourceManager.allocateResource(1, 102);

        // =====================================
        // Create Wait-For Graph
        // =====================================

        WaitForGraph graph = new WaitForGraph();

        graph.addDependency(101, 102);
        graph.addDependency(102, 101);

        System.out.println("\n===== WAIT-FOR GRAPH =====");

        graph.displayGraph();

        // =====================================
        // Create Deadlock Resolver
        // =====================================

        DeadlockResolver resolver =
                new DeadlockResolver(
                        agentManager,
                        resourceManager,
                        graph
                );

        // =====================================
        // Resolve Deadlock
        // =====================================

        System.out.println("\n===== RESOLVING DEADLOCK =====");

        resolver.resolveDeadlock(102, 1);

        // =====================================
        // Display Final State
        // =====================================

        System.out.println("\n===== FINAL RESOURCE =====");

        System.out.println(
                resourceManager.getResource(1)
        );

        System.out.println("\n===== FINAL GRAPH =====");

        graph.displayGraph();

        System.out.println("\n===== FINAL AGENT =====");

        System.out.println(
                agentManager.getAgent(102)
        );

    }

}