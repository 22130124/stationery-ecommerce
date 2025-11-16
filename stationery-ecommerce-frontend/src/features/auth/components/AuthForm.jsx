// AuthForm.jsx
import React, { useState } from 'react';
import styles from './AuthForm.module.scss';
import { FaUser, FaLock } from 'react-icons/fa';
import {useNavigate, useLocation} from 'react-router-dom';
import {GoogleLogin} from '@react-oauth/google';
import {loginWithGoogle} from '../../../api/authApi';

// formType: 'login' | 'signup'
const AuthForm = ({ formType, title, buttonText, onSubmit, message, isSuccess, onGoogleSuccess }) => {
    const navigate = useNavigate();
    const location = useLocation();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    const handleSubmit = (e) => {
        e.preventDefault();
        if (formType === 'signup' && password !== confirmPassword) {
            onSubmit({ error: 'Mật khẩu không khớp' })
            return
        }

        if (!isValidEmail(email)) {
            onSubmit({ error: 'Vui lòng nhập email hợp lệ' })
            return
        }
        
        onSubmit({ email, password })
    };

    // Hàm kiểm tra định dạng email
    function isValidEmail(email) {
        return emailRegex.test(email);
    }

    // Hàm xử lý đăng nhập Google
    const handleGoogleSuccess = async (credentialResponse) => {
        try {
            // credentialResponse.credential là ID Token để backend xác thực
            const data = await loginWithGoogle(credentialResponse.credential);
            if (onGoogleSuccess) {
                onGoogleSuccess(data.token);
            }
        } catch (error) {
            console.error('Lỗi xác thực với backend:', error);
            alert('Xác thực với backend thất bại.');
        }
    };

    // Hàm xử lý đăng nhập Google thất bại
    const handleGoogleError = () => {
        console.log('Google Login Failed');
        alert('Đăng nhập thất bại hãy thử lại sau');
    };

    // Hàm xử lý chuyển đổi giữa 2 trang login và signup
    const handleSwitchPage = () => {
        const nextPage = formType === 'signup' ? 'login' : 'signup';
        const searchParams = new URLSearchParams(location.search);
        const redirectPath = searchParams.get("redirect");

        if (redirectPath) {
            navigate(`/${nextPage}?redirect=${redirectPath}`);
        } else {
            navigate(`/${nextPage}`);
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

                    {/* Hiển thị thông báo lỗi hoặc thành công */}
                    {message && (
                        <p className={isSuccess ? styles.successMessage : styles.errorMessage}>
                            {message}
                        </p>
                    )}

                    <button type='submit' className={styles.submitBtn}>
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
                            text="signin_with"
                        />
                    </div>
                </div>

                <button className={styles.switchBtn} onClick={handleSwitchPage}>
                    {formType === 'login' ? 'Create New Account' : 'Back To Login'}
                </button>
            </div>
        </div>
    );
};

export default AuthForm;