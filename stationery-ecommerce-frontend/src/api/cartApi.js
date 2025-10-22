// src/api/cartApi.js
import {API_URL} from "../config/apiConfig";
const BASE_URL = `${API_URL}/api/cart`;

export const getCartItems = async (variantIds) => {
    let response;
    try {
        const query = variantIds.join(',');
        response = await fetch(`${BASE_URL}/get-cart-items?variantIds=${query}`);
    } catch (error) {
        console.error(`Fetch product variants failed:`, error);
        throw new Error("Unable to connect to the server. Please check your network connection or try again later");
    }
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || "Something went wrong");
    }
    return data;
}