// LoginPage.jsx
import React, {useState} from 'react';
import styles from './LoginPage.module.scss';
import {FaUser, FaLock} from 'react-icons/fa';
import {FcGoogle} from 'react-icons/fc';
import {login} from "../../../api/authApi";

const LoginPage = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [isLoggedInSuccess, setIsLoggedInSuccess] = useState(false);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const data = await login(username, password);
            localStorage.setItem('token', data.token);
            setMessage("Success! Redirecting to the home page...");
            setIsLoggedInSuccess(true);
        } catch (error) {
            setMessage(error.message);
            setIsLoggedInSuccess(false);
        }
    };

    const handleGoogleLogin = () => {
        alert('Redirecting to the Google login page...');
    };

    return (
        <div className={styles.loginContainer}>
            <div className={styles.loginCard}>
                <h2>Đăng Nhập</h2>
                <form onSubmit={handleLogin}>
                    <div className={styles.inputGroup}>
                        <FaUser className={styles.inputIcon}/>
                        <input
                            type="text"
                            placeholder="Tài khoản"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div className={styles.inputGroup}>
                        <FaLock className={styles.inputIcon}/>
                        <input
                            type="password"
                            placeholder="Mật khẩu"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    {message && <p className={isLoggedInSuccess ? styles.successMessage : styles.errorMessage}>{message}</p>}
                    <button type="submit" className={styles.loginBtn}>
                        Đăng Nhập
                    </button>
                </form>

                <div className={styles.divider}>
                    <span>HOẶC</span>
                </div>

                <button onClick={handleGoogleLogin} className={styles.googleBtn}>
                    <FcGoogle className={styles.googleIcon}/>
                    Đăng nhập với Google
                </button>
            </div>
        </div>
    );
};

export default LoginPage;