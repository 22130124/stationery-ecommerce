// src/api/supplierApi.js
import { API_URLS, apiFetch } from "../config/apiConfig";

const BASE_URL = API_URLS.supplier;

export const getSuppliers = () => apiFetch(BASE_URL);

export const getSupplierById = (id) => apiFetch(`${BASE_URL}/${id}`);

// Thêm supplier mới
export const createSupplier = (request) =>
    apiFetch(`${BASE_URL}`, {
        method: 'POST',
        body: JSON.stringify(request),
    })

// Update supplier
export const updateSupplier = (id, request) =>
    apiFetch(`${BASE_URL}/${id}`, {
        method: 'PUT',
        body: JSON.stringify(request),
    })

// Delete supplier
export const deleteSupplier = (id) =>
    apiFetch(`${BASE_URL}/${id}`, {
        method: 'DELETE',
    });
