package test;

import manager.AgentManager;
import model.Agent;
import model.AgentStatus;

import java.util.Arrays;

public class AgentManagerTest {

    public static void main(String[] args) {

        AgentManager agentManager = new AgentManager();

        Agent agent1 = new Agent(
                101,
                "Agent-A",
                10,
                5,
                Arrays.asList("Printer", "GPU")
        );

        Agent agent2 = new Agent(
                102,
                "Agent-B",
                8,
                4,
                Arrays.asList("Scanner")
        );

        Agent agent3 = new Agent(
                103,
                "Agent-C",
                6,
                3,
                Arrays.asList("Printer")
        );

        // Add Agents
        agentManager.addAgent(agent1);
        agentManager.addAgent(agent2);
        agentManager.addAgent(agent3);

        // Display All
        System.out.println("\n===== ALL AGENTS =====");
        agentManager.displayAllAgents();

        // Search
        System.out.println("\n===== SEARCH AGENT =====");
        System.out.println(agentManager.getAgent(102));

        // Update Status
        agentManager.updateStatus(102, AgentStatus.RUNNING);

        System.out.println("\n===== AFTER STATUS UPDATE =====");
        System.out.println(agentManager.getAgent(102));

        // Waiting Time
        agentManager.increaseWaitingTime(103);
        agentManager.increaseWaitingTime(103);

        System.out.println("\n===== AFTER WAITING TIME UPDATE =====");
        System.out.println(agentManager.getAgent(103));

        // Count
        System.out.println("\nTotal Agents : " + agentManager.getTotalAgents());

        // Remove
        agentManager.removeAgent(101);

        System.out.println("\n===== AFTER REMOVE =====");
        agentManager.displayAllAgents();

        System.out.println("\nTotal Agents : " + agentManager.getTotalAgents());
    }
}