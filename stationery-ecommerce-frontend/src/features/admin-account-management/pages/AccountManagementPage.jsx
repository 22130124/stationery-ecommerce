import React, { useEffect, useState } from "react";
import { Table, Modal, Select } from "antd";
import toast from "react-hot-toast";
import styles from "./AccountManagementPage.module.scss";
import {getAllAccounts, lockAccount, toAdmin, toUser, unlockAccount} from "../../../api/authApi";

const { confirm } = Modal;
const { Option } = Select;

const ROLE_OPTIONS = ["USER", "ADMIN"];

const AccountManagementPage = () => {
    const [accounts, setAccounts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [searchText, setSearchText] = useState("");

    useEffect(() => {
        const fetchAccounts = async () => {
            setLoading(true);
            const data = await getAllAccounts();
            if (!data) {
                setLoading(false);
                return;
            }
            setAccounts(data);
            setLoading(false);
        };

        fetchAccounts();
    }, []);

    // const handleRoleChange = async (accountId, newRole) => {
    //     let data
    //     if (newRole === "ADMIN") {
    //         data = await toAdmin(accountId);
    //     } else if (newRole === "USER") {
    //         data = await toUser(accountId)
    //     }
    //     if (!data) return;
    //     setAccounts(prev =>
    //         prev.map(a => (a.id === accountId ? { ...a, role: newRole } : a))
    //     );
    //     toast.dismiss();
    //     toast.success("Cập nhật quyền thành công");
    // };

    const handleRoleChange = (accountId, newRole) => {
        confirm({
            title: "Thay đổi quyền của người dùng",
            content: `Bạn có chắc muốn thay đổi quyền của tài khoản này thành ${newRole}?`,
            okText: "Xác nhận",
            cancelText: "Hủy",
            onOk: async () => {
                let data
                if (newRole === "ADMIN") {
                    data = await toAdmin(accountId);
                } else if (newRole === "USER") {
                    data = await toUser(accountId)
                }
                if (!data) return;
                setAccounts(prev =>
                    prev.map(a => (a.id === accountId ? { ...a, role: newRole } : a))
                );
                toast.dismiss();
                toast.success("Cập nhật quyền thành công");
            }
        });
    };

    const handleToggleStatus = (account) => {
        confirm({
            title: account.activeStatus ? "Khóa tài khoản" : "Mở khóa tài khoản",
            content: `Bạn có chắc muốn ${account.activeStatus ? "khóa" : "mở khóa"} tài khoản ${account.email}?`,
            okText: "Xác nhận",
            cancelText: "Hủy",
            onOk: async () => {
                let data
                if (account.activeStatus) {
                    data = await lockAccount(account.id)
                } else {
                    data = await unlockAccount(account.id)
                }
                if (!data) return;
                setAccounts(prev =>
                    prev.map(a => (a.id === account.id ? { ...a, activeStatus: !a.activeStatus } : a))
                );
                toast.dismiss();
                toast.success("Cập nhật trạng thái thành công");
            }
        });
    };

    const filteredData = accounts.filter(
        a =>
            a.email.toLowerCase().includes(searchText.toLowerCase()) ||
            a.id.toString().includes(searchText)
    );

    const columns = [
        { title: "ID", dataIndex: "id", key: "id", sorter: (a, b) => a.id - b.id },
        { title: "Email", dataIndex: "email", key: "email" },
        {
            title: "Quyền hạn",
            dataIndex: "role",
            key: "role",
            render: (role, record) => (
                <Select
                    value={role}
                    style={{ width: 120 }}
                    onChange={(val) => handleRoleChange(record.id, val)}
                >
                    {ROLE_OPTIONS.map(r => (
                        <Option key={r} value={r}>{r}</Option>
                    ))}
                </Select>
            ),
        },
        {
            title: "Trạng thái",
            dataIndex: "activeStatus",
            key: "activeStatus",
            render: (active) => (
                <span className={active ? styles.active : styles.inactive}>
                    {active ? "Hoạt động" : "Bị khóa"}
                </span>
            ),
        },
        {
            title: "Đã xác thực",
            dataIndex: "verified",
            key: "verified",
            render: (v) => (v ? "Có" : "Chưa"),
        },
        {
            title: "Ngày tạo",
            dataIndex: "createdAt",
            key: "createdAt",
            render: (value) => new Date(value).toLocaleString("vi-VN"),
            sorter: (a, b) => new Date(a.createdAt) - new Date(b.createdAt),
        },
        {
            title: "Hành động",
            key: "action",
            render: (_, record) => (
                <div className={styles.actions}>
                    <button
                        className={styles.toggleBtn}
                        onClick={() => handleToggleStatus(record)}
                    >
                        {record.activeStatus ? "Khóa" : "Mở khóa"}
                    </button>
                </div>
            ),
        },
    ];

    return (
        <div className={styles.pageContainer}>
            <header className={styles.header}>
                <h1>Quản lý tài khoản người dùng</h1>
                <p>Phân quyền và khóa/mở khóa tài khoản.</p>
            </header>

            <div className={styles.actionBar}>
                <input
                    type="text"
                    className={styles.searchInput}
                    placeholder="Tìm kiếm theo email hoặc ID..."
                    onChange={(e) => setSearchText(e.target.value)}
                />
            </div>

            <Table
                columns={columns}
                dataSource={filteredData}
                rowKey="id"
                loading={loading}
                pagination={{ pageSize: 10, showSizeChanger: true }}
                bordered
            />
        </div>
    );
};

export default AccountManagementPage;
