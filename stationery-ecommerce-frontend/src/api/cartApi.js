// src/api/cartApi.js
import { API_URLS, apiFetch } from "../config/apiConfig";

const BASE_URL = API_URLS.cart;

// Lấy giỏ hàng
export const getCart = () => apiFetch(BASE_URL);

// Xoá 1 item trong giỏ
export const removeCartItem = (itemId) =>
    apiFetch(`${BASE_URL}/${itemId}`, { method: "DELETE" });

// Cập nhật số lượng item
export const updateCartItem = (itemId, quantity) =>
    apiFetch(`${BASE_URL}/${itemId}`, {
        method: "PUT",
        body: JSON.stringify({ quantity }),
    });

// Thêm sản phẩm vào giỏ
export const addToCart = ({ productId, variantId, quantity }) =>
    apiFetch(BASE_URL, {
        method: "POST",
        body: JSON.stringify({ productId, variantId, quantity }),
    });

// Xoá toàn bộ giỏ hàng
export const resetCart = () =>
    apiFetch(BASE_URL, { method: "DELETE" });
