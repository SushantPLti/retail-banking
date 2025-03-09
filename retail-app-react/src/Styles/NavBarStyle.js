import styled from "styled-components";

const NavContainer = styled.div`
    width: 100%;
    height : 10vh;
    display: flex;
    flex-direction: row;
    background-color: #db0011;
    justify-content: space-between;
    color: white;
    font-weight: 500;
`;

const HomeLink = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    width: 10vw;
    height: 100%;
    box-sizing: border-box;
    >a{
        padding: 10%;
        width: 100%;
        height: 100%;
        align-items: center;
        justify-content: center;
        display: flex;
        text-decoration: none;
        color: white;
        // font-weight: bold;
        cursor: pointer;
        flex-direction: column;
    }
`;


const SideBar = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-end;
    margin-right: 10px;
    width: 100%;
    > a {
        padding: 10px;
        justify-content: flex-end;
        align-items: center;
        display: flex;
        text-decoration: none;
        color: white;
        // font-weight: bold;
        cursor: pointer;
        flex-direction: column;
    }
`;


export {
    NavContainer,
    HomeLink,
    SideBar
}