import React from "react";
import {
    FaClock,
    FaRobot,
    FaArrowRight
} from "react-icons/fa";

function WaitingQueue({ queue }) {

    const formatDuration = (index) => {
        const minutes = Math.floor((index + 1) * 0.75);
        const seconds = 30 + index * 10;
        return `${String(minutes).padStart(2, "0")}:${String(seconds % 60).padStart(2, "0")}`;
    };

    return (

        <div className="panel queue-panel">

            <div className="panel-title">

                <FaClock className="me-2" />

                Waiting Queue

            </div>

            {

                queue.length === 0 ? (

                    <div className="empty-panel">

                        <FaClock size={40} />

                        <h4>No Waiting Agents</h4>

                        <p>All agents are executing normally.</p>

                    </div>

                ) : (

                    queue.map((agent, index) => (

                        <div
                            className="queue-row queue-active"
                            key={index}
                        >

                            <div className="queue-left">

                                <div className="queue-icon">

                                    <FaRobot />

                                </div>

                                <div>

                                    <h5>

                                        {agent.agentName}

                                    </h5>

                                    <small>

                                        Agent ID: {agent.agentId}

                                    </small>

                                </div>

                            </div>

                            <div className="queue-right">

                                <span className="queue-position">

                                    #{index + 1}

                                </span>

                                <span className="queue-duration">

                                    {formatDuration(index)}

                                </span>

                                <span className="queue-status">

                                    WAITING

                                </span>

                                <FaArrowRight />

                            </div>

                        </div>

                    ))

                )

            }

        </div>

    );

}

export default WaitingQueue;