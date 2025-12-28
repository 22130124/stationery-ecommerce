// src/features/product-details/pages/ProductDetailsPage.jsx
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {getProductBySlug} from "../../../api/productApi";
import styles from "./ProductDetailsPage.module.scss";
import DOMPurify from 'dompurify'
import toast from "react-hot-toast";
import {addToCart} from "../../../api/cartApi";
import RecommendedProducts from "../components/RecommendedProducts";

const ProductDetailsPage = () => {
    const {slug} = useParams();
    const [product, setProduct] = useState(null);
    const [selectedVariant, setSelectedVariant] = useState(null);
    const [mainImage, setMainImage] = useState('');
    const [quantity, setQuantity] = useState(1);

    // Hàm fetch thông tin sản phẩm
    useEffect(() => {
        if (!slug) return;

        const fetchProduct = async () => {
            const data = await getProductBySlug(slug);
            if (!data) return
            const prod = data.product;
            setProduct(prod);

            // Tìm variant còn hàng đầu tiên để làm default
            const availableVariants = prod.variants.filter(v => v.stock > 0);
            const hasAvailable = availableVariants.length > 0;

            let defaultVariant;
            if (hasAvailable) {
                // Ưu tiên defaultVariant nếu còn hàng, không thì lấy cái đầu tiên còn hàng
                defaultVariant = prod.defaultVariant && prod.defaultVariant.stock > 0
                    ? prod.defaultVariant
                    : availableVariants[0];
            } else {
                // Không còn variant nào có hàng thì lấy default hoặc cái đầu
                defaultVariant = prod.defaultVariant || prod.variants?.[0];
            }

            setSelectedVariant(defaultVariant);
            setMainImage(defaultVariant?.defaultImage?.url || defaultVariant?.images?.[0]?.url || '');
        };

        fetchProduct();
    }, [slug]);

    console.log(product);

    const formatPrice = (price) =>
        price?.toLocaleString("vi-VN", {style: "currency", currency: "VND"}) || "";

    const calculateDiscount = (originalPrice, discountPrice) => {
        if (originalPrice && discountPrice && originalPrice > discountPrice) {
            return Math.round(((originalPrice - discountPrice) / originalPrice) * 100);
        }
        return 0;
    };

    // Hàm xử lý sự kiện
    const handleVariantSelect = (variant) => {
        if (variant.stock > 0) { // Chỉ cho chọn nếu còn hàng
            setSelectedVariant(variant);
            setMainImage(variant.defaultImage?.url || variant.images?.[0]?.url || '');
        }
    };

    const handleQuantityChange = (amount) => {
        setQuantity(prev => Math.max(1, prev + amount));
    };

    const handleAddToCartButtonClick = async () => {
        if (selectedVariant.stock === 0) {
            toast.error("Sản phẩm này đã hết hàng");
            return;
        }

        try {
            await addToCart({
                productId: product.id,
                variantId: selectedVariant.id,
                quantity: quantity,
            });
        } catch (error) {
            console.error(error);
        }

        toast.dismiss();
        toast.success(
            `Đã thêm ${quantity} ${product.name} ${selectedVariant.name} vào giỏ hàng`
        );
    };

    const handleBuyNowButtonClick = () => {
        if (selectedVariant.stock === 0) {
            toast.error("Sản phẩm này đã hết hàng, không thể mua");
        }
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
                    <img src={mainImage} alt={selectedVariant.name} className={styles.mainImage}/>
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
                        <div className={styles.metaDivider}>|</div>
                        <div>Thương hiệu: <span
                            className={styles.metaValue}>{product.brand?.name || "Chưa xác định"}</span></div>
                        <div className={styles.metaDivider}>|</div>
                        <div>Danh mục: <span
                            className={styles.metaValue}>{product.category?.name || "Chưa xác định"}</span></div>
                        <div className={styles.metaDivider}>|</div>
                        <div>Màu sắc: <span className={styles.metaValue}>
                                {selectedVariant.colors.map(item => item.color).join(", ")}
                            </span>
                        </div>
                        <div className={styles.metaDivider}>|</div>
                        {selectedVariant.stock === 0 ? (
                            <div><span className={styles.metaValue}>Hết hàng</span></div>
                        ) : (
                            <div>Tồn kho: <span className={styles.metaValue}>{selectedVariant.stock}</span></div>
                        )}
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
                            {product.variants.map(variant => {
                                const outOfStock = variant.stock === 0;
                                return (
                                    <button
                                        key={variant.id}
                                        className={`
                                            ${styles.variantButton}
                                            ${selectedVariant.id === variant.id ? styles.activeVariant : ''}
                                            ${outOfStock ? styles.disabledVariant : ''}
                                        `}
                                        onClick={() => handleVariantSelect(variant)}
                                        disabled={outOfStock}
                                    >
                                        {variant.name}
                                        {outOfStock && <span className={styles.outOfStockTag}> (Hết hàng)</span>}
                                    </button>
                                );
                            })}
                        </div>
                    </div>

                    <div className={styles.quantitySection}>
                        <span className={styles.quantityLabel}>Số lượng:</span>
                        <div className={styles.quantitySelector}>
                            <button onClick={() => handleQuantityChange(-1)} disabled={quantity <= 1}>-</button>
                            <input type="text" value={quantity} readOnly/>
                            <button onClick={() => handleQuantityChange(1)}>+</button>
                        </div>
                    </div>

                    <div className={styles.actionButtons}>
                        <button className={styles.addToCartButton} onClick={handleAddToCartButtonClick}>Thêm vào giỏ
                            hàng
                        </button>
                        <button className={styles.buyNowButton} onClick={handleBuyNowButtonClick}>Mua ngay</button>
                    </div>
                </div>
            </div>

            {/* Phần mô tả sản phẩm */}
            <div className={styles.descriptionSection}>
                <h2>Mô tả sản phẩm</h2>
                <div
                    className={styles.descriptionContent}
                    dangerouslySetInnerHTML={{__html: DOMPurify.sanitize(product.description)}}
                />
            </div>

            <RecommendedProducts productId={product.id}/>
        </div>
    );
};

export default ProductDetailsPage;