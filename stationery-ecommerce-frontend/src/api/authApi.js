// authApi.js
import {API_URLS} from "../config/apiConfig";

const BASE_URL = `${API_URLS.auth}/api/auth`;

const postAuthRequest = async (endpoint, payload) => {
    let response;

    try {
        response = await fetch(`${BASE_URL}/${endpoint}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload),
        });
    } catch (error) {
        console.error(`${endpoint} failed:`, error);
        throw new Error(
            "Unable to connect to the server. Please check your network connection or try again later"
        );
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Something went wrong");
    }

    return data;
};

export const signUp = (email, password) =>
    postAuthRequest("signup", { email, password });

export const login = (email, password) =>
    postAuthRequest("login", { email, password });


export const loginWithGoogle = (googleAccessToken) =>
    postAuthRequest("google", { token: googleAccessToken });