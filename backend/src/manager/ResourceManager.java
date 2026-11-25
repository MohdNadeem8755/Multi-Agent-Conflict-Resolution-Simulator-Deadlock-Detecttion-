package manager;

import model.Resource;

import java.util.Collection;
import java.util.HashMap;

public class ResourceManager {

    // Stores all resources
    private HashMap<Integer, Resource> resources;

    // Constructor
    public ResourceManager() {
        resources = new HashMap<>();
    }

    // ==========================
    // Add Resource
    // ==========================

    public void addResource(Resource resource) {

        resources.put(resource.getResourceId(), resource);

        System.out.println("Resource Added : " + resource.getResourceName());

    }

    // ==========================
    // Remove Resource
    // ==========================

    public void removeResource(int resourceId) {

        if(resources.containsKey(resourceId)) {

            resources.remove(resourceId);

            System.out.println("Resource Removed : " + resourceId);

        }
        else {

            System.out.println("Resource Not Found.");

        }

    }

    // ==========================
    // Search Resource
    // ==========================

    public Resource getResource(int resourceId) {

        return resources.get(resourceId);

    }

    // ==========================
    // Snapshot All Resources
    // ==========================

    public Collection<Resource> getAllResources() {

        return resources.values();

    }

    // ==========================
    // Check Availability
    // ==========================

    public boolean isResourceAvailable(int resourceId) {

        Resource resource = resources.get(resourceId);

        if(resource == null) {

            return false;

        }

        return resource.isFree();

    }

    // ==========================
    // Allocate Resource
    // ==========================

    public boolean allocateResource(int resourceId, int agentId) {

        Resource resource = resources.get(resourceId);

        if(resource != null && resource.isFree()) {

            resource.allocateToAgent(agentId);

            System.out.println(
                    resource.getResourceName() +
                    " allocated to Agent " +
                    agentId);

            return true;

        }

        return false;

    }

    // ==========================
    // Release Resource
    // ==========================

    public void releaseResource(int resourceId) {

        Resource resource = resources.get(resourceId);

        if(resource != null) {

            resource.release();

            System.out.println(
                    resource.getResourceName() +
                    " released."
            );

        }

    }

    // ==========================
    // Display All Resources
    // ==========================

    public void displayAllResources() {

        Collection<Resource> values = resources.values();

        for(Resource resource : values) {

            System.out.println(resource);

        }

    }

    // ==========================
    // Total Resources
    // ==========================

    public int getTotalResources() {

        return resources.size();

    }

}