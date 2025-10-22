// ShoppingCart.jsx
import React, { useState, useEffect } from 'react';
import styles from './ShoppingCart.module.scss';
import { FaTrashAlt } from 'react-icons/fa';

const initialCartItems = [
    {
        id: 1,
        name: "Bút bi Thiên Long TL-027",
        slug: "but-bi-thien-long-tl-027",
        brand: { name: "Thiên Long" },
        variants: [
            { id: 1, name: "Mực Xanh", basePrice: 5000, discountPrice: 4500, defaultImage: { url: "https://placehold.co/600x600/3498db/ffffff.png?text=SP1001+Xanh" } },
            { id: 2, name: "Mực Đỏ", basePrice: 5000, discountPrice: null, defaultImage: { url: "https://placehold.co/600x600/e74c3c/ffffff.png?text=SP1001+Do" } }
        ],
        quantity: 2,
        selectedVariantId: 1
    },
    {
        id: 2,
        name: "Tập 96 trang Campus",
        slug: "tap-96-trang-campus",
        brand: { name: "Campus" },
        variants: [{ id: 4, name: "Kẻ ngang", basePrice: 12000, discountPrice: 10000, defaultImage: { url: "https://placehold.co/600x600/2ecc71/ffffff.png?text=Tap+Campus" } }],
        quantity: 5,
        selectedVariantId: 4
    },
    {
        id: 3,
        name: "Gôm tẩy Pentel",
        slug: "gom-tay-pentel",
        brand: { name: "Pentel" },
        variants: [{ id: 5, name: "Trắng", basePrice: 8000, discountPrice: null, defaultImage: { url: "https://placehold.co/600x600/ecf0f1/2c3e50.png?text=Gom+Pentel" } }],
        quantity: 1,
        selectedVariantId: 5
    }
];

const formatCurrency = (amount) => {
    if (typeof amount !== 'number') return '';
    return amount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
};

const ShoppingCart = () => {
    const [cartItems, setCartItems] = useState(initialCartItems);
    const [totalPrice, setTotalPrice] = useState(0);

    useEffect(() => {
        const calculateTotal = () => {
            return cartItems.reduce((total, item) => {
                const variant = item.variants.find(v => v.id === item.selectedVariantId);
                if (!variant) return total;
                const price = variant.discountPrice ?? variant.basePrice;
                return total + (price * item.quantity);
            }, 0);
        };
        setTotalPrice(calculateTotal());
    }, [cartItems]);

    const handleQuantityChange = (productId, variantId, newQuantity) => {
        if (newQuantity < 1) return;
        setCartItems(prevItems =>
            prevItems.map(item =>
                (item.id === productId && item.selectedVariantId === variantId)
                    ? { ...item, quantity: newQuantity }
                    : item
            )
        );
    };

    const handleRemoveItem = (productId, variantId) => {
        setCartItems(prevItems =>
            prevItems.filter(item =>
                !(item.id === productId && item.selectedVariantId === variantId)
            )
        );
    };

    const CartItem = ({ item }) => {
        const variant = item.variants.find(v => v.id === item.selectedVariantId);
        if (!variant) return null;

        const finalPrice = variant.discountPrice ?? variant.basePrice;

        return (
            <div className={styles.cartItem}>
                <div className={styles.productInfo}>
                    <div className={styles.itemImage}>
                        <img src={variant.defaultImage.url} alt={item.name} />
                    </div>
                    <div className={styles.itemDetails}>
                        <h3 className={styles.itemName}>{item.name}</h3>
                        <p className={styles.itemVariant}>Phân loại: {variant.name}</p>
                        <p className={styles.itemBrand}>Thương hiệu: {item.brand.name}</p>
                    </div>
                </div>

                <div className={styles.itemPrice}>
                    {variant.discountPrice && (
                        <span className={styles.originalPrice}>{formatCurrency(variant.basePrice)}</span>
                    )}
                    <span className={styles.finalPrice}>{formatCurrency(finalPrice)}</span>
                </div>

                <div className={styles.itemQuantity}>
                    <button onClick={() => handleQuantityChange(item.id, variant.id, item.quantity - 1)}>-</button>

                    <input
                        type="number"
                        value={item.quantity}
                        onChange={(e) => handleQuantityChange(item.id, variant.id, parseInt(e.target.value, 10) || 1)}
                        min="1"
                    />
                    <button onClick={() => handleQuantityChange(item.id, variant.id, item.quantity + 1)}>+</button>
                </div>

                <div className={styles.itemTotal}>
                    {formatCurrency(finalPrice * item.quantity)}
                </div>

                <div className={styles.itemActions}>
                    <button onClick={() => handleRemoveItem(item.id, variant.id)} className={styles.removeButton}>
                        <FaTrashAlt />
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
                            <CartItem key={`${item.id}-${item.selectedVariantId}`} item={item} />
                        ))}
                    </div>

                    {/* --- Cải thiện UX: Khối tóm tắt sẽ được "ghim" lại khi cuộn --- */}
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
                        <hr />
                        <div className={`${styles.summaryLine} ${styles.total}`}>
                            <span>Tổng cộng</span>
                            <span>{formatCurrency(totalPrice)}</span>
                        </div>
                        <button className={styles.checkoutButton}>Tiến hành thanh toán</button>
                    </div>
                </div>
            ) : (
                <div className={styles.emptyCart}>
                    {/* Có thể thêm SVG Icon ở đây để đẹp hơn */}
                    <p>Giỏ hàng của bạn chưa có sản phẩm nào.</p>
                    <a href="/">Tiếp tục mua sắm</a>
                </div>
            )}
        </div>
    );
};

export default ShoppingCart;