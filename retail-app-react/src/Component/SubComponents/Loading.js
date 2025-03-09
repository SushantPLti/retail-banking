import React from 'react';
import { AiOutlineLoading3Quarters } from 'react-icons/ai';
import styled from "styled-components";

const LoadingContainer = styled.div`
    width: 100vw;
    height: 100vh;
    background-color: #db0011;
    align-items: center;
    justify-content: center;
    display: flex;
    flex-direction: column;
    color: white;
    font-size: 18px;
`;

function Loading() {
  return (
    <LoadingContainer>
        <AiOutlineLoading3Quarters size={40} color='white' />
        Loading Please wait....
    </LoadingContainer>
  )
}

export default Loading