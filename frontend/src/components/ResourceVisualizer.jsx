import React from "react";
import {
    FaRobot,
    FaLink,
    FaExclamationTriangle,
    FaCheckCircle
} from "react-icons/fa";

import "../styles/graph.css";

function ResourceVisualizer({ agents, resources, queue, incident, simulationStarted }) {

    const contestedResource = resources.find((resource) => resource.resourceId === incident.resourceId) || resources[0];

    const safeMode = incident.phase === "SAFE";

    const conflictMode = incident.phase === "CONFLICT";

    const resolvedMode = incident.phase === "RESOLVED";

    const queueToRender = resolvedMode && queue.length === 0
        ? [{ agentId: 102, agentName: "Agent-B", status: "WAITING" }]
        : queue;

    const blockedAgent = queueToRender[0];

    if (!simulationStarted || incident.phase === "IDLE") {

        return (

            <div className="visualizer visualizer-empty">

                <div className="visualizer-header">

                    <h2>

                        Live Resource Allocation Board

                    </h2>

                    <p>

                        Click Start Simulation to load the live resource map.

                    </p>

                </div>

                <div className="visualizer-placeholder">

                    Simulation is idle. No agents or conflicts are shown until the run starts.

                </div>

            </div>

        );

    }

    return (

        <div className="visualizer">

            <div className="visualizer-header">

                <h2>

                    Live Resource Allocation Board

                </h2>

                <p>

                    See who owns the resource, who is waiting, and how the deadlock is resolved

                </p>

            </div>

            <div className="visualizer-body visualizer-live-body">

                <div className="visualizer-column">

                    <h3>Agents</h3>

                    <div className="visualizer-list">

                        {agents.map((agent) => (

                            <div className={`visualizer-pill agent-pill ${agent.status.toLowerCase()}`} key={agent.agentId}>

                                <FaRobot />

                                <span>{agent.agentName}</span>

                                <small>{agent.status}</small>

                            </div>

                        ))}

                    </div>

                </div>

                <div className="visualizer-column visualizer-center">

                    <div className="resource-flow">

                        <div className={`resource-link ${conflictMode ? "active" : resolvedMode ? "resolved" : "safe"}`}>

                            <FaLink />

                            <span>{conflictMode ? "🔗 Cycle Detected" : resolvedMode ? "🔗 Cycle Broken" : "Idle"}</span>

                        </div>

                        <div className="resource-node waiting-node">

                            <div className="resource-node-icon waiting-icon">

                                <FaRobot />

                            </div>

                            <div>

                                <strong>{blockedAgent ? blockedAgent.agentName : "Agent-A and Agent-B"}</strong>

                                <p>{conflictMode ? "Blocked and waiting for the contested resource" : resolvedMode ? "Victim removed from the cycle" : "Nothing is blocked"}</p>

                            </div>

                        </div>

                    </div>

                    <div className="mini-summary-grid">

                        <div className="mini-summary-card">

                            <span>Status</span>

                            <strong>{incident.phase}</strong>

                        </div>

                        <div className="mini-summary-card">

                            <span>Winner</span>

                            <strong>{incident.winnerAgentName || "None"}</strong>

                        </div>

                        <div className="mini-summary-card">

                            <span>Victim</span>

                            <strong>{incident.victimAgentName || "None"}</strong>

                        </div>

                    </div>

                </div>

                <div className="visualizer-column">

                    <h3>Queue</h3>

                    <div className="visualizer-list">

                        {queueToRender.length === 0 ? (

                            <div className="queue-empty-card">

                                <FaCheckCircle />

                                <span>No waiting agents</span>

                            </div>

                        ) : (

                            queueToRender.map((agent) => (

                                <div className="visualizer-pill queue-pill" key={agent.agentId}>

                                    <FaRobot />

                                    <span>{agent.agentName}</span>

                                    <small>WAITING</small>

                                </div>

                            ))

                        )}

                    </div>

                </div>

            </div>

        </div>

    );

}

export default ResourceVisualizer;