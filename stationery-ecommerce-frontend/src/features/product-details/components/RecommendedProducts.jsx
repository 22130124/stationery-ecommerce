import React, { useEffect, useState } from "react";
import ProductCard from "../../product-list/components/ProductCard";
import styles from "./RecommendedProducts.module.scss";
import {getRecommendedProducts} from "../../../api/recommendApi";

export default function RecommendedProducts({ productId }) {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchRecommendations = async () => {
            setLoading(true);
            try {
                const res = await getRecommendedProducts(productId); // productId hiện tại
                setProducts(res.recommendations || []);
            } catch (error) {
                console.error("Lỗi khi lấy sản phẩm gợi ý:", error);
            } finally {
                setLoading(false);
            }
        };

        if (productId) fetchRecommendations();
    }, [productId]);
    console.log(products);

    if (!products.length) return null;

    return (
        <div className={styles.container}>
            <h3>Sản phẩm gợi ý cho bạn</h3>
            <div className={styles.list}>
                {loading
                    ? "Đang tải..."
                    : products.map((p) => <ProductCard key={p.id} product={p} />)}
            </div>
        </div>
    );
}