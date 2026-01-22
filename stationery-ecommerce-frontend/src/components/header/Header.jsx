import styles from './Header.module.scss'
import {FaBars, FaBell, FaShoppingCart, FaUser, FaBoxOpen, FaSearch} from 'react-icons/fa'
import {IoIosArrowForward} from 'react-icons/io'
import {getActiveCategories} from '../../api/categoryApi'
import {Link, useNavigate} from 'react-router-dom'
import {useEffect, useState} from 'react'
import {getProfile} from "../../api/profileApi";

const Header = () => {
    const [hoveredMenu, setHoveredMenu] = useState(null)
    const [categories, setCategories] = useState([])
    const [isLoggedIn, setIsLoggedIn] = useState(false)
    const [searchKeyword, setSearchKeyword] = useState('')
    const [user, setUser] = useState(null)

    const navigate = useNavigate()

    const handleMouseEnter = (menu) => setHoveredMenu(menu)
    const handleMouseLeave = () => setHoveredMenu(null)

    // Lấy danh sách danh mục
    useEffect(() => {
        const fetchCategories = async () => {
            const data = await getActiveCategories()
            setCategories(data)
        }
        fetchCategories()

        const token = localStorage.getItem('token')
        setIsLoggedIn(!!token)
    }, [])

    // Lấy thông tin user
    useEffect(() => {
        const token = localStorage.getItem('token')
        setIsLoggedIn(!!token)

        if (!token) return

        const fetchUser = async () => {
            try {
                const data = await getProfile()
                setUser({
                    email: token?.getEmail?.() || data.email, // tuỳ backend
                    avatarUrl: data.avatarUrl,
                })
            } catch (err) {
                console.error('Fetch user failed', err)
            }
        }

        fetchUser()
    }, [])

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
                                {cat.children?.length > 0 && <IoIosArrowForward className={styles.arrow}/>}
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
        )
    }

    const handleLogout = () => {
        localStorage.removeItem('token')
        setIsLoggedIn(false)
        navigate('/login')
    }

    // Hàm xử lý tìm kiếm sản phẩm từ search bar
    const handleSearch = () => {
        const keyword = searchKeyword.trim()
        if (!keyword) return

        // Chuyển hướng đến trang product-list để xử lý search
        navigate(`/product-list?search=${encodeURIComponent(keyword)}`)

        // Reset lại nội dung search bar
        setSearchKeyword('')
    }

    // Hàm rút ngắn email hiển thị
    const shortenEmail = (email, maxLength = 18) => {
        if (!email) return ''
        if (email.length <= maxLength) return email

        const [name, domain] = email.split('@')
        return `${name.slice(0, 6)}...@${domain}`
    }

    return (
        <header className={styles.header}>
            {/* Logo - dẫn về trang chủ */}
            <Link className={styles.logo} to='/'>Văn Phòng Phẩm</Link>

            {/* Danh mục sản phẩm */}
            <div
                className={styles.categoryMenu}
                onMouseEnter={() => handleMouseEnter('category')}
                onMouseLeave={handleMouseLeave}
            >
                <FaBars/>
                <span>Danh mục sản phẩm</span>

                {hoveredMenu === 'category' && (
                    <div className={styles.megaDropdown}>
                        {renderCategories(categories)}
                    </div>
                )}
            </div>

            {/* Thanh tìm kiếm */}
            <div className={styles.searchBox}>
                <input
                    type='text'
                    placeholder='Tìm kiếm sản phẩm...'
                    value={searchKeyword}
                    onChange={(e) => setSearchKeyword(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
                />
                <button
                    className={styles.searchButton}
                    onClick={handleSearch}
                >
                    <FaSearch/>
                </button>
            </div>


            {/* Thông báo */}
            <div
                className={styles.menuItem}
                onMouseEnter={() => handleMouseEnter('notification')}
                onMouseLeave={handleMouseLeave}
            >
                <FaBell/>
                <span>Thông báo</span>
                {hoveredMenu === 'notification' && (
                    <div className={styles.dropdown}>
                        <FaBoxOpen className={styles.emptyIcon}/>
                        {isLoggedIn ? (
                            <p>Chưa có thông báo mới</p>
                        ) : (
                            <>
                                <p>Vui lòng đăng nhập để xem thông báo</p>
                                <div className={styles.actions}>
                                    <Link to='/login'>Đăng nhập</Link>
                                    <Link to='/signup'>Đăng ký</Link>
                                </div>
                            </>
                        )}
                    </div>
                )}
            </div>

            {/* Giỏ hàng */}
            <Link className={styles.menuItem} to='/shopping-cart'>
                <FaShoppingCart/>
                <span>Giỏ hàng</span>
            </Link>

            {/* Tài khoản */}
            <div
                className={styles.menuItem}
                onMouseEnter={() => handleMouseEnter('user')}
                onMouseLeave={handleMouseLeave}
            >
                {isLoggedIn && user ? (
                    <>
                        <img
                            src={user.avatarUrl || '/default-avatar.png'}
                            alt="avatar"
                            className={styles.headerAvatar}
                        />
                        <span className={styles.userEmail}>{shortenEmail(user.email)}</span>
                    </>
                ) : (
                    <>
                        <FaUser/>
                        <span>Tài khoản</span>
                    </>
                )}

                {hoveredMenu === 'user' && (
                    <div className={styles.userDropdown}>
                        {isLoggedIn ? (
                            <>
                                <div className={styles.userInfo}>
                                    <FaUser className={styles.avatar}/>
                                    <div>
                                        <strong>Tài khoản của bạn</strong>
                                        <span> Xem & quản lý</span>
                                    </div>
                                </div>

                                <ul className={styles.userMenu}>
                                    <li onClick={() => navigate('/profile')}>Hồ sơ cá nhân</li>
                                    <li onClick={() => navigate('/order-history')}>Đơn hàng của tôi</li>
                                    <li className={styles.logout} onClick={handleLogout}>
                                        Đăng xuất
                                    </li>
                                </ul>
                            </>
                        ) : (
                            <div className={styles.guestActions}>
                                <button onClick={() => navigate('/login')}>Đăng nhập</button>
                                <button className={styles.outline} onClick={() => navigate('/signup')}>
                                    Đăng ký
                                </button>
                            </div>
                        )}
                    </div>
                )}
            </div>
        </header>
    )
}

export default Header