import React, {useId, useState, useRef, useEffect} from "react";
import styles from "./ProductImages.module.scss";
import {deleteImage, uploadImage} from "../../../../api/uploadApi";
import {MoreOutlined} from "@ant-design/icons";

const ProductImages = ({value = [], onChange, allowSetDefault = false}) => {
    const images = value || [];
    const [opened, setOpened] = useState(null);
    const inputId = useId();
    const dropdownRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
                setOpened(null);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

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

        let updatedImages;
        if (images.length === 0 && newImages.length > 0) {
            newImages[0].isDefault = true;
            updatedImages = [...images, ...newImages];
        } else {
            updatedImages = [...images, ...newImages];
        }

        onChange?.(updatedImages);

        for (const img of newImages) {
            try {
                const res = await uploadImage(img.file);
                updatedImages = updatedImages.map(i =>
                    i.fingerprint === img.fingerprint
                        ? {...i, url: res.secure_url, public_id: res.public_id, uploading: false}
                        : i
                );
                onChange?.(updatedImages);
            } catch {
                updatedImages = updatedImages.map(i =>
                    i.fingerprint === img.fingerprint
                        ? {...i, uploading: false, error: true}
                        : i
                );
                onChange?.(updatedImages);
            }
        }
    };
    console.log(images)

    const setDefault = (fp) => {
        const updatedImages = images.map(i => ({...i, isDefault: i.fingerprint === fp}));
        onChange?.(updatedImages);
        setOpened(null);
    };

    const removeImage = async (fp) => {
        const target = images.find(i => i.fingerprint === fp);
        const updatedImages = images.filter(i => i.fingerprint !== fp);
        onChange?.(updatedImages);
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
                onClick={() => document.getElementById(inputId).click()}
                onDrop={handleDrop}
                onDragOver={(e) => e.preventDefault()}
            >
                <p>+ Thêm ảnh hoặc kéo thả vào đây</p>
            </div>
            <input
                type="file"
                id={inputId}
                multiple
                accept="image/*"
                onChange={handleInputChange}
                style={{display: "none"}}
            />

            <div className={styles.previewGrid}>
                {images.map(img => (
                    <div key={img.fingerprint} className={styles.imageCard}>
                        <img src={img.url} alt=""/>
                        {img.isDefault && <span className={styles.defaultBadge}>Mặc định</span>}

                        <div
                            className={styles.menuIcon}
                            onClick={(e) => {
                                e.stopPropagation();
                                setOpened(opened === img.fingerprint ? null : img.fingerprint);
                            }}
                        >
                            <MoreOutlined/>
                        </div>

                        {opened === img.fingerprint && (
                            <div className={styles.menuDropdown}
                                 ref={dropdownRef}
                                 onClick={(e) => e.stopPropagation()}
                            >
                                {allowSetDefault && !img.isDefault &&
                                    <button onClick={() => setDefault(img.fingerprint)}>Đặt làm mặc định</button>}
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
