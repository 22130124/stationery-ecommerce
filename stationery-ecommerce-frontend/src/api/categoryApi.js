// src/api/categoryApi.js
import {API_URLS} from "../config/apiConfig";

const BASE_URL = API_URLS.category

export const getCategories = async () => {
    let response;

    try {
        response = await fetch(`${BASE_URL}`);
    } catch (error) {
        throw new Error("Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau")
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Đã có lỗi xảy ra")
    }

    return data;
}

export const getCategoryById = async (id) => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/${id}`);
    } catch (error) {
        throw new Error("Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau")
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Đã có lỗi xảy ra")
    }

    return data;
}

export const getCategoryBySlug = async (slug) => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/by-slug/${slug}`);
    } catch (error) {
        throw new Error("Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau")
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Đã có lỗi xảy ra")
    }

    return data;
}