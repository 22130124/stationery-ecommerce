import React, {useState, useEffect} from 'react';
import {Modal, Form, Input, Select, TreeSelect, Upload} from 'antd';
import {getCategories} from "../../../../api/categoryApi";
import {getSuppliers} from "../../../../api/supplierApi";
import {getBrandsBySupplierId} from "../../../../api/brandApi";
import styles from "./AddProductModal.module.scss"
import ProductImages from "../product-images/ProductImages";

const {Option} = Select;

const AddProductModal = ({visible, onClose, onSubmit}) => {
    const [form] = Form.useForm();
    const [selectedSupplierId, setSelectedSupplierId] = useState(null);
    const [categories, setCategories] = useState([])
    const [suppliers, setSuppliers] = useState([]);
    const [brands, setBrands] = useState([]);

    // Fetch các dữ liệu ban đầu cho form
    useEffect(() => {
        const fetchInitialData = async () => {
            const categoryData = await getCategories();
            const supplierData = await getSuppliers();
            setCategories(categoryData.categories);
            setSuppliers(supplierData.suppliers);
        };
        fetchInitialData();
    }, [])

    // Fetch danh sách brands dựa trên supplier id
    useEffect(() => {
        const fetchBrands = async () => {
            if (!selectedSupplierId) {
                setBrands([]);
                return;
            }
            const data = await getBrandsBySupplierId(selectedSupplierId);
            setBrands(data.brands || []);
        };
        fetchBrands();
    }, [selectedSupplierId]);

    // Xử lý logic khi chọn supplier
    const handleSupplierChange = (value) => {
        setSelectedSupplierId(value);
        form.setFieldsValue({brandId: undefined});
    };

    // Hàm chuyển đổi dữ liệu category sang định dạng của TreeSelect
    const transformCategories = (nodes) => {
        return nodes.map(node => ({
            title: node.name,
            value: node.id,
            // Chỉ cho phép chọn nút lá
            selectable: !node.children || node.children.length === 0,
            children: node.children ? transformCategories(node.children) : [],
        }));
    };

    const categoryTreeData = transformCategories(categories);

    return (
        <Modal
            title="Thêm sản phẩm mới"
            open={visible}
            onCancel={onClose}
            onOk={() => form.submit()}
            okText="Tạo sản phẩm"
            cancelText="Hủy"
            width={800}
        >
            <Form
                form={form}
                layout="vertical"
                onFinish={onSubmit}
                initialValues={{name: '', slug: ''}}
            >
                <Form.Item
                    name="name"
                    label="Tên sản phẩm"
                    rules={[{required: true, message: 'Vui lòng nhập tên sản phẩm'}]}
                >
                    <Input/>
                </Form.Item>

                <Form.Item
                    name="slug"
                    label="Slug"
                    rules={[{required: true, message: 'Vui lòng nhập slug'}]}
                >
                    <Input/>
                </Form.Item>

                <Form.Item
                    name="supplierId"
                    label="Nhà cung cấp"
                    rules={[{required: true, message: 'Vui lòng chọn nhà cung cấp'}]}
                >
                    <Select
                        showSearch
                        placeholder="Chọn hoặc tìm kiếm nhà cung cấp"
                        onChange={handleSupplierChange}
                        filterOption={(input, option) =>
                            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                    >
                        {suppliers.map(supplier => (
                            <Option key={supplier.id} value={supplier.id}>{supplier.name}</Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item
                    name="brandId"
                    label="Thương hiệu"
                    rules={[{required: true, message: 'Vui lòng chọn thương hiệu'}]}
                >
                    <Select
                        showSearch
                        placeholder="Chọn hoặc tìm kiếm thương hiệu"
                        disabled={!selectedSupplierId} // Vô hiệu hóa khi chưa chọn supplier
                        filterOption={(input, option) =>
                            option?.children?.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }
                        notFoundContent={selectedSupplierId ? "Không có thương hiệu nào" : "Chọn nhà cung cấp trước"}
                    >
                        {brands.map((brand) => (
                            <Option key={brand.id} value={brand.id}>
                                {brand.name}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>

                <Form.Item
                    name="categoryId"
                    label="Danh mục"
                    rules={[{required: true, message: 'Vui lòng chọn danh mục'}]}
                >
                    <TreeSelect
                        treeData={categoryTreeData}
                        placeholder="Chọn danh mục cho sản phẩm"
                        allowClear
                        treeExpandAction="click"
                    />
                </Form.Item>

                <Form.Item name="images"
                           label="Ảnh sản phẩm (chung)">
                    <ProductImages/>
                </Form.Item>
            </Form>
        </Modal>
    );
};

export default AddProductModal;