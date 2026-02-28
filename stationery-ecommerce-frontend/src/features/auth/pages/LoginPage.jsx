// LoginPage.jsx
import React, {useEffect, useState} from 'react'
import {login, logout} from '../../../redux/slices/authSlice'
import AuthForm from '../components/AuthForm'
import {useNavigate, useLocation} from 'react-router-dom'
import toast from 'react-hot-toast'
import {getProfile} from '../../../api/profileApi'
import authBanner from '../assets/auth-banner.jpg';
import styles from './AuthPage.module.scss'
import {useDispatch, useSelector} from "react-redux";
import {getCart} from "../../../redux/slices/cartSlice";

const LoginPage = () => {
    const location = useLocation()
    const navigate = useNavigate()
    const dispatch = useDispatch()

    const [message, setMessage] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)

    useEffect(() => {
        dispatch(logout)
    })

    const handleSubmit = async (formData) => {
        if (formData.error) {
            setMessage(formData.error)
            return
        }

        setIsSubmitting(true)
        setMessage('')

        toast.dismiss()
        const toastId = toast.loading('Đang xử lý đăng nhập...')

        const loginResult = await dispatch(login({
            email: formData.email,
            password: formData.password
        }))

        if (login.fulfilled.match(loginResult)) {

            const data = loginResult.payload
            toast.success('Đăng nhập thành công...', { id: toastId, duration: 2000 })

            const getCartResult = await dispatch(getCart())
            if(getCart.rejected.match(getCartResult)) {
                toast.error(getCartResult.payload || "Lấy thông tin giỏ hàng thất bại", {
                    id: toastId,
                    duration: 5000
                })
            }

            const role = data.role

            if (role === 'ADMIN') {
                navigate('/admin/dashboard')
            } else {
                const profile = await getProfile()

                if (profile.status === 'INCOMPLETED') {
                    navigate('/profile')
                    return
                }

                navigate('/product-list')
            }

        } else {
            toast.error(loginResult.payload || "Đăng nhập thất bại", {
                id: toastId,
                duration: 5000
            })
        }

        setIsSubmitting(false)
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
                    isSubmitting={isSubmitting}
                />
            </div>
        </div>
    )
}

export default LoginPage