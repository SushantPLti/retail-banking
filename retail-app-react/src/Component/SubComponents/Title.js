import React from 'react'
import { TitleContainer } from '../../Styles/TitleStyle'
import MySVG from '../../Logo/HDFC-Bank-logo.png';
import { useNavigate } from 'react-router-dom';

function TitleBar() {
  const navigate = useNavigate();
  const handleClick = () =>{
    navigate('/dashboard');
  }
  return (
    <TitleContainer>
        <a>Welcome to HDFC Bank</a>
        <img src = {MySVG} alt='HDFC Logo' onClick={handleClick}/>
    </TitleContainer>
  )
}

export default TitleBar