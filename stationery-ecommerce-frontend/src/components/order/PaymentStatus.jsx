import styles from './OrderStatus.module.scss'
import React from 'react'

const PaymentStatus = ({status}) => {
    const STATUS_TEXT = {
        'UNPAID': 'Chưa thanh toán',
        'PAID': 'Đã thanh toán',
    }

    const STATUS_CLASS = {
        'UNPAID': 'status-pending',
        'PAID': 'status-done',
    }

    return (
        <span className={`${styles.statusBadge} ${styles[STATUS_CLASS[status]]}`}>{STATUS_TEXT[status]}</span>
    )
}

export default PaymentStatus