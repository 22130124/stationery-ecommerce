// src/api/supplierApi.js
import {API_URLS, apiFetch} from "../config/apiConfig";

const BASE_URL = API_URLS.brand;

// Lấy danh sách brand theo supplierId
export const getBrandsBySupplierId = (supplierId) =>
    apiFetch(`${BASE_URL}/by-supplier/${supplierId}`);

// Lấy thông tin brand theo id
export const getBrandById = (id) =>
    apiFetch(`${BASE_URL}/${id}`);

// Thêm brand mới
export const createBrand = (request) =>
    apiFetch(`${BASE_URL}`, {
        method: 'POST',
        body: JSON.stringify(request),
    })

// Update brand
export const updateBrand = (id, request) =>
    apiFetch(`${BASE_URL}/${id}`, {
        method: 'PUT',
        body: JSON.stringify(request),
    })

// Delete brand
export const deleteBrand = (id) =>
    apiFetch(`${BASE_URL}/${id}`, {
        method: 'DELETE',
    });