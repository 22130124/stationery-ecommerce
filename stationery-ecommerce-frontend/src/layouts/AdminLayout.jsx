// src/features/admin/layouts/AdminLayout.jsx
import React from 'react';
import {Outlet, Link, useLocation, useNavigate} from 'react-router-dom';
import styles from './AdminLayout.module.scss';
import RequireAuth from "../components/RequireAdminAuth";
import {useDispatch} from "react-redux";
import {logout} from "../features/auth/slice/authSlice";

const AdminLayout = () => {
    const location = useLocation();
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const menuItems = [
        {label: "Tổng quan", path: "/admin/dashboard"},
        {label: "Quản lý sản phẩm", path: "/admin/product-management"},
        {label: "Quản lý đơn hàng", path: "/admin/order-management"},
        {label: "Quản lý tài khoản", path: "/admin/account-management"},
        {label: "Quản lý danh mục", path: "/admin/category-management"},
        {label: "Quản lý nhà cung cấp và thương hiệu", path: "/admin/supplier-management"},
    ];

    const handleLogout = () => {
        dispatch(logout())
        navigate('/login', {replace: true})
    }

    return (
        <div className={styles.container}>
            <aside className={styles.sidebar}>
                <h2 className={styles.logo}>ADMIN</h2>

                <ul className={styles.menu}>
                    {menuItems.map(item => (
                        <li
                            key={item.path}
                            className={location.pathname === item.path ? styles.active : ""}
                        >
                            <Link to={item.path}>{item.label}</Link>
                        </li>
                    ))}
                    <li key='logout' onClick={handleLogout}>
                        <Link>Đăng xuất</Link>
                    </li>
                </ul>
            </aside>

            <main className={styles.content}>
                <Outlet/>
            </main>
        </div>
    );
};

export default AdminLayout;
