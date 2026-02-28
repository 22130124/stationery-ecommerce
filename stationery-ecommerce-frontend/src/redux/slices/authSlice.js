import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import * as authApi from "../../api/authApi"

export const login = createAsyncThunk(
    "auth/login",
    async ({email, password}, {rejectWithValue}) => {
        try {
            const response = await authApi.login(email, password)
            localStorage.setItem('token', response.token)
            return response
        } catch (error) {
            return rejectWithValue(error.message)
        }

    }
)

const authSlice = createSlice({
    name: "auth",
    initialState: {
        token: null,
        email: null,
        role: null,
        isLoggedIn: false,
        loading: false,
        error: null,
    },
    reducers: {
        logout: (state) => {
            state.token = null
            state.email = null
            state.role = null
            state.isLoggedIn = false
        }
    },
    extraReducers: (builder) => {
        builder
            .addCase(login.pending, (state) => {
                state.loading = true
                state.error = null
            })
            .addCase(login.fulfilled, (state, action) => {
                state.loading = false
                state.token = action.payload.token
                state.email = action.payload.email
                state.role = action.payload.role
                state.isLoggedIn = true
            })
            .addCase(login.rejected, (state, action) => {
                state.loading = false
                state.error = action.payload
            })
    }
})

export const {logout} = authSlice.actions
export default authSlice.reducer