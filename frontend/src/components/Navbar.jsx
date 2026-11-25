import React from "react";
import {
    FaRobot,
    FaServer,
    FaSyncAlt,
    FaCircle
} from "react-icons/fa";

import "../styles/navbar.css";

function Navbar() {

    const currentTime = new Date().toLocaleTimeString();

    return (

        <nav className="navbar-container">

            {/* Left Side */}

            <div className="navbar-left">

                <div className="logo">

                    <FaRobot />

                </div>

                <div>

                    <h2>

                        Multi-Agent Conflict Resolution Engine

                    </h2>

                    <p>

                        AI-Based Resource Scheduling & Deadlock Management

                    </p>

                </div>

            </div>

            {/* Right Side */}

            <div className="navbar-right">

                <div className="status-box status-online">

                    <span className="online-indicator" />

                    <span>

                        Backend Connected

                    </span>

                </div>

                <div className="status-box status-secondary">

                    <FaServer />

                    <span>

                        Simulation Ready

                    </span>

                </div>

                <div className="time-box">

                    {currentTime}

                </div>

                <button className="refresh-btn" title="Refresh dashboard">

                    <FaSyncAlt />

                </button>

            </div>

        </nav>

    );

}

export default Navbar;