// src/api/supplierApi.js
import { API_URLS, apiFetch } from "../config/apiConfig";

const BASE_URL = API_URLS.supplier;

export const getSuppliers = () => apiFetch(BASE_URL);

export const getSupplierById = (id) => apiFetch(`${BASE_URL}/${id}`);
