import React, {useEffect, useState} from "react";
import styles from "./OrderHistoryPage.module.scss";
import {getOrders} from "../../api/orderApi";
import ShippingStatus from "../../components/order/ShippingStatus";
import OrderDetailModal from "../admin-order-management/components/OrderDetailModal";
import PaymentStatus from "../../components/order/PaymentStatus";
import {pay} from "../../api/paymentApi";
import {useNavigate} from "react-router-dom";
import shippingStatus from "../../components/order/ShippingStatus";

const OrderHistoryPage = () => {
    const [data, setData] = useState([]);
    const [openDetail, setOpenDetail] = useState(false);
    const [selectedOrderCode, setSelectedOrderCode] = useState(null);

    useEffect(() => {
        const fetchOrders = async () => {
            const data = await getOrders();
            setData(data);
            console.log("Data", data);
        };
        fetchOrders();
    }, []);

    const handlePayClick = async (orderCode) => {
        const response = await pay(orderCode)
        const paymentUrl = response.paymentUrl
        if (paymentUrl) {
            window.location.href = paymentUrl;
        }
    }

    const handleOrderCancelled = (orderCode) => {
        setData(prev =>
            prev.map(item =>
                item.order.code === orderCode
                    ? {
                        ...item,
                        order: {
                            ...item.order,
                            shippingStatus: 'CANCELLED'
                        }
                    }
                    : item
            )
        )
    }

    return (
        <div className={styles.container}>
            <h2>Lịch sử đơn hàng</h2>
            {data.length === 0 ? (
                <p className={styles.empty}>Chưa có đơn hàng nào.</p>
            ) : (
                <ul className={styles.list}>
                    {data.map((item) => {
                            const order = item.order;
                            return (
                                <li key={order.code} className={styles.card}>
                                    <div className={styles.header}>
                                        <span className={styles.orderCode}>{order.code}</span>

                                        <div className={styles.orderStatusBadges}>
                                            <ShippingStatus status={order.shippingStatus}/>
                                        </div>
                                    </div>

                                    <div className={styles.info}>
                                        <div>
                                            <strong>Ngày tạo:</strong>{" "}
                                            {new Date(order.createdAt).toLocaleString("vi-VN")}
                                        </div>
                                        <div>
                                            <strong>Tổng tiền:</strong> {order.totalAmount.toLocaleString("vi-VN")}₫
                                        </div>
                                    </div>

                                    {order.shippingStatus === 'WAITING_PAYMENT' && order.paymentStatus === 'UNPAID' && (
                                        <div className={styles.warningMessage}>
                                            Lưu ý: Đơn hàng sẽ hết hạn sau 60 phút kể từ khi đặt hàng nếu không thanh toán.
                                        </div>
                                    )}

                                    <div className={styles.actions}>
                                        {/* Nếu chưa thanh toán thì hiện nút */}
                                        {
                                            order.paymentStatus === 'UNPAID' &&
                                            order.shippingStatus !== 'CANCELLED' &&
                                            order.shippingStatus !== 'EXPIRED' && (
                                                <button
                                                    className={styles.payBtn}
                                                    onClick={() => handlePayClick(order.code)}
                                                >
                                                    Thanh toán
                                                </button>
                                            )}

                                        <button
                                            className={styles.detailBtn}
                                            onClick={() => {
                                                setSelectedOrderCode(order.code);
                                                setOpenDetail(true);
                                            }}
                                        >
                                            Chi tiết
                                        </button>
                                    </div>
                                </li>
                            )
                        }
                    )}
                </ul>
            )}

            <OrderDetailModal
                orderCode={selectedOrderCode}
                open={openDetail}
                onClose={() => setOpenDetail(false)}
                onOrderCancelled={handleOrderCancelled}
            />
        </div>
    );
};

export default OrderHistoryPage;
