// authApi.js
import {API_URLS} from '../config/apiConfig';

const BASE_URL = API_URLS.auth

const postAuthRequest = async (endpoint, payload) => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload),
        });
    } catch (error) {
        console.error(`${endpoint} failed:`, error);
        throw new Error(
            'Không thể kết nối đến máy chủ. Vui lòng thử lại sau'
        );
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || 'Đã xảy ra lỗi');
    }

    return data;
}

export const verifyAccount = async (token) => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/verify?token=${token}`)
    } catch (error) {
        console.error(`Xác minh thất bại:`, error);
        throw new Error(
            'Không thể kết nối tới máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau'
        );
    }

    const data = await response.json();
    if (!response.ok) throw new Error(data.message || 'Đã xảy ra lỗi');

    return data
}

export const signUp = (email, password) =>
    postAuthRequest('signup', { email, password })

export const login = (email, password) =>
    postAuthRequest('login', { email, password })


export const loginWithGoogle = (googleAccessToken) =>
    postAuthRequest('google', { token: googleAccessToken })