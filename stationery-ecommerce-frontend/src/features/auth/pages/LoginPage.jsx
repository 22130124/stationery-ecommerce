// LoginPage.jsx
import React, {useState} from 'react';
import {login} from "../../../api/authApi";
import AuthForm from "../components/AuthForm";
import {useNavigate, useLocation} from "react-router-dom";
import toast from "react-hot-toast";

const LoginPage = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const [message, setMessage] = useState('');
    const [isSuccess, setIsSuccess] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (formData) => {
        if (formData.error) {
            setMessage(formData.error)
            setIsSuccess(false)
            return
        }

        setIsSubmitting(true)
        setMessage('')

        toast.dismiss()
        const toastId = toast.loading("Đang xử lý đăng nhập...");

        try {
            const data = await login(formData.email, formData.password);

            toast.success(
                'Đăng nhập thành công. Đang chuyển hướng tới trang mua hàng...',
                { id: toastId, duration: 2000 }
            );

            localStorage.setItem("token", data.token);

            const searchParams = new URLSearchParams(location.search);
            const redirectPath = searchParams.get("redirect");

            setTimeout(() => {
                if (redirectPath) {
                    navigate(redirectPath);
                } else {
                    navigate("/product-list");
                }
            }, 2000);
        } catch (error) {
            setIsSuccess(false);
            toast.error(error.message, { id: toastId, duration: 5000 });
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <AuthForm
            formType={'login'}
            title={'LOGIN'}
            buttonText={'Login'}
            onSubmit={handleSubmit}
            message={message}
            isSuccess={isSuccess}
            isSubmitting={isSubmitting}
        />
    );
};

export default LoginPage;