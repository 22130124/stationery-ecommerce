// src/api/profileApi.js
import { API_URLS, apiFetch } from "../config/apiConfig";

const BASE_URL = API_URLS.profile;

export const getProfile = () => apiFetch(`${BASE_URL}/by-account`);