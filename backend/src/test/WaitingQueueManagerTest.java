package test;

import manager.WaitingQueueManager;

public class WaitingQueueManagerTest {

    public static void main(String[] args) {

        // Create Waiting Queue Manager
        WaitingQueueManager queueManager = new WaitingQueueManager();

        // Add Agents to Queue
        queueManager.addAgent(101);
        queueManager.addAgent(102);
        queueManager.addAgent(103);
        queueManager.addAgent(104);

        // Display Queue
        System.out.println("\n===== WAITING QUEUE =====");
        queueManager.displayQueue();

        // Queue Size
        System.out.println("\nQueue Size : "
                + queueManager.getQueueSize());

        // Peek Next Agent
        System.out.println("\nNext Agent : "
                + queueManager.peekNextAgent());

        // Remove Next Agent
        System.out.println("\nDequeued Agent : "
                + queueManager.getNextAgent());

        // Display Queue Again
        System.out.println("\n===== AFTER DEQUEUE =====");
        queueManager.displayQueue();

        // Queue Size Again
        System.out.println("\nQueue Size : "
                + queueManager.getQueueSize());

        // Remove Remaining Agents
        System.out.println("\nDequeued Agent : "
                + queueManager.getNextAgent());

        System.out.println("Dequeued Agent : "
                + queueManager.getNextAgent());

        System.out.println("Dequeued Agent : "
                + queueManager.getNextAgent());

        // Check Queue Empty
        System.out.println("\nQueue Empty : "
                + queueManager.isQueueEmpty());

        // Display Empty Queue
        System.out.println("\n===== FINAL QUEUE =====");
        queueManager.displayQueue();

    }
}