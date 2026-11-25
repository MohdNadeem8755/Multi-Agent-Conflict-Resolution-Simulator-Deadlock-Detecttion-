package engine;

import manager.ResourceManager;
import manager.WaitingQueueManager;
import model.Conflict;
import model.Resource;
import model.ResourceRequest;
import scheduler.HybridScheduler;

public class ConflictEngine {

    private ResourceManager resourceManager;
    private WaitingQueueManager waitingQueueManager;
    private HybridScheduler scheduler;

    public ConflictEngine(ResourceManager resourceManager,
                          WaitingQueueManager waitingQueueManager,
                          HybridScheduler scheduler) {

        this.resourceManager = resourceManager;
        this.waitingQueueManager = waitingQueueManager;
        this.scheduler = scheduler;
    }

    // Process a resource request
    public int processRequest(ResourceRequest request) {

        Resource resource =
                resourceManager.getResource(request.getResourceId());

        if (resource == null) {

            System.out.println("Resource not found.");

            return -1;

        }

        // ==========================================
        // Resource is FREE
        // ==========================================

        if (resource.isFree()) {

            resourceManager.allocateResource(
                    request.getResourceId(),
                    request.getAgentId()
            );

            System.out.println(
                    "No Conflict. Resource allocated successfully."
            );

            // Resource mil gaya
            return request.getAgentId();

        }

        // ==========================================
        // Conflict Detected
        // ==========================================

        System.out.println();
        System.out.println("======================================");
        System.out.println("      CONFLICT DETECTED");
        System.out.println("======================================");

        System.out.println("Resource : " + resource.getResourceName());

        Conflict conflict =
                new Conflict(
                        1,
                        request.getResourceId()
                );

        // Current Owner
        conflict.addAgent(resource.getOwnerAgentId());

        // New Request
        conflict.addAgent(request.getAgentId());

        System.out.println(conflict);

        // ==========================================
        // Hybrid Scheduler
        // ==========================================

        System.out.println();
        System.out.println("======================================");
        System.out.println("      HYBRID SCHEDULER");
        System.out.println("======================================");

        int winner =
                scheduler.selectWinner(conflict);

        int loser;

        if (winner == resource.getOwnerAgentId()) {

            loser = request.getAgentId();

        } else {

            loser = resource.getOwnerAgentId();

        }

        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("Winner Agent : " + winner);
        System.out.println("Loser Agent  : " + loser);
        System.out.println("--------------------------------------");

        // Do not enqueue the loser here. Queueing happens during deadlock resolution.
        return winner;

    }

}