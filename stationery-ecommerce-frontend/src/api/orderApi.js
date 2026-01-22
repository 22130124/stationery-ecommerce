// src/api/orderApi.js
import { API_URLS, apiFetch } from "../config/apiConfig";

const BASE_URL = API_URLS.order;

export const getOrders = () => apiFetch(BASE_URL);

export const getOrderDetail = (orderCode) => apiFetch(`${BASE_URL}/${orderCode}`);

export const createOrders = (payload) =>
    apiFetch(BASE_URL, {
        method: "POST",
        body: JSON.stringify(payload),
    });

export const getAllOrders = () => apiFetch(`${BASE_URL}/admin`);

export const updateOrderStatus = (orderCode, status) =>
    apiFetch(`${BASE_URL}/admin/${orderCode}`, {
        method: "PUT",
        body: JSON.stringify({ status }),
    });

export const cancelOrder = (orderCode) =>
    apiFetch(`${BASE_URL}/cancel/${orderCode}`, {
        method: "PUT",
    });