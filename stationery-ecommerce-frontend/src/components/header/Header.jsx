import React, {useEffect, useState} from "react";
import styles from "./Header.module.scss";
import {FaBars, FaBell, FaShoppingCart, FaUser, FaBoxOpen} from "react-icons/fa";
import {IoIosArrowForward} from "react-icons/io";
import {getCategories} from "../../api/categoryApi";
import {Link} from "react-router-dom";

const Header = () => {
    const [hoveredMenu, setHoveredMenu] = useState(null);
    const [categories, setCategories] = useState([]);

    const handleMouseEnter = (menu) => setHoveredMenu(menu);
    const handleMouseLeave = () => setHoveredMenu(null);

    useEffect(() => {
        const fetchCategories = async () => {
            const data = await getCategories();
            setCategories(data.categories);
        }
        fetchCategories();
    }, [])

    const renderCategories = (categories) => {
        return (
            <ul className={styles.categoryList}>
                {categories.map((cat) => (
                    <li key={cat.id} className={styles.categoryItem}>
            <span>
              {cat.name}
                {cat.children?.length > 0 && <IoIosArrowForward/>}
            </span>
                        {cat.children?.length > 0 && (
                            <div className={styles.submenu}>{renderCategories(cat.children)}</div>
                        )}
                    </li>
                ))}
            </ul>
        );
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
                        <p>Đăng nhập để xem thông báo</p>
                        <div className={styles.actions}>
                            <button>Đăng nhập</button>
                            <button className={styles.registerBtn}>Đăng ký</button>
                        </div>
                    </div>
                )}
            </div>

            {/* Giỏ hàng */}
            <div className={styles.menuItem}>
                <FaShoppingCart/>
                <span>Giỏ hàng</span>
            </div>

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
                        <button>Đăng nhập</button>
                        <button className={styles.registerBtn}>Đăng ký</button>
                    </div>
                )}
            </div>
        </header>
    );
};

export default Header;