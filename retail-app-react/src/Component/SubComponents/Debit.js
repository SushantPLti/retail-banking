import React, { useState } from 'react';
import { FormGroup } from 'reactstrap';
import { useDispatch, useSelector } from 'react-redux';
import { debitAmount } from '../Redux/Slices/performTransactionSlice';
import { FormContainer, StyledButton, StyledInput, StyledLabel } from '../../Styles/DebitCreditStyle';

function Debit() {
  const [amount, setAmount] = useState('');
  const dispatch = useDispatch();
  const Debit = useSelector((state) => state.performTransaction);
  const { loading, success, error, closingBalance } = Debit;

  // Handle debit action
  const handleDebit = () => {
    // Validate the amount before dispatching the action
    if (amount > 0) {
      dispatch(debitAmount(Number(amount)));
      setAmount(''); // Reset the amount input after dispatch
    } else {
      alert('Please enter a valid amount'); // Alert user if the amount is invalid
    }
  };

  return (
    <FormGroup style={{ display: 'flex', flexDirection: 'column' }}>
      <StyledLabel for="Debit">Debit</StyledLabel>
      <FormContainer>
        <StyledInput
          type="number"
          name="Debit"
          id="Debit"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          placeholder="Enter Amount"
        />
        <StyledButton name="Debit" onClick={handleDebit} disabled={loading} id="Debit">
          Debit
        </StyledButton>
      </FormContainer>
    </FormGroup>
  );
}

export default Debit;