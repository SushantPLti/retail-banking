import React, { useState } from 'react';
import { FormGroup } from 'reactstrap';
import { useDispatch, useSelector } from 'react-redux';
import { creditAmount } from '../Redux/Slices/performTransactionSlice';
import { FormContainer, StyledButton, StyledInput, StyledLabel } from '../../Styles/DebitCreditStyle';

function Credit() {
  const [amount, setAmount] = useState('');
  const dispatch = useDispatch();
  const credit = useSelector((state) => state.performTransaction);
  const { loading, success, error, closingBalance } = credit;

  // Handle credit action
  const handleCredit = () => {
    // Validate the amount before dispatching the action
    if (amount > 0) {
      dispatch(creditAmount(Number(amount)));
      setAmount(''); // Reset the amount input after dispatch
    } else {
      alert('Please enter a valid amount'); // Alert user if the amount is invalid
    }
  };

  return (
    <FormGroup style={{ display: 'flex', flexDirection: 'column' }}>
      <StyledLabel for="Credit">Credit</StyledLabel>
      <FormContainer>
        <StyledInput
          type="number"
          name="Credit"
          id="Credit"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          placeholder="Enter Amount"
        />
        <StyledButton name="Credit" onClick={handleCredit} disabled={loading} id="Credit">
          Credit
        </StyledButton>
      </FormContainer>
    </FormGroup>
  );
}

export default Credit;