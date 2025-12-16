// AuthForm.jsx
import React, { useState } from 'react'
import styles from './AuthForm.module.scss'
import { FaUser, FaLock } from 'react-icons/fa'
import {useNavigate, useLocation} from 'react-router-dom'
import {GoogleLogin} from '@react-oauth/google'
import {loginWithGoogle} from '../../../api/authApi'
import toast from 'react-hot-toast'
import {getProfile} from '../../../api/profileApi'

// formType: 'login' | 'signup'
const AuthForm = ({ formType, title, buttonText, onSubmit, message, isSuccess, isSubmitting}) => {
    const navigate = useNavigate()
    const location = useLocation()

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [confirmPassword, setConfirmPassword] = useState('')

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

    const handleSubmit = (e) => {
        e.preventDefault()
        if (formType === 'signup' && password !== confirmPassword) {
            onSubmit({ error: 'Mật khẩu không khớp' })
            return
        }

        if (!isValidEmail(email)) {
            onSubmit({ error: 'Vui lòng nhập email hợp lệ' })
            return
        }
        
        onSubmit({ email, password })
    }

    // Hàm kiểm tra định dạng email
    function isValidEmail(email) {
        return emailRegex.test(email)
    }

    // Hàm xử lý đăng nhập Google thành công
    const handleGoogleSuccess = async (credentialResponse) => {
        toast.dismiss()
        const toastId = toast.loading('Đang xử lý đăng nhập...')
        try {
            // credentialResponse.credential là ID Token để backend xác thực
            const data = await loginWithGoogle(credentialResponse.credential)
            localStorage.setItem('token', data.token)

            toast.success('Đăng nhập thành công. Đang chuyển hướng...',
                { id: toastId, duration: 2000 }
            )

            const searchParams = new URLSearchParams(location.search)
            const redirectPath = searchParams.get('redirect')

            setTimeout(async () => {
                // Nếu có redirectPath
                if (redirectPath) {
                    navigate(redirectPath)
                } else {
                    // Nếu tài khoản đăng nhập vừa được đăng ký thì chuyển hướng vào trang hồ sơ cá nhân để cập nhật thông tin cá nhân
                    const profile = await getProfile()
                    if (!profile.completedStatus) {
                        navigate('/profile')
                        return
                    }
                    // Nếu không thì vào trang danh sách sản phẩm
                    navigate('/profile')
                }
            }, 2000)
        } catch (error) {
            toast.dismiss()
            toast.error(error.message, { duration: 5000 })
        }
    }

    // Hàm xử lý đăng nhập Google thất bại
    const handleGoogleError = () => {
        toast.error('Đăng nhập Google thất bại. Vui lòng thử lại!')
    }

    // Hàm xử lý chuyển đổi giữa 2 trang login và signup
    const handleSwitchPage = () => {
        const nextPage = formType === 'signup' ? 'login' : 'signup'
        const searchParams = new URLSearchParams(location.search)
        const redirectPath = searchParams.get('redirect')

        if (redirectPath) {
            navigate(`/${nextPage}?redirect=${redirectPath}`)
        } else {
            navigate(`/${nextPage}`)
        }
    }

    return (
        <div className={styles.authContainer}>
            <div className={styles.authCard}>
                <h2>{title}</h2>
                <form onSubmit={handleSubmit}>
                    {/* Input Email */}
                    <div className={styles.inputGroup}>
                        <FaUser className={styles.inputIcon} />
                        <input
                            id='email'
                            type='text'
                            placeholder='Email'
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    {/* Input Password */}
                    <div className={styles.inputGroup}>
                        <FaLock className={styles.inputIcon} />
                        <input
                            id='password'
                            type='password'
                            placeholder='Mật khẩu'
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    {/* Input Confirm Password (chỉ hiển thị khi đăng ký) */}
                    {formType === 'signup' && (
                        <div className={styles.inputGroup}>
                            <FaLock className={styles.inputIcon} />
                            <input
                                type='password'
                                placeholder='Xác nhận mật khẩu'
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                            />
                        </div>
                    )}

                    <button type='submit'
                            className={styles.submitBtn}
                            disabled={isSubmitting}>
                        {buttonText}
                    </button>
                </form>

                <div className={styles.loginCard} style={{paddingTop: 0, background: 'none', boxShadow: 'none'}}>
                    <div className={styles.divider}>
                        <span>HOẶC</span>
                    </div>
                    <div className={styles.googleBtnContainer}>
                        <GoogleLogin
                            onSuccess={handleGoogleSuccess}
                            onError={handleGoogleError}
                            useOneTap={false}
                            text='signin_with'
                        />
                    </div>
                </div>

                <button className={styles.switchBtn}
                        onClick={handleSwitchPage}
                        disabled={isSubmitting}
                >
                    {formType === 'login' ? 'Create New Account' : 'Back To Login'}
                </button>
            </div>
        </div>
    )
}

export default AuthForm