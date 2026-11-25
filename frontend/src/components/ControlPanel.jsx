import React from "react";
import {
    FaPlay,
    FaExclamationTriangle,
    FaUnlockAlt,
    FaRedoAlt
} from "react-icons/fa";

import "../styles/controlpanel.css";

function ControlPanel({

    onStart,
    onConflict,
    onResolve,
    onReset,
    disableConflict,
    disableResolve

}) {

    return (

        <div className="control-panel">

            <div className="control-header">

                <h2>

                    Simulation Control Panel

                </h2>

                <p>

                    Manage Resource Allocation & Conflict Resolution

                </p>

            </div>

            <div className="control-buttons">

                <button
                    className="btn btn-start"
                    onClick={onStart}
                >

                    <FaPlay />

                    <span>

                        Start Simulation

                    </span>

                </button>

                <button
                    className="btn btn-conflict"
                    onClick={onConflict}
                    disabled={disableConflict}
                >

                    <FaExclamationTriangle />

                    <span>

                        Generate Conflict

                    </span>

                </button>

                <button
                    className="btn-resolve"
                    onClick={onResolve}
                    disabled={disableResolve}
                >

                    <FaUnlockAlt />

                    <span>

                        Resolve Deadlock

                    </span>

                </button>

                <button
                    className="btn-reset"
                    onClick={onReset}
                >

                    <FaRedoAlt />

                    <span>

                        Reset System

                    </span>

                </button>

            </div>

        </div>

    );

}

export default ControlPanel;