import React, {useState, useEffect} from 'react'
import {Table, Modal} from 'antd'
import styles from './ProductManagementPage.module.scss'
import ProductFormModal from '../components/modals/ProductFormModal'
import {addProduct, deleteProduct, getAllProducts, updateProduct} from '../../../api/productApi'
import toast from 'react-hot-toast'
import InventoryModal from '../components/modals/InventoryModal'

const ProductManagementPage = () => {
    const [products, setProducts] = useState([])
    const [loading, setLoading] = useState(false)
    const [searchText, setSearchText] = useState('')
    const [isModalVisible, setIsModalVisible] = useState(false)
    const [editingProduct, setEditingProduct] = useState(null)
    const [isInventoryModalVisible, setIsInventoryModalVisible] = useState(false)

    const {confirm} = Modal

    // Hàm fetch products khi mới vào trang
    useEffect(() => {
        const fetchProducts = async () => {
            setLoading(true)
            const data = await getAllProducts()
            if (!data) {
                setLoading(false)
                return
            }
            setProducts(data.products)
            setLoading(false)
        }
        fetchProducts()
    }, [])

    console.log(products)

    const showCreateModal = () => {
        setIsModalVisible(true)
    }

    // Hàm xử lý thêm sản phẩm mới
    const handleCreateProduct = async (values) => {
        try {
            toast.dismiss()
            toast.loading('Đang xử lý thêm sản phẩm. Vui lòng đợi...', {
                toasterId: 'loading'
            })
            const data = await addProduct(values)
            if (!data) return
            console.log('createdProduct:', data.product)
            setProducts(prev => [...prev, data.product])
            setIsModalVisible(false)
            toast.success('Lưu thông tin sản phẩm thành công')
        } catch (error) {
            toast.dismiss('loading')
        }
    }

    // Hàm xử lý sửa thông tin sản phẩm
    const handleUpdateProduct = async (values) => {
        if (!editingProduct) return
        toast.loading('Đang xử lý cập nhật thông tin sản phẩm. Vui lòng đợi...', {
            toasterId: 'loading'
        })
        try {
            const data = await updateProduct(editingProduct.id, values)
            const updatedProduct = data.updatedProduct
            setProducts(prev =>
                prev.map(p => (p.id === updatedProduct.id ? updatedProduct : p))
            )
            setIsModalVisible(false)
            setEditingProduct(null)
            toast.success('Lưu thông tin sản phẩm thành công')
        } catch (error) {
        }
    }

    const handleDeleteProduct = async (id) => {
        const response = await deleteProduct(id)
        toast.dismiss()
        setProducts(prev =>
            prev.filter(p => p.id !== id)
        )
        toast.success('Xóa sản phẩm thành công', {duration: 3000})
    }

    const showDeleteConfirm = (product, onOk) => {
        confirm({
            title: 'Xóa sản phẩm',
            content: `Bạn có chắc chắn muốn xóa sản phẩm '${product.name}' không?`,
            okText: 'Xác nhận',
            okType: 'danger',
            cancelText: 'Hủy',
            onOk,
        })
    }

    const showInventoryModal = (product) => {
        setEditingProduct(product) // dùng luôn editingProduct
        setIsInventoryModalVisible(true)
    }

    // Hàm chỉnh định dạng tiền VND
    const formatCurrency = (value) => {
        if (!value) return ''
        return value.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'})
    }

    // Helper function để tạo options cho bộ lọc từ dữ liệu
    const getColumnFilterOptions = (dataIndex) => {
        if (!products) return []
        const uniqueValues = [...new Set(products.map(p => p[dataIndex]?.name).filter(Boolean))]
        return uniqueValues.map(value => ({
            text: value,
            value: value,
        }))
    }

    const filteredData = products.filter(item =>
        (item.name?.toLowerCase().includes(searchText.toLowerCase()) || false) ||
        (item.code?.toLowerCase().includes(searchText.toLowerCase()) || false)
    )

    // Định nghĩa các cột cho bảng
    const columns = [
        {
            title: 'Mã SP',
            dataIndex: 'code',
            key: 'code',
            sorter: (a, b) => a.code.localeCompare(b.code),
        },
        {
            title: 'Sản phẩm',
            dataIndex: 'name',
            key: 'name',
            width: 500,
            ellipsis: true,
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
            title: 'Nhà cung cấp',
            dataIndex: ['supplier', 'name'],
            key: 'supplier',
            filters: getColumnFilterOptions('supplier'),
            onFilter: (value, record) => record.supplier.name === value,
        },
        {
            title: 'Giá bán',
            key: 'price',
            sorter: (a, b) => {
                const priceA = a.defaultVariant.discountPrice || a.defaultVariant.basePrice
                const priceB = b.defaultVariant.discountPrice || b.defaultVariant.basePrice
                return priceA - priceB
            },
            render: (_, record) => {
                const {basePrice, discountPrice} = record.defaultVariant
                return (
                    <div className={styles.priceCell}>
                        <span className={styles.currentPrice}>{formatCurrency(discountPrice || basePrice)}</span>
                        {discountPrice && (
                            <span className={styles.originalPrice}>{formatCurrency(basePrice)}</span>
                        )}
                    </div>
                )
            }
        },
        {
            title: 'Tồn kho',
            key: 'stock',
            render: (_, record) => (
                <span>{record.totalStock}</span>
            ),
            sorter: (a, b) => a.totalStock - b.totalStock
        },
        {
            title: 'Trạng thái',
            dataIndex: 'activeStatus',
            key: 'status',
            filters: [
                {text: 'Đang bán', value: true},
                {text: 'Ngừng bán', value: false},
            ],
            onFilter: (value, record) => record.activeStatus === value,
            render: (activeStatus) => (
                <span className={activeStatus ? styles.activeStatus : styles.inactiveStatus}>
                    {activeStatus ? 'Đang bán' : 'Ngừng bán'}
                </span>
            ),
        },
        {
            title: 'Hành động',
            key: 'action',
            fixed: 'right',
            width: 250,
            render: (_, record) => (
                <div className={styles.actions}>
                    <button
                        className={`${styles.actionBtn} ${styles.editBtn}`}
                        onClick={() => {
                            setEditingProduct(record)
                            setIsModalVisible(true)
                        }}
                    >
                        Chi tiết
                    </button>
                    <button
                        className={styles.actionBtn}
                        onClick={() => showInventoryModal(record)}
                    >
                        Quản lý kho
                    </button>
                    <button
                        className={`${styles.actionButton} ${styles.deleteBtn}`}
                        onClick={() => showDeleteConfirm(record, () => handleDeleteProduct(record.id))}
                    >
                        Xóa
                    </button>
                </div>
            ),
        },
    ]

    return (
        <div className={styles.pageContainer}>
            <header className={styles.header}>
                <h1>Quản lý Sản phẩm</h1>
                <p>Quản lý, thêm mới và cập nhật thông tin sản phẩm của bạn.</p>
            </header>

            <div className={styles.actionBar}>
                <input
                    type='text'
                    className={styles.searchInput}
                    placeholder='Tìm kiếm theo tên, mã sản phẩm...'
                    onChange={e => setSearchText(e.target.value)}
                />
                <button className={styles.addButton} onClick={showCreateModal}>
                    + Thêm sản phẩm
                </button>
            </div>

            <div className={styles.tableWrapper}>
                <Table
                    columns={columns}
                    dataSource={filteredData}
                    rowKey='id'
                    loading={loading}
                    pagination={{
                        pageSize: 10,
                        showSizeChanger: true,
                        pageSizeOptions: ['10', '20', '50'],
                        showTotal: (total, range) => `${range[0]}-${range[1]} của ${total} sản phẩm`
                    }}
                    scroll={{x: 'max-content'}}
                    bordered
                />
            </div>
            {isModalVisible && (
                <ProductFormModal
                    visible={isModalVisible}
                    editingProduct={editingProduct}
                    onClose={() => {
                        setIsModalVisible(false)
                        setEditingProduct(null)
                    }}
                    onSubmit={editingProduct ? handleUpdateProduct : handleCreateProduct}
                />
            )}
            {isInventoryModalVisible && editingProduct && (
                <InventoryModal
                    open={isInventoryModalVisible}
                    product={editingProduct}
                    onClose={() => {
                        setIsInventoryModalVisible(false)
                        setEditingProduct(null)
                    }}
                    onUpdate={(updatedVariants) => {
                        setProducts(prev =>
                            prev.map(p =>
                                p.id === editingProduct.id
                                    ? {
                                        ...p,
                                        variants: p.variants.map(v => {
                                            const updated = updatedVariants.find(u => u.id === v.id)
                                            if (!updated) return v

                                            let newStock = v.stock
                                            switch (updated.changeType) {
                                                case 'replace':
                                                    newStock = updated.quantity
                                                    break
                                                case 'increase':
                                                    newStock = v.stock + updated.quantity
                                                    break
                                                case 'decrease':
                                                    newStock = v.stock - updated.quantity
                                                    break
                                            }

                                            return {
                                                ...v,
                                                stock: newStock
                                            }
                                        }),
                                        totalStock: updatedVariants.reduce((sum, u) => {
                                            const original = p.variants.find(v => v.id === u.id)
                                            let newStock = original?.stock || 0
                                            switch (u.changeType) {
                                                case 'replace':
                                                    newStock = u.quantity;
                                                    break
                                                case 'increase':
                                                    newStock += u.quantity;
                                                    break
                                                case 'decrease':
                                                    newStock -= u.quantity;
                                                    break
                                            }
                                            return sum + newStock
                                        }, 0)
                                    }
                                    : p
                            )
                        )

                        setEditingProduct(null)
                    }}

                />
            )}

        </div>
    )
}

export default ProductManagementPage