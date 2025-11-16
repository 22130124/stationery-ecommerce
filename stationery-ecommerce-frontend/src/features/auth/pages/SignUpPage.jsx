// LoginPage.jsx
import React, {useState} from 'react';
import AuthForm from "../components/AuthForm";
import {signUp} from "../../../api/authApi";
import {useNavigate, useLocation} from "react-router-dom";

const SignUpPage = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [message, setMessage] = useState('')
    const [isSuccess, setIsSuccess] = useState(false)

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

    // Hàm xử lý nếu đăng ký tài khoản thành công
    const handleSubmitSuccess = () => {
        setIsSuccess(true)
        setMessage("Đăng ký thành công");

        const searchParams = new URLSearchParams(location.search);
        const redirectPath = searchParams.get("redirect");

        setTimeout(() => {
            if (redirectPath) {
                navigate(`/login?redirect=${redirectPath}`);
            } else {
                navigate("/login");
            }
        }, 1000);
    };

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

        console.log(formData)

        try {
            const data = await signUp(formData.email, formData.password)
            handleSubmitSuccess()
        } catch (error) {
            setMessage(error.message)
            setIsSuccess(false)
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
        />
    );
};

export default SignUpPage;