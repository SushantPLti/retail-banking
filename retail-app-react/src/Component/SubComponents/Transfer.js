import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { TransferDiv } from '../../Styles/TransferStyle';
import { FormGroup } from 'reactstrap';
import { FormContainer, StyledButton, StyledInput, StyledLabel } from '../../Styles/DebitCreditStyle';
import { transferAmount } from '../Redux/Slices/performTransactionSlice';

function Transfer() {
  // State to manage receiver's account number and transfer amount
  const [receiverAccount, setReceiverAccount] = useState('');
  const [amount, setAmount] = useState('');

  // Dispatch function to dispatch actions
  const dispatch = useDispatch();

  // Accessing the transaction state from the Redux store
  const { loading, error, success, closingBalance } = useSelector(state => state.performTransaction);

  // Function to handle form submission
  const handleTransfer = (e) => {
    e.preventDefault();
    if (amount > 0 && receiverAccount) {
      dispatch(transferAmount({ receiverAccount, amount: Number(amount) }));
      setAmount('');
      setReceiverAccount('');
    } else {
      alert('Please enter valid details');
    }
  };

  return (
    <TransferDiv>
      <h3>Transfer Funds</h3>
      <form  onSubmit={handleTransfer}>
        <FormGroup>
          <StyledLabel for="To">Receiver</StyledLabel>
          <StyledInput
            type="number"
            name="To"
            id="To"
            placeholder="Receiver Account"
            value={receiverAccount}
            onChange={(e) => setReceiverAccount(e.target.value)}
          />
          <FormContainer>
            <StyledInput
              type="number"
              name="Transfer"
              id="Transfer"
              placeholder="Enter Amount"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
            />
            <StyledButton type="submit" name="Transfer" id="Transfer" disabled={loading}>
              Transfer
            </StyledButton>
          </FormContainer>
        </FormGroup>
        </form>
    </TransferDiv>
  );
}

export default Transfer;