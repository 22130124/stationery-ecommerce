import React, {useState, useEffect} from 'react'
import {Modal, Form, Input, Select, TreeSelect, Upload} from 'antd'
import ReactQuill from 'react-quill'
import 'react-quill/dist/quill.snow.css'
import countries from 'i18n-iso-countries'
import viLocale from 'i18n-iso-countries/langs/vi.json'
import {getActiveCategories} from '../../../../api/categoryApi'
import {getSuppliers} from '../../../../api/supplierApi'
import {getBrandsBySupplierId} from '../../../../api/brandApi'
import ProductImages from '../product-images/ProductImages'
import ProductVariantsForm from '../product-variants-form/ProductVariantsForm'
import styles from './ProductFormModal.module.scss'

const {Option} = Select

const ProductFormModal = ({visible, onClose, onSubmit, editingProduct}) => {
    const [form] = Form.useForm()
    const [selectedSupplierId, setSelectedSupplierId] = useState(null)
    const [categories, setCategories] = useState([])
    const [suppliers, setSuppliers] = useState([])
    const [brands, setBrands] = useState([])

    countries.registerLocale(viLocale)
    const countryNames = Object.values(countries.getNames('vi'))

    const [imagesUploading, setImagesUploading] = useState(false)

    // Nếu là chế độ chỉnh sửa thông tin sản phẩm thì gán thông tin editingProduct cho form
    useEffect(() => {
        if (editingProduct) {
            form.resetFields()
            form.setFieldsValue(editingProduct)

            setSelectedSupplierId(editingProduct.supplier.id);

            // Gán brand của editingProduct vào brands luôn
            setBrands([editingProduct.brand]);

            // Set các giá trị khác cho form
            form.setFieldsValue({
                brandId: editingProduct.brand.id,
                supplierId: editingProduct.supplier.id,
                categoryId: editingProduct.category.id,
                origin: editingProduct.origin,
            });
        } else {
            form.resetFields();
            setSelectedSupplierId(null);
            setBrands([]);
        }
    }, [editingProduct]);

    // Fetch các dữ liệu ban đầu cho form
    useEffect(() => {
        const fetchInitialData = async () => {
            const categoryData = await getActiveCategories()
            const supplierData = await getSuppliers()
            setCategories(categoryData)
            setSuppliers(supplierData)
        }
        fetchInitialData()
    }, [])

    // Fetch danh sách brands dựa trên supplier id
    useEffect(() => {
        const fetchBrands = async () => {
            if (!selectedSupplierId) {
                setBrands([])
                return
            }
            const data = await getBrandsBySupplierId(selectedSupplierId)
            setBrands(data || [])
        }
        fetchBrands()
    }, [selectedSupplierId])

    // Xử lý logic khi chọn supplier
    const handleSupplierChange = (value) => {
        setSelectedSupplierId(value)
        form.setFieldsValue({brandId: undefined})
    }

    // Hàm chuyển đổi dữ liệu category sang định dạng của TreeSelect
    const transformCategories = (nodes) => {
        return nodes.map(node => ({
            title: node.name,
            value: node.id,
            // Chỉ cho phép chọn nút lá
            selectable: !node.children || node.children.length === 0,
            children: node.children ? transformCategories(node.children) : [],
        }))
    }

    const categoryTreeData = transformCategories(categories)

    const handleClose = () => {
        onClose?.()
        form.resetFields()
    }

    // Hàm xử lý callback từ ProductImages
    const handleImagesUploadingChange = (uploading) => {
        setImagesUploading(uploading)
    }

    return (
        <Modal
            title={editingProduct ? 'Chi tiết sản phẩm' : 'Thêm sản phẩm mới'}
            open={visible}
            onCancel={handleClose}
            footer={null}
            width={800}
        >
            <Form
                form={form}
                layout='vertical'
                onFinish={onSubmit}
                initialValues={{
                    name: '',
                    slug: '',
                    variants: [
                        {
                            name: '',
                            basePrice: null,
                            discountPrice: null,
                            color: null,
                            activeStatus: true,
                            defaultStatus: true,
                            images: [],
                        },
                    ],
                }}
            >
                <Form.Item
                    name='name'
                    label='Tên sản phẩm'
                    rules={[{required: true, message: 'Vui lòng nhập tên sản phẩm'}]}
                >
                    <Input/>
                </Form.Item>

                <Form.Item
                    name='slug'
                    label='Slug'
                    rules={[{required: true, message: 'Vui lòng nhập slug'}]}
                >
                    <Input/>
                </Form.Item>

                <Form.Item
                    name='supplierId'
                    label='Nhà cung cấp'
                    rules={[{required: true, message: 'Vui lòng chọn nhà cung cấp'}]}
                >
                    <Select
                        showSearch
                        placeholder='Chọn hoặc tìm kiếm nhà cung cấp'
                        onChange={handleSupplierChange}
                        value={selectedSupplierId}
                    >
                        {suppliers.map(supplier => (
                            <Option key={supplier.id} value={supplier.id}>
                                {supplier.name}  {/* Hiển thị tên nhà cung cấp */}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item
                    name='brandId'
                    label='Thương hiệu'
                    rules={[{required: true, message: 'Vui lòng chọn thương hiệu'}]}
                >
                    <Select
                        showSearch
                        placeholder='Chọn hoặc tìm kiếm thương hiệu'
                        disabled={!selectedSupplierId} // Vô hiệu hóa khi chưa chọn supplier
                        filterOption={(input, option) =>
                            option?.children?.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                        notFoundContent={selectedSupplierId ? 'Không có thương hiệu nào' : 'Chọn nhà cung cấp trước'}
                    >
                        {brands.map((brand) => (
                            <Option key={brand.id} value={brand.id}>
                                {brand.name}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item
                    name='categoryId'
                    label='Danh mục'
                    rules={[{required: true, message: 'Vui lòng chọn danh mục'}]}
                >
                    <TreeSelect
                        treeData={categoryTreeData}
                        placeholder='Chọn danh mục cho sản phẩm'
                        allowClear
                        treeExpandAction='click'
                    />
                </Form.Item>

                <Form.Item
                    name='origin'
                    label='Xuất xứ'
                    rules={[{required: true, message: 'Vui lòng chọn xuất xứ'}]}
                >
                    <Select
                        showSearch
                        placeholder='Chọn quốc gia'
                        filterOption={(input, option) =>
                            option.children.toLowerCase().includes(input.toLowerCase())
                        }
                    >
                        {countryNames.map((country) => (
                            <Option key={country} value={country}>
                                {country}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item
                    name='description'
                    label='Mô tả sản phẩm'
                >
                    <ReactQuill
                        theme='snow'
                        placeholder='Nhập mô tả sản phẩm...'
                    />
                </Form.Item>

                {/*<Form.Item name='images'*/}
                {/*           label='Ảnh sản phẩm (chung)'>*/}
                {/*    <ProductImages*/}
                {/*        value={form.getFieldValue('images')}*/}
                {/*        onChange={(imgs) => form.setFieldsValue({images: imgs})}*/}
                {/*        onUploadingChange={handleImagesUploadingChange}*/}
                {/*        allowSetDefault={true}*/}
                {/*    />*/}
                {/*</Form.Item>*/}

                <ProductVariantsForm form={form} handleImagesUploadingChange={handleImagesUploadingChange}/>

                <Form.Item>
                    <div style={{display: 'flex', justifyContent: 'flex-end', gap: '8px'}}>
                        <button id={styles.cancelBtn} type='button' onClick={handleClose}>Hủy</button>
                        <button id={styles.submitBtn}
                                type='submit'
                                disabled={imagesUploading}
                        >
                            {imagesUploading ? 'Đang tải ảnh...' : (editingProduct ? 'Lưu thay đổi' : 'Tạo sản phẩm')}
                        </button>
                    </div>
                </Form.Item>
            </Form>
        </Modal>
    )
}

export default ProductFormModal