import React, {useEffect, useState} from "react";
import {Table, Modal, Input} from "antd";
import toast from "react-hot-toast";
import styles from "./SupplierManagementPage.module.scss";

import {getSuppliers, createSupplier, updateSupplier, deleteSupplier} from "../../../api/supplierApi";

import BrandManagementModal from "../components/BrandManagementModal";
import StatusToggle from "../../../components/status-badge/StatusToggle";

const SupplierManagementPage = () => {
    const [suppliers, setSuppliers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [searchText, setSearchText] = useState("");

    const [selectedSupplier, setSelectedSupplier] = useState(null);
    const [brandModalOpen, setBrandModalOpen] = useState(false);

    const [supplierModalOpen, setSupplierModalOpen] = useState(false);
    const [editingSupplier, setEditingSupplier] = useState(null);
    const [supplierName, setSupplierName] = useState("");
    const [supplierActive, setSupplierActive] = useState(true);

    const {confirm} = Modal;

    // Fetch data
    useEffect(() => {
        loadSuppliers();
    }, []);

    const loadSuppliers = async () => {
        setLoading(true);
        const data = await getSuppliers();
        setSuppliers(data || []);
        setLoading(false);
    };

    const openBrandModal = (supplier) => {
        setSelectedSupplier(supplier);
        setBrandModalOpen(true);
    };

    // Thêm/Sửa supplier
    const openSupplierModal = (supplier = null) => {
        setEditingSupplier(supplier);
        setSupplierName(supplier?.name || "");
        setSupplierActive(supplier?.activeStatus ?? true);
        setSupplierModalOpen(true);
    };

    // Hàm xử lý lưu thay đổi thông tin supplier
    const handleSaveSupplier = async () => {
        const payload = {name: supplierName, activeStatus: supplierActive};

        if (editingSupplier) {
            await updateSupplier(editingSupplier.id, payload);
            toast.dismiss()
            toast.success("Cập nhật nhà cung cấp thành công");
        } else {
            await createSupplier(payload);
            toast.dismiss()
            toast.success("Thêm nhà cung cấp thành công");
        }

        setSupplierModalOpen(false);
        loadSuppliers();
    };

    const showDeleteConfirm = (id) => {
        confirm({
            title: 'Xóa nhà cung cấp',
            content: `Bạn có chắc muốn xóa nhà cung cấp này?`,
            okText: 'Xác nhận',
            okType: 'danger',
            cancelText: 'Hủy',
            onOk: () => handleDeleteSupplier(id),
        });
    };

    // Hàm xử lý xóa nhà cung cấp
    const handleDeleteSupplier = async (id) => {
        await deleteSupplier(id);
        toast.dismiss()
        toast.success("Xóa thành công");
        loadSuppliers();
    };

    // Filter
    const filteredSuppliers = suppliers.filter((s) =>
        s.name.toLowerCase().includes(searchText.toLowerCase()) ||
        s.id.toString().includes(searchText)
    );

    // Định nghĩa các cột
    const columns = [
        {
            title: "ID",
            dataIndex: "id",
            sorter: (a, b) => a.id - b.id
        },
        {
            title: "Tên nhà cung cấp",
            dataIndex: "name"
        },
        {
            title: "Trạng thái",
            dataIndex: "activeStatus",
            render: (active) => (
                <span className={active ? styles.active : styles.inactive}>
                    {active ? "Hoạt động" : "Ngừng hoạt động"}
                </span>
            )
        },
        {
            title: "Ngày tạo",
            dataIndex: "createdAt",
            sorter: (a, b) => new Date(a.createdAt) - new Date(b.createdAt),
            render: (v) => new Date(v).toLocaleString("vi-VN")
        },
        {
            title: "Hành động",
            render: (_, record) => (
                <div className={styles.actions}>
                    <button
                        className={styles.detailBtn}
                        onClick={() => openBrandModal(record)}
                    >
                        Xem thương hiệu
                    </button>

                    <button
                        className={styles.updateBtn}
                        onClick={() => openSupplierModal(record)}
                    >
                        Sửa
                    </button>

                    <button
                        className={styles.deleteBtn}
                        onClick={() => showDeleteConfirm(record.id)}
                    >
                        Xóa
                    </button>
                </div>
            )
        }
    ];

    return (
        <div className={styles.pageContainer}>
            <header className={styles.header}>
                <h1>Quản lý Nhà cung cấp</h1>
                <p>Quản lý danh sách nhà cung cấp và thương hiệu liên kết.</p>
            </header>

            <div className={styles.actionBar}>
                <input
                    type="text"
                    className={styles.searchInput}
                    placeholder="Tìm theo tên hoặc ID..."
                    onChange={(e) => setSearchText(e.target.value)}
                />

                <button
                    className={styles.detailBtn}
                    onClick={() => openSupplierModal()}
                >
                    Thêm nhà cung cấp
                </button>
            </div>

            <Table
                columns={columns}
                dataSource={filteredSuppliers}
                rowKey="id"
                loading={loading}
                pagination={{
                    pageSize: 10,
                    showSizeChanger: true,
                    pageSizeOptions: ["10", "20", "50"]
                }}
                bordered
            />

            {/* Modal Thêm / Sửa Supplier */}
            <Modal
                open={supplierModalOpen}
                title={editingSupplier ? "Sửa nhà cung cấp" : "Thêm nhà cung cấp"}
                onCancel={() => setSupplierModalOpen(false)}
                onOk={handleSaveSupplier}
            >
                <Input
                    placeholder="Tên nhà cung cấp"
                    value={supplierName}
                    onChange={(e) => setSupplierName(e.target.value)}
                    style={{marginBottom: 12}}
                />

                <div style={{display: "flex", alignItems: "center", gap: 10}}>
                    <span>Hoạt động:</span>

                    <StatusToggle
                        active={supplierActive}
                        onToggle={() => setSupplierActive((prev) => !prev)}
                    />
                </div>
            </Modal>

            {/* Modal Brand */}
            <BrandManagementModal
                open={brandModalOpen}
                onClose={() => setBrandModalOpen(false)}
                supplier={selectedSupplier}
            />
        </div>
    );
};

export default SupplierManagementPage;