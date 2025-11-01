// src/api/supplierApi.js
import {API_URL} from "../config/apiConfig";

const BASE_URL = `${API_URL}/api/brands`;

export const getBrandsBySupplierId = async (supplierId) => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/${supplierId}`);
    } catch (error) {
        throw new Error("Unable to connect to the server. Please check your network connection or try again later")
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Something went wrong")
    }

    return data;
}