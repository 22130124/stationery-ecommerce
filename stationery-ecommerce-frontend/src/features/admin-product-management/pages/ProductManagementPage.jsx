import React, { useState, useEffect } from 'react';
import { Table } from 'antd';
import styles from './ProductManagementPage.module.scss';

const ProductManagementPage = () => {
    const [products, setProducts] = useState([
        {
            "id": 1,
            "code": "SP1001",
            "name": "Bút bi Thiên Long TL-027",
            "category": { "name": "Bút bi" },
            "brand": { "name": "Thiên Long" },
            "isActive": true,
            "defaultVariant": {
                "basePrice": 5000,
                "discountPrice": 4500
            },
            "defaultImage": { "url": "https://placehold.co/60x60/3498db/ffffff.png?text=SP1001" }
        },
        {
            "id": 2,
            "code": "SP1002",
            "name": "Bút bi gel Zebra Sarasa Clip 0.5mm",
            "category": { "name": "Bút bi" },
            "brand": { "name": "Zebra" },
            "isActive": true,
            "defaultVariant": {
                "basePrice": 25000,
                "discountPrice": 22000
            },
            "defaultImage": { "url": "https://placehold.co/60x60/34495e/ffffff.png?text=SP1002" }
        },
        {
            "id": 3,
            "code": "SP1003",
            "name": "Bút bi Pilot Super Grip",
            "category": { "name": "Bút bi" },
            "brand": { "name": "Pilot" },
            "isActive": false,
            "defaultVariant": {
                "basePrice": 14000,
                "discountPrice": null
            },
            "defaultImage": { "url": "https://placehold.co/60x60/3498db/ffffff.png?text=SP1003" }
        },
        {
            "id": 6,
            "code": "SP1006",
            "name": "Bút chì gỗ Staedtler Noris 120-2B",
            "category": { "name": "Bút chì" },
            "brand": { "name": "Staedtler" },
            "isActive": true,
            "defaultVariant": {
                "basePrice": 8000,
                "discountPrice": null
            },
            "defaultImage": { "url": "https://placehold.co/60x60/f1c40f/000000.png?text=SP1006" }
        },
        {
            "id": 10,
            "code": "SP1010",
            "name": "Bút chì bấm Tombow Monograph 0.5mm",
            "category": { "name": "Bút chì" },
            "brand": { "name": "Tombow" },
            "isActive": true,
            "defaultVariant": {
                "basePrice": 135000,
                "discountPrice": null
            },
            "defaultImage": { "url": "https://placehold.co/60x60/7f8c8d/ffffff.png?text=SP1010" }
        },
        {
            "id": 11,
            "code": "SP1011",
            "name": "Bút lông dầu Thiên Long PM-09",
            "category": { "name": "Bút lông" },
            "brand": { "name": "Thiên Long" },
            "isActive": true,
            "defaultVariant": {
                "basePrice": 9000,
                "discountPrice": null
            },
            "defaultImage": { "url": "https://placehold.co/60x60/2c3e50/ffffff.png?text=SP1011" }
        }
    ]);
    const [loading, setLoading] = useState(false);
    const [searchText, setSearchText] = useState('');

    const formatCurrency = (value) => {
        if (!value) return '';
        return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    };

    // Helper function để tạo options cho bộ lọc từ dữ liệu
    const getColumnFilterOptions = (dataIndex) => {
        if (!products) return [];
        const uniqueValues = [...new Set(products.map(p => p[dataIndex]?.name))];
        return uniqueValues.map(value => ({
            text: value,
            value: value,
        }));
    };

    // Định nghĩa các cột cho bảng
    const columns = [
        {
            title: 'Sản phẩm',
            dataIndex: 'name',
            key: 'name',
            render: (text, record) => (
                <div className={styles.productInfo}>
                    <img
                        className={styles.productImage}
                        src={record.defaultImage?.url || 'https://placehold.co/60x60/cccccc/ffffff.png?text=N/A'}
                        alt={text}
                    />
                    <span className={styles.productName}>{text}</span>
                </div>
            ),
        },
        {
            title: 'Mã SP',
            dataIndex: 'code',
            key: 'code',
            sorter: (a, b) => a.code.localeCompare(b.code),
        },
        {
            title: 'Danh mục',
            dataIndex: ['category', 'name'],
            key: 'category',
            filters: getColumnFilterOptions('category'),
            onFilter: (value, record) => record.category.name === value,
        },
        {
            title: 'Thương hiệu',
            dataIndex: ['brand', 'name'],
            key: 'brand',
            filters: getColumnFilterOptions('brand'),
            onFilter: (value, record) => record.brand.name === value,
        },
        {
            title: 'Giá bán',
            key: 'price',
            sorter: (a, b) => {
                const priceA = a.defaultVariant.discountPrice || a.defaultVariant.basePrice;
                const priceB = b.defaultVariant.discountPrice || b.defaultVariant.basePrice;
                return priceA - priceB;
            },
            render: (_, record) => {
                const { basePrice, discountPrice } = record.defaultVariant;
                return (
                    <div className={styles.priceCell}>
                        <span className={styles.currentPrice}>{formatCurrency(discountPrice || basePrice)}</span>
                        {discountPrice && (
                            <span className={styles.originalPrice}>{formatCurrency(basePrice)}</span>
                        )}
                    </div>
                );
            }
        },
        {
            title: 'Trạng thái',
            dataIndex: 'isActive',
            key: 'status',
            filters: [
                { text: 'Đang bán', value: true },
                { text: 'Ngừng bán', value: false },
            ],
            onFilter: (value, record) => record.isActive === value,
            render: (isActive) => (
                <span className={isActive ? styles.activeStatus : styles.inactiveStatus}>
                    {isActive ? 'Đang bán' : 'Ngừng bán'}
                </span>
            ),
        },
        {
            title: 'Hành động',
            key: 'action',
            render: (_, record) => (
                <div className={styles.actions}>
                    <button className={styles.actionButton} onClick={() => console.log('Edit', record.id)}>Sửa</button>
                    <button className={`${styles.actionButton} ${styles.deleteButton}`} onClick={() => console.log('Delete', record.id)}>Xóa</button>
                </div>
            ),
        },
    ];

    const filteredData = products.filter(item =>
        item.name.toLowerCase().includes(searchText.toLowerCase()) ||
        item.code.toLowerCase().includes(searchText.toLowerCase())
    );

    return (
        <div className={styles.pageContainer}>
            <header className={styles.header}>
                <h1>Quản lý Sản phẩm</h1>
                <p>Quản lý, thêm mới và cập nhật thông tin sản phẩm của bạn.</p>
            </header>

            <div className={styles.actionBar}>
                <input
                    type="text"
                    className={styles.searchInput}
                    placeholder="Tìm kiếm theo tên, mã sản phẩm..."
                    onChange={e => setSearchText(e.target.value)}
                />
                <button className={styles.addButton}>
                    + Thêm sản phẩm
                </button>
            </div>

            <div className={styles.tableWrapper}>
                <Table
                    columns={columns}
                    dataSource={filteredData}
                    rowKey="id"
                    loading={loading}
                    pagination={{
                        pageSize: 10,
                        showSizeChanger: true,
                        pageSizeOptions: ['10', '20', '50'],
                        showTotal: (total, range) => `${range[0]}-${range[1]} của ${total} sản phẩm`
                    }}
                    scroll={{ x: 1300 }}
                    bordered
                />
            </div>
        </div>
    );
};

export default ProductManagementPage;