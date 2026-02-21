import React, {useEffect, useState, useMemo} from "react";
import {Modal, Select, Input} from "antd";
import styles from "./CategoryModal.module.scss";
import StatusToggle from "../../../components/status-badge/StatusToggle";

const CategoryModal = ({open, onClose, onSubmit, mode, category, categories}) => {
    const initialForm = () => ({
        name: "",
        slug: "",
        parentId: null,
        status: "INACTIVE",
    });

    const [form, setForm] = useState(initialForm());

    // Reset form theo mode
    useEffect(() => {
        if (mode === "edit" && category) {
            setForm({
                name: category.name || "",
                slug: category.slug || "",
                parentId: category.parentId || null,
                status: category.status || "",
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
        console.log(form);
        onSubmit(form);
    };

    // Hàm xử lý khi gõ tên danh mục
    const handleTypingName = (e) => {
        const name = e.target.value;
        setForm((prev) => ({
                ...prev,
                name: name,
                slug: slugify(name),
            })
        )
    }

    // Hàm chuyển đổi từ tên danh mục thành slug tương ứng
    const slugify = (text) => {
        return text
            .toLowerCase()
            .normalize("NFD") // tách dấu khỏi chữ
            .replace(/[\u0300-\u036f]/g, "") // xoá dấu
            .replace(/đ/g, "d") // xử lý riêng chữ đ
            .replace(/[^a-z0-9\s-]/g, "") // bỏ ký tự đặc biệt
            .trim()
            .replace(/\s+/g, "-") // space -> -
            .replace(/-+/g, "-"); // gộp nhiều - thành 1
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
                        onChange={handleTypingName}
                        // onChange={(e) => setForm({...form, name: e.target.value})}
                    />
                </div>

                <div className={styles.formGroup}>
                    <label>Slug</label>
                    <Input
                        value={form.slug}
                        onChange={(e) => setForm({...form, slug: e.target.value})}
                    />
                </div>

                <div className={styles.formGroup}>
                    <label>Danh mục cha</label>
                    <Select
                        allowClear
                        placeholder="Không có"
                        value={form.parentId}
                        onChange={(v) => setForm({...form, parentId: v || null})}
                        options={parentOptions.map((c) => ({
                            value: c.id,
                            label: c.label,
                        }))}
                    />
                </div>

                <div className={styles.formGroup}>
                    <label>Trạng thái</label>
                    <StatusToggle
                        active={form.status === 'ACTIVE'}
                        onToggle={() =>
                            setForm((prev) => ({...prev, status: prev.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'}))
                        }
                    />
                </div>
            </div>
        </Modal>
    );
};

export default CategoryModal;