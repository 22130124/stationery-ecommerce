import React, { useEffect, useState, useMemo } from "react";
import { Modal, Select, Input } from "antd";
import styles from "./CategoryModal.module.scss";
import StatusToggle from "../../../components/status-badge/StatusToggle";

const CategoryModal = ({ open, onClose, onSubmit, mode, category, categories }) => {
    const initialForm = () => ({
        name: "",
        slug: "",
        parentId: null,
        activeStatus: true,
    });

    const [form, setForm] = useState(initialForm());

    // Reset form theo mode
    useEffect(() => {
        if (mode === "edit" && category) {
            setForm({
                name: category.name || "",
                slug: category.slug || "",
                parentId: category.parentId || null,
                activeStatus: category.activeStatus ?? true,
            });
        } else {
            setForm(initialForm());
        }
    }, [mode, category]);

    // Flatten categories
    const flattenCategories = (nodes, depth = 0) => {
        if (!nodes) return [];
        let result = [];
        nodes.forEach((n) => {
            result.push({
                id: n.id,
                label: `${"— ".repeat(depth)}${n.name}`,
            });
            if (n.children?.length) {
                result = result.concat(flattenCategories(n.children, depth + 1));
            }
        });
        return result;
    };

    // Hàm lấy toàn bộ ID descendants
    const getDescendantIds = (node) => {
        let ids = [node.id];

        if (node.children?.length) {
            node.children.forEach(child => {
                ids = ids.concat(getDescendantIds(child));
            });
        }
        return ids;
    };

    // Parent options (loại bỏ chính nó nếu đang edit)
    const parentOptions = useMemo(() => {
        const flat = flattenCategories(categories);
        if (mode !== "edit" || !category) return flat;

        if (mode !== "edit" || !category) return flat;

        // Lấy toàn bộ id của node đang sửa và toàn bộ con cháu
        const blockedIds = getDescendantIds(category);

        return flat.filter((c) => !blockedIds.includes(c.id));

    }, [categories, mode, category]);

    const handleSubmit = () => {
        onSubmit(form);
    };

    return (
        <Modal
            title={mode === "create" ? "Thêm Danh mục" : "Sửa Danh mục"}
            open={open}
            onCancel={onClose}
            onOk={handleSubmit}
            okText="Lưu"
            cancelText="Hủy"
        >
            <div className={styles.form}>

                <div className={styles.formGroup}>
                    <label>Tên danh mục</label>
                    <Input
                        value={form.name}
                        onChange={(e) => setForm({ ...form, name: e.target.value })}
                    />
                </div>

                <div className={styles.formGroup}>
                    <label>Slug</label>
                    <Input
                        value={form.slug}
                        onChange={(e) => setForm({ ...form, slug: e.target.value })}
                    />
                </div>

                <div className={styles.formGroup}>
                    <label>Danh mục cha</label>
                    <Select
                        allowClear
                        placeholder="Không có"
                        value={form.parentId}
                        onChange={(v) => setForm({ ...form, parentId: v || null })}
                        options={parentOptions.map((c) => ({
                            value: c.id,
                            label: c.label,
                        }))}
                    />
                </div>

                <div className={styles.formGroup}>
                    <label>Trạng thái</label>
                    <StatusToggle
                        active={form.activeStatus}
                        onToggle={() =>
                            setForm((prev) => ({ ...prev, activeStatus: !prev.activeStatus }))
                        }
                    />
                </div>
            </div>
        </Modal>
    );
};

export default CategoryModal;