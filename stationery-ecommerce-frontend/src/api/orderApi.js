import {API_URLS} from "../config/apiConfig";

const BASE_URL = API_URLS.order

export const getOrders = async () => {
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

export const createOrders = async (payload) => {
    const token = localStorage.getItem('token');
    const response = await fetch(BASE_URL, {
        method: 'POST',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload),
    });

    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || 'Đã có lỗi xảy ra');
    }
    return data;
}

export const getAllOrders = async () => {
    const token = localStorage.getItem('token');
    const response = await fetch(`${BASE_URL}/admin`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || 'Đã có lỗi xảy ra');
    }
    return data;
}

export const updateOrderStatus = async (id, status) => {
    const token = localStorage.getItem('token');
    const response = await fetch(`${BASE_URL}/admin/${id}`, {
        method: 'PUT',
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({status}),
    });

    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || 'Đã có lỗi xảy ra');
    }
    return data;
}

export const cancelOrder = async (id) => {
    const token = localStorage.getItem('token');
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: 'PUT',
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });

    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || 'Đã có lỗi xảy ra');
    }
    return data;
}