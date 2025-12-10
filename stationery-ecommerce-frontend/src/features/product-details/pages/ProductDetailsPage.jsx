// src/features/product-details/pages/ProductDetailsPage.jsx

import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {getProductBySlug} from "../../../api/productApi";
import styles from "./ProductDetailsPage.module.scss";
import DOMPurify from 'dompurify'
import toast from "react-hot-toast";
import {getSupplierById} from "../../../api/supplierApi";
import {getBrandById} from "../../../api/brandApi";
import {getCategoryById} from "../../../api/categoryApi";
import {addToCart} from "../../../api/cartApi";
import RecommendedProducts from "../components/RecommendedProducts";

// Icon ngôi sao đơn giản để đánh giá
const StarIcon = () => <>⭐</>;

const ProductDetailsPage = () => {
    const {slug} = useParams();
    const [product, setProduct] = useState(null);
    const [supplier, setSupplier] = useState(null);
    const [brand, setBrand] = useState(null);
    const [category, setCategory] = useState(null);
    const [selectedVariant, setSelectedVariant] = useState(null);
    const [mainImage, setMainImage] = useState('');
    const [quantity, setQuantity] = useState(1);

    // Hàm fetch thông tin sản phẩm
    useEffect(() => {
        if (!slug) return;

        const fetchProduct = async () => {
            const data = await getProductBySlug(slug);
            if (!data) return
            if (data && data.product) {
                setProduct(data.product);
                const defaultVar = data.product.defaultVariant || data.product.variants?.[0];
                if (defaultVar) {
                    setSelectedVariant(defaultVar);
                    setMainImage(defaultVar.defaultImage?.url || defaultVar.images?.[0]?.url || '');
                }
            }
        };

        fetchProduct();
    }, [slug]);

    // Hàm fetch thông tin supplier, brand, category của sản phẩm
    useEffect(() => {
        if (!product) return;

        if (product.supplier && product.brand && product.category) {
            return;
        }

        const categoryId = product.categoryId;
        const supplierId = product.supplierId;
        const brandId = product.brandId;

        const fetchProductSubInfo = async () => {
            try {
                const [supplier, brand, category] = await Promise.all([
                    getSupplierById(supplierId),
                    getBrandById(brandId),
                    getCategoryById(categoryId)
                ]);

                setProduct(prevProduct => ({
                    ...prevProduct,
                    supplier: supplier,
                    brand: brand,
                    category: category,
                }));

            } catch (error) {
                console.error("Lỗi khi tải thông tin sản phẩm:", error);
            }
        }

        fetchProductSubInfo();
    }, [product]);

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
        setSelectedVariant(variant);
        setMainImage(variant.defaultImage?.url || variant.images?.[0]?.url || '');
    };

    const handleQuantityChange = (amount) => {
        setQuantity(prev => Math.max(1, prev + amount));
    };

    const handleAddToCartButtonClick = async () => {
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
                        <div className={styles.rating}>
                            <span>{product.rating}</span> <StarIcon/>
                        </div>
                        <div className={styles.metaDivider}>|</div>
                        <div>Thương hiệu: <span className={styles.metaValue}>{product.brand?.name}</span></div>
                        <div className={styles.metaDivider}>|</div>
                        <div>Danh mục: <span className={styles.metaValue}>{product.category?.name}</span></div>
                        <div>Màu sắc: <span className={styles.metaValue}>{selectedVariant.color}</span></div>
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

            <RecommendedProducts productId={product.id} />
        </div>
    );
};

export default ProductDetailsPage;