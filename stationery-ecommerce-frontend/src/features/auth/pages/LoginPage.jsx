// LoginPage.jsx
import React, {useState} from 'react';
import {login} from "../../../api/authApi";
import AuthForm from "./AuthForm";

const LoginPage = () => {
    const [message, setMessage] = useState('');
    const [isSuccess, setIsSuccess] = useState(false);

    const handleSubmit = async (formData) => {
        try {
            const data = await login(formData.username, formData.password);
            localStorage.setItem('token', data.token);
            setMessage("Success! Redirecting to the home page...");
            setIsSuccess(true);
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
        />
    );
};

export default LoginPage;