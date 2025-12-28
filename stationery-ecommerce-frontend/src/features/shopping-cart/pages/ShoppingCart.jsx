import React, { useState, useEffect, useMemo } from 'react';
import styles from './ShoppingCart.module.scss';
import { FaTrashAlt, FaRegTrashAlt } from 'react-icons/fa';
import { getCart, removeCartItem, updateCartItem } from '../../../api/cartApi';
import { useNavigate, useLocation } from 'react-router-dom';
import { Modal, Checkbox, Spin } from 'antd';
import toast from 'react-hot-toast';
import { createOrders } from '../../../api/orderApi';
import { getProfile } from '../../../api/profileApi';

const { confirm } = Modal;

const ShoppingCart = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const [cart, setCart] = useState({});
    const [cartItems, setCartItems] = useState([]);
    const [selectedItems, setSelectedItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [totalPrice, setTotalPrice] = useState(0);

    useEffect(() => {
        const fetchCart = async () => {
            setLoading(true);
            const data = await getCart();
            if (data) {
                setCart(data.cart);
                setCartItems(data.cart.items || []);
                setSelectedItems(data.cart.items?.map(item => item.id) || []);
            }
            setLoading(false);
        };
        fetchCart();
    }, []);

    // Tính tổng tiền chỉ của các sản phẩm được chọn
    const selectedTotal = useMemo(() => {
        return cartItems
            .filter(item => selectedItems.includes(item.id))
            .reduce((sum, item) => sum + item.finalPrice * item.quantity, 0);
    }, [cartItems, selectedItems]);

    // Debounce update quantity
    const useDebounce = (callback, delay) => {
        const timeoutRef = React.useRef(null);
        return (...args) => {
            clearTimeout(timeoutRef.current);
            timeoutRef.current = setTimeout(() => callback(...args), delay);
        };
    };

    const handleUpdateQuantityApi = async (itemId, quantity) => {
        if (quantity < 1) return;
        const data = await updateCartItem(itemId, quantity);
        if (data) {
            setCart(data.cart);
            setCartItems(data.cart.items);
        }
    };

    const debouncedUpdate = useDebounce(handleUpdateQuantityApi, 400);

    const handleQuantityChange = (itemId, newQuantity) => {
        if (newQuantity < 1) return;
        setCartItems(prev =>
            prev.map(item => (item.id === itemId ? { ...item, quantity: newQuantity } : item))
        );
        debouncedUpdate(itemId, newQuantity);
    };

    const handleRemoveItem = async (itemId) => {
        const newCartData = await removeCartItem(itemId);
        if (newCartData) {
            setCart(newCartData.cart);
            setCartItems(newCartData.cart.items);
            setSelectedItems(prev => prev.filter(id => id !== itemId));
        }
    };

    const handleRemoveSelected = () => {
        if (selectedItems.length === 0) return;

        confirm({
            title: 'Xóa các sản phẩm đã chọn',
            content: `Bạn có chắc muốn xóa ${selectedItems.length} sản phẩm khỏi giỏ hàng?`,
            okText: 'Xóa',
            okType: 'danger',
            cancelText: 'Hủy',
            onOk: async () => {
                // Xóa lần lượt
                for (const id of selectedItems) {
                    await handleRemoveItem(id);
                }
                toast.success('Đã xóa các sản phẩm đã chọn');
            },
        });
    };

    const handleSelectAll = (e) => {
        if (e.target.checked) {
            setSelectedItems(cartItems.map(item => item.id));
        } else {
            setSelectedItems([]);
        }
    };

    const handleSelectItem = (itemId) => {
        setSelectedItems(prev =>
            prev.includes(itemId)
                ? prev.filter(id => id !== itemId)
                : [...prev, itemId]
        );
    };

    const handleCheckout = async () => {
        if (selectedItems.length === 0) {
            toast.error('Vui lòng chọn ít nhất một sản phẩm để đặt hàng');
            return;
        }

        if (!localStorage.getItem('token')) {
            navigate(`/login?redirect=${location.pathname}`);
            return;
        }

        const profileData = await getProfile();
        if (!profileData.completedStatus) {
            navigate('/profile');
            return;
        }

        const toastId = toast.loading('Đang tạo đơn hàng...');

        const orderItems = cartItems
            .filter(item => selectedItems.includes(item.id))
            .map(item => ({
                productId: item.productId,
                variantId: item.variantId,
                price: item.finalPrice,
                quantity: item.quantity,
            }));

        const data = await createOrders({ orderItems });
        if (data) {
            toast.success('Tạo đơn hàng thành công!', { id: toastId });
            navigate('/order-history');
        } else {
            toast.error('Có lỗi xảy ra', { id: toastId });
        }
    };

    const formatCurrency = (amount) => {
        return amount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    };

    const CartItem = ({ item }) => {
        const isSelected = selectedItems.includes(item.id);

        return (
            <div className={`${styles.cartItem} ${isSelected ? styles.selected : ''}`}>
                <Checkbox
                    checked={isSelected}
                    onChange={() => handleSelectItem(item.id)}
                    className={styles.itemCheckbox}
                />

                <div className={styles.productInfo}>
                    <div className={styles.itemImage}>
                        <img src={item.product.defaultImage.url} alt={item.product.name} />
                    </div>
                    <div className={styles.itemDetails}>
                        <h3 className={styles.itemName}>{item.product.name}</h3>
                        <p className={styles.itemVariant}>Phân loại: {item.product.defaultVariant.name}</p>
                    </div>
                </div>

                <div className={styles.itemPrice}>
                    {item.product.defaultVariant.discountPrice && (
                        <span className={styles.originalPrice}>
              {formatCurrency(item.product.defaultVariant.basePrice)}
            </span>
                    )}
                    <span className={styles.finalPrice}>{formatCurrency(item.finalPrice)}</span>
                </div>

                <div className={styles.itemQuantity}>
                    <button onClick={() => handleQuantityChange(item.id, item.quantity - 1)}>-</button>
                    <input
                        type="number"
                        value={item.quantity}
                        onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value) || 1)}
                        min="1"
                    />
                    <button onClick={() => handleQuantityChange(item.id, item.quantity + 1)}>+</button>
                </div>

                <div className={styles.itemTotal}>
                    {formatCurrency(item.finalPrice * item.quantity)}
                </div>

                <div className={styles.itemActions}>
                    <button
                        onClick={() =>
                            confirm({
                                title: 'Xóa sản phẩm',
                                content: `Xóa "${item.product.name}" khỏi giỏ hàng?`,
                                okText: 'Xóa',
                                okType: 'danger',
                                cancelText: 'Hủy',
                                onOk: () => handleRemoveItem(item.id),
                            })
                        }
                        className={styles.removeButton}
                    >
                        <FaTrashAlt />
                    </button>
                </div>
            </div>
        );
    };

    if (loading) {
        return (
            <div className={styles.shoppingCart}>
                <Spin size="large" tip="Đang tải giỏ hàng..." style={{ marginTop: '100px' }} />
            </div>
        );
    }

    return (
        <div className={styles.shoppingCart}>
            <h1 className={styles.cartHeader}>
                Giỏ hàng của bạn
                {cartItems.length > 0 && <span className={styles.itemCount}>({cartItems.length} sản phẩm)</span>}
            </h1>

            {cartItems.length > 0 ? (
                <div className={styles.cartBody}>
                    <div className={styles.cartItemsList}>
                        <div className={styles.listControls}>
                            <Checkbox
                                checked={selectedItems.length === cartItems.length && cartItems.length > 0}
                                indeterminate={selectedItems.length > 0 && selectedItems.length < cartItems.length}
                                onChange={handleSelectAll}
                            >
                                Chọn tất cả
                            </Checkbox>
                            {selectedItems.length > 0 && (
                                <button onClick={handleRemoveSelected} className={styles.deleteSelectedBtn}>
                                    <FaRegTrashAlt /> Xóa đã chọn ({selectedItems.length})
                                </button>
                            )}
                        </div>

                        <div className={styles.listHeader}>
                            <div className={styles.headerProduct}>Sản phẩm</div>
                            <div className={styles.headerPrice}>Đơn giá</div>
                            <div className={styles.headerQuantity}>Số lượng</div>
                            <div className={styles.headerTotal}>Thành tiền</div>
                            <div className={styles.headerActions}></div>
                        </div>

                        {cartItems.map(item => (
                            <CartItem key={item.id} item={item} />
                        ))}
                    </div>

                    <div className={styles.cartSummary}>
                        <h2>Tóm tắt đơn hàng</h2>
                        <div className={styles.summaryLine}>
                            <span>Tạm tính ({selectedItems.length} sản phẩm)</span>
                            <span>{formatCurrency(selectedTotal)}</span>
                        </div>
                        <div className={styles.summaryLine}>
                            <span>Phí vận chuyển</span>
                            <span>Miễn phí</span>
                        </div>
                        <hr />
                        <div className={`${styles.summaryLine} ${styles.total}`}>
                            <span>Tổng cộng</span>
                            <span>{formatCurrency(selectedTotal)}</span>
                        </div>
                        <button className={styles.checkoutButton} onClick={handleCheckout}>
                            Đặt hàng ngay
                        </button>
                        <p className={styles.note}>Bạn có thể chọn sản phẩm để đặt hàng riêng</p>
                    </div>
                </div>
            ) : (
                <div className={styles.emptyCart}>
                    <p>Giỏ hàng trống</p>
                    <a href="/">Tiếp tục mua sắm</a>
                </div>
            )}
        </div>
    );
};

export default ShoppingCart;