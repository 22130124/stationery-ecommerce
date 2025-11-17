// src/routes/AppRoutes.jsx
import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

// Layout
import MainLayout from '../layouts/MainLayout'; // Import layout mới

// Pages
import LoginPage from '../features/auth/pages/LoginPage';
import SignUpPage from "../features/auth/pages/SignUpPage";
import ProductListPage from "../features/product-list/pages/ProductListPage";
import ProductDetailsPage from "../features/product-details/pages/ProductDetailsPage";
import ShoppingCart from "../features/shopping-cart/pages/ShoppingCart";
import ProductManagementPage from "../features/admin-product-management/pages/ProductManagementPage";
import VerifyPage from "../features/auth/pages/VerifyPage";

const AppRoutes = () => {
    return (
        <Routes>
            {/* Các Route không có header */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignUpPage />} />
            <Route path="/verify" element={<VerifyPage />} />
            <Route path="/admin/product-management" element={<ProductManagementPage />} />

            {/* Các Route có header */}
            <Route element={<MainLayout />}>
                <Route path="/" element={<Navigate replace to="/product-list" />} />
                <Route path="/product-list" element={<ProductListPage />} />
                <Route path="/:slug" element={<ProductDetailsPage />} />
                <Route path="/shopping-cart" element={<ShoppingCart />} />
            </Route>
        </Routes>
    );
};

export default AppRoutes;