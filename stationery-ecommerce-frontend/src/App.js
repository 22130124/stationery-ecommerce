import React, {useEffect} from 'react';
import AppRoutes from "./routes/AppRoutes";
import {BrowserRouter} from "react-router-dom";
import {Toaster} from "react-hot-toast";
import 'antd/dist/reset.css';
import AppToaster from "./components/AppToaster";
import {useDispatch} from "react-redux";
import {getCurrent, logout} from "./redux/slices/authSlice";
import {getCart} from "./redux/slices/cartSlice";

function App() {
    const dispatch = useDispatch();

    useEffect(() => {
        const token = localStorage.getItem('token');

        if (token) {
            dispatch(getCurrent()).unwrap()
                .then(() => {
                    dispatch(getCart())
                })
                .catch(() => {
                    dispatch(logout())
                })
        }
    }, []);

    return (
        <BrowserRouter>
            <div className="App">
                <AppRoutes/>
                <AppToaster/>
            </div>
        </BrowserRouter>
    );
}

export default App;