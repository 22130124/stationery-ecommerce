// SignupPage.jsx
import React, {useState} from 'react';
import AuthForm from "../components/AuthForm";
import {signUp} from "../../../api/authApi";
import {useNavigate, useLocation} from "react-router-dom";
import toast from "react-hot-toast";

const SignUpPage = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [message, setMessage] = useState('')
    const [isSuccess, setIsSuccess] = useState(false)
    const [isSubmitting, setIsSubmitting] = useState(false);

    const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d).+$/;
    const minPasswordLength = 8

    // Hàm kiểm tra mật khẩu hợp lệ
    const isValidPassword = (password) => {
        if (password.length < minPasswordLength) {
            setMessage(`Mật khẩu cần tối thiểu ${minPasswordLength} ký tự`)
            setIsSuccess(false)
            return false
        }

        if (!passwordRegex.test(password)) {
            setMessage('Mật khẩu cần kết hợp giữa các chữ cái và số')
            setIsSuccess(false)
            return false
        }

        return true
    }

    // Hàm xử lý nhấn submit
    const handleSubmit = async (formData) => {
        if (formData.error) {
            setMessage(formData.error)
            setIsSuccess(false)
            return
        }

        if (!isValidPassword(formData.password)) {
            return
        }

        setIsSubmitting(true)
        setMessage('')

        toast.dismiss()
        const toastId = toast.loading("Đang đăng ký...");

        try {
            await signUp(formData.email, formData.password)
            // cập nhật toast thành success
            toast.success(
                'Đăng ký thành công. Vui lòng kiểm tra email để xác minh tài khoản',
                { id: toastId, duration: 10000 }
            );

            const searchParams = new URLSearchParams(location.search);
            const redirectPath = searchParams.get("redirect");

            setTimeout(() => {
                if (redirectPath) {
                    navigate(`/login?redirect=${redirectPath}`);
                } else {
                    navigate("/login");
                }
            }, 1000);

        } catch (error) {
            setIsSuccess(false);
            setMessage(error.message);

            // cập nhật toast thành error
            toast.error(error.message, { id: toastId, duration: 5000 });
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <AuthForm
            formType={'signup'}
            title={'SIGN UP'}
            buttonText={'Sign up'}
            onSubmit={handleSubmit}
            message={message}
            isSuccess={isSuccess}
            isSubmitting={isSubmitting}
        />
    );
};

export default SignUpPage;