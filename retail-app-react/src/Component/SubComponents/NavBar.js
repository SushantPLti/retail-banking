import React from 'react'
import { HomeLink, NavContainer, SideBar } from '../../Styles/NavBarStyle'
import { useDispatch,useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { signOut } from '../Redux/Slices/userLoginSlice';
import { IoHome } from "react-icons/io5";
import { CgProfile } from "react-icons/cg";
import { RiLogoutCircleRLine } from 'react-icons/ri';

function NavBar() {
  const { bearerToken, loading, error } = useSelector(state => state.user);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const home = e => {
    e.preventDefault();
    if (bearerToken) {
      navigate('/dashboard');
    } else {
      navigate('/');
    }
  }

  const profile = e => {
    e.preventDefault();
    if (bearerToken) {
      navigate('/profile');
    } else {
      navigate('/');
    }
  }

  const logout = e => {
    e.preventDefault();
    dispatch(signOut());
    if (!bearerToken) {
      navigate('/');
    }
  }

  return (
    <NavContainer>
      <HomeLink>
        <a onClick={home}>
          <IoHome size ={20}/>
          Home
        </a>
      </HomeLink>
      <SideBar>
        <a onClick={profile}>
          <CgProfile size ={20}/>
          Profile
        </a>
        <a onClick={logout}>
          <RiLogoutCircleRLine color='red' size ={20} />
          Log Out
        </a>
      </SideBar>
    </NavContainer>
  )
}

export default NavBar