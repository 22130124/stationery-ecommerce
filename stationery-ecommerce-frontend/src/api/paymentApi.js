import {API_URLS, apiFetch} from "../config/apiConfig";

const BASE_URL = API_URLS.payment

export const pay = (orderCode) =>
    apiFetch(`${BASE_URL}/pay?orderCode=${orderCode}`, {
        method: "POST",
    });