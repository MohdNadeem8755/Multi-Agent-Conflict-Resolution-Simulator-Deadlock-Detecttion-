import React, { useEffect, useState } from "react";

import Navbar from "../components/Navbar";
import StatsCard from "../components/StatsCard";
import ResourceVisualizer from "../components/ResourceVisualizer";
import ResourcePanel from "../components/ResourcePanel";
import LogConsole from "../components/LogConsole";
import ControlPanel from "../components/ControlPanel";

import {
    FaRobot,
    FaServer,
    FaExclamationTriangle,
    FaLock
} from "react-icons/fa";

import StatusChip from "../components/StatusChip";

import {
    getSimulationSnapshot,
    startSimulation as apiStartSimulation,
    generateConflict as apiGenerateConflict,
    resolveDeadlock as apiResolveDeadlock,
    resetSimulation as apiResetSimulation
} from "../services/api";

import "../styles/dashboard.css";

const createInitialAgents = () => ([
    {
        agentId:101,
        agentName:"Agent-A",
        priority:10,
        status:"RUNNING"
    },
    {
        agentId:102,
        agentName:"Agent-B",
        priority:8,
        status:"READY"
    },
    {
        agentId:103,
        agentName:"Agent-C",
        priority:6,
        status:"READY"
    }
]);

const createInitialResources = () => ([
    {
        resourceId:1,
        resourceName:"Printer",
        status:"BUSY",
        ownerAgentId:101
    },
    {
        resourceId:2,
        resourceName:"GPU",
        status:"FREE",
        ownerAgentId:null
    }
]);

const createInitialQueue = () => ([]);

const createInitialLogs = () => ([
    {
        time:"14:10:00",
        message:"Simulation started: Agent-A owns Printer."
    }
]);

const createInitialIncident = () => ({

    phase:"IDLE",

    title:"Press Run to start simulation",

    resourceId:0,

    resourceName:"No active resource",

    ownerAgentName:"None",

    waitingAgentName:null,

    blockerText:"No simulation is running yet. Click Start Simulation to load agents and resources.",

    resolutionText:"After start, you can generate a conflict and watch the queue resolve it.",

    victimAgentName:null,

    winnerAgentName:null

});

const createIdleAgents = () => ([
    {
        agentId:101,
        agentName:"Agent-A",
        priority:10,
        status:"READY"
    },
    {
        agentId:102,
        agentName:"Agent-B",
        priority:8,
        status:"READY"
    },
    {
        agentId:103,
        agentName:"Agent-C",
        priority:6,
        status:"READY"
    }
]);

const createIdleResources = () => ([
    {
        resourceId:1,
        resourceName:"Printer",
        status:"FREE",
        ownerAgentId:null
    },
    {
        resourceId:2,
        resourceName:"GPU",
        status:"FREE",
        ownerAgentId:null
    }
]);

const createIdleQueue = () => ([]);

const createIdleLogs = () => ([
    {
        time:"14:10:00",
        message:"Simulation idle: all agents are ready and no resources are contested."
    }
]);

const applySnapshot = (snapshot, setState) => {

    const started = snapshot.stats?.started ?? false;

    setState.setAgents(snapshot.agents || createIdleAgents());
    setState.setResources(snapshot.resources || createIdleResources());
    setState.setQueue(snapshot.queue || createIdleQueue());
    setState.setLogs(snapshot.logs || createIdleLogs());
    setState.setConflicts(snapshot.stats?.conflicts ?? 0);
    setState.setDeadlocks(snapshot.stats?.deadlocks ?? 0);
    setState.setExecutionStatus(snapshot.system?.executionStatus || (started ? "Running Normally" : "Idle"));
    setState.setConflictEngineStatus(snapshot.system?.conflictEngineStatus || (started ? "Monitoring Resources" : "Press Run to start simulation"));
    setState.setDeadlockDetectorStatus(snapshot.system?.deadlockDetectorStatus || (started ? "No Deadlock Found" : "Idle"));
    setState.setIncident(snapshot.incident || createInitialIncident());
    setState.setSimulationStarted(started);

};

