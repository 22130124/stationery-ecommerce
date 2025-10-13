// AuthForm.jsx
import React, { useState } from 'react';
import styles from './AuthForm.module.scss';
import { FaUser, FaLock } from 'react-icons/fa';
import {FcGoogle} from 'react-icons/fc';
import {useNavigate} from 'react-router-dom';
import {GoogleLogin, useGoogleLogin} from '@react-oauth/google';
import {loginWithGoogle} from '../../../api/authApi';

// formType: 'login' | 'signup'
const AuthForm = ({ formType, title, buttonText, onSubmit, message, isSuccess }) => {
    const navigate = useNavigate();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (formType === 'signup' && password !== confirmPassword) {
            // Xử lý lỗi mật khẩu không khớp
            onSubmit({ error: 'Confirmation password does not match' });
            return;
        }
        
        onSubmit({ username, password });
    };

    // Hàm xử lý đăng nhập Google
    const handleGoogleSuccess = async (credentialResponse) => {
        try {
            // credentialResponse.credential là ID Token để backend xác thực
            const data = await loginWithGoogle(credentialResponse.credential);
            localStorage.setItem('token', data.token);
            // navigate('/');
            alert('Success! Redirecting to the home page...');
        } catch (error) {
            console.error('Lỗi xác thực với backend:', error);
            alert('Xác thực với backend thất bại.');
        }
    };

    const handleGoogleError = () => {
        console.log('Google Login Failed');
        alert('Login failed! Please try again');
    };

    const handleSwitchPage = () => {
        const nextPage = formType === 'signup' ? 'login' : 'signup';
        navigate(`/${nextPage}`)
    }

    return (
        <div className={styles.authContainer}>
            <div className={styles.authCard}>
                <h2>{title}</h2>
                <form onSubmit={handleSubmit}>
                    {/* Input Username */}
                    <div className={styles.inputGroup}>
                        <FaUser className={styles.inputIcon} />
                        <input
                            type='text'
                            placeholder='Tài khoản'
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>

                    {/* Input Password */}
                    <div className={styles.inputGroup}>
                        <FaLock className={styles.inputIcon} />
                        <input
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