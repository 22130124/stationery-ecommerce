import {API_URLS ,apiFetch} from '../config/apiConfig'

const BASE_URL = API_URLS.ai

export const processChatRequest = (request) =>
    apiFetch(`${BASE_URL}`, {
        method: 'POST',
        body: JSON.stringify(request),
    })