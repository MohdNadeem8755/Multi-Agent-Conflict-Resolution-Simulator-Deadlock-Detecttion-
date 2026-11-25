package test;

import engine.ConflictEngine;
import manager.AgentManager;
import manager.ResourceManager;
import manager.WaitingQueueManager;
import model.Agent;
import model.Resource;
import model.ResourceRequest;
import scheduler.HybridScheduler;

import java.util.Arrays;

public class ConflictEngineTest {

    public static void main(String[] args) {

        // =====================================
        // Create Managers
        // =====================================

        AgentManager agentManager = new AgentManager();
        ResourceManager resourceManager = new ResourceManager();
        WaitingQueueManager waitingQueueManager = new WaitingQueueManager();

        // =====================================
        // Create Scheduler
        // =====================================

        HybridScheduler scheduler =
                new HybridScheduler(agentManager);

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
                Arrays.asList("Printer")
        );

        agentManager.addAgent(agent1);
        agentManager.addAgent(agent2);

        // =====================================
        // Create Resource
        // =====================================

        Resource printer = new Resource(1, "Printer");

        resourceManager.addResource(printer);

        // =====================================
        // Create Conflict Engine
        // =====================================

        ConflictEngine conflictEngine =
                new ConflictEngine(
                        resourceManager,
                        waitingQueueManager,
                        scheduler
                );

        // =====================================
        // First Request
        // =====================================

        ResourceRequest request1 =
                new ResourceRequest(
                        1,
                        101,
                        1,
                        10
                );

        System.out.println("\n===== FIRST REQUEST =====");

        conflictEngine.processRequest(request1);

        // =====================================
        // Second Request (Conflict)
        // =====================================

        ResourceRequest request2 =
                new ResourceRequest(
                        2,
                        102,
                        1,
                        8
                );

        System.out.println("\n===== SECOND REQUEST =====");

        conflictEngine.processRequest(request2);

        // =====================================
        // Waiting Queue
        // =====================================

        System.out.println("\n===== WAITING QUEUE =====");

        waitingQueueManager.displayQueue();

    }

}