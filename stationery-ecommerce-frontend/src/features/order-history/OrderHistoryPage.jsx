import React, {useEffect, useState} from "react";
import styles from "./OrderHistoryPage.module.scss";
import {getOrders} from "../../api/orderApi";
import ShippingStatus from "../../components/order/ShippingStatus";
import OrderDetailModal from "../admin-order-management/components/OrderDetailModal";
import PaymentStatus from "../../components/order/PaymentStatus";
import {pay} from "../../api/paymentApi";
import {useNavigate} from "react-router-dom";

const OrderHistoryPage = () => {
    const [orders, setOrders] = useState([]);
    const [openDetail, setOpenDetail] = useState(false);
    const [selectedOrderId, setSelectedOrderId] = useState(null);

    useEffect(() => {
        const fetchOrders = async () => {
            const data = await getOrders();
            setOrders(data);
        };
        fetchOrders();
    }, []);

    const formatDateVN = (isoString) => {
        if (!isoString) return "";

        const normalized = isoString.endsWith("Z") ? isoString : isoString + "Z";

        const date = new Date(normalized);

        return new Intl.DateTimeFormat("vi-VN", {
            timeZone: "Asia/Ho_Chi_Minh",
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit",
        }).format(date);
    };

    const handlePayClick = async (orderId) => {
        const response = await pay(orderId)
        const paymentUrl = response.paymentUrl
        if (paymentUrl) {
            window.location.href = paymentUrl;
        }
    }

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
                                <div className={styles.orderStatusBadges}>
                                    <ShippingStatus status={order.shippingStatus}/>
                                    {order.shippingStatus !== -1 && (
                                        <PaymentStatus status={order.paymentStatus}/>
                                    )}
                                </div>
                            </div>

                            <div className={styles.info}>
                                <div>
                                    <strong>Ngày tạo:</strong> {formatDateVN(order.createdAt)}
                                </div>
                                <div>
                                    <strong>Tổng tiền:</strong> {order.totalAmount.toLocaleString("vi-VN")}₫
                                </div>
                            </div>

                            <div className={styles.actions}>
                                {/* Nếu chưa thanh toán thì hiện nút */}
                                {order.paymentStatus === 0 && order.shippingStatus !== -1 && (
                                    <button
                                        className={styles.payBtn}
                                        onClick={() => handlePayClick(order.id)}
                                    >
                                        Thanh toán
                                    </button>
                                )}

                                <button
                                    className={styles.detailBtn}
                                    onClick={() => {
                                        setSelectedOrderId(order.id);
                                        setOpenDetail(true);
                                    }}
                                >
                                    Chi tiết
                                </button>
                            </div>
                        </li>
                    ))}
                </ul>
            )}

            <OrderDetailModal
                orderId={selectedOrderId}
                open={openDetail}
                onClose={() => setOpenDetail(false)}
            />
        </div>
    );
};

export default OrderHistoryPage;
