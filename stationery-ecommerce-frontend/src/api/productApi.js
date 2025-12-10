// src/api/productApi.js
import { API_URLS, apiFetch } from '../config/apiConfig';

const BASE_URL = API_URLS.product;

export const getAllProducts = () => apiFetch(`${BASE_URL}/admin`);

export const getProductsByCategory = (requestParams) => {
    const queryString = new URLSearchParams(requestParams).toString();
    return apiFetch(`${BASE_URL}/by-category?${queryString}`);
};

export const getProductBySlug = (slug) => apiFetch(`${BASE_URL}/by-slug/${slug}`);

export const addProduct = (productData) =>
    apiFetch(`${BASE_URL}/admin`, {
        method: 'POST',
        body: JSON.stringify(productData),
    });

export const updateProduct = (productId, productData) =>
    apiFetch(`${BASE_URL}/admin/${productId}`, {
        method: 'PUT',
        body: JSON.stringify(productData),
    });

export const deleteProduct = (productId) =>
    apiFetch(`${BASE_URL}/admin/${productId}`, {
        method: 'DELETE',
    });

export const updateInventory = (request) =>
    apiFetch(`${BASE_URL}/inventory`, {
        method: 'PUT',
        body: JSON.stringify(request),
    });

export const increaseInventory = (request) =>
    apiFetch(`${BASE_URL}/inventory/increase`, {
        method: 'PUT',
        body: JSON.stringify(request),
    });

export const decreaseInventory = (request) =>
    apiFetch(`${BASE_URL}/inventory/decrease`, {
        method: 'PUT',
        body: JSON.stringify(request),
    });