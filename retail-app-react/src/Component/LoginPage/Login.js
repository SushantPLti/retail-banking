import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { signin } from '../Redux/Slices/userLoginSlice'; // Ensure the correct import path
import { LoginContainer, LoginInfo, SignInBtn } from '../../Styles/LoginStyle';
import 'bootstrap/dist/css/bootstrap.min.css';
import {jwtDecode} from 'jwt-decode'; // Corrected import statement
import Loading from '../SubComponents/Loading';

function Login() {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [custid, setCustomerId] = useState('');
    const [password, setPassword] = useState('');
    const { bearerToken, loading, error } = useSelector(state => state.user); // Ensure the correct state path

    // Handle sign-in action
    const signIn = e => {
        e.preventDefault();
        dispatch(signin({ custID: custid, password })); // Pass an object with custID and password
    };

    // Display error notification
    const notifyError = (message) => toast.error(message, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
    });

    useEffect(() => {
        if (bearerToken) {
            const decodedToken = jwtDecode(bearerToken);
            const userRoles = decodedToken.roles; // Extract roles from JWT

            if (userRoles.includes('MANAGER') || userRoles.includes('OPERATOR')) {
                navigate('/admin/home');
            } else {
                navigate('/dashboard');
            }
            
        }
    }, [bearerToken, navigate]);

    useEffect(() => {
        if (error) {
            notifyError(error);
        }
    }, [error]);

    if (loading) {
        return <Loading />; // Show loading indicator
    }

    return (
        <LoginContainer>
            <LoginInfo>
                <ToastContainer />
                <h1>Sign In</h1>

                <form onSubmit={signIn}>
                    <h5>Customer ID</h5>
                    <input
                        type="number"
                        name='custid'
                        value={custid}
                        onChange={e => setCustomerId(e.target.value)}
                        required
                        autoComplete="off"
                        autoCorrect="off"
                    />
                    <h5>Password</h5>
                    <input
                        type="password"
                        name='password'
                        value={password}
                        onChange={e => setPassword(e.target.value)} // Ensure onChange handler is set
                        required
                        autoComplete="off"
                        autoCorrect="off"
                    />
                    <SignInBtn type='submit'>Sign In</SignInBtn>
                    <p>
                        By Signing-in you agree to LTI's terms and conditions.
                        Please read our terms of condition and use here.
                    </p>
                </form>
            </LoginInfo>
        </LoginContainer>
    );
}

export default Login;