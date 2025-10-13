// authApi.js
import {API_URL} from "../config/apiConfig";

const BASE_URL = `${API_URL}/api/auth`;

export const login = async (username, password) => {
    const request = {
        username,
        password,
    }

    let response

    try {
        response = await fetch(`${BASE_URL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(request)
        });
    } catch (error) {
        console.log('Login failed:', error)
        throw new Error("Unable to connect to the server. Please check your network connection or try again later")
    }

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message || "Something went wrong")
    }

    return data
}