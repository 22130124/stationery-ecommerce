// src/utils/token.js
import { jwtDecode } from "jwt-decode";

const TOKEN_KEY = "token";

export const token = {
    save(token) {
        localStorage.setItem(TOKEN_KEY, token);
    },

    get() {
        return localStorage.getItem(TOKEN_KEY);
    },

    clear() {
        localStorage.removeItem(TOKEN_KEY);
    },

    decode() {
        const t = this.get();
        if (!t) return null;
        try {
            return jwtDecode(t);
        } catch {
            return null;
        }
    },

    getEmail() {
        return this.decode()?.sub || null;
    },

    getAccountId() {
        return this.decode()?.account_id || null;
    },

    getRole() {
        return this.decode()?.role || null;
    },

    isExpired() {
        const decoded = this.decode();
        if (!decoded) return true;

        return decoded.exp < Date.now() / 1000;
    }
};
