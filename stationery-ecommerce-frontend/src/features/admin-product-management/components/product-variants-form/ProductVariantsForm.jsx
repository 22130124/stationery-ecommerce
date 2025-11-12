import React from 'react';
import {Form, Input, InputNumber, Checkbox, Button, Space, Row, Col, Radio} from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import ProductImages from '../product-images/ProductImages';
import styles from './ProductVariantsForm.module.scss';

const ProductVariantsForm = ({ form, handleImagesUploadingChange }) => {
    // Hàm kiểm tra nếu giá khuyến mãi lớn hơn giá gốc
    const validateDiscountPrice = ({ getFieldValue }) => ({
        validator(_, value) {
            const fieldPath = _.field.split('.');
            if (fieldPath.length < 2) {
                return Promise.resolve();
            }
            const variantIndex = fieldPath[1];
            const basePrice = getFieldValue(['variants', variantIndex, 'basePrice']);

            if (value === null || value === undefined) {
                return Promise.resolve();
            }

            if (basePrice !== null && basePrice !== undefined && value > basePrice) {
                return Promise.reject(new Error('Giá khuyến mãi không được lớn hơn Giá gốc'));
            }

            // Giá khuyến mãi không được nhỏ hơn 0
            if (value < 0) {
                return Promise.reject(new Error('Giá khuyến mãi không được nhỏ hơn 0'));
            }

            return Promise.resolve();
        },
    });

    return (
        <div className={styles.container}>
            <h3 className={styles.heading}>
                Biến thể sản phẩm
            </h3>

            <Form.List name="variants">
                {(fields, { add, remove }) => (
                    <>
                        {fields.map(({ key, name, ...restField }) => (
                            <div key={key} className={styles.variantItem}>

                                {/* Nút xóa (chỉ hiển thị nếu có 2 biến thể trở lên*/}
                                {fields.length > 1 && (
                                    <MinusCircleOutlined
                                        onClick={() => remove(name)}
                                        className={styles.removeButton}
                                        title="Xóa biến thể"
                                    />
                                )}

                                <Row gutter={16}>
                                    {/* Tên biến thể */}
                                    <Col span={6}>
                                        <Form.Item
                                            {...restField}
                                            name={[name, 'name']}
                                            label="Tên biến thể"
                                            rules={[{ required: true, message: 'Thiếu tên biến thể' }]}
                                        >
                                            <Input placeholder="Ví dụ: Màu đỏ, Size L" />
                                        </Form.Item>
                                    </Col>


                                    {/* Base price */}
                                    <Col span={5}>
                                        <Form.Item
                                            {...restField}
                                            name={[name, 'basePrice']}
                                            label="Giá cơ bản"
                                            rules={[{ required: true, message: 'Thiếu giá cơ bản' }]}
                                        >
                                            <InputNumber
                                                min={0}
                                                addonAfter="VNĐ"
                                                style={{ width: '100%' }}
                                                formatter={(value) =>
                                                    `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                                                }
                                                parser={(value) => value.replace(/,/g, '')}
                                            />
                                        </Form.Item>
                                    </Col>

                                    <Col span={5}>
                                        <Form.Item
                                            {...restField}
                                            name={[name, 'discountPrice']}
                                            label="Giá khuyến mãi"
                                            rules={[validateDiscountPrice]}
                                        >
                                            <InputNumber
                                                min={0}
                                                addonAfter="VNĐ"
                                                style={{ width: '100%' }}
                                                formatter={(value) =>
                                                    `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
                                                }
                                                parser={(value) => value.replace(/,/g, '')}
                                            />
                                        </Form.Item>
                                    </Col>

                                    {/* isDefault và isActive */}
                                    <Col span={8}>
                                        <Form.Item label="Trạng thái">
                                            <Space>
                                                <Form.Item
                                                    {...restField}
                                                    name={[name, 'isDefault']}
                                                    valuePropName="checked"
                                                    noStyle
                                                >
                                                    <Radio
                                                        onChange={(e) => {
                                                            // Logic đảm bảo chỉ 1 Radio được chọn
                                                            if (!e.target.checked) return;

                                                            const variants = form.getFieldValue('variants') || [];
                                                            const updated = variants.map((v, i) => ({
                                                                ...v,
                                                                isDefault: i === name,
                                                            }));
                                                            form.setFieldsValue({ variants: updated });
                                                        }}
                                                    >
                                                        Mặc định
                                                    </Radio>
                                                </Form.Item>

                                                <Form.Item
                                                    {...restField}
                                                    name={[name, 'isActive']}
                                                    valuePropName="checked"
                                                    noStyle
                                                >
                                                    <Checkbox>Hoạt động</Checkbox>
                                                </Form.Item>
                                            </Space>
                                        </Form.Item>
                                    </Col>
                                </Row>

                                <Form.Item
                                    {...restField}
                                    name={[name, 'images']}
                                    label="Ảnh biến thể (riêng)"
                                    style={{ marginTop: '10px' }}
                                >
                                    {/* ProductImages */}
                                    <ProductImages onUploadingChange={handleImagesUploadingChange}/>
                                </Form.Item>
                            </div>
                        ))}
                        <Form.Item>
                            <Button
                                type="dashed"
                                onClick={() => add({ name: '', basePrice: null, discountPrice: null, isActive: true, isDefault: false, images: [] })}
                                block
                                icon={<PlusOutlined />}
                            >
                                Thêm biến thể
                            </Button>
                        </Form.Item>
                    </>
                )}
            </Form.List>
        </div>
    );
};

export default ProductVariantsForm;