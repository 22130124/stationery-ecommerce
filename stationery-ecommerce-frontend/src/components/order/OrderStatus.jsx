import styles from "../../features/admin-order-management/pages/OrderManagementPage.module.scss";
import React from "react";

const OrderStatus = ({status}) => {
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

    return (
        <span className={`${styles.statusBadge} ${styles[STATUS_CLASS[status]]}`}>{STATUS_TEXT[status]}</span>
    )
}

export default OrderStatus;