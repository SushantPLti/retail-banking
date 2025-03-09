import { configureStore } from '@reduxjs/toolkit';
import rootReducer from './Slices/index'; // Import the combined reducers
import {thunk} from 'redux-thunk';

const isValidJSON = (str) => {
    try {
        JSON.parse(str);
        return true;
    } catch (e) {
        return false;
    }
};

const initialState = {
    user: {
        userInfo: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : null,
        loading: false,
        bearerToken: localStorage.getItem('bearerToken') || null,
        accountNumbers: localStorage.getItem('accountNumbers') ? JSON.parse(localStorage.getItem('accountNumbers')) : [],
        error: null,
    },
    performTransaction: {
        loading: false,
        success: false,
        transaction: null,
        error: null,
        closingBalance: 0,
        lastTransactionType: null,
    },
    transactionList: {
        loading: false,
        transactions: [],
        error: null,
    },

};

const store = configureStore({
    reducer: rootReducer,
    preloadedState: initialState,
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(thunk),
});

export default store;