package model;

public class ResourceRequest {

    // Unique Request ID
    private int requestId;

    // Agent requesting the resource
    private int agentId;

    // Requested Resource
    private int resourceId;

    // Time when request was made
    private long requestTime;

    // Priority of requesting agent
    private int priority;

    // Constructor
    public ResourceRequest(int requestId,
                           int agentId,
                           int resourceId,
                           int priority) {

        this.requestId = requestId;
        this.agentId = agentId;
        this.resourceId = resourceId;
        this.priority = priority;

        this.requestTime = System.currentTimeMillis();
    }

    // ==========================
    // Getters
    // ==========================

    public int getRequestId() {
        return requestId;
    }

    public int getAgentId() {
        return agentId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public int getPriority() {
        return priority;
    }

    // ==========================
    // Display
    // ==========================

    @Override
    public String toString() {
        return "ResourceRequest{" +
                "requestId=" + requestId +
                ", agentId=" + agentId +
                ", resourceId=" + resourceId +
                ", requestTime=" + requestTime +
                ", priority=" + priority +
                '}';
    }
}