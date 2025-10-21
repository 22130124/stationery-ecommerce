import React from 'react';
import styles from './Pagination.module.scss';

const Pagination = ({ currentPage, totalPages, onPageChange }) => {
    // Nếu chỉ có 1 trang thì không hiển thị
    if (totalPages <= 1) {
        return null;
    }

    const pageNumbers = Array.from({ length: totalPages }, (_, i) => i + 1);

    return (
        <nav className={styles.pagination}>
            {/* Nút về trang đầu */}
            <button
                onClick={() => onPageChange(1)}
                disabled={currentPage === 1}
            >
                &laquo;&laquo;
            </button>

            {/* Nút lùi 1 trang */}
            <button
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 1}
            >
                &laquo;
            </button>

            {/* Danh sách các số trang */}
            {pageNumbers.map(number => (
                <button
                    key={number}
                    onClick={() => onPageChange(number)}
                    className={currentPage === number ? styles.active : ''}
                >
                    {number}
                </button>
            ))}

            {/* Nút tiến 1 trang */}
            <button
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage === totalPages}
            >
                &raquo;
            </button>

            {/* Nút đến trang cuối */}
            <button
                onClick={() => onPageChange(totalPages)}
                disabled={currentPage === totalPages}
            >
                &raquo;&raquo;
            </button>
        </nav>
    );
};

export default Pagination;