package test;

import manager.ResourceManager;
import model.Resource;

public class ResourceManagerTest {

    public static void main(String[] args) {

        // Create Resource Manager
        ResourceManager resourceManager = new ResourceManager();

        // Create Resources
        Resource printer = new Resource(1, "Printer");
        Resource gpu = new Resource(2, "GPU");
        Resource scanner = new Resource(3, "Scanner");

        // Add Resources
        resourceManager.addResource(printer);
        resourceManager.addResource(gpu);
        resourceManager.addResource(scanner);

        // Display All Resources
        System.out.println("\n===== ALL RESOURCES =====");
        resourceManager.displayAllResources();

        // Search Resource
        System.out.println("\n===== SEARCH RESOURCE =====");
        System.out.println(resourceManager.getResource(2));

        // Check Availability
        System.out.println("\n===== RESOURCE AVAILABILITY =====");
        System.out.println("Printer Available : "
                + resourceManager.isResourceAvailable(1));

        // Allocate Resource
        System.out.println("\n===== ALLOCATE RESOURCE =====");
        resourceManager.allocateResource(1, 101);

        // Display After Allocation
        System.out.println("\n===== AFTER ALLOCATION =====");
        resourceManager.displayAllResources();

        // Release Resource
        System.out.println("\n===== RELEASE RESOURCE =====");
        resourceManager.releaseResource(1);

        // Display After Release
        System.out.println("\n===== AFTER RELEASE =====");
        resourceManager.displayAllResources();

        // Remove Resource
        System.out.println("\n===== REMOVE RESOURCE =====");
        resourceManager.removeResource(3);

        // Display After Removal
        System.out.println("\n===== AFTER REMOVAL =====");
        resourceManager.displayAllResources();

        // Total Resources
        System.out.println("\nTotal Resources : "
                + resourceManager.getTotalResources());

    }
}