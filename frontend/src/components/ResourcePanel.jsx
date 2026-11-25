import React from "react";
import {
    FaPrint,
    FaMicrochip,
    FaCheckCircle,
    FaTimesCircle,
    FaUser
} from "react-icons/fa";

function ResourcePanel({ resources }) {

    const getIcon = (name) => {

        if (name === "Printer") {

            return <FaPrint size={24} color="#ffffff" />;

        }

        return <FaMicrochip size={24} color="#ffffff" />;

    };

    return (

        <div className="panel resource-panel">

            <div className="panel-title">

                🖥 Resource Monitor

            </div>

            {

                resources.map((resource) => (

                    <div
                        className="resource-row"
                        key={resource.resourceId}
                    >

                        <div className="resource-left">

                            <div className="resource-icon">

                                {getIcon(resource.resourceName)}

                            </div>

                            <div>

                                <h5>

                                    {resource.resourceName}

                                </h5>

                                <small>

                                    Resource ID: {resource.resourceId}

                                </small>

                            </div>

                        </div>

                        <div className="resource-right">

                            <span className={`resource-tag ${resource.status === "FREE" ? "resource-free" : "resource-busy"}`}>

                                {resource.status === "FREE" ? <FaCheckCircle /> : <FaTimesCircle />}

                                {resource.status}

                            </span>

                            <div className="owner">

                                <FaUser />

                                {

                                    resource.ownerAgentId

                                        ?

                                        `Agent-${resource.ownerAgentId}`

                                        :

                                        "No Owner"

                                }

                            </div>

                        </div>

                    </div>

                ))

            }

        </div>

    );

}

export default ResourcePanel;