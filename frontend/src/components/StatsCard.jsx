import React, { useEffect, useState } from "react";
import "../styles/cards.css";

function StatsCard({

    title,
    value,
    icon,
    color,
    subtitle,
    details = []

}) {

    const [displayValue, setDisplayValue] = useState(0);

    useEffect(() => {
        if (typeof value === "number") {
            const duration = 700;
            const start = 0;
            const range = value - start;
            const startTime = performance.now();

            const animate = (now) => {
                const elapsed = now - startTime;
                const progress = Math.min(elapsed / duration, 1);
                setDisplayValue(Math.round(start + range * progress));
                if (progress < 1) {
                    requestAnimationFrame(animate);
                }
            };
            requestAnimationFrame(animate);
        } else {
            setDisplayValue(value);
        }
    }, [value]);

    return (

        <div
            className="stats-card"
            style={{ borderTop: `5px solid ${color}` }}
        >

            <div className="stats-top">

                <div>

                    <h5>{title}</h5>

                    <p>{subtitle}</p>

                </div>

                <div
                    className="stats-icon"
                    style={{ background: color }}
                >

                    {icon}

                </div>

            </div>

            <div className="stats-value">

                {displayValue}

            </div>

            <div className="stats-details">
                {details.map((detail, index) => (
                    <div className="stats-detail" key={`${detail.label}-${index}`}>
                        <span>{detail.label}</span>
                        <strong>{detail.value}</strong>
                    </div>
                ))}
            </div>

        </div>

    );

}

export default StatsCard;