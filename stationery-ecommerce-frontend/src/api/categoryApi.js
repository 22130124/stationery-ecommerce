// src/api/categoryApi.js
import {API_URLS} from "../config/apiConfig";

const BASE_URL = API_URLS.category

export const getCategories = async () => {
    let response;

    try {
        response = await fetch(`${BASE_URL}`);
    } catch (error) {
        throw new Error("Unable to connect to the server. Please check your network connection or try again later")
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Something went wrong")
    }

    return data;
}

export const getCategoryById = async (id) => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/${id}`);
    } catch (error) {
        throw new Error("Unable to connect to the server. Please check your network connection or try again later")
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Something went wrong")
    }

    return data;
}

export const getCategoryBySlug = async (slug) => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/by-slug/${slug}`);
    } catch (error) {
        throw new Error("Unable to connect to the server. Please check your network connection or try again later")
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Something went wrong")
    }

    return data;
}