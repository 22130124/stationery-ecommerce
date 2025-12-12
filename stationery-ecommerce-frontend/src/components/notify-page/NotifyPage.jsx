import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { AiOutlineCheckCircle, AiOutlineCloseCircle } from 'react-icons/ai';
import styles from './NotifyPage.module.scss';

function NotifyPage() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const type = searchParams.get("type") || "success"; // success | error
    const message = searchParams.get("message") || "Hoàn tất";
    const redirect = searchParams.get("redirect"); // optional
    const sec = Number(searchParams.get("sec") || 5);

    const [countdown, setCountdown] = useState(sec);

    useEffect(() => {
        if (!redirect) return;

        if (countdown <= 0) {
            navigate(redirect);
            return;
        }

        const timer = setTimeout(() => setCountdown(countdown - 1), 1000);
        return () => clearTimeout(timer);

    }, [countdown, redirect, navigate]);

    return (
        <div className={styles.container}>
            <div className={styles.messageBox}>
                {type === "success" && (
                    <AiOutlineCheckCircle className={styles.successIcon} size={70} />
                )}
                {type === "error" && (
                    <AiOutlineCloseCircle className={styles.errorIcon} size={70} />
                )}

                <div className={styles.messageText}>{message}</div>
            </div>

            {redirect && (
                <p className={styles.countdown}>
                    Chuyển trang sau {countdown} giây...
                </p>
            )}
        </div>
    );
}

export default NotifyPage;
