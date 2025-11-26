import {API_URLS} from "../config/apiConfig";

const BASE_URL = API_URLS.order

export const getOrders = async () => {
    const token = localStorage.getItem('token')
    let response;
    try {
        response = await fetch(BASE_URL,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            })
    } catch (error) {
        throw new Error("Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối hoặc thử lại sau");
    }
    const data = await response.json();
    if (!response.ok) {
        throw new Error(data.message || "Đã có lỗi xảy ray");
    }
    return data;
}