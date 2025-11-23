// src/api/cartApi.js
import {API_URLS} from "../config/apiConfig";
const BASE_URL = API_URLS.cart

export const getCart = async () => {
    const token = localStorage.getItem('token')
    let response;
    try {
        response = await fetch(BASE_URL,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            })
    } catch (error) {
        throw new Error("Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau");
    }
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || "Đã có lỗi xảy ray");
    }
    return data;
}

export const removeCartItem = async (itemId) => {
    const token = localStorage.getItem('token')
    let response;
    try {
        response = await fetch(`${BASE_URL}/${itemId}`,
            {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            })
    } catch (error) {
        throw new Error("Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau");
    }
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || "Đã có lỗi xảy ray");
    }
    return data;
}

export const updateCartItem = async (itemId, quantity) => {
    const token = localStorage.getItem('token');

    let response;
    try {
        response = await fetch(`${BASE_URL}/${itemId}`, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ quantity }),
        });
    } catch (error) {
        throw new Error("Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau");
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Đã có lỗi xảy ra");
    }

    return data;
};

export const addToCart = async ({productId, variantId, quantity}) => {
    const token = localStorage.getItem("token");

    const response = await fetch(`${BASE_URL}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
            productId,
            variantId,
            quantity
        })
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Không thể thêm vào giỏ hàng");
    }

    return data;
};