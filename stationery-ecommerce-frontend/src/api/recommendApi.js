import {API_URLS ,apiFetch} from '../config/apiConfig'

const BASE_URL = API_URLS.recommend

export const getRecommendedProducts = (productId) =>
    apiFetch(`${BASE_URL}`, {
        method: 'POST',
        body: JSON.stringify({
            "productId": productId,
        }),
    })