import styles from './OrderStatus.module.scss'
import React from 'react'

const ShippingStatus = ({status}) => {
    const STATUS_TEXT = {
        'WAITING_PAYMENT': 'Chờ thanh toán',
        'READY_TO_PICK': 'Đang lấy hàng',
        'SHIPPING': 'Đang giao hàng',
        'DELIVERED': 'Đã giao',
        'CANCELLED': 'Đã hủy',
        'EXPIRED': 'Hết hạn'
    }

    const STATUS_CLASS = {
        'WAITING_PAYMENT': 'status-pending',
        'READY_TO_PICK': 'status-pending',
        'SHIPPING': 'status-shipping',
        'DELIVERED': 'status-done',
        'CANCELLED': 'status-cancel',
        'EXPIRED': 'status-cancel',
    }

    return (
        <span className={`${styles.statusBadge} ${styles[STATUS_CLASS[status]]}`}>{STATUS_TEXT[status]}</span>
    )
}

export default ShippingStatus