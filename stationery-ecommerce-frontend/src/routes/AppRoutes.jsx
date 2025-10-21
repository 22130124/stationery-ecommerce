// AppRoutes.jsx

import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '../features/auth/pages/LoginPage';
import SignUpPage from "../features/auth/pages/SignUpPage";
import ProductListPage from "../features/product-list/pages/ProductListPage";

const AppRoutes = () => {
    return (
        <Routes>
            {/* Route mặc định, chuyển hướng đến trang đăng nhập */}
            <Route path="/" element={<Navigate replace to="/login" />} />

            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignUpPage />} />
            <Route path="/product-list" element={<ProductListPage />} />
        </Routes>
    );
};

export default AppRoutes;