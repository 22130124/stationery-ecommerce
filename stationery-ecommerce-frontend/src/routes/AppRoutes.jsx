// AppRoutes.jsx

import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '../features/auth/pages/LoginPage';
import SignUpPage from "../features/auth/pages/SignUpPage";
import ProductListPage from "../features/product-list/pages/ProductListPage";
import ProductDetailsPage from "../features/product-details/pages/ProductDetailsPage";
import ShoppingCart from "../features/shopping-cart/pages/ShoppingCart";

const AppRoutes = () => {
    return (
        <Routes>
            {/* Route mặc định, chuyển hướng đến trang đăng nhập */}
            <Route path="/" element={<Navigate replace to="/product-list" />} />

            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignUpPage />} />
            <Route path="/product-list" element={<ProductListPage />} />
            <Route path="/:slug" element={<ProductDetailsPage />} />
            <Route path="/shopping-cart" element={<ShoppingCart />} />
        </Routes>
    );
};

export default AppRoutes;