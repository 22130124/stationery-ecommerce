import React, { useState, useEffect, useMemo } from 'react';
import styles from './ShoppingCart.module.scss';
import { FaTrashAlt, FaRegTrashAlt } from 'react-icons/fa';
import { removeCartItem, updateCartItem } from '../../../api/cartApi';
import {useNavigate, useLocation, Link} from 'react-router-dom';
import { Modal, Checkbox, Spin } from 'antd';
import toast from 'react-hot-toast';
import { createOrders } from '../../../api/orderApi';
import { getProfile } from '../../../api/profileApi';
import {pay} from "../../../api/paymentApi";
import {getCart, removeItem} from '../../../redux/slices/cartSlice'
import {useDispatch} from "react-redux";

const { confirm } = Modal;

const ShoppingCart = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const dispatch = useDispatch();

    const [cartItems, setCartItems] = useState([]);
    const [selectedItems, setSelectedItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const buyNowVariantId = location.state?.buyNowVariantId;
    const [isBuyNow, setIsBuyNow] = useState(false);

    // Phương thức xử lý logic khi mới truy cập trang giỏ hàng
    useEffect(() => {
        const fetchCart = async () => {
            setLoading(true);
            const getCartResult = await dispatch(getCart())
            if (getCart.fulfilled.match(getCartResult)) {
                const items = getCartResult.payload.items || []
                console.log(items);
                setCartItems(items);

                // Kiểm tra nếu là truy cập trang giỏ hàng thông qua nút mua ngay
                if (buyNowVariantId) {
                    setIsBuyNow(true);
                    // Chỉ select đúng sản phẩm mua ngay
                    const targetItem = items.find(item => String(item.variantId) === String(buyNowVariantId));

                    if (targetItem) {
                        // Nếu tìm thấy, set mảng chứa chính xác ID lấy từ item (để đảm bảo đúng kiểu dữ liệu)
                        setSelectedItems([targetItem.variantId]);
                    }

                    // Xóa state sau khi dùng
                    navigate(location.pathname, { replace: true, state: null });
                } else {
                    // Vào giỏ hàng bình thường
                    setSelectedItems(items.map(item => item.variantId));
                }
            } else {
                toast.dismissAll()
                toast.error(getCartResult.payload || "Lấy thông tin giỏ hàng thất bại", {
                    duration: 5000
                })
            }
            setLoading(false);
        };
        fetchCart();
    }, []);

    // Tính tổng tiền chỉ của các sản phẩm được chọn
    const selectedTotal = useMemo(() => {
        return cartItems
            .filter(item => selectedItems.includes(item.variantId))
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

    const handleUpdateQuantityApi = async (variantId, quantity) => {
        if (quantity < 1) return;
        const data = await updateCartItem(variantId, quantity);
        if (data) {
            setCartItems(data.cart.items);
        }
    };

    const debouncedUpdate = useDebounce(handleUpdateQuantityApi, 400);

    const handleQuantityChange = (variantId, newQuantity) => {
        if (newQuantity < 1) return;
        setCartItems(prev =>
            prev.map(item => (item.variantId === variantId ? { ...item, quantity: newQuantity } : item))
        );
        debouncedUpdate(variantId, newQuantity);
    };

    const handleRemoveItem = async (variantId) => {
        const removeItemResult = await dispatch(removeItem({variantId: variantId}));
        if(removeItem.fulfilled.match(removeItemResult)) {
            setCartItems(removeItemResult.payload.items || []);
            setSelectedItems(prev => prev.filter(selectedId => selectedId !== variantId));
        } else {
            toast.dismissAll()
            toast.error(removeItemResult.payload || "Loại bỏ sản phẩm ra khỏi giỏ hàng thất bại", {
                duration: 5000
            })
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
                for (const variantId of selectedItems) {
                    await handleRemoveItem(variantId);
                }
                toast.success('Đã xóa các sản phẩm đã chọn');
            },
        });
    };

    const handleSelectAll = (e) => {
        if (e.target.checked) {
            setSelectedItems(cartItems.map(item => item.variantId));
        } else {
            setSelectedItems([]);
        }
    };

    const handleSelectItem = (variantId) => {
        setSelectedItems(prev =>
            prev.includes(variantId)
                ? prev.filter(selectedId => selectedId !== variantId)
                : [...prev, variantId]
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
        if (profileData.status === 'INCOMPLETED') {
            navigate('/profile');
            return;
        }

        const toastId = toast.loading('Đang tạo đơn hàng...');

        const orderItems = cartItems
            .filter(item => selectedItems.includes(item.variantId))
            .map(item => ({
                productId: item.productId,
                variantId: item.variantId,
                price: item.finalPrice,
                quantity: item.quantity,
            }));

        const data = await createOrders({ orderItems });
        if (data) {
            toast.success('Tạo đơn hàng thành công!', { id: toastId });

            // Hiển thị modal hỏi thanh toán
            Modal.confirm({
                title: 'Thanh toán đơn hàng',
                content: 'Bạn có muốn thanh toán đơn hàng này ngay bây giờ không?',
                okText: 'Thanh toán ngay',
                cancelText: 'Để sau',
                onOk: () => {handlePay(data.code)},
                onCancel: () => {
                    // Xem lịch sử đơn hàng
                    navigate('/order-history');
                },
            });
        } else {
            toast.dismiss(toastId)
        }
    };

    const handlePay = async (orderCode) => {
        const response = await pay(orderCode)
        const paymentUrl = response.paymentUrl
        if (paymentUrl) {
            window.location.href = paymentUrl;
        }
    }

    const formatCurrency = (amount) => {
        return amount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    };

    const CartItem = ({ item }) => {
        const isSelected = selectedItems.includes(item.variantId);

        return (
            <div className={`${styles.cartItem} ${isSelected ? styles.selected : ''}`}>
                <Checkbox
                    checked={isSelected}
                    onChange={() => handleSelectItem(item.variantId)}
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
                    <button onClick={() => handleQuantityChange(item.variantId, item.quantity - 1)}>-</button>
                    <input
                        type="number"
                        value={item.quantity}
                        onChange={(e) => handleQuantityChange(item.variantId, parseInt(e.target.value) || 1)}
                        min="1"
                    />
                    <button onClick={() => handleQuantityChange(item.variantId, item.quantity + 1)}>+</button>
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
                                onOk: () => handleRemoveItem(item.variantId),
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
                                checked={!isBuyNow && selectedItems.length === cartItems.length && cartItems.length > 0}
                                indeterminate={!isBuyNow && selectedItems.length > 0 && selectedItems.length < cartItems.length}
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
                            <CartItem key={item.variantId} item={item} />
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
                    <Link to='product-list'>Tiếp tục mua sắm</Link>
                </div>
            )}
        </div>
    );
};

export default ShoppingCart;