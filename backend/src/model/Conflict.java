package model;

import java.util.ArrayList;
import java.util.List;

public class Conflict {

    // Unique Conflict ID
    private int conflictId;

    // Resource on which conflict occurred
    private int resourceId;

    // Agents involved in the conflict
    private List<Integer> agentIds;

    // Winning Agent
    private Integer winnerAgentId;

    // Time when conflict occurred
    private long conflictTime;

    // Constructor
    public Conflict(int conflictId, int resourceId) {

        this.conflictId = conflictId;
        this.resourceId = resourceId;
        this.agentIds = new ArrayList<>();
        this.winnerAgentId = null;
        this.conflictTime = System.currentTimeMillis();
    }

    // ==========================
    // Getters
    // ==========================

    public int getConflictId() {
        return conflictId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public List<Integer> getAgentIds() {
        return agentIds;
    }

    public Integer getWinnerAgentId() {
        return winnerAgentId;
    }

    public long getConflictTime() {
        return conflictTime;
    }

    // ==========================
    // Utility Methods
    // ==========================

    // Add an agent involved in the conflict
    public void addAgent(int agentId) {
        agentIds.add(agentId);
    }

    // Set the winner selected by the scheduler
    public void setWinnerAgentId(Integer winnerAgentId) {
        this.winnerAgentId = winnerAgentId;
    }

    // ==========================
    // Display
    // ==========================

    @Override
    public String toString() {
        return "Conflict{" +
                "conflictId=" + conflictId +
                ", resourceId=" + resourceId +
                ", agentIds=" + agentIds +
                ", winnerAgentId=" + winnerAgentId +
                ", conflictTime=" + conflictTime +
                '}';
    }
}