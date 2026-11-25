import React from "react";
import {
    FaRobot,
    FaCircle
} from "react-icons/fa";

function AgentPanel({ agents }) {

    const getStatusClass = (status) => {
        switch (status) {
            case "RUNNING":
                return "status-running";
            case "WAITING":
                return "status-waiting";
            case "BLOCKED":
                return "status-blocked";
            case "READY":
                return "status-ready";
            default:
                return "status-neutral";
        }
    };

    return (

        <div className="panel agent-panel">

            <div className="panel-title">

                🤖 Active Agents

            </div>

            {

                agents.map((agent) => (

                    <div
                        className="agent-row"
                        key={agent.agentId}
                    >

                        <div className="agent-info">

                            <div className="agent-avatar">
                                <FaRobot />
                            </div>

                            <div>

                                <h5>

                                    {agent.agentName}

                                </h5>

                                <small>

                                    Priority: {agent.priority}

                                </small>

                            </div>

                        </div>

                        <div className={`status-chip ${getStatusClass(agent.status)}`}>

                            <FaCircle className="status-dot" />

                            {agent.status}

                        </div>

                    </div>

                ))

            }

        </div>

    );

}

export default AgentPanel;