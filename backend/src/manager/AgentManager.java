package manager;

import model.Agent;
import model.AgentStatus;

import java.util.Collection;
import java.util.HashMap;

public class AgentManager {

    // Stores all agents
    private HashMap<Integer, Agent> agents;

    // Constructor
    public AgentManager() {

        agents = new HashMap<>();

    }

    // ==========================
    // Add Agent
    // ==========================

    public void addAgent(Agent agent) {

        agents.put(agent.getAgentId(), agent);

        System.out.println("Agent Added : " + agent.getAgentName());

    }

    // ==========================
    // Remove Agent
    // ==========================

    public void removeAgent(int agentId) {

        if (agents.containsKey(agentId)) {

            agents.remove(agentId);

            System.out.println("Agent Removed : " + agentId);

        } else {

            System.out.println("Agent Not Found.");

        }

    }

    // ==========================
    // Search Agent
    // ==========================

    public Agent getAgent(int agentId) {

        return agents.get(agentId);

    }

    // ==========================
    // Snapshot All Agents
    // ==========================

    public Collection<Agent> getAllAgents() {

        return agents.values();

    }

    // ==========================
    // Update Status
    // ==========================

    public void updateStatus(int agentId, AgentStatus status) {

        Agent agent = agents.get(agentId);

        if (agent != null) {

            agent.setStatus(status);

        }

    }

    // ==========================
    // Increase Waiting Time
    // ==========================

    public void increaseWaitingTime(int agentId) {

        Agent agent = agents.get(agentId);

        if (agent != null) {

            agent.increaseWaitingTime();

        }

    }

    // ==========================
    // Reset Waiting Time
    // ==========================

    public void resetWaitingTime(int agentId) {

        Agent agent = agents.get(agentId);

        if (agent != null) {

            agent.resetWaitingTime();

        }

    }

    // ==========================
    // Display All Agents
    // ==========================

    public void displayAllAgents() {

        Collection<Agent> values = agents.values();

        for (Agent agent : values) {

            System.out.println(agent);

        }

    }

    // ==========================
    // Total Agents
    // ==========================

    public int getTotalAgents() {

        return agents.size();

    }

}