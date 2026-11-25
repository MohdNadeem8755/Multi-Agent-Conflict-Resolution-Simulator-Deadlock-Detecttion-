import React from "react";

function StatusChip({

    status,
    label

}) {

    const getColor = () => {

        switch (status) {

            case "RUNNING":
                return "#22c55e";

            case "READY":
                return "#3b82f6";

            case "WAITING":
                return "#f59e0b";

            case "BLOCKED":
                return "#ef4444";

            case "FINISHED":
                return "#64748b";

            case "FREE":
                return "#16a34a";

            case "BUSY":
                return "#dc2626";

            case "CONNECTED":
                return "#22c55e";

            case "DISCONNECTED":
                return "#ef4444";

            case "SAFE":
                return "#16a34a";

            case "DEADLOCK":
                return "#dc2626";

            case "IDLE":
                return "#64748b";

            default:
                return "#475569";

        }

    };

    return (

        <div
            className="status-chip"
            style={{
                background:getColor()
            }}
        >

            <span className="status-dot"></span>

            {label || status}

        </div>

    );

}

export default StatusChip;