// src/layouts/MainLayout.jsx
import React from 'react';
import { Outlet } from 'react-router-dom';
import Header from '../components/header/Header';
import ChatbotWidget from "../components/chatbot/ChatbotWidget";

const MainLayout = () => {
    return (
        <>
            <Header />
            <main>
                <Outlet />
                <ChatbotWidget />
            </main>
        </>
    );
};

export default MainLayout;