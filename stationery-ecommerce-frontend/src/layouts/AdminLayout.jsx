// src/features/admin/layouts/AdminLayout.jsx
import React from 'react';
import { Outlet, Link, useLocation } from 'react-router-dom';
import styles from './AdminLayout.module.scss';

const AdminLayout = () => {
    const location = useLocation();

    const menuItems = [
        { label: "Quản lý sản phẩm", path: "/admin/product-management" },
        { label: "Quản lý đơn hàng", path: "/admin/order-management" },
        { label: "Quản lý tài khoản", path: "/admin/users" },
        { label: "Đăng xuất", path: "/login" },
    ];

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
                </ul>
            </aside>

            <main className={styles.content}>
                <Outlet />
            </main>
        </div>
    );
};

export default AdminLayout;
