// AuthForm.jsx
import React, { useState } from 'react';
import styles from './AuthForm.module.scss';
import { FaUser, FaLock } from 'react-icons/fa';
import {FcGoogle} from "react-icons/fc";
import {useNavigate} from "react-router-dom";

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

    const handleGoogleLogin = () => {
        alert('Redirecting to the Google auth page...');
    };

    const handleSwitchPage = () => {
        const nextPage = formType === "signup" ? "login" : "signup";
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
                            type="text"
                            placeholder="Tài khoản"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>

                    {/* Input Password */}
                    <div className={styles.inputGroup}>
                        <FaLock className={styles.inputIcon} />
                        <input
                            type="password"
                            placeholder="Mật khẩu"
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
                                type="password"
                                placeholder="Xác nhận mật khẩu"
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

                    <button type="submit" className={styles.submitBtn}>
                        {buttonText}
                    </button>
                </form>

                <div className={styles.loginCard} style={{paddingTop: 0, background: 'none', boxShadow: 'none'}}>
                    <div className={styles.divider}>
                        <span>HOẶC</span>
                    </div>
                    <button onClick={handleGoogleLogin} className={styles.googleBtn}>
                        <FcGoogle className={styles.googleIcon} />
                        Đăng nhập với Google
                    </button>
                </div>

                <button className={styles.switchBtn} onClick={handleSwitchPage}>
                    {formType === "login" ? "Create New Account" : "Back To Login"}
                </button>
            </div>
        </div>
    );
};

export default AuthForm;