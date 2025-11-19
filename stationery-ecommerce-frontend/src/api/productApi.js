// src/api/productApi.js
import {API_URLS} from '../config/apiConfig';
const BASE_URL = API_URLS.product

export const getAllProducts = async () => {
    let response;
    const token = localStorage.getItem('token');

    try {
        response = await fetch(`${BASE_URL}/admin`, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
        });
    } catch (error) {
        throw new Error('Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau')
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || 'Đã xảy ra lỗi')
    }

    return data;
}

export const getProductsByCategory = async (requestParams) => {
    const queryString = new URLSearchParams(requestParams).toString();
    let response;
    try {
        response = await fetch(`${BASE_URL}/by-category?${queryString}`);
    } catch (error) {
        throw new Error(
            'Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau'
        );
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || 'Đã xảy ra lỗi');
    }

    return data;
}

export const getProductBySlug = async (slug) => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/by-slug/${slug}`);
    } catch (error) {
        throw new Error(
            'Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau'
        );
    }
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || 'Đã xảy ra lỗi');
    }

    return data;
}

export const addProduct = async (productData) => {
    let response;
    const token = localStorage.getItem('token');

    try {
        response = await fetch(`${BASE_URL}/admin`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(productData),
        });
    } catch (error) {
        throw new Error(
            'Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau'
        );
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || 'Thêm sản phẩm mới thất bại')
    }

    return data;
}

export const updateProduct = async (productId, productData) => {
    let response;
    const token = localStorage.getItem('token');

    try {
        response = await fetch(`${BASE_URL}/admin/${productId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(productData),
        });
    } catch (error) {
        throw new Error(
            'Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau'
        );
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || 'Cập nhật sản phẩm thất bại');
    }

    return data;
};

export const deleteProduct = async (productId) => {
    let response;
    const token = localStorage.getItem('token');

    try {
        response = await fetch(`${BASE_URL}/admin/${productId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            },
        });
    } catch (error) {
        throw new Error(
            'Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau'
        );
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || 'Xóa sản phẩm thất bại');
    }

    return data;
};

