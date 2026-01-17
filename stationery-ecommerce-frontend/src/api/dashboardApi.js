import {apiFetch} from "../config/apiConfig";
import {API_URLS} from "../config/apiConfig";

const BASE_URL = API_URLS.dashboard;

export const getRevenue = (range) => {
    return apiFetch(`${BASE_URL}/revenue?range=${range}`)
}

export const getTopProducts = (range, limit) => {
    return apiFetch(`${BASE_URL}/top-products?limit=${limit}&range=${range}`)
}

export const getTodayRevenue = (range) => {
    return apiFetch(`${BASE_URL}/today/revenue`)
}

export const getTodayOrders = (range) => {
    return apiFetch(`${BASE_URL}/today/orders`)
}