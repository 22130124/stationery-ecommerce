import React, {useEffect, useState} from 'react';
import {Table, Modal, Select} from 'antd';
import styles from './OrderManagementPage.module.scss';
import toast from 'react-hot-toast';
import {cancelOrder, getAllOrders, updateOrderStatus} from '../../../api/orderApi';
import OrderDetailModal from "../components/OrderDetailModal";
import OrderStatus from "../../../components/order/OrderStatus";

const OrderManagementPage = () => {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(false);
    const [searchText, setSearchText] = useState("");
    const [selectedOrder, setSelectedOrder] = useState(null);
    const [editOrder, setEditOrder] = useState(null);
    const [selectedOrderId, setSelectedOrderId] = useState(null);

    const {confirm} = Modal;

    const STATUS_TEXT = {
        1: 'Đang lấy hàng',
        2: 'Đang giao hàng',
        3: 'Đã giao',
        0: 'Đã hủy',
    };

    const STATUS_CLASS = {
        1: 'status-pending',
        2: 'status-shipping',
        3: 'status-done',
        0: 'status-cancel',
    };

    const getNextStatus = (current) => {
        switch (current) {
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return null;
            default:
                return null;
        }
    };

    // Fetch orders
    useEffect(() => {
        const fetchOrders = async () => {
            setLoading(true);
            const data = await getAllOrders();
            if (!data) {
                setLoading(false);
                return
            }
            setOrders(data);
            setLoading(false);
        }

        fetchOrders();
    }, []);

    const handleStatusChange = async (orderId, newStatus) => {
            const data = await updateOrderStatus(orderId, newStatus)
            if (!data) return
            setOrders(prev =>
                prev.map(o =>
                    o.id === orderId ? {...o, status: newStatus} : o
                )
            );
            toast.dismiss()
            toast.success('Cập nhật trạng thái thành công');
    };

    const showCancelConfirm = (order) => {
        if (order.status === 3 || order.status === 0) {
            toast.dismiss()
            toast.error('Trạng thái này không thể cập nhật thêm');
            return;
        }

        confirm({
            title: 'Hủy đơn hàng',
            content: `Bạn có chắc hủy đơn hàng #${order.id}?`,
            okText: 'Xác nhận',
            okType: 'danger',
            cancelText: 'Hủy',
            onOk: () => handleStatusChange(order.id, 0),
        });
    };

    const openStatusModal = (order) => {
        const next = getNextStatus(order.status);

        if (!next) {
            toast.dismiss()
            toast.error('Trạng thái này không thể cập nhật thêm.');
            return;
        }

        confirm({
            title: 'Xác nhận cập nhật',
            content: `Xác nhận cập nhật trạng thái đơn hàng thành '${STATUS_TEXT[next]}'?`,
            okText: 'Xác nhận',
            cancelText: 'Hủy',
            onOk: () => handleStatusChange(order.id, next),
        });
    };

    const formatCurrency = (value) => {
        return value?.toLocaleString('vi-VN', {
            style: 'currency',
            currency: 'VND',
        });
    };

    const filteredData = orders.filter(order =>
        order.id.toString().includes(searchText) ||
        order.accountId.toString().includes(searchText)
    );

    const columns = [
        {
            title: 'Mã đơn',
            dataIndex: 'id',
            key: 'id',
            sorter: (a, b) => a.id - b.id,
        },
        {
            title: 'Mã tài khoản',
            dataIndex: 'accountId',
            key: 'accountId',
        },
        {
            title: 'Tổng tiền',
            dataIndex: 'totalAmount',
            key: 'totalAmount',
            render: (value) => formatCurrency(value),
            sorter: (a, b) => a.totalAmount - b.totalAmount,
        },
        {
            title: 'Trạng thái',
            dataIndex: 'status',
            key: 'status',
            render: (status) => {
                return (
                    <OrderStatus status={status}/>
                )
            }
        },
        {
            title: 'Ngày tạo',
            dataIndex: 'createdAt',
            key: 'createdAt',
            render: (value) => new Date(value).toLocaleString('vi-VN'),
            sorter: (a, b) =>
                new Date(a.createdAt) - new Date(b.createdAt),
        },
        {
            title: 'Hành động',
            key: 'action',
            render: (_, record) => (
                <div className={styles.actions}>
                    <button
                        className={styles.detailBtn}
                        onClick={() => setSelectedOrderId(record.id)}
                    >
                        Chi tiết
                    </button>

                    <button
                        className={styles.updateBtn}
                        onClick={() => openStatusModal(record)}
                    >
                        Cập nhật
                    </button>

                    <button
                        className={styles.deleteBtn}
                        onClick={() => showCancelConfirm(record)}
                    >
                        Hủy đơn
                    </button>
                </div>
            ),
        },
    ];

    return (
        <div className={styles.pageContainer}>
            <header className={styles.header}>
                <h1>Quản lý Đơn hàng</h1>
                <p>Theo dõi và xử lý đơn hàng của khách hàng.</p>
            </header>

            <div className={styles.actionBar}>
                <input
                    type='text'
                    className={styles.searchInput}
                    placeholder='Tìm theo mã đơn hoặc mã tài khoản...'
                    onChange={(e) => setSearchText(e.target.value)}
                />
            </div>

            <Table
                columns={columns}
                dataSource={filteredData}
                rowKey='id'
                loading={loading}
                pagination={{
                    pageSize: 10,
                    showSizeChanger: true,
                    pageSizeOptions: ['10', '20', '50'],
                }}
                bordered
            />

            {/* Modal chi tiết đơn hàng */}
            <OrderDetailModal
                orderId={selectedOrderId}
                open={!!selectedOrderId}
                onClose={() => setSelectedOrderId(null)}
            />
        </div>
    );
};

export default OrderManagementPage;
