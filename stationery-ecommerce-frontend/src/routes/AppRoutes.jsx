import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

// Layout
import MainLayout from '../layouts/MainLayout';
import AdminLayout from '../layouts/AdminLayout';

// Pages
import LoginPage from '../features/auth/pages/LoginPage';
import SignUpPage from "../features/auth/pages/SignUpPage";
import VerifyPage from "../features/auth/pages/VerifyPage";
import ProductListPage from "../features/product-list/pages/ProductListPage";
import ProductDetailsPage from "../features/product-details/pages/ProductDetailsPage";
import ShoppingCart from "../features/shopping-cart/pages/ShoppingCart";
import ProfilePage from "../features/user-profile/pages/ProfilePage";
import OrderHistoryPage from "../features/order-history/OrderHistoryPage";
import ProductManagementPage from "../features/admin-product-management/pages/ProductManagementPage";
import OrderManagementPage from "../features/admin-order-management/pages/OrderManagementPage";

import RequireUserAuth from "../components/RequireUserAuth";
import RequireAdminAuth from "../components/RequireAdminAuth";

const AppRoutes = () => {
    return (
        <Routes>
            {/* Routes công khai */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/signup" element={<SignUpPage />} />
            <Route path="/verify" element={<VerifyPage />} />

            {/* Admin routes */}
            <Route element={<RequireAdminAuth />}>
                <Route element={<AdminLayout />}>
                    <Route path="/admin/product-management" element={<ProductManagementPage />} />
                    <Route path="/admin/order-management" element={<OrderManagementPage />} />
                </Route>
            </Route>

            {/* User routes có header */}
            <Route element={<MainLayout />}>
                <Route path="/" element={<Navigate replace to="/product-list" />} />
                <Route path="/product-list" element={<ProductListPage />} />
                <Route path="/:slug" element={<ProductDetailsPage />} />

                <Route element={<RequireUserAuth />}>
                    <Route path="/shopping-cart" element={<ShoppingCart />} />
                    <Route path="/profile" element={<ProfilePage />} />
                    <Route path="/order-history" element={<OrderHistoryPage />} />
                </Route>
            </Route>
        </Routes>
    );
};

export default AppRoutes;