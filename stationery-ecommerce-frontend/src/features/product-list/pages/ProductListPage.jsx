// src/features/product-list/pages/ProductListPage.jsx
"use client";

// External Libraries
import React, {useState, useEffect} from "react";
import {useSearchParams} from "react-router-dom";

// Import Components
import Sidebar from "../components/Sidebar";
import ProductCard from "../components/ProductCard";
import Pagination from "../components/Pagination";

// Styles
import styles from "./ProductListPage.module.scss";

// APIs
import {getActiveCategories} from "../../../api/categoryApi";
import {getProductsByCategory, getProductsByCategoryAndPagination} from "../../../api/productApi";

const PRODUCTS_PER_PAGE = 16;

const ProductListPage = () => {
    const [searchParams, setSearchParams] = useSearchParams()

    const [categories, setCategories] = useState([])
    const [products, setProducts] = useState([])
    const categoryParam = searchParams.get("category") ? searchParams.get("category") : "all"
    const pageParam = searchParams.get("page") ? parseInt(searchParams.get("page")) : "1"
    const [totalPages, setTotalPages] = useState(1);
    const [totalItems, setTotalItems] = useState(0);

    // Fetch categories và thêm các param cần thiết trên url (nếu thiếu)
    useEffect(() => {
        const newParams = new URLSearchParams(searchParams)
        const categoryParam = searchParams.get("category")
        if(!categoryParam) {
            newParams.set("category", "all")
        }
        const pageParams = searchParams.get("page")
        if (!pageParams) {
            newParams.set("page", "1")
        }
        setSearchParams(newParams)

        const fetchCategories = async () => {
            const data = await getActiveCategories()
            setCategories(data)
        }

        fetchCategories()
    }, [])

    // Fetch products dựa trên các tham số trên url
    useEffect(() => {
        const fetchProducts = async () => {
            const data = await getProductsByCategory({
                categorySlug: categoryParam,
                page: pageParam,
                limit: PRODUCTS_PER_PAGE,
            })
            setProducts(data.products)
            setTotalPages(data.totalPages)
            setTotalItems(data.totalItems)
        }
        fetchProducts()
    }, [searchParams, setSearchParams])

    const handlePageChange = (page) => {
        const newParams = new URLSearchParams(searchParams);
        newParams.set("page", page);
        setSearchParams(newParams);
        window.scrollTo({
            top: 0,
            left: 0,
            behavior: 'smooth'
        });
    };

    return (
        <div className={styles.container}>
            <div className={styles.pageLayout}>
                {/* Sidebar */}
                <div className={styles.sidebar}>
                    <Sidebar categories={categories}/>
                </div>

                {/* Main Content */}
                <main className={styles.mainContent}>
                    <div className={styles.controls}>
                        <div className={styles.resultInfo}>
                            Hiển thị {(pageParam - 1) * PRODUCTS_PER_PAGE + 1} – {Math.min(pageParam * PRODUCTS_PER_PAGE, totalItems)} trong tổng {totalItems} sản phẩm
                        </div>
                    </div>

                    {products && products.length === 0 ? (
                        <div className={styles.noProducts}>
                            <p>Hiện chưa có sản phẩm nào</p>
                        </div>
                    ) : (
                        <div className={styles.productGrid}>
                            {products.map((product) => (
                                <ProductCard
                                    key={product.id}
                                    product={product}
                                />
                            ))}
                        </div>
                    )}

                    <Pagination
                        currentPage={pageParam}
                        totalPages={totalPages}
                        onPageChange={handlePageChange}
                    />
                </main>
            </div>
        </div>
    );
};

export default ProductListPage;
