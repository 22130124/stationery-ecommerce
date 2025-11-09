import React, { useState } from "react";
import styles from "./ProductImages.module.scss";
import { deleteImage, uploadImage } from "../../../../api/uploadApi";
import { MoreOutlined } from "@ant-design/icons";

const ProductImages = ({ value = [], onChange }) => {
    const [images, setImages] = useState(value);
    const [opened, setOpened] = useState(null);

    const handleFiles = async (files) => {
        const fileArray = Array.from(files);
        const newImages = [];

        for (const f of fileArray) {
            const fp = `${f.name}_${f.size}_${f.lastModified || 0}`;
            if (!images.some(img => img.fingerprint === fp)) {
                newImages.push({
                    file: f,
                    url: URL.createObjectURL(f),
                    fingerprint: fp,
                    uploading: true,
                });
            }
        }

        setImages(prev => [...prev, ...newImages]);
        onChange?.([...images, ...newImages]);

        for (const img of newImages) {
            try {
                const res = await uploadImage(img.file);
                setImages(prev => prev.map(i =>
                    i.fingerprint === img.fingerprint
                        ? { ...i, url: res.secure_url, public_id: res.public_id, uploading: false }
                        : i
                ));
            } catch {
                setImages(prev => prev.map(i =>
                    i.fingerprint === img.fingerprint
                        ? { ...i, uploading: false, error: true }
                        : i
                ));
            }
        }
    };

    const setDefault = (fp) => {
        const updated = images.map(i => ({ ...i, isDefault: i.fingerprint === fp }));
        setImages(updated);
        onChange?.(updated);
        setOpened(null);
    };

    const removeImage = async (fp) => {
        const target = images.find(i => i.fingerprint === fp);
        const updated = images.filter(i => i.fingerprint !== fp);
        setImages(updated);
        onChange?.(updated);
        setOpened(null);

        if (target?.public_id) {
            try {
                await deleteImage(target.public_id);
            } catch (err) {
                console.error("Xóa ảnh thất bại:", err);
            }
        }
    };

    const preview = (img) => {
        window.open(img.url, "_blank");
        setOpened(null);
    };

    const handleInputChange = (e) => {
        handleFiles(e.target.files);
    };

    const handleDrop = (e) => {
        e.preventDefault();
        handleFiles(e.dataTransfer.files);
    };

    return (
        <div className={styles.container}>
            <div
                className={styles.uploadZone}
                onClick={() => document.getElementById("fileInput").click()}
                onDrop={handleDrop}
                onDragOver={(e) => e.preventDefault()}
            >
                <p>+ Thêm ảnh hoặc kéo thả vào đây</p>
            </div>
            <input
                type="file"
                id="fileInput"
                multiple
                accept="image/*"
                onChange={handleInputChange}
                style={{ display: "none" }}
            />

            <div className={styles.previewGrid}>
                {images.map(img => (
                    <div key={img.fingerprint} className={styles.imageCard}>
                        <img src={img.url} alt="" />
                        {img.isDefault && <span className={styles.defaultBadge}>Mặc định</span>}

                        <div className={styles.menuIcon} onClick={() => setOpened(opened === img.fingerprint ? null : img.fingerprint)}>
                            <MoreOutlined />
                        </div>

                        {opened === img.fingerprint && (
                            <div className={styles.menuDropdown}>
                                {!img.isDefault && <button onClick={() => setDefault(img.fingerprint)}>Đặt làm mặc định</button>}
                                <button onClick={() => preview(img)}>Xem ảnh</button>
                                <button onClick={() => removeImage(img.fingerprint)}>Xóa</button>
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ProductImages;