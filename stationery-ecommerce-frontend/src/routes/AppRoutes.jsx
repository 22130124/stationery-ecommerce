// AppRoutes.jsx

import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '../features/auth/pages/LoginPage';
import SignUpPage from "../features/auth/pages/SignUpPage";

const AppRoutes = () => {
    return (
        <Routes>
            {/* Route mặc định, chuyển hướng đến trang đăng nhập */}
            <Route path="/" element={<Navigate replace to="/login" />} />

            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignUpPage />} />
        </Routes>
    );
};

export default AppRoutes;