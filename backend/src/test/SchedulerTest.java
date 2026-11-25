package test;

import manager.AgentManager;
import model.Agent;
import model.Conflict;
import scheduler.HybridScheduler;

import java.util.Arrays;

public class SchedulerTest {

    public static void main(String[] args) {

        // Create Agent Manager
        AgentManager agentManager = new AgentManager();

        // Create Agents
        Agent agent1 = new Agent(
                101,
                "Agent-A",
                8,
                5,
                Arrays.asList("Printer")
        );

        Agent agent2 = new Agent(
                102,
                "Agent-B",
                10,
                4,
                Arrays.asList("Printer")
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

        // Create Conflict
        Conflict conflict = new Conflict(1, 1);

        conflict.addAgent(101);
        conflict.addAgent(102);
        conflict.addAgent(103);

        // Create Scheduler
        HybridScheduler scheduler =
                new HybridScheduler(agentManager);

        // Select Winner
        int winner = scheduler.selectWinner(conflict);

        // Display Result
        System.out.println("\n===== SCHEDULER RESULT =====");

        System.out.println("Winner Agent ID : " + winner);

        System.out.println("Winner Stored In Conflict : "
                + conflict.getWinnerAgentId());

    }

}