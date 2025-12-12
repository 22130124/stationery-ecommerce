import {API_URLS, apiFetch} from "../config/apiConfig";

const BASE_URL = API_URLS.payment

export const pay = (orderId) =>
    apiFetch(`${BASE_URL}/pay?orderId=${orderId}`, {
        method: "POST",
    });