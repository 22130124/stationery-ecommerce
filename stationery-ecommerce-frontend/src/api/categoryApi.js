// src/api/categoryApi.js
import { API_URLS, apiFetch } from "../config/apiConfig";

const BASE_URL = API_URLS.category;

export const getActiveCategories = () => apiFetch(BASE_URL);

export const getCategoryById = (id) => apiFetch(`${BASE_URL}/${id}`);

export const getCategoryBySlug = (slug) => apiFetch(`${BASE_URL}/by-slug/${slug}`);

export const getAllCategories = () => apiFetch(`${BASE_URL}/admin`);

export const createCategory = (request) => apiFetch(`${BASE_URL}/admin`, {
    method: 'POST',
    body: JSON.stringify(request),
});

export const updateCategory = (id, request) => apiFetch(`${BASE_URL}/admin/${id}`, {
    method: 'PUT',
    body: JSON.stringify(request),
});


export const deleteCategory = (id) => apiFetch(`${BASE_URL}/admin/${id}`, {
    method: 'DELETE',
});