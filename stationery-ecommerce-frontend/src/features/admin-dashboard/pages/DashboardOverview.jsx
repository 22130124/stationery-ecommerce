// DashboardOverview.jsx
import React, {useEffect, useState} from "react";
import styles from "./DashboardOverview.module.scss";
import StatCard from "../components/StatCard";
import RevenueChart from "../components/RevenueChart";
import TopProductsChart from "../components/TopProductsChart";
import {getTodayOrders, getTodayRevenue} from "../../../api/dashboardApi";
// import { getDailyStats } from "../api/dashboardApi"; // Giả sử có API này

export default function DashboardOverview() {
    // State cho Stats hôm nay
    const [todayRevenue, setTodayRevenue] = useState({
        value: 0,
        percentChange: 0,
    });

    const [todayOrders, setTodayOrders] = useState({
        value: 0,
        percentChange: 0,
    });

    useEffect(() => {
        const fetchTodayStats = async () => {
            const revenueRes = await getTodayRevenue();
            const ordersRes = await getTodayOrders();

            setTodayRevenue({
                value: revenueRes.todayRevenue,
                percentChange: revenueRes.percentChange,
            });

            setTodayOrders({
                value: ordersRes.todayOrders,
                percentChange: ordersRes.percentChange,
            });
        };

        fetchTodayStats();
    }, []);

    const formatTrend = (percent) => {
        if (percent === 0) return "Không đổi so với hôm qua";
        const sign = percent > 0 ? "+" : "";
        return `${sign}${percent.toFixed(1)}% so với hôm qua`;
    };

    return (
        <div className={styles.dashboard}>
            <div className={styles.header}>
                <h1 className={styles.title}>Tổng quan quản trị</h1>
                <div className={styles.dateBadge}>Hôm nay: {new Date().toLocaleDateString('vi-VN')}</div>
            </div>

            {/* Grid Stat Cards */}
            <section className={styles.statsGrid}>
                <StatCard
                    title="Doanh thu hôm nay"
                    value={new Intl.NumberFormat("vi-VN", {
                        style: "currency",
                        currency: "VND",
                    }).format(todayRevenue.value)}
                    trend={formatTrend(todayRevenue.percentChange)}
                    variant="revenue"
                />

                <StatCard
                    title="Đơn hàng hôm nay"
                    value={todayOrders.value}
                    trend={formatTrend(todayOrders.percentChange)}
                    variant="orders"
                />
            </section>


            {/* Grid Charts */}
            <section className={styles.chartsGrid}>
                <RevenueChart/>
                <TopProductsChart/>
            </section>
        </div>
    );
}