import styles from './OrderStatus.module.scss'
import React from 'react'

const PaymentStatus = ({status}) => {
    const STATUS_TEXT = {
        '0': 'Chưa thanh toán',
        '1': 'Đã thanh toán',
        '-1': 'Đã hủy',
    }

    const STATUS_CLASS = {
        '0': 'status-pending',
        '1': 'status-done',
        '-1': 'status-cancel',
    }

    return (
        <span className={`${styles.statusBadge} ${styles[STATUS_CLASS[status]]}`}>{STATUS_TEXT[status]}</span>
    )
}

export default PaymentStatus