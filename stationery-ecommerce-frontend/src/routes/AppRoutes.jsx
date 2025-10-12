// AppRoutes.jsx

import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '../features/auth/pages/LoginPage';

const AppRoutes = () => {
    return (
        <Routes>
            {/* Route mặc định, chuyển hướng đến trang đăng nhập */}
            <Route path="/" element={<Navigate replace to="/login" />} />

            {/* Route cho trang đăng nhập */}
            <Route path="/login" element={<LoginPage />} />
        </Routes>
    );
};

export default AppRoutes;