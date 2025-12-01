import { useNavigate, Outlet } from "react-router-dom";
import { useEffect, useState } from "react";
import { token as tokenUtils } from "../utils/token";
import toast from "react-hot-toast";

const RequireUserAuth = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const t = tokenUtils.get();

        if (!t || tokenUtils.isExpired()) {
            toast.error("Vui lòng đăng nhập để tiếp tục");
            navigate("/login");
        } else {
            setLoading(false);
        }
    }, [navigate]);

    if (loading) return null;

    return <Outlet />;
};

export default RequireUserAuth;