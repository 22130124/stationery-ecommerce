// LoginPage.jsx
import React, { useState } from 'react';
import styles from './LoginPage.module.scss';
import { FaUser, FaLock } from 'react-icons/fa';
import { FcGoogle } from 'react-icons/fc';

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = (e) => {
        e.preventDefault();
        alert(`Đăng nhập với:\nTài khoản: ${username}\nMật khẩu: ${password}`);
    };

    const handleGoogleLogin = () => {
        alert('Đang chuyển hướng đến trang đăng nhập của Google...');
    };

    return (
        <div className={styles.loginContainer}>
            <div className={styles.loginCard}>
                <h2>Đăng Nhập</h2>
                <form onSubmit={handleLogin}>
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
                    <button type="submit" className={styles.loginBtn}>
                        Đăng Nhập
                    </button>
                </form>

                <div className={styles.divider}>
                    <span>HOẶC</span>
                </div>

                <button onClick={handleGoogleLogin} className={styles.googleBtn}>
                    <FcGoogle className={styles.googleIcon} />
                    Đăng nhập với Google
                </button>
            </div>
        </div>
    );
};

export default LoginPage;