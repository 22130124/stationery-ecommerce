import React, {useEffect, useState} from "react";
import styles from "./Header.module.scss";
import {FaBars, FaBell, FaShoppingCart, FaUser, FaBoxOpen} from "react-icons/fa";
import {IoIosArrowForward} from "react-icons/io";
import {getCategories} from "../../api/categoryApi";
import {Link, useNavigate} from "react-router-dom";

const Header = () => {
    const [hoveredMenu, setHoveredMenu] = useState(null);
    const [categories, setCategories] = useState([]);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigate = useNavigate();

    const handleMouseEnter = (menu) => setHoveredMenu(menu);
    const handleMouseLeave = () => setHoveredMenu(null);

    useEffect(() => {
        const fetchCategories = async () => {
            const data = await getCategories();
            setCategories(data);
        }
        fetchCategories();

        // Check trạng thái đăng nhập
        const token = localStorage.getItem("token");
        setIsLoggedIn(!!token);
    }, [])

    const renderCategories = (categories) => {
        return (
            <ul className={styles.categoryList}>
                {categories.map((cat) => (
                    <li key={cat.id} className={styles.categoryItem}>
                        <Link
                            to={`/product-list?category=${cat.slug}`}
                            className={styles.categoryLink}
                        >
                        <span>
                            {cat.name}
                            {cat.children?.length > 0 && <IoIosArrowForward />}
                        </span>
                        </Link>

                        {cat.children?.length > 0 && (
                            <div className={styles.submenu}>
                                {renderCategories(cat.children)}
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        );
    };

    const handleLogout = () => {
        localStorage.removeItem("token"); // xóa token
        setIsLoggedIn(false);
        navigate("/login");
    };

    return (
        <header className={styles.header}>
            {/* Logo */}
            <Link className={styles.logo} to={`/product-list`}>Văn Phòng Phẩm</Link>

            {/* Danh mục */}
            <div
                className={styles.menuItem}
                onMouseEnter={() => handleMouseEnter("category")}
                onMouseLeave={handleMouseLeave}
            >
                <FaBars/>
                <span>Danh mục</span>

                {hoveredMenu === "category" && (
                    <div
                        className={styles.dropdown}
                        onMouseEnter={() => handleMouseEnter("category")}
                        onMouseLeave={handleMouseLeave}
                    >
                        {renderCategories(categories)}
                    </div>
                )}
            </div>

            {/* Ô tìm kiếm */}
            <div className={styles.searchBox}>
                <input type="text" placeholder="Tìm kiếm sản phẩm..."/>
            </div>

            {/* Thông báo */}
            <div
                className={styles.menuItem}
                onMouseEnter={() => handleMouseEnter("notification")}
                onMouseLeave={handleMouseLeave}
            >
                <FaBell/>
                <span>Thông báo</span>
                {hoveredMenu === "notification" && (
                    <div className={styles.dropdownEmpty}>
                        <FaBoxOpen className={styles.emptyIcon}/>
                        {isLoggedIn ? (<p>Chưa có thông báo nào</p>) :
                            (
                                <>
                                    <p>Đăng nhập để xem thông báo</p>
                                    <div className={styles.actions}>
                                        <Link to="/login" className={styles.actionLink}>
                                            <button>Đăng nhập</button>
                                        </Link>
                                        <Link to="/sign-up" className={styles.actionLink}>
                                            <button className={styles.registerBtn}>Đăng ký</button>
                                        </Link>
                                    </div>
                                </>
                            )
                        }
                    </div>
                )}
            </div>

            {/* Giỏ hàng */}
            <Link className={styles.menuItem} to={"/shopping-cart"}>
                <FaShoppingCart/>
                <span>Giỏ hàng</span>
            </Link>

            {/* User */}
            <div
                className={styles.menuItem}
                onMouseEnter={() => handleMouseEnter("user")}
                onMouseLeave={handleMouseLeave}
            >
                <FaUser/>
                <span>Tài khoản</span>
                {hoveredMenu === "user" && (
                    <div className={styles.dropdownUser}>
                        {isLoggedIn ? (<button onClick={handleLogout}>Đăng xuất</button>) :
                            (
                                <>
                                    <button onClick={() => navigate("/login")}>Đăng nhập</button>
                                    <button onClick={() => navigate("/sign-up")}>Đăng ký</button>
                                </>
                            )
                        }
                    </div>
                )}
            </div>
        </header>
    );
};

export default Header;