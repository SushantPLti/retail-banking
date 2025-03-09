import styled from "styled-components";

const LoginContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100vh;
    box-shadow: 0 0.06vw 0.19vw rgba(0,0,0,0.12),0 0.06vw 0.13vw rgba(0,0,0,0.24);
    padding: 4vw;
    padding-top: 8%;
    background-color: #db0011;
`;

const LoginInfo = styled.div`
    height: fit-content;
    display: flex;
    border-radius: 0.325vw;
    padding: 1.3vw;
    box-shadow: 0 0.065vw 0.2vw rgba(0,0,0,0.12),0 0.065vw 0.13vw rgba(0,0,0,0.24);
    background-color: white;
    box-sizing: border-box; /* Ensure padding doesn't affect width */
    flex-direction: column;
    > h1 {
        font-weight: 500;
        margin-bottom: 2.77vh;
        text-align: left;
        width: 100%;
    }
    > form {
        width: 100%; /* Ensure form takes full width */
        box-sizing: border-box; /* Ensure padding doesn't affect width */
        > h5 {
            margin-bottom: 0.69vh;
            text-align: left;
        }
        > input {
    height: 28px;
    margin-bottom: 6px;
    background-color: white;
    width: 100%;
    border-radius: 12px;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
    padding: 8px;
    border: 1px solid #ccc;
    box-sizing: border-box;
        }
        > select {
            height: 4.16vh;
            margin-bottom: 1.38vh;
            background-color: white;
            width: 100%; /* Ensure consistent width */
            border-radius: 0.5vw; /* Round border */
            text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1); /* Text shadow */
            padding: 0.5vw; /* Add padding for better appearance */
            border: 1px solid #ccc; /* Optional: Add a border */
            box-sizing: border-box; /* Ensure padding doesn't affect width */
        }
        > p {
            font-size: 0.78vw;
            margin-top: 2vh;
        }
    }
`;

const SignUpBtn = styled.button`
    border-radius: 0.13vw;
    width: 100%;
    height: 28px;
    border: 0.06vw solid;
    margin-top: 8px;
    border-color: darkgray;
    font-weight: 500;
    background-color: rgb(208, 204, 238);
    cursor: pointer;
    :hover {
        cursor: pointer;
        background-color:rgb(109, 103, 103); /* Change color on hover */
    }
    box-sizing: border-box; /* Ensure padding doesn't affect width */
`;

const SignInBtn = styled.button`
    background-color: rgb(111, 17, 233);
    border-radius: 12px;
    width: 100%;
    height: 28px;
    margin-top: 1.38vh;
    border-color: rgb(5, 58, 253);
    color: white;
    cursor: pointer;
    font-weight: 500;
    :hover {
        background-color:rgb(249, 48, 17); /* Change color on hover */
    }
    box-sizing: border-box; /* Ensure padding doesn't affect width */
`;

export {
    LoginInfo,
    SignInBtn,
    SignUpBtn,
    LoginContainer
};