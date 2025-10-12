// LoginPage.jsx
import React, {useState} from 'react';
import AuthForm from "./AuthForm";
import {signUp} from "../../../api/authApi";
import {useNavigate} from "react-router-dom";

const SignUpPage = () => {
    const navigate = useNavigate();

    const [message, setMessage] = useState('')
    const [isSuccess, setIsSuccess] = useState(false)

    const validateInput = (formData) => {
        const minUsernameLength = 6
        const minPasswordLength = 8
        const usernameRegex = /^[a-zA-Z0-9]+$/ // Chỉ cho phép chữ cái (hoa, thường) và số
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d).+$/;

        const username = formData.username
        const password = formData.password

        // Kiểm tra username
        if (username.length < minUsernameLength) {
            setMessage(`Username must be at least ${minUsernameLength} characters`)
            setIsSuccess(false)
            return false;
        }

        if (!usernameRegex.test(username)) {
            setMessage("Username must not contains special characters")
            setIsSuccess(false)
            return false;
        }

        // Kiểm tra password
        if (password.length < minPasswordLength) {
            setMessage(`Password must be at least ${minPasswordLength} characters`)
            setIsSuccess(false)
            return false;
        }

        if (!passwordRegex.test(password)) {
            setMessage(`Password must contain both letters and numbers`)
            setIsSuccess(false)
            return false;
        }

        return true;
    }

    const handleSubmit = async (formData) => {
        if (formData.error) {
            setMessage(formData.error)
            setIsSuccess(false)
            return
        }

        if(!validateInput(formData)) {
            return
        }

        try {
            const data = await signUp(formData.username, formData.password)
            setMessage("Success! Redirecting to the login page...")
            setIsSuccess(true)
            delayNavigate()
        } catch (error) {
            setMessage(error.message)
            setIsSuccess(false)
        }
    };

    const delayNavigate = () => {
        setTimeout(() => {
            navigate("/login");
        }, 2000)
    }

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