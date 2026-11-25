package engine;

import manager.AgentManager;
import manager.ResourceManager;
import manager.WaitingQueueManager;
import model.Agent;
import model.AgentStatus;

public class ExecutionEngine {

    private AgentManager agentManager;
    private ResourceManager resourceManager;
    private WaitingQueueManager waitingQueueManager;

    public ExecutionEngine(AgentManager agentManager,
                           ResourceManager resourceManager,
                           WaitingQueueManager waitingQueueManager) {

        this.agentManager = agentManager;
        this.resourceManager = resourceManager;
        this.waitingQueueManager = waitingQueueManager;
    }

    // Execute an Agent
    public void executeAgent(int agentId, int resourceId) {

        Agent agent = agentManager.getAgent(agentId);

        if (agent == null) {

            System.out.println("Agent not found.");
            return;
        }

        // Change status to RUNNING
        agentManager.updateStatus(agentId, AgentStatus.RUNNING);

        System.out.println("--------------------------------");
        System.out.println("Executing : " + agent.getAgentName());
        System.out.println("--------------------------------");

        try {

            Thread.sleep(agent.getExecutionTime() * 1000);

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

        // Release Resource
        resourceManager.releaseResource(resourceId);

        // Change Status
        agentManager.updateStatus(agentId, AgentStatus.FINISHED);

        System.out.println(agent.getAgentName() + " Finished Execution.");

        // Check Waiting Queue
        if (!waitingQueueManager.isQueueEmpty()) {

            Integer nextAgent =
                    waitingQueueManager.getNextAgent();

            System.out.println(
                    "Next Waiting Agent : " +
                    nextAgent
            );

        }

    }

}