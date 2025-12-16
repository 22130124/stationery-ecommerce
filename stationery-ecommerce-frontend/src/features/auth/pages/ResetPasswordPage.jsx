import {useNavigate, useSearchParams} from 'react-router-dom';
import {useState} from 'react';
import toast from 'react-hot-toast';
import {resetPassword} from '../../../api/authApi';
import styles from './ResetPasswordPage.module.scss'

export const ResetPasswordPage = () => {
    const [params] = useSearchParams()
    const navigate = useNavigate()
    const token = params.get('token')

    const [newPassword, setNewPassword] = useState('')
    const [confirmPassword, setConfirmPassword] = useState('')
    const [loading, setLoading] = useState(false)
    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d).+$/
    const minPasswordLength = 8

    // Hàm kiểm tra mật khẩu hợp lệ
    const isValidPassword = (newPassword) => {
        if (newPassword.length < minPasswordLength) {
            toast.error(`Mật khẩu cần tối thiểu ${minPasswordLength} ký tự`)
            return false
        }

        if (!passwordRegex.test(newPassword)) {
            toast.error('Mật khẩu cần kết hợp giữa các chữ cái và số')
            return false
        }

        return true
    }

    const handleSubmit = async (e) => {
        e.preventDefault()

        if (newPassword !== confirmPassword) {
            toast.error('Mật khẩu không khớp')
            return
        }

        if (!isValidPassword(newPassword)) {
            return
        }

        setLoading(true)
        try {
            await resetPassword({
                token: token,
                newPassword: newPassword,
            })
            toast.success('Đổi mật khẩu thành công')
            navigate('/login')
        } catch (err) {
            toast.error(err?.message || 'Token không hợp lệ hoặc đã hết hạn')
        } finally {
            setLoading(false)
        }
    }

    if (!token) {
        return <p>Link không hợp lệ</p>
    }

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <h2>Đặt lại mật khẩu</h2>

                <form onSubmit={handleSubmit}>
                    <input
                        type='password'
                        placeholder='Mật khẩu mới'
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        required
                    />

                    <input
                        type='password'
                        placeholder='Xác nhận mật khẩu'
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />

                    <button type='submit' disabled={loading}>
                        {loading ? 'Đang xử lý...' : 'Đổi mật khẩu'}
                    </button>
                </form>
            </div>
        </div>
    )
}