// src/api/categoryApi.js
import { API_URLS, apiFetch } from "../config/apiConfig";

const BASE_URL = API_URLS.category;

export const getCategories = () => apiFetch(BASE_URL);

export const getCategoryById = (id) => apiFetch(`${BASE_URL}/${id}`);

export const getCategoryBySlug = (slug) => apiFetch(`${BASE_URL}/by-slug/${slug}`);