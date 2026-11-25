package model;

import java.util.ArrayList;
import java.util.List;

public class Agent {

    // Unique Agent ID
    private int agentId;

    // Agent Name
    private String agentName;

    // Higher value = Higher Priority
    private int priority;

    // Time when agent was created
    private long arrivalTime;

    // Time spent waiting for resources
    private int waitingTime;

    // Total execution time (seconds)
    private int executionTime;

    // Current status of the agent
    private AgentStatus status;

    // Resources required by this agent
    private List<String> requiredResources;

    // Resources currently allocated
    private List<String> allocatedResources;

    // Constructor
    public Agent(int agentId,
                 String agentName,
                 int priority,
                 int executionTime,
                 List<String> requiredResources) {

        this.agentId = agentId;
        this.agentName = agentName;
        this.priority = priority;
        this.executionTime = executionTime;

        this.arrivalTime = System.currentTimeMillis();

        this.waitingTime = 0;

        this.status = AgentStatus.READY;

        this.requiredResources = requiredResources;

        this.allocatedResources = new ArrayList<>();
    }

    // ===========================
    // Getters
    // ===========================

    public int getAgentId() {
        return agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public int getPriority() {
        return priority;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public AgentStatus getStatus() {
        return status;
    }

    public List<String> getRequiredResources() {
        return requiredResources;
    }

    public List<String> getAllocatedResources() {
        return allocatedResources;
    }

    // ===========================
    // Setters
    // ===========================

    public void setStatus(AgentStatus status) {
        this.status = status;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    // ===========================
    // Utility Methods
    // ===========================

    public void increaseWaitingTime() {
        waitingTime++;
    }

    public void resetWaitingTime() {
        waitingTime = 0;
    }

    public void allocateResource(String resource) {
        allocatedResources.add(resource);
    }

    public void releaseResource(String resource) {
        allocatedResources.remove(resource);
    }

    // ===========================
    // Display Agent Details
    // ===========================

    @Override
    public String toString() {
        return "Agent{" +
                "agentId=" + agentId +
                ", agentName='" + agentName + '\'' +
                ", priority=" + priority +
                ", arrivalTime=" + arrivalTime +
                ", waitingTime=" + waitingTime +
                ", executionTime=" + executionTime +
                ", status=" + status +
                ", requiredResources=" + requiredResources +
                ", allocatedResources=" + allocatedResources +
                '}';
    }
}