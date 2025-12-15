// src/api/profileApi.js
import { API_URLS, apiFetch } from "../config/apiConfig";

const BASE_URL = API_URLS.profile;

export const getProfile = () => apiFetch(`${BASE_URL}/current`);

export const updateAvatar = (avatarUrl) => apiFetch(`${BASE_URL}/avatar`, {
    method: 'PUT',
    body: JSON.stringify({ avatarUrl }),
})

export const updateProfile = (payload) => apiFetch(`${BASE_URL}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
})