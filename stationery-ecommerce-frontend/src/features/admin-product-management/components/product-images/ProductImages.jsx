import React, { useState } from "react";
import { Upload, message } from "antd";
import { MoreOutlined } from "@ant-design/icons";
import styles from "./ProductImages.module.scss";

const ProductImages = ({ value = [], onChange }) => {
    const [productImages, setProductImages] = useState(value);
    const [activeMenu, setActiveMenu] = useState(null);

    const handleBeforeUpload = (file) => {
        const isImage = file.type.startsWith("image/");
        if (!isImage) {
            message.error("Chỉ được tải lên file ảnh");
            return Upload.LIST_IGNORE;
        }
        const isLt5M = file.size / 1024 / 1024 < 10;
        if (!isLt5M) {
            message.error("Ảnh phải nhỏ hơn 10MB");
            return Upload.LIST_IGNORE;
        }
        return false;
    };

    const handleUploadChange = ({ fileList }) => {
        const newList = fileList.map((file) => {
            const existing = productImages.find((img) => img.uid === file.uid);

            return {
                uid: file.uid,
                url: file.url || URL.createObjectURL(file.originFileObj),
                isDefault: existing ? existing.isDefault : false,
                originFileObj: file.originFileObj,
            };
        });

        setProductImages(newList);
        onChange?.(newList);
    };


    const handleSetDefault = (uid) => {
        const updated = productImages.map((img) => ({
            ...img,
            isDefault: img.uid === uid,
        }));
        setProductImages(updated);
        onChange?.(updated);
        setActiveMenu(null);
    };

    const handleRemove = (uid) => {
        const updated = productImages.filter((img) => img.uid !== uid);
        setProductImages(updated);
        onChange?.(updated);
        setActiveMenu(null);
    };

    const handlePreview = (img) => {
        window.open(img.url, "_blank");
        setActiveMenu(null);
    };

    console.log("images:", productImages)

    return (
        <div className={styles.container}>
            <Upload
                multiple
                accept="image/*"
                listType="picture-card"
                fileList={productImages}
                onChange={handleUploadChange}
                beforeUpload={handleBeforeUpload}
                showUploadList={false}
            >
                {productImages.length >= 8 ? null : "+ Upload"}
            </Upload>

            <div className={styles.previewGrid}>
                {productImages.map((img) => (
                    <div key={img.uid} className={styles.imageCard}>
                        <img src={img.url} alt="" />
                        {img.isDefault && <span className={styles.defaultBadge}>Mặc định</span>}

                        <div
                            className={styles.menuIcon}
                            onClick={() =>
                                setActiveMenu(activeMenu === img.uid ? null : img.uid)
                            }
                        >
                            <MoreOutlined />
                        </div>

                        {activeMenu === img.uid && (
                            <div className={styles.menuDropdown}>
                                {!img.isDefault && (
                                    <button onClick={() => handleSetDefault(img.uid)}>
                                        Đặt làm mặc định
                                    </button>
                                )}
                                <button onClick={() => handlePreview(img)}>Xem ảnh</button>
                                <button onClick={() => handleRemove(img.uid)}>Xóa</button>
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ProductImages;