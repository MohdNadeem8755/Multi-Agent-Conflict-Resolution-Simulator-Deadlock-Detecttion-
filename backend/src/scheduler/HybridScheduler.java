package scheduler;

import manager.AgentManager;
import model.Agent;
import model.Conflict;

import java.util.Comparator;
import java.util.PriorityQueue;

public class HybridScheduler implements Scheduler {

    private AgentManager agentManager;

    public HybridScheduler(AgentManager agentManager) {
        this.agentManager = agentManager;
    }

    @Override
    public int selectWinner(Conflict conflict) {

        PriorityQueue<Agent> queue =
                new PriorityQueue<>(new Comparator<Agent>() {

                    @Override
                    public int compare(Agent a1, Agent a2) {

                        // Higher Priority Wins
                        if (a1.getPriority() != a2.getPriority()) {

                            return Integer.compare(
                                    a2.getPriority(),
                                    a1.getPriority());

                        }

                        // Earlier Arrival Wins
                        if (a1.getArrivalTime() != a2.getArrivalTime()) {

                            return Long.compare(
                                    a1.getArrivalTime(),
                                    a2.getArrivalTime());

                        }

                        // Longer Waiting Wins
                        if (a1.getWaitingTime() != a2.getWaitingTime()) {

                            return Integer.compare(
                                    a2.getWaitingTime(),
                                    a1.getWaitingTime());

                        }

                        // Smaller Agent ID Wins
                        return Integer.compare(
                                a1.getAgentId(),
                                a2.getAgentId());

                    }

                });

        // Add all conflict agents
        for (Integer agentId : conflict.getAgentIds()) {

            Agent agent = agentManager.getAgent(agentId);

            if (agent != null) {

                queue.offer(agent);

            }

        }

        Agent winner = queue.poll();

        if (winner == null) {

            return -1;

        }

        conflict.setWinnerAgentId(winner.getAgentId());

        System.out.println(
                "Winner Selected : " +
                        winner.getAgentName());

        return winner.getAgentId();

    }

}