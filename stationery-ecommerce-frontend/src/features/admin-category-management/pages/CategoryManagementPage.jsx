import React, { useEffect, useState } from "react";
import { Table, Modal } from "antd";
import styles from "./CategoryManagementPage.module.scss";
import toast from "react-hot-toast";
import {getAllCategories, createCategory, updateCategory, deleteCategory} from "../../../api/categoryApi";
import CategoryModal from "../components/CategoryModal";

const CategoryManagementPage = () => {
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(false);
    const [searchText, setSearchText] = useState("");
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [modalMode, setModalMode] = useState(null); // 'create' | 'edit'

    const { confirm } = Modal;

    // Fetch categories
    useEffect(() => {
        fetchCategories();
    }, []);

    const fetchCategories = async () => {
        setLoading(true);
        const data = await getAllCategories();
        setLoading(false);
        if (!data) return;
        setCategories(data);
    }

    // Mở modal để thêm mới danh mục
    const openCreateModal = () => {
        setModalMode("create");
        setSelectedCategory(null);
    };

    // Mở modal để chỉnh sửa thông tin danh mục
    const openEditModal = (category) => {
        setModalMode("edit");
        setSelectedCategory(category);
    };

    // Xử lý xóa danh mục
    const handleDelete = (category) => {
        confirm({
            title: "Xóa danh mục",
            content: `Bạn có chắc muốn xóa danh mục "${category.name}"?`,
            okText: "Xóa",
            okType: "danger",
            cancelText: "Hủy",
            onOk: async () => {
                const success = await deleteCategory(category.id);
                if (!success) return;
                toast.success("Xóa thành công");
                fetchCategories();
            }
        });
    };

    // Xử lý submit thêm mới/chỉnh sửa danh mục
    const handleSubmit = async (formData) => {
        let result = null;

        if (modalMode === "create") {
            result = await createCategory(formData);
        } else if (modalMode === "edit") {
            result = await updateCategory(selectedCategory.id, formData);
        }

        if (!result) return;

        toast.success("Lưu thành công");
        fetchCategories();
        setModalMode(null);
    };

    // Hàm lọc cây danh mục theo từ khóa tìm kiếm
    const filterTree = (nodes) => {
        if (!searchText) return nodes;

        const search = searchText.toLowerCase();

        const walk = (node) => {
            const isMatch = node.name.toLowerCase().includes(search);

            if (!node.children) return isMatch ? { ...node } : null;

            const filteredChildren = node.children
                .map(walk)
                .filter(Boolean);

            if (isMatch || filteredChildren.length > 0) {
                return {
                    ...node,
                    children: filteredChildren
                };
            }

            return null;
        };

        return nodes
            .map(walk)
            .filter(Boolean);
    };

    const columns = [
        {
            title: "Tên danh mục",
            dataIndex: "name",
            key: "name",
        },
        {
            title: "Slug",
            dataIndex: "slug",
            key: "slug",
        },
        {
            title: "Trạng thái",
            dataIndex: "status",
            key: "status",
            render: (v) =>
                v === 'ACTIVE' ? (
                    <span className={styles.active}>Hoạt động</span>
                ) : (
                    <span className={styles.inactive}>Đã ẩn</span>
                )
        },
        {
            title: "Ngày tạo",
            dataIndex: "createdAt",
            render: (v) => new Date(v).toLocaleString("vi-VN"),
        },
        {
            title: "Hành động",
            key: "action",
            render: (_, record) => (
                <div className={styles.actions}>
                    <button
                        className={styles.updateBtn}
                        onClick={() => openEditModal(record)}
                    >
                        Sửa
                    </button>

                    <button
                        className={styles.deleteBtn}
                        onClick={() => handleDelete(record)}
                    >
                        Xóa
                    </button>
                </div>
            ),
        },
    ];

    return (
        <div className={styles.pageContainer}>
            <header className={styles.header}>
                <h1>Quản lý Danh mục</h1>
                <p>Quản lý danh mục và danh mục con.</p>
            </header>

            <div className={styles.actionBar}>
                <input
                    type="text"
                    className={styles.searchInput}
                    placeholder="Tìm theo tên danh mục..."
                    onChange={(e) => setSearchText(e.target.value)}
                />

                <button className={styles.createBtn} onClick={openCreateModal}>
                    + Thêm danh mục
                </button>
            </div>

            <Table
                columns={columns}
                dataSource={filterTree(categories)}
                rowKey="id"
                loading={loading}
                pagination={false}
                bordered
                expandable={{ defaultExpandAllRows: true }}
            />

            {/* Modal thêm/sửa */}
            <CategoryModal
                open={!!modalMode}
                mode={modalMode}
                onClose={() => setModalMode(null)}
                onSubmit={handleSubmit}
                category={selectedCategory}
                categories={categories}
            />
        </div>
    );
};

export default CategoryManagementPage;