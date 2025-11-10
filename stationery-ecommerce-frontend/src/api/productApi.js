// src/api/productApi.js
import {API_URL} from "../config/apiConfig";
const BASE_URL = `${API_URL}/api/products`;

export const getAllProducts = async () => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/admin`);
    } catch (error) {
        throw new Error("Unable to connect to the server. Please check your network connection or try again later")
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Something went wrong")
    }

    return data;
}

export const getProductsByCategoryAndPagination = async (requestParams) => {
    const queryString = new URLSearchParams(requestParams).toString();
    let response;
    try {
        response = await fetch(`${BASE_URL}?${queryString}`);
    } catch (error) {
        console.error(`Get products failed:`, error);
        throw new Error(
            "Unable to connect to the server. Please check your network connection or try again later"
        );
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Something went wrong");
    }

    return data;
}

export const getProductBySlug = async (slug) => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/${slug}`);
    } catch (error) {
        console.error(`Fetch product failed:`, error);
        throw new Error(
            "Unable to connect to the server. Please check your network connection or try again later"
        );
    }
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || "Something went wrong");
    }

    return data;
}

export const addProduct = async (productData) => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/admin`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(productData),
        });
    } catch (error) {
        console.error(`Add product failed:`, error);
        throw new Error(
            "Unable to connect to the server. Please check your network connection or try again later"
        );
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Failed to add product");
    }

    return data;
}