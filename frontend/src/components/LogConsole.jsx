import React, { useEffect, useMemo, useRef } from "react";
import {
    FaTerminal,
    FaCircle
} from "react-icons/fa";

import "../styles/console.css";

function LogConsole({ logs }) {
    const consoleRef = useRef(null);

    const displayLogs = useMemo(() => {
        return logs.map((log) => {
            const message = log.message.toLowerCase();
            if (/error|fail|unable|failed/.test(message)) {
                return { ...log, type: "ERROR" };
            }
            if (/warn|conflict|deadlock/.test(message)) {
                return { ...log, type: "WARNING" };
            }
            if (/resolve|resolved|selected|moved|success/.test(message)) {
                return { ...log, type: "SUCCESS" };
            }
            return { ...log, type: "INFO" };
        });
    }, [logs]);

    useEffect(() => {
        if (consoleRef.current) {
            consoleRef.current.scrollTop = consoleRef.current.scrollHeight;
        }
    }, [displayLogs]);

    return (

        <div className="log-console">

            <div className="console-header">

                <div className="console-title">

                    <FaTerminal />

                    <span>

                        Live Execution Console

                    </span>

                </div>

                <div className="console-status">

                    <FaCircle />

                    LIVE

                </div>

            </div>

            <div className="console-body" ref={consoleRef}>

                {

                    displayLogs.length === 0 ?

                    (

                        <div className="console-empty">

                            Waiting for Simulation...

                        </div>

                    )

                    :

                    (

                        displayLogs.map((log,index)=>(

                            <div
                                key={index}
                                className={`console-line ${log.type.toLowerCase()} ${index === displayLogs.length - 1 ? "latest" : ""}`}
                            >

                                <span className="console-time">

                                    [{log.time}]

                                </span>

                                <span className="console-type">

                                    {log.type}

                                </span>

                                <span className="console-message">

                                    {log.message}

                                </span>

                            </div>

                        ))

                    )

                }

            </div>

        </div>

    );

}

export default LogConsole;