// LoginPage.jsx
import React, {useState} from 'react'
import {login} from '../../../api/authApi'
import AuthForm from '../components/AuthForm'
import {useNavigate, useLocation} from 'react-router-dom'
import toast from 'react-hot-toast'
import { jwtDecode } from 'jwt-decode'
import {getProfile} from '../../../api/profileApi'
import authBanner from '../assets/auth-banner.jpg';
import styles from './AuthPage.module.scss'

const LoginPage = () => {
    const location = useLocation()
    const navigate = useNavigate()

    const [message, setMessage] = useState('')
    const [isSuccess, setIsSuccess] = useState(false)
    const [isSubmitting, setIsSubmitting] = useState(false)

    const handleSubmit = async (formData) => {
        if (formData.error) {
            setMessage(formData.error)
            setIsSuccess(false)
            return
        }

        setIsSubmitting(true)
        setMessage('')

        toast.dismiss()
        const toastId = toast.loading('Đang xử lý đăng nhập...')

        try {
            const data = await login(formData.email, formData.password)

            localStorage.setItem('token', data.token)

            const decoded = jwtDecode(data.token)
            const role = decoded.role || decoded.roles

            toast.success(
                'Đăng nhập thành công. Đang chuyển hướng...',
                { id: toastId, duration: 2000 }
            )

            const searchParams = new URLSearchParams(location.search)
            const redirectPath = searchParams.get('redirect')

            setTimeout(async () => {
                if (role === 'ADMIN') {
                    navigate('/admin/dashboard')
                } else {
                    // Nếu có redirectPath
                    if (redirectPath) {
                        navigate(redirectPath)
                    } else {
                        // Nếu tài khoản đăng nhập vừa được đăng ký thì chuyển hướng vào trang hồ sơ cá nhân để cập nhật thông tin cá nhân
                        const profile = await getProfile()
                        console.log(profile)
                        if (!profile.completedStatus) {
                            navigate('/profile')
                            return
                        }
                        // Nếu không thì vào trang danh sách sản phẩm
                        navigate('/product-list')
                    }
                }
            }, 2000)
        } catch (error) {
            setIsSuccess(false)
            toast.error(error.message, { id: toastId, duration: 5000 }) 
        } finally {
            setIsSubmitting(false)
        }
    }

    return (
        <div
            className={styles.loginContainer}
            style={{ backgroundImage: `url(${authBanner})` }}
        >
            <div className={styles.backgroundOverlay}></div>

            <div className={styles.formContainer}>
                <AuthForm
                    formType="login"
                    title="ĐĂNG NHẬP"
                    buttonText="Đăng nhập"
                    onSubmit={handleSubmit}
                    message={message}
                    isSuccess={isSuccess}
                    isSubmitting={isSubmitting}
                />
            </div>
        </div>
    )
}

export default LoginPage