// LoginPage.jsx
import React, {useState} from 'react';
import {login} from "../../../api/authApi";
import AuthForm from "../components/AuthForm";
import {useNavigate, useLocation} from "react-router-dom";

const LoginPage = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const [message, setMessage] = useState('');
    const [isSuccess, setIsSuccess] = useState(false);

    const handleSubmitSuccess = (token) => {
        localStorage.setItem('token', token);
        setMessage("Đăng nhập thành công");
        setIsSuccess(true);

        const searchParams = new URLSearchParams(location.search);
        const redirectPath = searchParams.get("redirect");

        setTimeout(() => {
            if (redirectPath) {
                navigate(redirectPath);
            } else {
                navigate("/product-list");
            }
        }, 1000);
    };

    const handleSubmit = async (formData) => {
        if (formData.error) {
            setMessage(formData.error)
            setIsSuccess(false)
            return
        }

        try {
            const data = await login(formData.email, formData.password);
            handleSubmitSuccess(data.token);
        } catch (error) {
            setMessage(error.message);
            setIsSuccess(false);
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
            onGoogleSuccess={handleSubmitSuccess}
        />
    );
};

export default LoginPage;