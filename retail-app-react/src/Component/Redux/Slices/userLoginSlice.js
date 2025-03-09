import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode'; // Corrected import statement

export const signin = createAsyncThunk(
    'user/signin',
    async ({ custID, password }, { rejectWithValue }) => {
        try {
            const longCustId = parseInt(custID, 10);

            // First, authenticate the user
            const { data } = await axios.post('http://localhost:7050/auth/login', {
                custID: longCustId,
                password,
            });
            localStorage.setItem('bearerToken', JSON.stringify(data.accessToken));
            const bearerToken = data.accessToken;


            if (bearerToken) {
                const decodedToken = jwtDecode(bearerToken);
                const userRoles = decodedToken.roles; // Extract roles from JWT

                if (userRoles.includes('CUSTOMER')) {
                    // Set the Authorization header for the next request
                    const config = {
                        headers: {
                            Authorization: `Bearer ${bearerToken}`
                        }
                    };

                    // Then, retrieve the account numbers
                    const accountResponse = await axios.get(`http://localhost:7050/accounts/getAccountNumbers/${longCustId}`, config);
                    const accountNumbers = accountResponse.data.data;

                    // Store account numbers in local storage
                    localStorage.setItem('accountNumbers', JSON.stringify(accountNumbers));
                    return { accessToken: bearerToken, accountNumbers };
                }

            }


            return { accessToken: bearerToken };
        } catch (error) {
            return rejectWithValue(error.response && error.response.data.message ? error.response.data.message : error.message);
        }
    }
);

export const signOut = createAsyncThunk(
    'user/signOut',
    async (_, { dispatch }) => {
        localStorage.removeItem('bearerToken');
        localStorage.removeItem('custId');
        localStorage.removeItem('accountNumbers');
        dispatch(userSlice.actions.signOut());
    }
);

const userSlice = createSlice({
    name: 'user',
    initialState: {
        userInfo: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : null,
        loading: false,
        bearerToken: localStorage.getItem('bearerToken') || null,
        accountNumbers: localStorage.getItem('accountNumbers') ? JSON.parse(localStorage.getItem('accountNumbers')) : [],
        error: null,
    },
    reducers: {
        signOut: (state) => {
            state.userInfo = null;
            state.bearerToken = null;
            state.accountNumbers = [];
            state.error = null;
        }
    },
    extraReducers: (builder) => {
        builder
            .addCase(signin.pending, (state) => {
                state.loading = true;
            })
            .addCase(signin.fulfilled, (state, action) => {
                state.loading = false;
                state.bearerToken = action.payload.accessToken;
                state.accountNumbers = action.payload.accountNumbers;
                state.error = null;
            })
            .addCase(signin.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            });
    },
});

export default userSlice.reducer;