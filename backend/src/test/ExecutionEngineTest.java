package test;

import engine.ExecutionEngine;
import manager.AgentManager;
import manager.ResourceManager;
import manager.WaitingQueueManager;
import model.Agent;
import model.Resource;

import java.util.Arrays;

public class ExecutionEngineTest {

    public static void main(String[] args) {

        // ===============================
        // Create Managers
        // ===============================

        AgentManager agentManager = new AgentManager();
        ResourceManager resourceManager = new ResourceManager();
        WaitingQueueManager waitingQueueManager = new WaitingQueueManager();

        // ===============================
        // Create Agent
        // ===============================

        Agent agent = new Agent(
                101,
                "Agent-A",
                10,
                3,
                Arrays.asList("Printer")
        );

        agentManager.addAgent(agent);

        // ===============================
        // Create Resource
        // ===============================

        Resource printer = new Resource(
                1,
                "Printer"
        );

        resourceManager.addResource(printer);

        // Allocate Resource
        resourceManager.allocateResource(1, 101);

        // Add Waiting Agent
        waitingQueueManager.addAgent(102);

        // ===============================
        // Create Execution Engine
        // ===============================

        ExecutionEngine executionEngine =
                new ExecutionEngine(
                        agentManager,
                        resourceManager,
                        waitingQueueManager
                );

        // ===============================
        // Execute Agent
        // ===============================

        System.out.println("\n===== EXECUTION START =====");

        executionEngine.executeAgent(101, 1);

        // ===============================
        // Final Status
        // ===============================

        System.out.println("\n===== FINAL AGENT STATUS =====");

        System.out.println(
                agentManager.getAgent(101)
        );

        System.out.println("\n===== RESOURCE STATUS =====");

        System.out.println(
                resourceManager.getResource(1)
        );

    }

}