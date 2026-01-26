// src/features/admin-order-management/pages/OrderManagementPage.jsx
import React, {useEffect, useState} from 'react';
import {Table, Modal} from 'antd';
import styles from './OrderManagementPage.module.scss';
import toast from 'react-hot-toast';
import {cancelOrder, getAllOrders, updateOrderStatus} from '../../../api/orderApi';
import OrderDetailModal from "../components/OrderDetailModal";
import ShippingStatus from "../../../components/order/ShippingStatus";
import PaymentStatus from "../../../components/order/PaymentStatus";

const OrderManagementPage = () => {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [searchText, setSearchText] = useState("");
    const [selectedOrderCode, setSelectedOrderCode] = useState(null);

    const {confirm} = Modal;

    const getNextStatus = (current) => {
        switch (current) {
            case 'READY_TO_PICK':
                return 'SHIPPING';
            case 'SHIPPING':
                return 'DELIVERED';
            case 'WAITING_PAYMENT':
                return 'WAITING_PAYMENT';
            default:
                return null;
        }
    };

    const getShippingStatusText = (shippingStatus) => {
        switch (shippingStatus) {
            case 'SHIPPING':
                return 'Đang giao hàng';
            case 'DELIVERED':
                return 'Đã giao';
            default:
                return null;
        }
    }

    // Fetch data
    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            const res = await getAllOrders();
            if (!res) return setLoading(false);

            const normalized = res.map(item => ({
                ...item.order,
                profile: item.profile
            }));

            setData(normalized);
            setLoading(false);
        };

        fetchData();
    }, []);

    // Hàm hiển thị modal xác nhận hủy đơn hàng
    const showCancelConfirm = (order) => {
        if (order.shippingStatus === 'DELIVERED' || order.shippingStatus === 'CANCELLED') {
            toast.dismiss()
            toast.error('Trạng thái này không thể cập nhật thêm');
            return;
        }

        confirm({
            title: 'Hủy đơn hàng',
            content: `Bạn có chắc hủy đơn hàng #${order.code}?`,
            okText: 'Xác nhận',
            okType: 'danger',
            cancelText: 'Hủy',
            onOk: () => handleStatusChange(order.code, 'CANCELLED'),
        });
    };

    // Hàm hiển thị modal xác nhận cập nhật trạng thái đơn hàng
    const openStatusModal = (order) => {
        const next = getNextStatus(order.shippingStatus);

        if (!next) {
            toast.dismiss()
            toast.error('Trạng thái này không thể cập nhật thêm.');
            return;
        }

        if (next === 'WAITING_PAYMENT') {
            toast.dismiss()
            toast('Đơn hàng này đang chờ thanh toán');
            return;
        }

        confirm({
            title: 'Xác nhận cập nhật',
            content: `Xác nhận cập nhật trạng thái đơn hàng thành '${getShippingStatusText(next)}'?`,
            okText: 'Xác nhận',
            cancelText: 'Hủy',
            onOk: () => handleStatusChange(order.code, next),
        });
    };

    // Hàm cập nhật trạng thái đơn hàng
    const handleStatusChange = async (orderCode, newStatus) => {
        const res = await updateOrderStatus(orderCode, newStatus)
        if (!res) return
        setData(prev =>
            prev.map(o =>
                o.code === orderCode ? {...o, shippingStatus: newStatus} : o
            )
        );
        toast.dismiss()
        toast.success('Cập nhật trạng thái thành công');
    };

    // Hàm định dạng tiền tệ
    const formatCurrency = (value) => {
        return value?.toLocaleString('vi-VN', {
            style: 'currency',
            currency: 'VND',
        });
    };

    const filteredData = data.filter(order => {
        const text = searchText.toLowerCase();

        return (
            order.code.toLowerCase().includes(text) ||
            order.profile?.fullName?.toLowerCase().includes(text) ||
            order.profile?.email?.toLowerCase().includes(text) ||
            order.profile?.phone?.includes(text)
        );
    });


    const columns = [
        {
            title: 'Mã đơn',
            dataIndex: 'code',
            key: 'code',
        },
        {
            title: 'Khách hàng',
            key: 'customer',
            render: (_, record) => {
                const profile = record.profile;
                return (
                    <div>
                        <div style={{fontWeight: 600}}>
                            {profile?.fullName || '---'}
                        </div>
                        <div style={{fontSize: 12, color: '#666'}}>
                            {profile?.email}
                        </div>
                        <div style={{fontSize: 12, color: '#999'}}>
                            {profile?.phone}
                        </div>
                    </div>
                );
            },
        },
        {
            title: 'Tổng tiền',
            dataIndex: 'totalAmount',
            key: 'totalAmount',
            render: (value) => formatCurrency(value),
            sorter: (a, b) => a.totalAmount - b.totalAmount,
        },
        {
            title: 'Giao hàng',
            dataIndex: 'shippingStatus',
            key: 'shippingStatus',
            filters: [
                {text: 'Chờ thanh toán', value: 'WAITING_PAYMENT'},
                {text: 'Chờ lấy hàng', value: 'READY_TO_PICK'},
                {text: 'Đang giao', value: 'SHIPPING'},
                {text: 'Đã giao', value: 'DELIVERED'},
                {text: 'Đã hủy', value: 'CANCELLED'},
                {text: 'Hết hạn', value: 'EXPIRED'},
            ],
            onFilter: (value, record) => record.shippingStatus === value,
            render: (status) => <ShippingStatus status={status}/>
        },
        {
            title: 'Thanh toán',
            dataIndex: 'paymentStatus',
            key: 'paymentStatus',
            filters: [
                { text: 'Chưa thanh toán', value: 'UNPAID' },
                { text: 'Đã thanh toán', value: 'PAID' },
            ],
            onFilter: (value, record) => record.paymentStatus === value,
            render: (status) => <PaymentStatus status={status} />
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
                        onClick={() => setSelectedOrderCode(record.code)}
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
                    placeholder='Tìm theo mã đơn, tên, email, SĐT...'
                    onChange={(e) => setSearchText(e.target.value)}
                />
            </div>

            <Table
                columns={columns}
                dataSource={filteredData}
                rowKey='code'
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
                orderCode={selectedOrderCode}
                open={!!selectedOrderCode}
                onClose={() => setSelectedOrderCode(null)}
            />
        </div>
    );
};

export default OrderManagementPage;
