// src/features/product-list/pages/ProductList.jsx
"use client";

// External Libraries
import React, {useState, useEffect} from "react";
import {useSearchParams} from "react-router"; // React Router

// Services/APIs
// import {getCategories} from "../../services/categoryApi";
// import {getProductsByFilter} from "../../services/productApi";

// Relative Imports
// import ProductListSidebar from "../../components/user/ProductListSidebar.jsx";
// import ProductListSortBar from "../../components/user/ProductListSortBar.jsx";
// import ProductItem from "../../components/user/ProductItem.jsx";
// import Pagination from "../../components/common/Pagination.jsx";

// Styles
import styles from "./ProductList.module.scss";
import {getCategories} from "../../../api/categoryApi";
import {getProducts} from "../../../api/productApi";

const ProductList = () => {
    const [categories, setCategories] = useState([])
    const [products, setProducts] = useState([])

    useEffect(() => {
        const fetchCategories = async () => {
            const data = await getCategories()
            setCategories(data.categories)
        }

        const fetchProducts = async () => {
            const data = await getProducts()
            setProducts(data.products)
        }

        fetchCategories()
        fetchProducts()
    }, [])

    console.log("Categories:", categories);
    console.log("Products:", products);

    return (
        <>
            <div className="container">
                <h1>This is Product List page</h1>
            </div>
        </>
    )
};

export default ProductList;
