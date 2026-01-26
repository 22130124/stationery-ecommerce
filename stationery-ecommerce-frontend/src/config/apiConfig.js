// apiConfig.js
import toast from "react-hot-toast";
import {ERROR_MESSAGES} from "../error/errorMessages";

export const API_URL = "http://localhost:8080";

export const API_URLS = {
    product: `${API_URL}/products`,
    search: `${API_URL}/search`,
    category: `${API_URL}/categories`,
    supplier: `${API_URL}/suppliers`,
    brand: `${API_URL}/brands`,
    auth: `${API_URL}/auth`,
    upload: `${API_URL}/upload`,
    cart: `${API_URL}/cart`,
    profile: `${API_URL}/profiles`,
    order: `${API_URL}/orders`,
    dashboard: `${API_URL}/dashboard`,
    payment: `${API_URL}/vnpay`,
    ai: `${API_URL}/ai`,
    recommend: `${API_URL}/recommend`,
};

export async function apiFetch(url, options = {}) {
    const token = localStorage.getItem("token");

    let response;

    try {
        response = await fetch(url, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                Authorization: token ? `Bearer ${token}` : "",
                ...options.headers
            },
        });
    } catch (err) {
        toast.error("Không thể kết nối đến máy chủ. Vui lòng thử lại sau.");
    }

    if (response.status === 401) {
        localStorage.removeItem("token");
        toast.dismiss();
        toast.error("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại. Đang chuyển hướng về trang đăng nhập...");

        setTimeout(() => {
            window.location.href = "/login";
        }, 5000);
    }

    const data = await response.json();

    // Xử lý các lỗi khác
    if (!response.ok) {
        const errorCode = data?.code;
        const messageResponse = data?.message;
        const message = ERROR_MESSAGES[errorCode] || messageResponse || "Đã có lỗi xảy ra. Vui lòng thử lại sau";
        toast.error(message);
        throw {code: errorCode, message, raw: data};
    }

    return data;
}