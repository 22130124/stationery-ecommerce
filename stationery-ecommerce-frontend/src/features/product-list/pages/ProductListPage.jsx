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
import {getCategories} from "../../../api/categoryApi";
import {getProductsByCategoryAndPagination} from "../../../api/productApi";

const PRODUCTS_PER_PAGE = 12;

const ProductListPage = () => {
    const [searchParams, setSearchParams] = useSearchParams()

    const [categories, setCategories] = useState([])
    const [products, setProducts] = useState([])
    const categoryParam = searchParams.get("category") ? searchParams.get("category") : "all"
    const pageParam = searchParams.get("page") ? parseInt(searchParams.get("page")) : "1"
    const [totalPages, setTotalPages] = useState(1);
    const [totalItems, setTotalItems] = useState(0);

    const [pagination, setPagination] = useState({
        currentPage: 1,
        totalPages: 1
    });

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
            const data = await getCategories()
            setCategories(data.categories)
        }

        fetchCategories()
    }, [])

    useEffect(() => {

    }, [searchParams])

    console.log("Categories:", categories)

    useEffect(() => {
        const fetchProducts = async () => {
            const data = await getProductsByCategoryAndPagination({
                categorySlug: categoryParam,
                page: pageParam,
                limit: PRODUCTS_PER_PAGE,
            })
            setProducts(data.products)
            setTotalPages(data.totalPages)
            setTotalItems(data.items)
        }
        fetchProducts()
    }, [searchParams, setSearchParams])
    console.log("Products:", products)

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
                        <div className={styles.sortBar}>
                            <label htmlFor="sort">Sắp xếp theo: </label>
                            <select name="sort" id="sort">
                                <option value="popular">Phổ biến</option>
                                <option value="newest">Mới nhất</option>
                                <option value="price-asc">Giá: Tăng dần</option>
                                <option value="price-desc">Giá: Giảm dần</option>
                            </select>
                        </div>
                    </div>

                    <div className={styles.productGrid}>
                        {products.map((product) => (
                            <ProductCard
                                key={product.id}
                                product={product}
                            />
                        ))}
                    </div>

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
