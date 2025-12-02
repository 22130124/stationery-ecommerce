import React from "react";
import styles from "./StatusToggle.module.scss";

const StatusToggle = ({ active, onToggle }) => {
    return (
        <div
            className={`${styles.toggle} ${active ? styles.active : ""}`}
            onClick={onToggle}
        >
            <div className={styles.circle}></div>
        </div>
    );
};

export default StatusToggle;