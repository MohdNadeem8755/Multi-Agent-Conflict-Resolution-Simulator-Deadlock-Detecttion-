package manager;

import java.util.LinkedList;
import java.util.Queue;

public class WaitingQueueManager {

    // Waiting Queue
    private Queue<Integer> waitingQueue;

    // Constructor
    public WaitingQueueManager() {

        waitingQueue = new LinkedList<>();

    }

    // ==========================
    // Add Agent to Queue
    // ==========================

    public void addAgent(int agentId) {

        if (waitingQueue.contains(agentId)) {

            System.out.println(
                    "Agent " +
                    agentId +
                    " is already in the waiting queue."
            );

            return;

        }

        waitingQueue.offer(agentId);

        System.out.println(
                "Agent " +
                agentId +
                " added to waiting queue."
        );

    }

    // ==========================
    // Remove Next Agent
    // ==========================

    public Integer getNextAgent() {

        return waitingQueue.poll();

    }

    // ==========================
    // View Next Agent
    // ==========================

    public Integer peekNextAgent() {

        return waitingQueue.peek();

    }

    // ==========================
    // Queue Empty?
    // ==========================

    public boolean isQueueEmpty() {

        return waitingQueue.isEmpty();

    }

    // ==========================
    // Queue Size
    // ==========================

    public int getQueueSize() {

        return waitingQueue.size();

    }

    // ==========================
    // Snapshot Queue
    // ==========================

    public java.util.List<Integer> getWaitingAgents() {

        return new java.util.ArrayList<>(waitingQueue);

    }

    // ==========================
    // Display Queue
    // ==========================

    public void displayQueue() {

        if(waitingQueue.isEmpty()) {

            System.out.println("Waiting Queue is Empty.");

            return;
        }

        System.out.println("Waiting Queue:");

        for(Integer agentId : waitingQueue) {

            System.out.println("Agent " + agentId);

        }

    }

}