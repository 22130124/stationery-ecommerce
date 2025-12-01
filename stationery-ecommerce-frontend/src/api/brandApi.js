// src/api/supplierApi.js
import { API_URLS, apiFetch } from "../config/apiConfig";

const BASE_URL = API_URLS.brand;

// Lấy danh sách brand theo supplierId
export const getBrandsBySupplierId = (supplierId) =>
    apiFetch(`${BASE_URL}/by-supplier/${supplierId}`);

// Lấy thông tin brand theo id
export const getBrandById = (id) =>
    apiFetch(`${BASE_URL}/${id}`);
