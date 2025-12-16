// ShoppingCart.jsx
import React, {useState, useEffect} from 'react'
import styles from './ShoppingCart.module.scss'
import {FaTrashAlt} from 'react-icons/fa'
import {getCart, removeCartItem, removeItem, updateCartItem} from '../../../api/cartApi'
import {useNavigate, useLocation} from 'react-router-dom'
import {Modal} from 'antd'
import toast from 'react-hot-toast'
import {createOrders} from '../../../api/orderApi'
import {getProfile} from '../../../api/profileApi'

const ShoppingCart = () => {
    const navigate = useNavigate()
    const location = useLocation()

    const [cart, setCart] = useState({})
    const [cartItems, setCartItems] = useState([])
    const [totalPrice, setTotalPrice] = useState(0)
    const {confirm} = Modal

    useEffect(() => {
        const fetchCart = async () => {
            const data = await getCart()
            if (!data) return
            setCart(data.cart)
            setCartItems(data.cart.items)
        }
        fetchCart()
    }, [])
    console.log('CartItems', cartItems)

    // Hàm tính tổng tiền
    useEffect(() => {
        const calculateTotal = () => {
            return cartItems.reduce((total, item) => {
                return total + (item.finalPrice * item.quantity)
            }, 0)
        }
        setTotalPrice(calculateTotal())
    }, [cartItems])

    // Hàm xử lý thay đổi số lượng
    const handleQuantityChange = (itemId, newQuantity) => {
        if (newQuantity < 1) return

        // Update UI ngay lập tức
        setCartItems(prev =>
            prev.map(item =>
                item.id === itemId
                    ? {...item, quantity: newQuantity}
                    : item
            )
        )

        // Gọi API sau 400ms nếu user ngừng thao tác
        debouncedUpdate(itemId, newQuantity)
    }

    const handleUpdateQuantityApi = async (itemId, quantity) => {
        const data = await updateCartItem(itemId, quantity)
        if (!data) return
        setCart(data.cart)
        setCartItems(data.cart.items)
    }


    // debounce hook cho việc cập nhật số lượng
    function useDebounce(callback, delay) {
        const timeoutRef = React.useRef(null)

        function debouncedFunction(...args) {
            clearTimeout(timeoutRef.current)
            timeoutRef.current = setTimeout(() => {
                callback(...args)
            }, delay)
        }

        return debouncedFunction
    }

    const debouncedUpdate = useDebounce(handleUpdateQuantityApi, 400)

    const handleRemoveItem = async (itemId) => {
        const newCartData = await removeCartItem(itemId)
        if (!newCartData) return
        setCart(newCartData.cart)
        setCartItems(newCartData.cart.items)
    }

    const showDeleteConfirm = (cartItem, onOk) => {
        confirm({
            title: 'Xóa sản phẩm',
            content: `Bạn có chắc chắn muốn xóa sản phẩm '${cartItem.productName}' ra khỏi giỏ hàng không?`,
            okText: 'Xác nhận',
            okType: 'danger',
            cancelText: 'Hủy',
            onOk,
        })
    }

    const handleConfirm = async () => {
        if (!localStorage.getItem('token')) {
            navigate(`/login?redirect=${location.pathname}`)
        } else {
            const profileData = await getProfile()
            if(!profileData.completedStatus) {
                navigate('/profile')
                return
            }
            
            const toastId = toast.loading('Đang xử lý tạo đơn hàng...')

            // Chỉ lấy các trường cần thiết
            const orderItems = cartItems.map(item => ({
                productId: item.productId,
                variantId: item.variantId,
                price: item.finalPrice,
                quantity: item.quantity
            }))

            const data = await createOrders({orderItems})
            if (!data) return
            toast.success('Tạo đơn hàng thành công', {id: toastId, duration: 2000})
            navigate('/order-history')
        }
    }

    const formatCurrency = (amount) => {
        if (typeof amount !== 'number') return ''
        return amount.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'})
    }

    const CartItem = ({item}) => {
        return (
            <div className={styles.cartItem}>
                {/*Hiển thị thông tin chung*/}
                <div className={styles.productInfo}>
                    <div className={styles.itemImage}>
                        <img src={item.product.defaultImage.url} alt={item.name}/>
                    </div>
                    <div className={styles.itemDetails}>
                        <h3 className={styles.itemName}>{item.product.name}</h3>
                        <p className={styles.itemVariant}>Phân loại: {item.product.defaultVariant.name}</p>
                    </div>
                </div>

                {/*Hiển thị giá gốc*/}
                <div className={styles.itemPrice}>
                    {item.product.defaultVariant.discountPrice && (
                        <span className={styles.originalPrice}>{formatCurrency(item.product.defaultVariant.basePrice)}</span>
                    )}
                    <span className={styles.finalPrice}>{formatCurrency(item.finalPrice)}</span>
                </div>

                {/*Hiển thị khu vực tăng/giảm số lượng*/}
                <div className={styles.itemQuantity}>
                    <button onClick={() => handleQuantityChange(item.id, item.quantity - 1)}>-</button>

                    <input
                        type='number'
                        value={item.quantity}
                        onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value) || 1)}
                        min='1'
                    />

                    <button onClick={() => handleQuantityChange(item.id, item.quantity + 1)}>+</button>
                </div>

                <div className={styles.itemTotal}>
                    {formatCurrency(item.finalPrice * item.quantity)}
                </div>

                <div className={styles.itemActions}>
                    <button onClick={() => showDeleteConfirm(item, () => handleRemoveItem(item.id))}>
                        <FaTrashAlt/>
                    </button>
                </div>
            </div>
        )
    }

    console.log('CartItems', cartItems)

    return (
        <div className={styles.shoppingCart}>
            <h1 className={styles.cartHeader}>Giỏ hàng của bạn</h1>

            {cartItems.length > 0 ? (
                <div className={styles.cartBody}>
                    <div className={styles.cartItemsList}>
                        <div className={styles.listHeader}>
                            <div className={styles.headerProduct}>Sản phẩm</div>
                            <div className={styles.headerPrice}>Đơn giá</div>
                            <div className={styles.headerQuantity}>Số lượng</div>
                            <div className={styles.headerTotal}>Thành tiền</div>
                            <div className={styles.headerActions}></div>
                        </div>
                        {cartItems.map(item => (
                            <CartItem key={`${item.variantId}`} item={item}/>
                        ))}
                    </div>

                    <div className={styles.cartSummary}>
                        <h2>Tóm tắt đơn hàng</h2>
                        <div className={styles.summaryLine}>
                            <span>Tạm tính</span>
                            <span>{formatCurrency(totalPrice)}</span>
                        </div>
                        <div className={styles.summaryLine}>
                            <span>Phí vận chuyển</span>
                            <span>Miễn phí</span>
                        </div>
                        <hr/>
                        <div className={`${styles.summaryLine} ${styles.total}`}>
                            <span>Tổng cộng</span>
                            <span>{formatCurrency(totalPrice)}</span>
                        </div>
                        <button className={styles.checkoutButton} onClick={handleConfirm}>Xác nhận đặt hàng</button>
                    </div>
                </div>
            ) : (
                <div className={styles.emptyCart}>
                    <p>Giỏ hàng của bạn chưa có sản phẩm nào.</p>
                    <a href='/'>Tiếp tục mua sắm</a>
                </div>
            )}
        </div>
    )
}

export default ShoppingCart