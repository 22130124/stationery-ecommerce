// components/RevenueChart.jsx
import React, { useEffect, useState } from "react";
import { XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, AreaChart, Area } from "recharts";
import DashboardCard from "./DashboardCard";
import { getRevenue } from "../../../api/dashboardApi";

export default function RevenueChart() {
    const [range, setRange] = useState("7d");
    const [data, setData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const res = await getRevenue(range);
            setData(res);
        }
        fetchData();
    }, [range])

    const filters = (
        <select
            value={range}
            onChange={(e) => setRange(e.target.value)}
            style={{ padding: '6px', borderRadius: '8px', border: '1px solid #ddd' }}
        >
            <option value="7d">7 ngày qua</option>
            <option value="30d">30 ngày qua</option>
            <option value="90d">3 tháng qua</option>
        </select>
    );

    return (
        <DashboardCard title="Biểu đồ doanh thu" filters={filters}>
            <ResponsiveContainer width="100%" height={300}>
                <AreaChart data={data}>
                    <defs>
                        <linearGradient id="colorRevenue" x1="0" y1="0" x2="0" y2="1">
                            <stop offset="5%" stopColor="#6366f1" stopOpacity={0.2}/>
                            <stop offset="95%" stopColor="#6366f1" stopOpacity={0}/>
                        </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} />
                    <XAxis dataKey="label" axisLine={false} tickLine={false} />
                    <YAxis axisLine={false} tickLine={false} />
                    <Tooltip
                        formatter={(value) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)}
                    />
                    <Area
                        type="monotone"
                        dataKey="revenue"
                        stroke="#6366f1"
                        fillOpacity={1}
                        fill="url(#colorRevenue)"
                        strokeWidth={2}
                    />
                </AreaChart>
            </ResponsiveContainer>
        </DashboardCard>
    );
}
