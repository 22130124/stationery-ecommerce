// components/TopProductsChart.jsx
import React, { useEffect, useState } from 'react'
import { getTopProducts } from '../../../api/dashboardApi'
import DashboardCard from './DashboardCard'
import {
    Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis
} from 'recharts'

export default function TopProductsChart() {
    const [range, setRange] = useState("7d");
    const [limit, setLimit] = useState(5);
    const [data, setData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const res = await getTopProducts(range, limit);

            // Xử lý dữ liệu (Mapping)
            setData(
                res.map((item) => ({
                    label: item.product.name,
                    quantity: item.quantity,
                    slug: item.product.slug,
                    image: item.product.defaultImage?.url,
                    url: `/product-detail/${item.product.slug}`
                }))
            );
        }
        fetchData();
    }, [range, limit]);

    const IMAGE_SIZE = 40;
    const ImageTick = ({ x, y, payload }) => {
        const item = data[payload.index];
        if (!item?.image) return null;
        return (
            <g transform={`translate(${x - IMAGE_SIZE / 2}, ${y + 8})`}>
                <foreignObject width={IMAGE_SIZE} height={IMAGE_SIZE}>
                    <img
                        src={item.image}
                        alt={item.label}
                        style={{
                            width: IMAGE_SIZE, height: IMAGE_SIZE,
                            objectFit: 'cover', borderRadius: 6,
                            border: '1px solid #eee'
                        }}
                    />
                </foreignObject>
            </g>
        )
    }

    const filters = (
        <>
            <select
                value={limit}
                onChange={(e) => setLimit(Number(e.target.value))}
                style={{ padding: '6px', borderRadius: '8px', border: '1px solid #ddd' }}
            >
                <option value={5}>Top 5</option>
                <option value={10}>Top 10</option>
                <option value={20}>Top 20</option>
            </select>

            <select
                value={range}
                onChange={(e) => setRange(e.target.value)}
                style={{ padding: '6px', borderRadius: '8px', border: '1px solid #ddd' }}
            >
                <option value="7d">7 ngày</option>
                <option value="30d">30 ngày</option>
                <option value="90d">3 tháng</option>
            </select>
        </>
    );

    return (
        <DashboardCard title="Sản phẩm bán chạy" filters={filters}>
            <ResponsiveContainer width='100%' height={350}>
                <BarChart data={data} margin={{ bottom: 20 }}>
                    <CartesianGrid strokeDasharray='3 3' vertical={false} />
                    <XAxis
                        dataKey="label"
                        tick={<ImageTick />}
                        interval={0}
                        height={60}
                        tickLine={false}
                        axisLine={false}
                    />
                    <YAxis axisLine={false} tickLine={false} />
                    <Tooltip cursor={{ fill: '#f3f4f6' }} />
                    <Bar
                        dataKey='quantity'
                        fill='#6366f1'
                        radius={[4, 4, 0, 0]}
                        barSize={40}
                        onClick={(data) => {
                            if(data && data.url) window.open(data.url, '_blank')
                        }}
                    />
                </BarChart>
            </ResponsiveContainer>
        </DashboardCard>
    )
}