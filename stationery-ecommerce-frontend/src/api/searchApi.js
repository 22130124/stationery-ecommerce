import {API_URLS ,apiFetch} from '../config/apiConfig'

const BASE_URL = API_URLS.search

export const searchProducts = (requestParams) => {
    const queryString = new URLSearchParams(requestParams).toString();
    return apiFetch(`${BASE_URL}?${queryString}`)
}