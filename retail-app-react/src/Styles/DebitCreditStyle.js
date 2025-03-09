import styled from "styled-components";
import { Input } from 'reactstrap';

const MidDiv = styled.div`
    display: flex;
    flex-direction: row;
    width: 100vw;
    margin-bottom: 20px;
    height: 40vh;
`;

const CDCommonDiv = styled.div`
    display: flex;
    flex-direction: column;
    flex:1;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
`;

const StyledLabel = styled.h4`
  height: 50px; /* Adjust the height as needed */
  display: flex;
  align-items: center; /* Center the text vertically */
`;

const StyledInput = styled(Input)`
  /* Hide the step controls */
  -moz-appearance: textfield;
  &::-webkit-outer-spin-button,
  &::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  flex: 8;
  height: 40px;
  margin-top: 10px;
`;

const StyledButton = styled.button`
    margin-left: 10px; /* Space between input and button */
    padding: 10px 20px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    &:hover {
        background-color: #0056b3;
    }
    flex: 2;
    height: 40px;
    text-align: top;
    margin-top: 10px;
`;

const FormContainer = styled.div`
    display: flex;
    align-items: center;
`;


export {
    MidDiv,
    CDCommonDiv,
    StyledLabel,
    StyledInput,
    StyledButton,
    FormContainer
}