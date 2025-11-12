import React, {useState, useEffect} from 'react';
import {Table} from 'antd';
import styles from './ProductManagementPage.module.scss';
import ProductFormModal from "../components/modals/ProductFormModal";
import {addProduct, getAllProducts, updateProduct} from "../../../api/productApi";

const ProductManagementPage = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [searchText, setSearchText] = useState('');
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [editingProduct, setEditingProduct] = useState(null);

    useEffect(() => {
        const fetchProducts = async () => {
            const data = await getAllProducts();
            setProducts(data.products);
        }
        fetchProducts();
    }, []);

    const showAddModal = () => {
        setIsModalVisible(true);
    };

    const handleModalClose = () => {
        setIsModalVisible(false);
    };

    const formatCurrency = (value) => {
        if (!value) return '';
        return value.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
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
                        src={record.defaultImage?.url}
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
                const {basePrice, discountPrice} = record.defaultVariant;
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
                {text: 'Đang bán', value: true},
                {text: 'Ngừng bán', value: false},
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
                    <button
                        className={`${styles.actionBtn} ${styles.editBtn}`}
                        onClick={() => {
                            setEditingProduct(record);
                            setIsModalVisible(true);
                        }}
                    >
                        Sửa
                    </button>
                    <button className={`${styles.actionButton} ${styles.deleteBtn}`}
                            onClick={() => console.log('Delete', record.id)}>Xóa
                    </button>
                </div>
            ),
        },
    ];

    const filteredData = products.filter(item =>
        item.name.toLowerCase().includes(searchText.toLowerCase()) ||
        item.code.toLowerCase().includes(searchText.toLowerCase())
    );

    // Thêm sản phẩm mới
    const handleAddProduct = async (values) => {
        console.log(values);
        try {
            const data = await addProduct(values);
            setProducts(prev => [...prev, data.product]);
            setIsModalVisible(false);
            console.log('Sản phẩm mới đã được tạo:', data.product);
        } catch (error) {
            console.error('Thêm sản phẩm thất bại:', error);
        }
    };

    // Cập nhật sản phẩm
    const handleEditProduct = async (values) => {
        if (!editingProduct) return;
        try {
            const data = await updateProduct(editingProduct.id, values);
            const updatedProduct = data.product;
            console.log("Updated product:", updatedProduct);
            setProducts(prev =>
                prev.map(p => (p.id === updatedProduct.id ? updatedProduct : p))
            );
            setIsModalVisible(false);
            setEditingProduct(null);
            console.log('Sản phẩm đã được cập nhật:', updatedProduct);
        } catch (error) {
            console.error('Cập nhật sản phẩm thất bại:', error);
        }
    };

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
                <button className={styles.addButton} onClick={showAddModal}>
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
                    scroll={{x: 1300}}
                    bordered
                />
            </div>
            {isModalVisible && (
                <ProductFormModal
                    visible={isModalVisible}
                    editingProduct={editingProduct}
                    onClose={() => {
                        setIsModalVisible(false);
                        setEditingProduct(null);
                    }}
                    onSubmit={editingProduct ? handleEditProduct : handleAddProduct}
                />
            )}
        </div>
    );
};

export default ProductManagementPage;