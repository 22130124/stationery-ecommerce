import React, { useEffect, useState } from "react";
import styles from "./OrderHistoryPage.module.scss";
import { getOrders } from "../../api/orderApi";

const OrderHistoryPage = () => {
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        const fetchOrders = async () => {
            const data = await getOrders();
            setOrders(data);
        };
        fetchOrders();
    }, []);

    const formatDateVN = (isoString) => {
        if (!isoString) return "";
        const normalized = isoString.replace(/\.(\d{3})\d+/, '.$1');
        const date = new Date(normalized);
        const options = {
            timeZone: "Asia/Ho_Chi_Minh",
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit",
        };
        return new Intl.DateTimeFormat("vi-VN", options).format(date);
    };

    return (
        <div className={styles.container}>
            <h2>Lịch sử đơn hàng</h2>
            {orders.length === 0 ? (
                <p className={styles.empty}>Chưa có đơn hàng nào.</p>
            ) : (
                <ul className={styles.list}>
                    {orders.map((order) => (
                        <li key={order.id} className={styles.card}>
                            <div className={styles.header}>
                                <span className={styles.orderId}>#{order.id}</span>
                                <span className={`${styles.status} ${styles[order.status.replace(/\s/g, "").toLowerCase()]}`}>
                  {order.status}
                </span>
                            </div>
                            <div className={styles.info}>
                                <div>
                                    <strong>Ngày tạo:</strong> {formatDateVN(order.createdAt)}
                                </div>
                                <div>
                                    <strong>Tổng tiền:</strong> {order.totalAmount.toLocaleString("vi-VN")}₫
                                </div>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default OrderHistoryPage;
