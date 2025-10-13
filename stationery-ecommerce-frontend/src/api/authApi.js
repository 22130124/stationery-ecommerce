// authApi.js
import {API_URL} from "../config/apiConfig";

const BASE_URL = `${API_URL}/api/auth`;

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

export const login = (username, password) =>
    postAuthRequest("login", { username, password });

export const signUp = (username, password) =>
    postAuthRequest("sign-up", { username, password });

export const loginWithGoogle = (googleAccessToken) =>
    postAuthRequest("google-login", { token: googleAccessToken });