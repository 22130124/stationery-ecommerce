// ShoppingCart.jsx
import React, {useState, useEffect} from 'react';
import styles from './ShoppingCart.module.scss';
import {FaTrashAlt} from 'react-icons/fa';
import {getCartItems} from "../../../api/cartApi";

const ShoppingCart = () => {
    const [cartItems, setCartItems] = useState([]);
    const [totalPrice, setTotalPrice] = useState(0);
    const [storedCart, setStoredCart] = useState(() => {
        try {
            const storedCart = localStorage.getItem("cart");
            return storedCart ? JSON.parse(storedCart) : [];
        } catch (error) {
            console.error("Lỗi khi đọc giỏ hàng từ localStorage:", error);
            return [];
        }
    });

    useEffect(() => {
        if (storedCart.length < 0) return;
        const variantIds = storedCart.map((item) => item.variantId);
        const fetchCartItems = async () => {
            const data = await getCartItems(variantIds);

            const cartItemsData = data.cartItems.map(item => {
                const storedItem = storedCart.find(
                    localItem => localItem.variantId === item.variant.id
                );

                return {
                    ...item,
                    quantity: storedItem ? storedItem.quantity : 1
                };
            });

            setCartItems(cartItemsData);
        }
        fetchCartItems();
    }, [])
    console.log("Cart Items:", cartItems);

    useEffect(() => {
        localStorage.setItem("cart", JSON.stringify(storedCart));
    }, [storedCart]);

    useEffect(() => {
        const calculateTotal = () => {
            return cartItems.reduce((total, item) => {
                const variant = item.variant
                if (!variant) return total;
                const price = variant.discountPrice ?? variant.basePrice;
                return total + (price * item.quantity);
            }, 0);
        };
        setTotalPrice(calculateTotal());
    }, [cartItems]);

    const handleQuantityChange = (variantId, newQuantity) => {
        if (newQuantity < 1) return;

        setStoredCart((prev) =>
            prev.map(item =>
                (item.variantId === variantId)
                    ? {...item, quantity: newQuantity}
                    : item
            )
        )

        setCartItems((prev) =>
            prev.map(item =>
                (item.variant.id === variantId)
                    ? {...item, quantity: newQuantity}
                    : item
            ));
    };

    const handleRemoveItem = (variantId) => {
        setCartItems(prevItems =>
            prevItems.filter(item =>
                !(item.variant.id === variantId)
            )
        );

        setStoredCart(prevItems =>
            prevItems.filter(item =>
                !(item.variantId === variantId)
            )
        );
    };

    const formatCurrency = (amount) => {
        if (typeof amount !== 'number') return '';
        return amount.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
    };

    const CartItem = ({item}) => {
        const variant = item.variant;
        if (!variant) return null;

        const finalPrice = variant.discountPrice ?? variant.basePrice;

        return (
            <div className={styles.cartItem}>
                <div className={styles.productInfo}>
                    <div className={styles.itemImage}>
                        <img src={variant.defaultImage.url} alt={item.name}/>
                    </div>
                    <div className={styles.itemDetails}>
                        <h3 className={styles.itemName}>{item.name}</h3>
                        <p className={styles.itemVariant}>Phân loại: {variant.name}</p>
                        <p className={styles.itemBrand}>Thương hiệu: {item.brandName}</p>
                    </div>
                </div>

                <div className={styles.itemPrice}>
                    {variant.discountPrice && (
                        <span className={styles.originalPrice}>{formatCurrency(variant.basePrice)}</span>
                    )}
                    <span className={styles.finalPrice}>{formatCurrency(finalPrice)}</span>
                </div>

                <div className={styles.itemQuantity}>
                    <button onClick={() => handleQuantityChange(variant.id, item.quantity - 1)}>-</button>

                    <input
                        type="number"
                        value={item.quantity}
                        onChange={(e) => handleQuantityChange(item.id, variant.id, parseInt(e.target.value, 10) || 1)}
                        min="1"
                    />
                    <button onClick={() => handleQuantityChange(variant.id, item.quantity + 1)}>+</button>
                </div>

                <div className={styles.itemTotal}>
                    {formatCurrency(finalPrice * item.quantity)}
                </div>

                <div className={styles.itemActions}>
                    <button onClick={() => handleRemoveItem(variant.id)} className={styles.removeButton}>
                        <FaTrashAlt/>
                    </button>
                </div>
            </div>
        );
    };

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
                            <CartItem key={`${item.variant.id}-${item.selectedVariantId}`} item={item}/>
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
                        <button className={styles.checkoutButton}>Tiến hành thanh toán</button>
                    </div>
                </div>
            ) : (
                <div className={styles.emptyCart}>
                    <p>Giỏ hàng của bạn chưa có sản phẩm nào.</p>
                    <a href="/">Tiếp tục mua sắm</a>
                </div>
            )}
        </div>
    );
};

export default ShoppingCart;