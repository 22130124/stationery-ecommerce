// apiConfig.js
import toast from "react-hot-toast";

export const API_URL = "http://localhost:8080";

export const API_URLS = {
    product: `${API_URL}/products`,
    category: `${API_URL}/categories`,
    supplier: `${API_URL}/suppliers`,
    brand: `${API_URL}/brands`,
    auth: `${API_URL}/auth`,
    upload: `${API_URL}/upload`,
    cart: `${API_URL}/cart`,
    profile: `${API_URL}/profiles`,
    order: `${API_URL}/orders`,
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
        return null;
    }

    if (response.status === 401) {
        localStorage.removeItem("token");
        toast.dismiss();
        toast.error("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại");

        setTimeout(() => {
            window.location.href = "/login";
        }, 5000);

        return null;
    }

    const data = await response.json();

    if (!response.ok) {
        toast.error(data.message || "Đã có lỗi xảy ra");
        return null;
    }

    return data;
}