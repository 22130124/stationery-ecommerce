import { useNavigate, Outlet } from "react-router-dom";
import { useEffect, useState } from "react";
import { token as tokenUtils } from "../utils/token";
import toast from "react-hot-toast";
import {useSelector} from "react-redux";

const RequireAdminAuth = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const isLoggedIn = useSelector(state => state.auth.isLoggedIn);
    const role = useSelector(state => state.auth.role);

    useEffect(() => {
        if (!isLoggedIn || tokenUtils.isExpired()) {
            toast.error("Vui lòng đăng nhập để tiếp tục");
            navigate("/login");
        } else if (role !== "ADMIN") {
            toast.error("Không có quyền truy cập");
            navigate("/");
        } else {
            setLoading(false);
        }
    }, [navigate]);

    if (loading) return null;

    return <Outlet />;
};

export default RequireAdminAuth;