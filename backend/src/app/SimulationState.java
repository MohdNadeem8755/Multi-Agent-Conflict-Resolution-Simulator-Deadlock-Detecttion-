package app;

import engine.ConflictEngine;
import engine.DeadlockResolver;
import graph.WaitForGraph;
import manager.AgentManager;
import manager.ResourceManager;
import manager.WaitingQueueManager;
import model.Agent;
import model.AgentStatus;
import model.Conflict;
import model.Resource;
import scheduler.HybridScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulationState {

    private static class LogEntry {

        private final String time;
        private final String message;

        private LogEntry(String time, String message) {
            this.time = time;
            this.message = message;
        }

    }

    private static class Incident {

        private String phase;
        private String title;
        private int resourceId;
        private String resourceName;
        private String ownerAgentName;
        private String waitingAgentName;
        private String blockerText;
        private String resolutionText;
        private String victimAgentName;
        private String winnerAgentName;

    }

    private final AgentManager agentManager = new AgentManager();
    private final ResourceManager resourceManager = new ResourceManager();
    private final WaitingQueueManager waitingQueueManager = new WaitingQueueManager();
    private final WaitForGraph waitForGraph = new WaitForGraph();
    private final HybridScheduler scheduler = new HybridScheduler(agentManager);
    private final ConflictEngine conflictEngine = new ConflictEngine(resourceManager, waitingQueueManager, scheduler);
    private final DeadlockResolver deadlockResolver = new DeadlockResolver(agentManager, resourceManager, waitForGraph);

    private final List<LogEntry> logs = new ArrayList<>();

    private int conflicts;
    private int deadlocks;
    private String executionStatus;
    private String conflictEngineStatus;
    private String deadlockDetectorStatus;
    private boolean simulationStarted;
    private Incident incident;

    public SimulationState() {
        loadIdleState();
    }

    public synchronized String snapshotJson() {
        return "{"
                + "\"agents\":" + agentsJson()
                + ",\"resources\":" + resourcesJson()
                + ",\"queue\":" + queueJson()
                + ",\"logs\":" + logsJson()
                + ",\"stats\":" + statsJson()
                + ",\"incident\":" + incidentJson()
                + ",\"system\":" + systemJson()
                + "}";
    }

    public synchronized void start() {
        loadActiveState(true);
    }

    public synchronized void generateConflict() {
        Resource printer = resourceManager.getResource(1);
        Resource gpu = resourceManager.getResource(2);

        if (printer == null || gpu == null) {
            appendLog("Unable to create deadlock: missing resources.");
            return;
        }

        if (printer.isFree() || gpu.isFree()) {
            appendLog("Resources are not fully allocated; cannot form a circular wait deadlock.");
            return;
        }

        // Create a real deadlock cycle: A holds Printer and requests GPU, B holds GPU and requests Printer.
        waitForGraph.addDependency(101, 102);
        waitForGraph.addDependency(102, 101);

        agentManager.updateStatus(101, AgentStatus.BLOCKED);
        agentManager.updateStatus(102, AgentStatus.BLOCKED);
        agentManager.updateStatus(103, AgentStatus.READY);

        conflicts = 1;
        deadlocks = 1;
        executionStatus = "Deadlock Detected";
        conflictEngineStatus = "Resource conflict detected on Printer and GPU";
        deadlockDetectorStatus = "Circular wait detected";
        incident = createConflictIncident();

        appendLog("Agent-A acquired Printer.");
        appendLog("Agent-B acquired GPU.");
        appendLog("Agent-A requested GPU.");
        appendLog("Agent-B requested Printer.");
        appendLog("Circular wait formed.");
        appendLog("Deadlock detected.");
    }

    public synchronized void resolveDeadlock() {
        int victimAgentId = 102;
        int resourceId = 2;

        appendLog("Victim selected: Agent-B.");

        deadlockResolver.resolveDeadlock(victimAgentId, resourceId);
        appendLog("GPU released.");

        // Determine winner from the deadlock cycle using the scheduler
        Conflict conflict = new Conflict(1, resourceId);
        conflict.addAgent(101);
        conflict.addAgent(102);

        int winnerAgentId = scheduler.selectWinner(conflict);
        int loserAgentId = winnerAgentId == 101 ? 102 : 101;
        String winnerName = agentManager.getAgent(winnerAgentId).getAgentName();
        String loserName = agentManager.getAgent(loserAgentId).getAgentName();

        // Allocate the released GPU to the winner and start execution
        if (resourceManager.allocateResource(resourceId, winnerAgentId)) {
            agentManager.updateStatus(winnerAgentId, AgentStatus.RUNNING);
            appendLog("GPU allocated to " + winnerName + ".");
            appendLog(winnerName + " started execution.");
        }

        // The loser returns to the ready state after the conflict is resolved
        agentManager.updateStatus(loserAgentId, AgentStatus.READY);
        appendLog(loserName + " is ready after resolution.");

        resourceManager.releaseResource(1);
        appendLog("Printer released.");

        resourceManager.allocateResource(1, loserAgentId);
        appendLog("Printer allocated to " + loserName + ".");

        agentManager.updateStatus(103, AgentStatus.READY);

        conflicts = 0;
        deadlocks = 0;
        executionStatus = "Deadlock resolved successfully";
        conflictEngineStatus = "Resources reassigned";
        deadlockDetectorStatus = "Cycle broken";
        incident = createResolvedIncident();

        appendLog("Deadlock resolved.");
    }

    public synchronized void reset() {
        loadIdleState();
    }

    private void loadIdleState() {
        agentManager.getAllAgents().clear();
        resourceManager.getAllResources().clear();
        while (!waitingQueueManager.isQueueEmpty()) {
            waitingQueueManager.getNextAgent();
        }

        agentManager.addAgent(new Agent(101, "Agent-A", 10, 3, Arrays.asList("Printer")));
        agentManager.addAgent(new Agent(102, "Agent-B", 8, 4, Arrays.asList("GPU")));
        agentManager.addAgent(new Agent(103, "Agent-C", 6, 2, Arrays.asList()));

        resourceManager.addResource(new Resource(1, "Printer"));
        resourceManager.addResource(new Resource(2, "GPU"));

        logs.clear();

        conflicts = 0;
        deadlocks = 0;
        simulationStarted = false;
        executionStatus = "Idle";
        conflictEngineStatus = "Press Run to start simulation";
        deadlockDetectorStatus = "Idle";
        incident = createIdleIncident();

        appendLog("Simulation reset: all agents are ready and no resources are contested.");
    }

    private void loadActiveState(boolean addLog) {
        agentManager.getAllAgents().clear();
        resourceManager.getAllResources().clear();
        while (!waitingQueueManager.isQueueEmpty()) {
            waitingQueueManager.getNextAgent();
        }

        agentManager.addAgent(new Agent(101, "Agent-A", 10, 3, Arrays.asList("Printer")));
        agentManager.addAgent(new Agent(102, "Agent-B", 8, 4, Arrays.asList("GPU")));
        agentManager.addAgent(new Agent(103, "Agent-C", 6, 2, Arrays.asList()));

        resourceManager.addResource(new Resource(1, "Printer"));
        resourceManager.addResource(new Resource(2, "GPU"));

        resourceManager.allocateResource(1, 101);
        resourceManager.allocateResource(2, 102);
        agentManager.updateStatus(101, AgentStatus.RUNNING);
        agentManager.updateStatus(102, AgentStatus.RUNNING);
        agentManager.updateStatus(103, AgentStatus.READY);

        logs.clear();
        appendInitialLogs();

        conflicts = 0;
        deadlocks = 0;
        simulationStarted = true;
        executionStatus = "Running Normally";
        conflictEngineStatus = "Monitoring Resources";
        deadlockDetectorStatus = "No Deadlock Found";
        incident = createInitialIncident();

        if (addLog) {
            appendLog("Simulation started with Printer assigned to Agent-A");
        }
    }

    private void appendInitialLogs() {
        logs.add(new LogEntry(currentTime(), "Agent-A acquired Printer."));
        logs.add(new LogEntry(currentTime(), "Agent-B acquired GPU."));
    }

    private void removeRuntimeLogs() {
        logs.clear();
    }

    private void appendLog(String message) {
        logs.add(new LogEntry(currentTime(), message));
    }

    private String currentTime() {
        return java.time.LocalTime.now().withNano(0).toString();
    }

    private Incident createInitialIncident() {
        Incident data = new Incident();
        data.phase = "SAFE";
        data.title = "System is ready";
        data.resourceId = 1;
        data.resourceName = "Printer";
        data.ownerAgentName = "Agent-A";
        data.waitingAgentName = null;
        data.blockerText = "Agent-A owns the Printer and Agent-B owns the GPU. Generate a conflict to form a circular wait deadlock.";
        data.resolutionText = "The system is stable until a resource conflict forms a cycle.";
        data.victimAgentName = null;
        data.winnerAgentName = null;
        return data;
    }

    private Incident createIdleIncident() {
        Incident data = new Incident();
        data.phase = "IDLE";
        data.title = "Press Run to start simulation";
        data.resourceId = 0;
        data.resourceName = "No active resource";
        data.ownerAgentName = "None";
        data.waitingAgentName = null;
        data.blockerText = "No simulation is running yet. Click Start Simulation to load agents and resources.";
        data.resolutionText = "After start, you can generate a conflict and watch the queue resolve it.";
        data.victimAgentName = null;
        data.winnerAgentName = null;
        return data;
    }

    private Incident createConflictIncident() {
        Incident data = new Incident();
        data.phase = "CONFLICT";
        data.title = "Deadlock detected";
        data.resourceId = 1;
        data.resourceName = "Printer";
        data.ownerAgentName = "Agent-A";
        data.waitingAgentName = "Agent-A and Agent-B";
        data.blockerText = "Agent-A owns Printer but requests GPU. Agent-B owns GPU but requests Printer. Circular wait detected.";
        data.resolutionText = "Deadlock detector has identified the cycle. Resolve deadlock to break the wait-for cycle.";
        data.victimAgentName = "Agent-B";
        data.winnerAgentName = "Agent-A";
        return data;
    }

    private Incident createResolvedIncident() {
        Incident data = new Incident();
        data.phase = "RESOLVED";
        data.title = "Deadlock resolved successfully";
        data.resourceId = 1;
        data.resourceName = "Printer";
        data.ownerAgentName = "Agent-B";
        data.waitingAgentName = null;
        data.blockerText = "Deadlock resolved successfully. Resources have been reassigned and execution has resumed.";
        data.resolutionText = "Agent-B continues with Printer, Agent-A is ready, and the queue is now empty.";
        data.victimAgentName = "Agent-B";
        data.winnerAgentName = "Agent-A";
        return data;
    }

    private String agentsJson() {
        StringBuilder builder = new StringBuilder("[");
        boolean first = true;
        for (Agent agent : agentManager.getAllAgents()) {
            if (!first) {
                builder.append(',');
            }
            builder.append('{')
                    .append("\"agentId\":").append(agent.getAgentId()).append(',')
                    .append("\"agentName\":\"").append(escape(agent.getAgentName())).append("\",")
                    .append("\"priority\":").append(agent.getPriority()).append(',')
                    .append("\"status\":\"").append(agent.getStatus()).append("\"")
                    .append('}');
            first = false;
        }
        return builder.append(']').toString();
    }

    private String resourcesJson() {
        StringBuilder builder = new StringBuilder("[");
        boolean first = true;
        for (Resource resource : resourceManager.getAllResources()) {
            if (!first) {
                builder.append(',');
            }
            builder.append('{')
                    .append("\"resourceId\":").append(resource.getResourceId()).append(',')
                    .append("\"resourceName\":\"").append(escape(resource.getResourceName())).append("\",")
                    .append("\"status\":\"").append(resource.getStatus()).append("\",")
                    .append("\"ownerAgentId\":").append(resource.getOwnerAgentId() == null ? "null" : resource.getOwnerAgentId())
                    .append('}');
            first = false;
        }
        return builder.append(']').toString();
    }

    private String queueJson() {
        StringBuilder builder = new StringBuilder("[");
        boolean first = true;
        for (Integer agentId : waitingQueueManager.getWaitingAgents()) {
            Agent agent = agentManager.getAgent(agentId);
            if (agent == null) {
                continue;
            }
            if (!first) {
                builder.append(',');
            }
            builder.append('{')
                    .append("\"agentId\":").append(agent.getAgentId()).append(',')
                    .append("\"agentName\":\"").append(escape(agent.getAgentName())).append("\"")
                    .append('}');
            first = false;
        }
        return builder.append(']').toString();
    }

    private String logsJson() {
        StringBuilder builder = new StringBuilder("[");
        for (int index = 0; index < logs.size(); index++) {
            LogEntry log = logs.get(index);
            if (index > 0) {
                builder.append(',');
            }
            builder.append('{')
                    .append("\"time\":\"").append(escape(log.time)).append("\",")
                    .append("\"message\":\"").append(escape(log.message)).append("\"")
                    .append('}');
        }
        return builder.append(']').toString();
    }

    private String statsJson() {
        return "{"
                + "\"agents\":" + agentManager.getTotalAgents()
                + ",\"resources\":" + resourceManager.getTotalResources()
                + ",\"conflicts\":" + conflicts
                + ",\"deadlocks\":" + deadlocks
                + ",\"started\":" + simulationStarted
                + "}";
    }

    private String incidentJson() {
        return "{"
                + "\"phase\":\"" + escape(incident.phase) + "\""
                + ",\"title\":\"" + escape(incident.title) + "\""
                + ",\"resourceId\":" + incident.resourceId
                + ",\"resourceName\":\"" + escape(incident.resourceName) + "\""
                + ",\"ownerAgentName\":\"" + escape(incident.ownerAgentName) + "\""
                + ",\"waitingAgentName\":" + stringOrNull(incident.waitingAgentName)
                + ",\"blockerText\":\"" + escape(incident.blockerText) + "\""
                + ",\"resolutionText\":\"" + escape(incident.resolutionText) + "\""
                + ",\"victimAgentName\":" + stringOrNull(incident.victimAgentName)
                + ",\"winnerAgentName\":" + stringOrNull(incident.winnerAgentName)
                + "}";
    }

    private String systemJson() {
        return "{"
                + "\"executionStatus\":\"" + escape(executionStatus) + "\""
                + ",\"conflictEngineStatus\":\"" + escape(conflictEngineStatus) + "\""
                + ",\"deadlockDetectorStatus\":\"" + escape(deadlockDetectorStatus) + "\""
                + "}";
    }

    private String stringOrNull(String value) {
        return value == null ? "null" : "\"" + escape(value) + "\"";
    }

    private String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

}