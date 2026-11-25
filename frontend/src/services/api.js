// ============================================
// Backend Base URL
// ============================================

const BASE_URL = "http://localhost:8080/api";

const MOCK_SNAPSHOT = {
    stats: {
        started: true,
        conflicts: 0,
        deadlocks: 0
    },
    agents: [
        { agentId: 101, agentName: "Agent-A", priority: 10, status: "RUNNING" },
        { agentId: 102, agentName: "Agent-B", priority: 8, status: "READY" },
        { agentId: 103, agentName: "Agent-C", priority: 6, status: "READY" }
    ],
    resources: [
        { resourceId: 1, resourceName: "Printer", status: "BUSY", ownerAgentId: 101 },
        { resourceId: 2, resourceName: "GPU", status: "FREE", ownerAgentId: null }
    ],
    queue: [],
    logs: [
        { time: "14:10:00", message: "Simulation ready: Agent-A owns Printer." }
    ],
    system: {
        executionStatus: "Running Normally",
        conflictEngineStatus: "Monitoring Resources",
        deadlockDetectorStatus: "No Deadlock Found"
    },
    incident: {
        phase: "SAFE",
        title: "System is ready",
        resourceId: 1,
        resourceName: "Printer",
        ownerAgentName: "Agent-A",
        waitingAgentName: null,
        blockerText: "All agents are ready. Generate a conflict to see Agent-B wait for Printer.",
        resolutionText: "The system is stable until a resource conflict occurs.",
        victimAgentName: null,
        winnerAgentName: null
    }
};

const request = async (path, method = "GET") => {
    try {
        const response = await fetch(`${BASE_URL}${path}`, {
            method: method
        });

        if (!response.ok) {
            throw new Error(`Request failed with status ${response.status}`);
        }

        return response.json();
    } catch (err) {
        // Network or CORS error: provide a graceful mock fallback for development
        // If the client was asking for the snapshot, return a sensible mock
        console.warn("API request failed, using mock fallback:", err.message);

        if (path === "/snapshot" && method === "GET") {
            return MOCK_SNAPSHOT;
        }

        // For POST operations just return a simple success object so UI doesn't break
        return { success: true };
    }
};

// ============================================
// Simulator APIs
// ============================================

export const getSimulationSnapshot = () => {

    return request("/snapshot");

};

export const startSimulation = () => request("/start", "POST");

export const generateConflict = () => request("/conflict", "POST");

export const resolveDeadlock = () => request("/resolve", "POST");

export const resetSimulation = () => request("/reset", "POST");