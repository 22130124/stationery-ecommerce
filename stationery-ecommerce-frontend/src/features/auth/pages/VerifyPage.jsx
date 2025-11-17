import {useEffect, useState} from 'react';
import {useNavigate, useSearchParams} from 'react-router-dom';
import {verifyAccount} from '../../../api/authApi';
import {AiOutlineCheckCircle, AiOutlineCloseCircle} from 'react-icons/ai';
import styles from './VerifyPage.module.scss'

function VerifyPage() {
    const [searchParams, setSearchParams] = useSearchParams();
    const token = searchParams.get('token');
    const navigate = useNavigate();
    const [message, setMessage] = useState('Đang xác thực...');
    const [countdown, setCountdown] = useState(5);
    const [verified, setVerified] = useState(false);
    const [error, setError] = useState(false);

    useEffect(() => {
        const fetchVerifyAccount = async () => {
            try {
                const result = await verifyAccount(token);
                setMessage(result.message);
                setVerified(true);
                setError(false);
            } catch (err) {
                setMessage(err.message || 'Xác thực thất bại');
                setError(true);
                setVerified(false);
            }
        };
        if (token) fetchVerifyAccount();
    }, [token]);

    useEffect(() => {
        if (error) return
        // Đếm ngược và redirect
        if (countdown <= 0) {
            navigate('/login');
            return;
        }
        const timer = setTimeout(() => setCountdown(countdown - 1), 1000);
        return () => clearTimeout(timer);
    }, [countdown, navigate]);

    return (
        <div className={styles.container}>
            <div className={styles.messageBox}>
                {verified && (
                    <div className={styles.iconWrapper}>
                        <AiOutlineCheckCircle className={styles.successIcon} size={64} />
                    </div>
                )}
                {error && (
                    <div className={styles.iconWrapper}>
                        <AiOutlineCloseCircle className={styles.errorIcon} size={64} />
                    </div>
                )}
                <div className={styles.messageText}>{message}</div>
            </div>

            {verified && (
                <p className={styles.countdown}>
                    Chuyển về trang đăng nhập sau {countdown} giây...
                </p>
            )}
        </div>
    );
}

export default VerifyPage;