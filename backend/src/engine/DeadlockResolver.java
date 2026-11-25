package engine;

import graph.WaitForGraph;
import manager.AgentManager;
import manager.ResourceManager;
import model.Agent;
import model.AgentStatus;
import model.Resource;

public class DeadlockResolver {

    private AgentManager agentManager;
    private ResourceManager resourceManager;
    private WaitForGraph waitForGraph;

    public DeadlockResolver(AgentManager agentManager,
                            ResourceManager resourceManager,
                            WaitForGraph waitForGraph) {

        this.agentManager = agentManager;
        this.resourceManager = resourceManager;
        this.waitForGraph = waitForGraph;
    }

    // Resolve Deadlock
    public void resolveDeadlock(int victimAgentId, int resourceId) {

        Agent victim = agentManager.getAgent(victimAgentId);

        if (victim == null) {

            System.out.println("Victim Agent Not Found.");
            return;

        }

        System.out.println("--------------------------------");
        System.out.println("Deadlock Resolver Started");
        System.out.println("--------------------------------");

        System.out.println(
                "Victim Selected : "
                        + victim.getAgentName()
        );

        // ================================
        // Release Resource Only If Busy
        // ================================

        Resource resource =
                resourceManager.getResource(resourceId);

        if (resource != null && !resource.isFree()) {

            resourceManager.releaseResource(resourceId);

        } else {

            System.out.println("Resource already free.");

        }

        // ================================
        // Change Agent Status
        // ================================

        agentManager.updateStatus(
                victimAgentId,
                AgentStatus.BLOCKED
        );

        // ================================
        // Remove Dependencies
        // ================================

        waitForGraph.removeAgent(victimAgentId);

        System.out.println(
                "Deadlock Resolved Successfully."
        );

    }

}