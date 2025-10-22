// src/components/common/AppToaster.jsx

import { Toaster } from 'react-hot-toast';

const toastOptions = {
    position: "top-center",
    style: {
        background: '#363636',
        color: '#fff',
        borderRadius: '10px',
        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
        fontSize: '15px',
        padding: '12px 18px',
    },

    success: {
        duration: 2500,
        iconTheme: {
            primary: '#10B981',
            secondary: '#FFFFFF',
        },
    },
    error: {
        duration: 4000,
        iconTheme: {
            primary: '#EF4444',
            secondary: '#FFFFFF',
        },
    },
};

const AppToaster = () => {
    return (
        <Toaster toastOptions={toastOptions} />
    );
};

export default AppToaster;