import React from "react";
import styles from './DashboardCard.module.scss';

export default function DashboardCard({ title, filters, children, className }) {
    return (
        <div className={`${styles.card} ${className || ''}`}>
            <div className={styles.header}>
                <h3>{title}</h3>
                {filters && <div className={styles.actions}>{filters}</div>}
            </div>
            <div className={styles.content}>
                {children}
            </div>
        </div>
    );
}