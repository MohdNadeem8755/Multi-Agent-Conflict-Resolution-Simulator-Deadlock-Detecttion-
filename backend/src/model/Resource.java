package model;

import java.util.LinkedList;
import java.util.Queue;

public class Resource {

    // Unique Resource ID
    private int resourceId;

    // Resource Name
    private String resourceName;

    // Current Status
    private ResourceStatus status;

    // Current Owner (Agent ID)
    private Integer ownerAgentId;

    // Agents waiting for this resource
    private Queue<Integer> waitingQueue;

    // Constructor
    public Resource(int resourceId, String resourceName) {

        this.resourceId = resourceId;
        this.resourceName = resourceName;

        this.status = ResourceStatus.FREE;

        this.ownerAgentId = null;

        this.waitingQueue = new LinkedList<>();
    }

    // ==========================
    // Getters
    // ==========================

    public int getResourceId() {
        return resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public ResourceStatus getStatus() {
        return status;
    }

    public Integer getOwnerAgentId() {
        return ownerAgentId;
    }

    public Queue<Integer> getWaitingQueue() {
        return waitingQueue;
    }

    // ==========================
    // Utility Methods
    // ==========================

    public boolean isFree() {
        return status == ResourceStatus.FREE;
    }

    public void allocateToAgent(int agentId) {

        this.ownerAgentId = agentId;
        this.status = ResourceStatus.BUSY;
    }

    public void release() {

        this.ownerAgentId = null;
        this.status = ResourceStatus.FREE;
    }

    public void addToWaitingQueue(int agentId) {

        waitingQueue.offer(agentId);
    }

    public Integer getNextWaitingAgent() {

        return waitingQueue.poll();
    }

    // ==========================
    // Display
    // ==========================

    @Override
    public String toString() {

        return "Resource{" +
                "resourceId=" + resourceId +
                ", resourceName='" + resourceName + '\'' +
                ", status=" + status +
                ", ownerAgentId=" + ownerAgentId +
                ", waitingQueue=" + waitingQueue +
                '}';
    }
}