// src/api/uploadApi.js
import {API_URLS} from "../config/apiConfig"

const BASE_URL = `${API_URLS.upload}`

const getAuthHeader = () => {
    const token = localStorage.getItem('token');
    return token ? { Authorization: `Bearer ${token}` } : {};
};

export const uploadImage = async (file) => {
    const formData = new FormData();
    formData.append('file', file);

    const res = await fetch(`${BASE_URL}/image`, {
        method: 'POST',
        body: formData,
        headers: getAuthHeader(),
    });

    if (!res.ok) {
        throw new Error('Upload ảnh thất bại');
    }

    return await res.json();
};

export const deleteImage = async (publicId) => {
    const res = await fetch(`${BASE_URL}/image?publicId=${encodeURIComponent(publicId)}`, {
        method: 'DELETE',
        headers: getAuthHeader(),
    });

    if (!res.ok) {
        throw new Error('Xóa ảnh thất bại');
    }

    return await res.json();
};