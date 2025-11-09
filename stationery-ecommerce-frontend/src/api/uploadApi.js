// src/api/uploadApi.js
import {API_URL} from "../config/apiConfig";

const BASE_URL = `${API_URL}/api/upload`;

export const uploadImage = async (file) => {
    const formData = new FormData();
    formData.append('file', file);

    const res = await fetch(`${BASE_URL}/image`, {
        method: 'POST',
        body: formData,
    });

    if (!res.ok) {
        throw new Error('Upload ảnh thất bại');
    }

    return await res.json();
};

export const deleteImage = async (publicId) => {
    const res = await fetch(`${BASE_URL}/image?publicId=${encodeURIComponent(publicId)}`, {
        method: 'DELETE',
    });

    if (!res.ok) {
        throw new Error('Xóa ảnh thất bại');
    }

    return await res.json();
};