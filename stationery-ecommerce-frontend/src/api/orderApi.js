// src/api/orderApi.js
import { API_URLS, apiFetch } from "../config/apiConfig";

const BASE_URL = API_URLS.order;

export const getOrders = () => apiFetch(BASE_URL);

export const getOrderDetail = (id) => apiFetch(`${BASE_URL}/${id}`);

export const createOrders = (payload) =>
    apiFetch(BASE_URL, {
        method: "POST",
        body: JSON.stringify(payload),
    });

export const getAllOrders = () => apiFetch(`${BASE_URL}/admin`);

export const updateOrderStatus = (id, status) =>
    apiFetch(`${BASE_URL}/admin/${id}`, {
        method: "PUT",
        body: JSON.stringify({ status }),
    });

export const cancelOrder = (id) =>
    apiFetch(`${BASE_URL}/cancel/${id}`, {
        method: "PUT",
    });