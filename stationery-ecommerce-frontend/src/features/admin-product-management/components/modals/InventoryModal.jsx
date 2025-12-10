import {Modal, Table, InputNumber, Select, Button} from 'antd'
import {useState, useEffect} from 'react'
import {decreaseInventory, increaseInventory, updateInventory} from "../../../../api/productApi"
import toast from "react-hot-toast";

const InventoryModal = ({open, product, onClose, onUpdate}) => {
    const [data, setData] = useState([])

    useEffect(() => {
        if (product) {
            setData(product.variants.map(v => ({
                key: v.id,
                id: v.id,
                name: v.name,
                currentStock: v.stock,
                changeType: 'replace',
                quantity: v.stock
            })))
        }
    }, [product])

    const handleChangeType = (id, value) => {
        setData(prev => prev.map(d => d.id === id ? {...d, changeType: value} : d))
    }

    const handleQuantityChange = (id, value) => {
        setData(prev => prev.map(d => d.id === id ? {...d, quantity: value} : d))
    }

    const handleSave = async () => {
        try {
            const replace = data.filter(d => d.changeType === 'replace')
            const increase = data.filter(d => d.changeType === 'increase')
            const decrease = data.filter(d => d.changeType === 'decrease')


            for (const v of replace) await updateInventory({
                variantId: v.id,
                quantity: v.quantity,
            })
            for (const v of increase) await increaseInventory({
                variantId: v.id,
                quantity: v.quantity,
            })
            for (const v of decrease) await decreaseInventory({
                variantId: v.id,
                quantity: v.quantity,
            })

            toast.success("Cập nhật thành công")
            onUpdate(data)
            onClose()
        } catch (err) {
            console.error(err)
            toast.error("Cập nhật thất bại. Hãy thử lại sau")
        }
    }

    const columns = [
        {title: 'Tên biến thể', dataIndex: 'name', key: 'name'},
        {title: 'Tồn kho hiện tại', dataIndex: 'currentStock', key: 'currentStock'},
        {
            title: 'Loại thay đổi',
            dataIndex: 'changeType',
            key: 'changeType',
            render: (_, record) => (
                <Select value={record.changeType} onChange={(val) => handleChangeType(record.id, val)}>
                    <Select.Option value="replace">Gán số lượng</Select.Option>
                    <Select.Option value="increase">Nhập thêm</Select.Option>
                    <Select.Option value="decrease">Trừ bớt</Select.Option>
                </Select>
            )
        },
        {
            title: 'Số lượng',
            dataIndex: 'quantity',
            key: 'quantity',
            render: (_, record) => (
                <InputNumber min={0} value={record.quantity} onChange={(val) => handleQuantityChange(record.id, val)}/>
            )
        }
    ]

    return (
        <Modal
            open={open}
            title={`Quản lý kho: ${product?.name}`}
            onCancel={onClose}
            footer={[
                <Button key="cancel" onClick={onClose}>Hủy</Button>,
                <Button key="save" type="primary" onClick={handleSave}>Lưu</Button>
            ]}
            width={700}
        >
            <Table
                columns={columns}
                dataSource={data}
                pagination={false}
                rowKey="id"
                scroll={{y: 300}}
            />
        </Modal>
    )
}

export default InventoryModal