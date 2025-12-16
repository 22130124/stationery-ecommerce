import { useState } from 'react'
import toast from 'react-hot-toast'

import styles from './ForgotPasswordPage.module.scss'
import {forgotPassword} from "../../../api/authApi";

export const ForgotPasswordPage = () => {
    const [email, setEmail] = useState('')
    const [loading, setLoading] = useState(false)

    const handleSubmit = async (e) => {
        e.preventDefault()
        setLoading(true)

        try {
            await forgotPassword({email})
            toast.dismiss()
            toast.success('Một tin nhắn đã được gửi về hộp thư email của bạn. Vui lòng kiểm tra hộp thư để thực hiện đặt lại mật khẩu')
        } catch (err) {
            toast.dismiss()
            toast.error(err?.message || 'Có lỗi xảy ra')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <h2>Quên mật khẩu</h2>
                <p>Nhập email để nhận link đặt lại mật khẩu</p>

                <form onSubmit={handleSubmit}>
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />

                    <button type="submit" disabled={loading}>
                        {loading ? 'Đang gửi...' : 'Gửi email'}
                    </button>
                </form>
            </div>
        </div>
    )
}
