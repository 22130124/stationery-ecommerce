import styles from "./Header.module.scss";
import { FaBars, FaBell, FaShoppingCart, FaUser, FaBoxOpen, FaSearch } from "react-icons/fa";
import { IoIosArrowForward } from "react-icons/io";
import { getActiveCategories } from "../../api/categoryApi";
import { Link, useNavigate } from "react-router-dom";
import {useEffect, useState} from "react";

const Header = () => {
    const [hoveredMenu, setHoveredMenu] = useState(null);
    const [categories, setCategories] = useState([]);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigate = useNavigate();

    const handleMouseEnter = (menu) => setHoveredMenu(menu);
    const handleMouseLeave = () => setHoveredMenu(null);

    useEffect(() => {
        const fetchCategories = async () => {
            const data = await getActiveCategories();
            setCategories(data);
        };
        fetchCategories();

        const token = localStorage.getItem("token");
        setIsLoggedIn(!!token);
    }, []);

    const renderCategories = (categories, level = 0) => {
        return (
            <ul className={`${styles.categoryList} ${level > 0 ? styles.subCategoryList : ''}`}>
                {categories.map((cat) => (
                    <li key={cat.id} className={styles.categoryItem}>
                        <Link
                            to={`/product-list?category=${cat.slug}`}
                            className={styles.categoryLink}
                        >
                            <span className={cat.children?.length > 0 ? styles.hasChildren : ''}>
                                {cat.name}
                                {cat.children?.length > 0 && <IoIosArrowForward className={styles.arrow} />}
                            </span>
                        </Link>

                        {cat.children?.length > 0 && (
                            <div className={styles.submenu}>
                                {renderCategories(cat.children, level + 1)}
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        );
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        setIsLoggedIn(false);
        navigate("/login");
    };

    return (
        <header className={styles.header}>
            {/* Logo - dẫn về trang chủ */}
            <Link className={styles.logo} to="/">Văn Phòng Phẩm</Link>

            {/* Danh mục sản phẩm */}
            <div
                className={styles.categoryMenu}
                onMouseEnter={() => handleMouseEnter("category")}
                onMouseLeave={handleMouseLeave}
            >
                <FaBars />
                <span>Danh mục sản phẩm</span>

                {hoveredMenu === "category" && (
                    <div className={styles.megaDropdown}>
                        {renderCategories(categories)}
                    </div>
                )}
            </div>

            {/* Thanh tìm kiếm */}
            <div className={styles.searchBox}>
                <input type="text" placeholder="Tìm kiếm sản phẩm, thương hiệu..." />
                <button className={styles.searchButton}>
                    <FaSearch />
                </button>
            </div>

            {/* Thông báo */}
            <div
                className={styles.menuItem}
                onMouseEnter={() => handleMouseEnter("notification")}
                onMouseLeave={handleMouseLeave}
            >
                <FaBell />
                <span>Thông báo</span>
                {hoveredMenu === "notification" && (
                    <div className={styles.dropdown}>
                        <FaBoxOpen className={styles.emptyIcon} />
                        {isLoggedIn ? (
                            <p>Chưa có thông báo mới</p>
                        ) : (
                            <>
                                <p>Vui lòng đăng nhập để xem thông báo</p>
                                <div className={styles.actions}>
                                    <Link to="/login">Đăng nhập</Link>
                                    <Link to="/signup">Đăng ký</Link>
                                </div>
                            </>
                        )}
                    </div>
                )}
            </div>

            {/* Giỏ hàng */}
            <Link className={styles.menuItem} to="/shopping-cart">
                <FaShoppingCart />
                <span>Giỏ hàng</span>
                {/* <span className={styles.cartBadge}>3</span> */} {/* Có thể thêm badge khi có số lượng */}
            </Link>

            {/* Tài khoản */}
            <div
                className={styles.menuItem}
                onMouseEnter={() => handleMouseEnter("user")}
                onMouseLeave={handleMouseLeave}
            >
                <FaUser />
                <span>Tài khoản</span>
                {hoveredMenu === "user" && (
                    <div className={styles.dropdown}>
                        {isLoggedIn ? (
                            <>
                                <button onClick={() => navigate("/profile")}>Hồ sơ cá nhân</button>
                                <button onClick={() => navigate("/order-history")}>Lịch sử mua hàng</button>
                                <button onClick={handleLogout}>Đăng xuất</button>
                            </>
                        ) : (
                            <>
                                <button onClick={() => navigate("/login")}>Đăng nhập</button>
                                <button onClick={() => navigate("/signup")}>Đăng ký tài khoản</button>
                            </>
                        )}
                    </div>
                )}
            </div>
        </header>
    );
};

export default Header;