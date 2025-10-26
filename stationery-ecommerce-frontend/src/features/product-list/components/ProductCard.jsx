import React from 'react';
import styles from './ProductCard.module.scss';
import {Link} from "react-router-dom";

// Định dạng tiền tệ
const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
};

const ProductCard = ({ product }) => {
    const defaultVariant = product.defaultVariant;
    const basePrice = defaultVariant?.basePrice || 0;
    const discountPrice = defaultVariant?.discountPrice || basePrice;
    const imageUrl = product.defaultImage.url;

    const hasDiscount = discountPrice < basePrice;
    const discountPercentage = hasDiscount
        ? Math.round(((basePrice - discountPrice) / basePrice) * 100)
        : 0;

    return (
        <Link to={`/${product.slug}`} className={styles.productLink}>
            <div className={styles.card}>
                <div className={styles.imageWrapper}>
                    <img src={imageUrl} alt={product.name} className={styles.image} />
                    {hasDiscount && (
                        <div className={styles.discountTag}>
                            -{discountPercentage}%
                        </div>
                    )}
                </div>
                <div className={styles.info}>
                    <h3 className={styles.name}>{product.name}</h3>
                    <div className={styles.price}>
                        {hasDiscount ? (
                            <>
                            <span className={styles.discountPrice}>
                                {formatCurrency(discountPrice)}
                            </span>
                                <span className={styles.basePrice}>
                                {formatCurrency(basePrice)}
                            </span>
                            </>
                        ) : (
                            <span className={styles.discountPrice}>
                            {formatCurrency(basePrice)}
                        </span>
                        )}
                    </div>
                </div>
            </div>
        </Link>
    );
};

export default ProductCard;