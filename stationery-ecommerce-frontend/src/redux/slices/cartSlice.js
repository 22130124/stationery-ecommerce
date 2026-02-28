import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import * as cartApi from "../../api/cartApi";

export const getCart = createAsyncThunk(
    "cart/getCart",
    async (_, {rejectWithValue}) => {
        try {
            return await cartApi.getCart()
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
)

export const addToCart = createAsyncThunk(
    "cart/addToCart",
    async (payload,
           {rejectWithValue}) => {
        try {
            return await cartApi.addToCart(payload)
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
)

export const removeItem = createAsyncThunk(
    "cart/removeItem",
    async ({variantId}, {rejectWithValue}) => {
        try {
            return await cartApi.removeCartItem(variantId)
        } catch (error) {
            return rejectWithValue(error.message);
        }
    }
)

const cartSlice = createSlice({
    name: "cart",
    initialState: {
        totalItems: 0,
        error: null,
    },
    extraReducers: (builder) => {
        builder
            .addCase(getCart.fulfilled, (state, action) => {
                state.totalItems = action.payload.totalItems
            })
            .addCase(getCart.rejected, (state, action) => {
                state.error = action.payload
            })
            .addCase(addToCart.fulfilled, (state, action) => {
                state.totalItems = action.payload.totalItems
            })
            .addCase(addToCart.rejected, (state, action) => {
                state.error = action.payload
            })
            .addCase(removeItem.fulfilled, (state, action) => {
                state.totalItems = action.payload.totalItems
            })
            .addCase(removeItem.rejected, (state, action) => {
                state.error = action.payload
            })
    }
})

export default cartSlice.reducer;