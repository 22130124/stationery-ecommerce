// src/features/product-details/pages/ProductDetailsPage.jsx

import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getProductBySlug } from "../../../api/productApi";
import styles from "./ProductDetailsPage.module.scss";
import ReactMarkdown from 'react-markdown';
import toast from "react-hot-toast";

// Icon ngôi sao đơn giản để đánh giá
const StarIcon = () => <>⭐</>;

const ProductDetailsPage = () => {
    const { slug } = useParams();
    const [product, setProduct] = useState(null);
    const [selectedVariant, setSelectedVariant] = useState(null);
    const [mainImage, setMainImage] = useState('');
    const [quantity, setQuantity] = useState(1);
    const [cart, setCart] = useState(() => {
        try {
            const storedCart = localStorage.getItem("cart");
            return storedCart ? JSON.parse(storedCart) : [];
        } catch (error) {
            console.error("Lỗi khi đọc giỏ hàng từ localStorage:", error);
            return [];
        }
    });

    useEffect(() => {
        if (!slug) return;

        const fetchProduct = async () => {
            try {
                const data = await getProductBySlug(slug);
                if (data && data.product) {
                    setProduct(data.product);
                    const defaultVar = data.product.defaultVariant || data.product.variants?.[0];
                    if (defaultVar) {
                        setSelectedVariant(defaultVar);
                        setMainImage(defaultVar.defaultImage?.url || defaultVar.images?.[0]?.url || '');
                    }
                }
            } catch (error) {
                console.error("Lỗi khi tải sản phẩm:", error);
            }
        };

        fetchProduct();
    }, [slug]);

    console.log(product);

    useEffect(() => {
        localStorage.setItem("cart", JSON.stringify(cart));
    }, [cart])

    const formatPrice = (price) =>
        price?.toLocaleString("vi-VN", { style: "currency", currency: "VND" }) || "";

    const calculateDiscount = (originalPrice, discountPrice) => {
        if (originalPrice && discountPrice && originalPrice > discountPrice) {
            return Math.round(((originalPrice - discountPrice) / originalPrice) * 100);
        }
        return 0;
    };

    // Hàm xử lý sự kiện
    const handleVariantSelect = (variant) => {
        setSelectedVariant(variant);
        setMainImage(variant.defaultImage?.url || variant.images?.[0]?.url || '');
    };

    const handleQuantityChange = (amount) => {
        setQuantity(prev => Math.max(1, prev + amount));
    };

    const handleAddToCartButtonClick = () => {
        setCart((prev) => {
            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            const existingItemIndex = prev.findIndex(
                (item) =>
                    item.productId === product.id &&
                    item.variantId === selectedVariant.id
            );

            // Nếu đã có, cập nhật quantity
            if (existingItemIndex !== -1) {
                const updatedCart = [...prev];
                updatedCart[existingItemIndex] = {
                    ...updatedCart[existingItemIndex],
                    quantity: updatedCart[existingItemIndex].quantity + quantity,
                };
                return updatedCart;
            }

            // Nếu chưa có, thêm mới vào giỏ
            return [
                ...prev,
                {
                    productId: product.id,
                    variantId: selectedVariant.id,
                    quantity: quantity,
                },
            ];
        });

        toast.dismiss();
        toast.success(
            `Đã thêm ${quantity} ${product.name} ${selectedVariant.name} vào giỏ hàng`
        );
    };

    console.log("Cart", cart)

    const handleBuyNowButtonClick = () => {
        alert("Buy now");
    }

    // Hiển thị trạng thái đang tải nếu chưa có dữ liệu
    if (!product || !selectedVariant) {
        return <div className={styles.loading}>Đang tải thông tin sản phẩm...</div>;
    }

    const currentPrice = selectedVariant.discountPrice || selectedVariant.basePrice;
    const discountPercent = calculateDiscount(selectedVariant.basePrice, selectedVariant.discountPrice);

    return (
        <div className={styles.productDetailsPage}>
            <div className={styles.mainContent}>
                {/* Hình ảnh */}
                <div className={styles.imageSection}>
                    <img src={mainImage} alt={selectedVariant.name} className={styles.mainImage} />
                    <div className={styles.thumbnailList}>
                        {selectedVariant.images?.map(image => (
                            <img
                                key={image.id}
                                src={image.url}
                                alt={`Thumbnail ${image.id}`}
                                className={`${styles.thumbnail} ${mainImage === image.url ? styles.activeThumbnail : ''}`}
                                onClick={() => setMainImage(image.url)}
                            />
                        ))}
                    </div>
                </div>

                {/* Thông tin chi tiết */}
                <div className={styles.detailsSection}>
                    <h1 className={styles.productName}>{product.name}</h1>

                    <div className={styles.metaInfo}>
                        <div className={styles.rating}>
                            <span>{product.rating}</span> <StarIcon />
                        </div>
                        <div className={styles.metaDivider}>|</div>
                        <div>Thương hiệu: <span className={styles.metaValue}>{product.brand.name}</span></div>
                        <div className={styles.metaDivider}>|</div>
                        <div>Loại: <span className={styles.metaValue}>{product.category.name}</span></div>
                    </div>

                    <div className={styles.priceSection}>
                        <span className={styles.currentPrice}>{formatPrice(currentPrice)}</span>
                        {discountPercent > 0 && (
                            <>
                                <span className={styles.originalPrice}>{formatPrice(selectedVariant.basePrice)}</span>
                                <span className={styles.discountTag}>-{discountPercent}%</span>
                            </>
                        )}
                    </div>

                    {/* Variants */}
                    <div className={styles.variantSection}>
                        <span className={styles.variantLabel}>Phân loại:</span>
                        <div className={styles.variantOptions}>
                            {product.variants.map(variant => (
                                <button
                                    key={variant.id}
                                    className={`${styles.variantButton} ${selectedVariant.id === variant.id ? styles.activeVariant : ''}`}
                                    onClick={() => handleVariantSelect(variant)}
                                >
                                    {variant.name}
                                </button>
                            ))}
                        </div>
                    </div>

                    <div className={styles.quantitySection}>
                        <span className={styles.quantityLabel}>Số lượng:</span>
                        <div className={styles.quantitySelector}>
                            <button onClick={() => handleQuantityChange(-1)} disabled={quantity <= 1}>-</button>
                            <input type="text" value={quantity} readOnly />
                            <button onClick={() => handleQuantityChange(1)}>+</button>
                        </div>
                    </div>

                    <div className={styles.actionButtons}>
                        <button className={styles.addToCartButton} onClick={handleAddToCartButtonClick}>Thêm vào giỏ hàng</button>
                        <button className={styles.buyNowButton} onClick={handleBuyNowButtonClick}>Mua ngay</button>
                    </div>
                </div>
            </div>

            {/* Phần mô tả sản phẩm */}
            <div className={styles.descriptionSection}>
                <h2>Mô tả sản phẩm</h2>
                <ReactMarkdown>
                    {product.description}
                </ReactMarkdown>
            </div>
        </div>
    );
};

export default ProductDetailsPage;