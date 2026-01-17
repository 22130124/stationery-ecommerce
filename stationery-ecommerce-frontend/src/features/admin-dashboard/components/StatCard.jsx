import React from "react";
import styles from "./StatCard.module.scss";

export default function StatCard({ title, value, trend, variant }) {
    return (
        <div className={`${styles.card} ${styles[variant]}`}>
            <span className={styles.title}>{title}</span>
            <strong className={styles.value}>{value}</strong>
            {trend && <span className={styles.trend}>{trend}</span>}
        </div>
    );
}