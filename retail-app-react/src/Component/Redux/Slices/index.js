import { combineReducers } from 'redux';
import userReducer from './userLoginSlice'; // Import the user slice
import transactionListReducer from './transactionListSlice'; // Import the transaction list slice
import performTransactionReducer from './performTransactionSlice'; // Import the perform transaction slice

export default combineReducers({
    user: userReducer,
    transactionList: transactionListReducer,
    performTransaction: performTransactionReducer,
});