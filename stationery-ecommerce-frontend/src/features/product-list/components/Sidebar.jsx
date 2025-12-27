// Sidebar.jsx
import React, {useEffect, useState} from 'react';
import styles from './Sidebar.module.scss';
import {useSearchParams} from "react-router-dom";

const Sidebar = ({categories = []}) => {
    const [searchParams, setSearchParams] = useSearchParams();
    const [openingCategories, setOpeningCategories] = useState({});

    const selectedCategorySlug = searchParams.get("category") || 'all';

    const displayCategories = [
        {
            id: 'all',
            name: "Tất cả sản phẩm",
            slug: "all"
        },
        ...categories,
    ];

    useEffect(() => {
        const openState = {};

        displayCategories.forEach(cat => {
            if (cat.children && cat.children.length > 0) {
                openState[cat.id] = true;
            }
        });

        setOpeningCategories(openState);

    }, [categories]);

    const toggleCategory = (id) => {
        setOpeningCategories(prev => ({
            ...prev,
            [id]: !prev[id]
        }));
    };

    const handleClickCategory = (slug) => {
        const newParams = new URLSearchParams(searchParams);
        newParams.set("category", slug);
        newParams.set("page", "1");
        setSearchParams(newParams);
    };

    return (
        <aside className={styles.sidebar}>
            <h2 className={styles.title}>Danh mục sản phẩm</h2>
            <ul className={styles.navList}>
                {displayCategories.map((parentCategory) => (
                    <li key={parentCategory.id} className={styles.navItem}>
                        <div
                            className={`${styles.parentLinkWrapper}
                                        ${!parentCategory.children?.length && selectedCategorySlug === parentCategory.slug
                                ? styles.active : ''}
                                        `}
                            onClick={
                                parentCategory.children?.length > 0
                                    ? () => toggleCategory(parentCategory.id)
                                    : () => handleClickCategory(parentCategory.slug)
                            }
                        >
                            <div className={styles.parentLink}>
                                {parentCategory.name}
                            </div>
                            {parentCategory.children?.length > 0 && (
                                <span
                                    className={`${styles.chevron} ${openingCategories[parentCategory.id] ? styles.open : ''}`}></span>
                            )}
                        </div>

                        {parentCategory.children?.length > 0 && (
                            <ul className={`${styles.submenu} ${openingCategories[parentCategory.id] ? styles.show : ''}`}>
                                {parentCategory.children.map((childCategory) => (
                                    <li key={childCategory.id} className={styles.navItem}>
                                        {/* THÊM CLASS 'active' KHI slug TRÙNG VỚI URL */}
                                        <div
                                            className={`${styles.link} ${selectedCategorySlug === childCategory.slug ? styles.active : ''}`}
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                handleClickCategory(childCategory.slug)
                                            }}
                                        >
                                            {childCategory.name}
                                        </div>
                                    </li>
                                ))}
                            </ul>
                        )}
                    </li>
                ))}
            </ul>
        </aside>
    );
};

export default Sidebar;