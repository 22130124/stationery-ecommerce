import {configureStore} from "@reduxjs/toolkit";
import authReducer from "../features/auth/slice/authSlice"

export const store = configureStore({
    reducer: {
        auth: authReducer,
    }
})