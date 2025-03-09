import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import sessionStorage from 'redux-persist/lib/storage/session';

const getBearerToken = (getState) => {
    const { user: { bearerToken } } = getState();
    return bearerToken || JSON.parse(localStorage.getItem('bearerToken'));
};

const getAccountNumber = (getState) => {
    const { user: { accountNumbers } } = getState();
    return accountNumbers || JSON.parse(localStorage.getItem('accountNumbers'));
};

export const fetchBalance = createAsyncThunk(
    'performTransaction/fetchBalance',
    async (_, { getState, rejectWithValue }) => {
        try {
            const bearerToken = getBearerToken(getState);
            const accountNumber = getAccountNumber(getState)[0];
            const config = {
                headers: {
                    Authorization: `Bearer ${bearerToken}`,
                },
            };
            const { data } = await axios.get(`http://localhost:7050/accounts/balance/${accountNumber}`, config);
            return data.balance;
        } catch (error) {
            return rejectWithValue(error.response && error.response.data.message
                ? error.response.data.message
                : error.message);
        }
    }
);

export const creditAmount = createAsyncThunk(
    'performTransaction/creditAmount',
    async (amount, { getState, dispatch, rejectWithValue }) => {
        try {
            const bearerToken = getBearerToken(getState);
            const accountNumber = getAccountNumber(getState)[0];
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${bearerToken}`,
                },
            };
            const { data } = await axios.post('http://localhost:7050/transaction', {
                accountNumber: accountNumber,
                amount,
                transactionType: 'CREDIT',
            }, config);
            sessionStorage.setItem('data'.data);
            dispatch(fetchBalance());
            return { ...data, transactionType: 'CREDIT' };
        } catch (error) {
            return rejectWithValue(error.response && error.response.data.message
                ? error.response.data.message
                : error.message);
        }
    }
);

export const debitAmount = createAsyncThunk(
    'performTransaction/debitAmount',
    async (amount, { getState, dispatch, rejectWithValue }) => {
        try {
            const bearerToken = getBearerToken(getState);
            const accountNumber = getAccountNumber(getState)[0];
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${bearerToken}`,
                },
            };
            const { data } = await axios.post('http://localhost:7050/transaction', {
                accountNumber: accountNumber,
                amount,
                transactionType: 'DEBIT',
            }, config);
            dispatch(fetchBalance());
            return { ...data, transactionType: 'DEBIT' };
        } catch (error) {
            return rejectWithValue(error.response && error.response.data.message
                ? error.response.data.message
                : error.message);
        }
    }
);

export const transferAmount = createAsyncThunk(
    'performTransaction/transferAmount',
    async ({ receiverAccount, amount }, { getState, dispatch, rejectWithValue }) => {
        try {
            const bearerToken = getBearerToken(getState);
            const accountNumber = getAccountNumber(getState)[0];
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${bearerToken}`,
                },
            };
            const { data } = await axios.post('http://localhost:7050/transaction/transfer', {
                senderAccount: accountNumber,
                receiverAccount,
                amount,
            }, config);
            
            dispatch(fetchBalance());
            return { ...data, transactionType: 'TRANSFER' };
        } catch (error) {
            return rejectWithValue(error.response && error.response.data.message
                ? error.response.data.message
                : error.message);
        }
    }
);

const performTransactionSlice = createSlice({
    name: 'performTransaction',
    initialState: {
        loading: false,
        success: false,
        transaction: null,
        error: null,
        closingBalance: 0,
        lastTransactionType: null, // New state property to track the last transaction type
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchBalance.pending, (state) => {
                state.loading = true;
            })
            .addCase(fetchBalance.fulfilled, (state, action) => {
                state.loading = false;
                state.closingBalance = action.payload;
            })
            .addCase(fetchBalance.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
            .addCase(creditAmount.pending, (state) => {
                state.loading = true;
            })
            .addCase(creditAmount.fulfilled, (state, action) => {
                state.loading = false;
                state.success = true;
                state.transaction = action.payload;
                state.lastTransactionType = action.payload.transactionType; // Set the last transaction type
            })
            .addCase(creditAmount.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
            .addCase(debitAmount.pending, (state) => {
                state.loading = true;
            })
            .addCase(debitAmount.fulfilled, (state, action) => {
                state.loading = false;
                state.success = true;
                state.transaction = action.payload;
                state.lastTransactionType = action.payload.transactionType; // Set the last transaction type
            })
            .addCase(debitAmount.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
            .addCase(transferAmount.pending, (state) => {
                state.loading = true;
            })
            .addCase(transferAmount.fulfilled, (state, action) => {
                state.loading = false;
                state.success = true;
                state.transaction = action.payload;
                state.lastTransactionType = action.payload.transactionType; // Set the last transaction type
            })
            .addCase(transferAmount.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            });
    },
});

export default performTransactionSlice.reducer;