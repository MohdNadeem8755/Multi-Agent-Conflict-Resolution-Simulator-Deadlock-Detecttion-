package test;

import engine.DeadlockDetector;
import graph.WaitForGraph;

public class DeadlockDetectorTest {

    public static void main(String[] args) {

        // ===============================
        // Create Wait-For Graph
        // ===============================

        WaitForGraph graph = new WaitForGraph();

        // Create Deadlock
        // 101 -> 102 -> 103 -> 101

        graph.addDependency(101, 102);
        graph.addDependency(102, 103);
        graph.addDependency(103, 101);

        System.out.println("\n===== WAIT-FOR GRAPH =====");
        graph.displayGraph();

        // ===============================
        // Create Deadlock Detector
        // ===============================

        DeadlockDetector detector =
                new DeadlockDetector(graph);

        // ===============================
        // Detect Deadlock
        // ===============================

        System.out.println("\n===== DEADLOCK DETECTION =====");

        boolean deadlock = detector.detectDeadlock();

        if (deadlock) {

            System.out.println("\nDeadlock Found!");

        } else {

            System.out.println("\nNo Deadlock.");

        }

    }

}