import React, {useEffect, useState} from "react";
import {Modal, Spin} from "antd";
import toast from "react-hot-toast";
import {getOrderDetail} from "../../../api/orderApi";
import styles from "./OrderDetailModal.module.scss";
import OrderStatus from "../../../components/order/OrderStatus";

const OrderDetailModal = ({orderId, open, onClose}) => {
    const [loading, setLoading] = useState(false);
    const [orderDetail, setOrderDetail] = useState(null);
    const [profileDetail, setProfileDetail] = useState(null);

    const STATUS_TEXT = {
        1: 'Đang lấy hàng',
        2: 'Đang giao hàng',
        3: 'Đã giao',
        0: 'Đã hủy',
    };

    const STATUS_CLASS = {
        1: 'status-pending',
        2: 'status-shipping',
        3: 'status-done',
        0: 'status-cancel',
    };

    useEffect(() => {
        if (!orderId) return;

        const fetchDetail = async () => {
            setLoading(true);
            const data = await getOrderDetail(orderId);
            if (!data) {
                setLoading(false);
                return
            }
            setOrderDetail(data.order);
            setProfileDetail(data.profile);
        };

        fetchDetail();
    }, [orderId]);
    console.log("orderDetail", orderDetail);
    console.log("profileDetail", profileDetail);

    return (
        <Modal
            open={open}
            title={<>Chi tiết đơn hàng <span style={{opacity: 0.6}}>#{orderId}</span></>}
            onCancel={onClose}
            footer={null}
            width={650}
        >
            {loading ? (
                <div style={{textAlign: "center", padding: 50}}>
                    <Spin/>
                </div>
            ) : orderDetail ? (
                <div className={styles.orderDetailWrapper}>

                    {/* KHÁCH HÀNG */}
                    <div className={styles.card}>
                        <div className={styles.cardTitle}>Thông tin khách hàng</div>
                        <div className={styles.grid}>
                            <div><b>Tên:</b> {profileDetail?.fullName}</div>
                            <div><b>Email:</b> {profileDetail?.email}</div>
                            <div><b>Điện thoại:</b> {profileDetail?.phone}</div>
                        </div>
                    </div>

                    {/* ĐƠN HÀNG */}
                    <div className={styles.card}>
                        <div className={styles.cardTitle}>Thông tin đơn hàng</div>
                        <div className={styles.grid}>
                            <div>
                                <b>Tổng tiền:</b> {orderDetail?.totalAmount?.toLocaleString("vi-VN", {
                                style: "currency", currency: "VND"
                            })}
                            </div>

                            <div>
                                <b>Trạng thái:</b>
                                <OrderStatus status={orderDetail.status}/>
                            </div>

                            <div>
                                <b>Ngày tạo:</b> {new Date(orderDetail?.createdAt).toLocaleString("vi-VN")}
                            </div>
                        </div>
                    </div>

                    {/* SẢN PHẨM */}
                    <div className={styles.card}>
                        <div className={styles.cardTitle}>Danh sách sản phẩm</div>

                        <table className={styles.productTable}>
                            <thead>
                            <tr>
                                <th>Mã SP</th>
                                <th>Số lượng</th>
                                <th>Giá</th>
                            </tr>
                            </thead>
                            <tbody>
                            {orderDetail.orderItems?.map(item => (
                                <tr key={item.id}>
                                    <td>{item.productId}</td>
                                    <td>{item.quantity}</td>
                                    <td>{item.price?.toLocaleString("vi-VN", {
                                        style: "currency",
                                        currency: "VND",
                                    })}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>

                </div>
            ) : (
                <p>Không có dữ liệu</p>
            )}
        </Modal>
    );
};

export default OrderDetailModal;