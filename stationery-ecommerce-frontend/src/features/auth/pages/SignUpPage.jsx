// LoginPage.jsx
import React, {useState} from 'react';
import AuthForm from "./AuthForm";

const SignUpPage = () => {
    const [message, setMessage] = useState('');
    const [isSuccess, setIsSuccess] = useState(false);

    const handleSubmit = async (formData) => {
        try {
            setMessage("Signing up...");
            setIsSuccess(true);
        } catch (error) {
            setMessage(error.message);
            setIsSuccess(false);
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