import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';

const getBearerToken = (getState) => {
    const { user: { bearerToken } } = getState();
    return bearerToken || JSON.parse(localStorage.getItem('bearerToken'));
};

const getAccountNumber = (getState) => {
    const { user: { accountNumbers } } = getState();
    return accountNumbers || JSON.parse(localStorage.getItem('accountNumbers'));
};

export const getQuarterlyTransactions = createAsyncThunk(
    'transactionList/getQuarterlyTransactions',
    async (accountNumber, { getState, rejectWithValue }) => {
        try {
            const bearerToken = getBearerToken(getState);
            const config = {
                headers: {
                    Authorization: `Bearer ${bearerToken}`,
                },
            };
            const { data } = await axios.get(`http://localhost:7050/transaction/quarterlyTransactions?accountNumber=${accountNumber}`, config);
            return data;
        } catch (error) {
            return rejectWithValue(error.response && error.response.data.message
                ? error.response.data.message
                : error.message);
        }
    }
);

export const getLastTransactions = createAsyncThunk(
    'transactionList/getLastTransactions',
    async ({ accountNumber, days }, { getState, rejectWithValue }) => {
        try {
            const bearerToken = getBearerToken(getState);
            const config = {
                headers: {
                    Authorization: `Bearer ${bearerToken}`,
                },
            };
            const { data } = await axios.get(`http://localhost:7050/transaction/lastTransactions?accountNumber=${accountNumber}&days=${days}`, config);
            return data;
        } catch (error) {
            return rejectWithValue(error.response && error.response.data.message
                ? error.response.data.message
                : error.message);
        }
    }
);

export const getTransactionPeriod = createAsyncThunk(
    'transactionList/getTransactionPeriod',
    async ({ accountNumber, fromDate, toDate }, { getState, rejectWithValue }) => {
        try {
            const bearerToken = getBearerToken(getState);
            const config = {
                headers: {
                    Authorization: `Bearer ${bearerToken}`,
                },
            };
            const { data } = await axios.get(`http://localhost:7050/transaction/transactionPeriod?accountNumber=${accountNumber}&fromDate=${fromDate}&toDate=${toDate}`, config);
            return data;
        } catch (error) {
            return rejectWithValue(error.response && error.response.data.message
                ? error.response.data.message
                : error.message);
        }
    }
);

const transactionListSlice = createSlice({
    name: 'transactionList',
    initialState: {
        loading: false,
        transactions: [],
        error: null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(getQuarterlyTransactions.pending, (state) => {
                state.loading = true;
            })
            .addCase(getQuarterlyTransactions.fulfilled, (state, action) => {
                state.loading = false;
                state.transactions = action.payload;
                state.error = null;
            })
            .addCase(getQuarterlyTransactions.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
                state.transactions = [];
            })
            .addCase(getLastTransactions.pending, (state) => {
                state.loading = true;
            })
            .addCase(getLastTransactions.fulfilled, (state, action) => {
                state.loading = false;
                state.transactions = action.payload;
                state.error = null;
            })
            .addCase(getLastTransactions.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
                state.transactions = [];
            })
            .addCase(getTransactionPeriod.pending, (state) => {
                state.loading = true;
            })
            .addCase(getTransactionPeriod.fulfilled, (state, action) => {
                state.loading = false;
                state.transactions = action.payload;
                state.error = null;
            })
            .addCase(getTransactionPeriod.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
                state.transactions = [];
            });
    },
});

export default transactionListSlice.reducer;