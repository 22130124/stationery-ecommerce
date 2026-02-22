import React, {useId, useState, useRef, useEffect} from "react";
import styles from "./ProductImages.module.scss";
import {deleteImage, uploadImage} from "../../../../api/uploadApi";
import {MoreOutlined} from "@ant-design/icons";
import toast from "react-hot-toast";

const ProductImages = ({value = [], onChange, onUploadingChange, allowSetDefault = false}) => {
    const [opened, setOpened] = useState(null);
    const inputId = useId();
    const dropdownRef = useRef(null);
    const images = (value || []).map(img => ({
        ...img,
        fingerprint: img.fingerprint || `server_${img.id}`,
    }));

    // Xử lý việc truyền trạng thái upload ảnh cho modal
    useEffect(() => {
        const uploading = images.some(img => img.uploading);
        onUploadingChange?.(uploading);
    }, [images]);

    // Xử lý việc click ra ngoài sẽ đóng menu ảnh
    useEffect(() => {
        const handleClickOutside = (e) => {
            if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
                setOpened(null);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    useEffect(() => {
        setOpened(null);
    }, [value])

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
            newImages[0].defaultStatus = true;
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
                        ? {...i, url: res.secure_url, publicId: res.public_id, uploading: false}
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
    console.log("images", images);

    const setDefault = (fp) => {
        const updatedImages = images.map(i => ({...i, defaultStatus: i.fingerprint === fp}));
        onChange?.(updatedImages);
        setOpened(null);
    };

    const removeImage = async (img) => {
        // Cập nhật mảng ngay lập tức để UI phản hồi
        const updatedImages = images.filter(i => i.fingerprint !== img.fingerprint);
        onChange?.(updatedImages);
        setOpened(null);

        // Xóa ảnh trên server nếu có public_id
        if (img.publicId) {
            try {
                await deleteImage(img.publicId);
            } catch (err) {
                toast.dismiss()
                toast.error("Xóa ảnh thất bại")
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
                        {img.defaultStatus && <span className={styles.defaultBadge}>Mặc định</span>}

                        {/* Overlay khi đang upload */}
                        {img.uploading && (
                            <div className={styles.uploadingOverlay}>
                                Đang tải ảnh...
                            </div>
                        )}

                        <div
                            className={styles.menuIcon}
                            onClick={(e) => {
                                e.stopPropagation();
                                setOpened(opened === img.fingerprint ? null : img.fingerprint);
                            }}
                        >
                            <MoreOutlined/>
                        </div>

                        {opened === img.fingerprint && !img.uploading && (
                            <div className={styles.menuDropdown} ref={dropdownRef} onClick={(e) => e.stopPropagation()}>
                                {allowSetDefault && !img.defaultStatus &&
                                    <button onClick={() => setDefault(img.fingerprint)}>Đặt làm mặc định</button>}
                                <button onClick={() => preview(img)}>Xem ảnh</button>
                                <button onClick={() => removeImage(img)}>Xóa</button>
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ProductImages;