function Dashboard() {

    // ===========================================
    // Agent Data
    // ===========================================

    const [agents, setAgents] = useState(createIdleAgents());

    // ===========================================
    // Resource Data
    // ===========================================

    const [resources, setResources] = useState(createIdleResources());

    // ===========================================
    // Waiting Queue
    // ===========================================

    const [queue, setQueue] = useState(createIdleQueue());

    // ===========================================
    // Console Logs
    // ===========================================

    const [logs, setLogs] = useState(createIdleLogs());

    const [conflicts, setConflicts] = useState(0);

    const [deadlocks, setDeadlocks] = useState(0);

    const [executionStatus, setExecutionStatus] = useState("Idle");

    const [conflictEngineStatus, setConflictEngineStatus] = useState("Press Run to start simulation");

    const [deadlockDetectorStatus, setDeadlockDetectorStatus] = useState("Idle");

    const [incident, setIncident] = useState(createInitialIncident());

    const [simulationStarted, setSimulationStarted] = useState(false);

    const [loading, setLoading] = useState(true);

    const [error, setError] = useState("");

    const conflictSummary =
        incident.phase === "CONFLICT"
            ? null
            : incident.phase === "RESOLVED"
                ? "The deadlock has been resolved. The Printer is free again and the wait-for cycle has been broken."
                : "After you press Run Simulation, the live details will appear here."

    const agentCounts = {
        total: agents.length,
        running: agents.filter((agent) => agent.status === "RUNNING").length,
        waiting: agents.filter((agent) => agent.status === "WAITING").length,
        ready: agents.filter((agent) => agent.status === "READY").length,
    };

    const resourceCounts = {
        total: resources.length,
        busy: resources.filter((resource) => resource.status === "BUSY").length,
        free: resources.filter((resource) => resource.status === "FREE").length,
    };

    const resolvedConflicts = incident.phase === "RESOLVED" ? 1 : 0;
    const resolvedDeadlocks = incident.phase === "RESOLVED" ? 1 : 0;

    // ===========================================
    // Statistics
    // ===========================================

    const stats = {

        agents: agents.length,

        resources: resources.length,

        conflicts: conflicts,

        deadlocks: deadlocks

    };

    const syncSnapshot = async (showLoading = false) => {

        if (showLoading) {

            setLoading(true);

        }

        try {

            const snapshot = await getSimulationSnapshot();

            applySnapshot(snapshot, {
                setAgents,
                setResources,
                setQueue,
                setLogs,
                setConflicts,
                setDeadlocks,
                setExecutionStatus,
                setConflictEngineStatus,
                setDeadlockDetectorStatus,
                setIncident,
                setSimulationStarted
            });

            setError("");

        } catch {

            setError("Backend connection failed. Start the Java API on port 8080.");

            setAgents([]);
            setResources([]);
            setQueue([]);
            setLogs([]);
            setConflicts(0);
            setDeadlocks(0);
            setExecutionStatus("Idle");
            setConflictEngineStatus("Press Run to start simulation");
            setDeadlockDetectorStatus("Idle");
            setIncident(createInitialIncident());
            setSimulationStarted(false);

        } finally {

            setLoading(false);

        }

    };

    useEffect(() => {

        syncSnapshot();

        const intervalId = window.setInterval(() => {

            syncSnapshot(false);

        }, 3000);

        return () => window.clearInterval(intervalId);

    }, []);

    // ===========================================
    // Button Functions
    // (Later Spring Boot APIs)
    // ===========================================

    const startSimulation = async () => {

        try {

            await apiStartSimulation();
            await syncSnapshot(true);

        } catch {

            setError("Unable to start simulation from backend.");
            setLoading(false);

        }

    };

    const generateConflict = async () => {

        try {

            await apiGenerateConflict();
            await syncSnapshot(true);

        } catch {

            setError("Unable to generate conflict from backend.");
            setLoading(false);

        }

    };

    const resolveDeadlock = async () => {

        try {

            await apiResolveDeadlock();
            await syncSnapshot(true);

        } catch {

            setError("Unable to resolve deadlock from backend.");
            setLoading(false);

        }

    };

    const resetSimulation = async () => {

        try {

            await apiResetSimulation();
            await syncSnapshot(true);

        } catch {

            setError("Unable to reset simulation from backend.");
            setLoading(false);

        }

    };

    const showSimulationDetails = simulationStarted && incident.phase !== "IDLE";

    // ===========================================
    // UI
    // ===========================================

    return(

        <>

            <Navbar/>

            <div className="dashboard-container">

                {(error || loading) && (

                    <div className={`connection-banner ${error ? "connection-error" : "connection-loading"}`}>

                        <strong>

                            {error ? "Backend connection issue" : "Syncing live backend state"}

                        </strong>

                        <span>

                            {error || "Fetching the current simulator snapshot from the Java API."}

                        </span>

                    </div>

                )}

                {/* ==========================
                    Statistics
                ========================== */}

                <div className="stats-grid">

                    <StatsCard

                        title="Agents"

                        subtitle="Current Agent Status"

                        value={stats.agents}

                        color="#2563eb"

                        icon={<FaRobot/>}

                        details={[
                            { label: "Total Agents", value: agentCounts.total },
                            { label: "Running", value: agentCounts.running },
                            { label: "Waiting", value: agentCounts.waiting },
                            { label: "Ready", value: agentCounts.ready },
                        ]}

                    />

                    <StatsCard

                        title="Resources"

                        subtitle="Current Resource Status"

                        value={stats.resources}

                        color="#16a34a"

                        icon={<FaServer/>}

                        details={[
                            { label: "Total Resources", value: resourceCounts.total },
                            { label: "Busy", value: resourceCounts.busy },
                            { label: "Free", value: resourceCounts.free },
                        ]}

                    />

                    <StatsCard

                        title="Conflicts"

                        subtitle="Conflict Overview"

                        value={stats.conflicts}

                        color="#f59e0b"

                        icon={<FaExclamationTriangle/>}

                        details={[
                            { label: "Active Conflicts", value: stats.conflicts },
                            { label: "Resolved Conflicts", value: resolvedConflicts },
                        ]}

                    />

                    <StatsCard

                        title="Deadlocks"

                        subtitle="Deadlock Overview"

                        value={stats.deadlocks}

                        color="#dc2626"

                        icon={<FaLock/>}

                        details={[
                            { label: "Current Deadlocks", value: stats.deadlocks },
                            { label: "Resolved Deadlocks", value: resolvedDeadlocks },
                        ]}

                    />

                </div>

                {/* ==========================
                    Live Incident Board
                ========================== */}

                <div className="incident-board">

                    <div className="incident-card incident-primary">

                        <div className="incident-header">

                            <div>

                                <p className="incident-label">

                                    Current Simulation

                                </p>

                                <h2>

                                    {incident.title}

                                </h2>

                            </div>

                            <StatusChip

                                status={incident.phase}

                                label={incident.phase === "IDLE" ? "IDLE" : incident.phase === "SAFE" ? "SAFE" : incident.phase === "CONFLICT" ? "DEADLOCK" : "RESOLVED"}

                            />

                        </div>

                        {showSimulationDetails ? (

                            <>

                                <div className="incident-detail-grid">

                                    <div>

                                        <span className="incident-meta-label">Waiting Agent</span>

                                        <strong>{incident.waitingAgentName || "None"}</strong>

                                        <p>{incident.phase === "SAFE" ? "No one is blocked right now" : incident.waitingAgentName ? "Blocked in the queue" : "Queue is clear"}</p>

                                    </div>

                                    <div>

                                        <span className="incident-meta-label">Deadlock Victim</span>

                                        <strong>{incident.victimAgentName || "None"}</strong>

                                        <p>{incident.phase === "RESOLVED" ? `Winner after resolution: ${incident.winnerAgentName}` : incident.phase === "CONFLICT" ? "Resolver chooses the victim after detection" : "No victim selected yet"}</p>

                                    </div>

                                </div>

                                <div className={`conflict-summary ${incident.phase === "CONFLICT" ? "is-conflict" : incident.phase === "RESOLVED" ? "is-resolved" : "is-safe"}`}>

                            <span className="conflict-summary-label">

                                {incident.phase === "CONFLICT" ? "Conflict flow" : incident.phase === "RESOLVED" ? "Resolution flow" : "Normal flow"}

                            </span>

                            {incident.phase === "CONFLICT" ? (
                                <div className="conflict-summary-list">
                                    <p><strong>Agent-A</strong> owns <strong>Printer</strong> but requests <strong>GPU</strong>.</p>
                                    <p><strong>Agent-B</strong> owns <strong>GPU</strong> but requests <strong>Printer</strong>.</p>
                                    <p><strong>Circular wait detected.</strong></p>
                                </div>
                            ) : (
                                <p>
                                    {conflictSummary}
                                </p>
                            )}

                            </div>

                            <div className="incident-explanation">

                            <div>

                                <h3>Why it happened</h3>

                                <p>{incident.blockerText}</p>

                            </div>

                            <div>

                                <h3>How it resolves</h3>

                                <p>{incident.resolutionText}</p>

                            </div>

                                </div>

                            </>

                        ) : (

                            <div className="incident-empty-state">

                                <h3>

                                    No simulation running

                                </h3>

                                <p>

                                    When you press Start Simulation, agents, resources, queue, and conflict details will appear here.

                                </p>

                            </div>

                        )}

                    </div>

                    <div className="incident-card incident-side">

                        <ResourceVisualizer
                            agents={agents}
                            resources={resources}
                            queue={queue}
                            incident={incident}
                            simulationStarted={simulationStarted}
                        />

                    </div>

                </div>
                {/* ==========================
                    Resource & Console Section
                ========================== */}

                <div className="dashboard-row">

                    <div className="dashboard-column">

                        <ResourcePanel
                            resources={resources}
                        />

                    </div>

                    <div className="dashboard-column console-column">

                        <LogConsole
                            logs={logs}
                        />

                    </div>

                </div>

                {/* ==========================
                    System Information
                ========================== */}

                <div className="system-status">

                    <div className="status-card">

                        <h3>

                            Scheduler

                        </h3>

                        <p>

                            Hybrid Priority Scheduler

                        </p>

                    </div>

                    <div className="status-card">

                        <h3>

                            Execution Engine

                        </h3>

                        <p>

                            {executionStatus}

                        </p>

                    </div>

                    <div className="status-card">

                        <h3>

                            Conflict Engine

                        </h3>

                        <p>

                            {conflictEngineStatus}

                        </p>

                    </div>

                    <div className="status-card">

                        <h3>

                            Deadlock Detector

                        </h3>

                        <p>{deadlockDetectorStatus}</p>

                    </div>

                </div>

                                {/* ==========================
                    Control Panel
                ========================== */}

                <ControlPanel

                    onStart={startSimulation}

                    onConflict={generateConflict}

                    onResolve={resolveDeadlock}

                    onReset={resetSimulation}

                    disableConflict={!simulationStarted}

                    disableResolve={!simulationStarted}

                />

            </div>

            {/* ==========================
                Footer
            ========================== */}

            <footer className="dashboard-footer">

                <div className="footer-left">

                    <h3>

                        Multi-Agent Conflict Resolution Engine

                    </h3>

                    <p>

                        AI Based Resource Scheduling • Conflict Detection •
                        Hybrid Scheduler • Deadlock Resolution

                    </p>

                </div>

                <div className="footer-right">

                    <p>

                        Version 1.0

                    </p>

                    <p>

                        Developed using Java • Spring Boot • React

                    </p>

                </div>

            </footer>

        </>

    );

}

export default Dashboard;