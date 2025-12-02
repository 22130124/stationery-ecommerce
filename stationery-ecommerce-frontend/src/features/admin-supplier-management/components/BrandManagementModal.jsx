import React, {useState, useEffect} from "react";
import {Modal, Input} from "antd";
import styles from "./BrandManagementModal.module.scss";
import {createBrand, updateBrand, deleteBrand, getBrandsBySupplierId} from "../../../api/brandApi";
import toast from "react-hot-toast";
import StatusToggle from "../../../components/status-badge/StatusToggle";

const BrandManagementModal = ({open, onClose, supplier}) => {
    const [brands, setBrands] = useState([]);
    const [brandName, setBrandName] = useState("");
    const [brandActive, setBrandActive] = useState(true);
    const [editingBrand, setEditingBrand] = useState(null);
    const [loading, setLoading] = useState(false);

    const {confirm} = Modal;

    useEffect(() => {
        if (supplier && open) {
            loadBrands();
        }
    }, [supplier, open]);

    const loadBrands = async () => {
        setLoading(true);
        const data = await getBrandsBySupplierId(supplier.id);
        setBrands(data || []);
        setLoading(false);
    };

    const handleSaveBrand = async () => {
        if (editingBrand) {
            await updateBrand(editingBrand.id, {name: brandName, activeStatus: brandActive});
            toast.success("Cập nhật thương hiệu thành công!");
        } else {
            await createBrand({
                supplierId: supplier.id,
                name: brandName,
                activeStatus: brandActive
            });
            toast.success("Thêm thương hiệu thành công!");
        }
        setBrandName("");
        setBrandActive(true);
        setEditingBrand(null);
        loadBrands();
    };

    const showDeleteConfirm = (id) => {
        confirm({
            title: 'Xóa thương hiệu',
            content: `Bạn có chắc muốn xóa thương hiệu này?`,
            okText: 'Xác nhận',
            okType: 'danger',
            cancelText: 'Hủy',
            onOk: () => handleDeleteBrand(id),
        });
    };

    const handleDeleteBrand = async (brandId) => {
        await deleteBrand(brandId);
        toast.dismiss()
        toast.success("Xóa thành công");
        loadBrands();
    };

    return (
        <Modal
            open={open}
            title={`Thương hiệu của ${supplier?.name}`}
            onCancel={onClose}
            footer={null}
        >
            <div className={styles.brandActions}>
                <Input
                    placeholder="Tên thương hiệu"
                    value={brandName}
                    onChange={e => setBrandName(e.target.value)}
                />

                <div className={styles.switchRow}>
                    <span>Hoạt động:</span>
                    <StatusToggle
                        active={brandActive}
                        onToggle={() => setBrandActive(prev => !prev)}
                    />
                </div>

                <button className={styles.saveBtn} onClick={handleSaveBrand}>
                    {editingBrand ? "Cập nhật" : "Thêm"}
                </button>
            </div>

            <ul className={styles.brandList}>
                {brands.map((b) => (
                    <li key={b.id}>
                        <div>
                            <strong>{b.name}</strong>
                            <span className={b.activeStatus ? styles.active : styles.inactive}>
                                {b.activeStatus ? " (Hoạt động)" : " (Ngừng)"}
                            </span>
                        </div>

                        <div className={styles.actions}>
                            <button
                                className={styles.updateBtn}
                                onClick={() => {
                                    setEditingBrand(b);
                                    setBrandName(b.name);
                                    setBrandActive(b.activeStatus);
                                }}
                            >
                                Sửa
                            </button>

                            <button
                                className={styles.deleteBtn}
                                onClick={() => showDeleteConfirm(b.id)}
                            >
                                Xóa
                            </button>
                        </div>
                    </li>
                ))}
            </ul>
        </Modal>
    );
};

export default BrandManagementModal;