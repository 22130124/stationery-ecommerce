import styles from './OrderStatus.module.scss'
import React from 'react'

const ShippingStatus = ({status}) => {
    const STATUS_TEXT = {
        '0': 'Đang lấy hàng',
        '1': 'Đang giao hàng',
        '2': 'Đã giao',
        '-1': 'Đã hủy',
    }

    const STATUS_CLASS = {
        '0': 'status-pending',
        '1': 'status-shipping',
        '2': 'status-done',
        '-1': 'status-cancel',
    }

    return (
        <span className={`${styles.statusBadge} ${styles[STATUS_CLASS[status]]}`}>{STATUS_TEXT[status]}</span>
    )
}

export default ShippingStatus