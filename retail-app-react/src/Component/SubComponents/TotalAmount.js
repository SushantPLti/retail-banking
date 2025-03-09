import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import axios from 'axios';
import { FormGroup } from 'reactstrap';
import { FormContainer, StyledButton, StyledInput, StyledLabel } from '../../Styles/DebitCreditStyle';

function TotalAmount() {
    const [balance, setBalance] = useState(0);
    const [transactionPerformed, setTransactionPerformed] = useState(false); // State to track if a transaction has been performed
    const dispatch = useDispatch();
    const { accountNumbers, bearerToken } = useSelector(state => state.user);
    const { success, error, closingBalance, lastTransactionType } = useSelector(state => state.performTransaction);

    // Use the first account number
    const accountNumber = accountNumbers[0];

    const fetchBalance = async () => {
        try {
            const config = {
                headers: {
                    Authorization: `Bearer ${bearerToken}`,
                },
            };
            const { data } = await axios.get(`http://localhost:7050/accounts/balance/${accountNumber}`, config);
            setBalance(data.balance);
        } catch (error) {
            console.error("Failed to fetch balance:", error);
        }
    };

    useEffect(() => {
        fetchBalance();
    }, [accountNumber, bearerToken, closingBalance]);

    useEffect(() => {
        if (success || error) {
            setTransactionPerformed(true); // Update state when a transaction is successful or fails
        }
    }, [success, error]);

    const getSuccessMessage = () => {
        if(transactionPerformed) return '';
        switch (lastTransactionType) {
            case 'CREDIT':
                return 'Amount Credited!';
            case 'DEBIT':
                return 'Amount Debited!';
            case 'TRANSFER':
                return 'Amount Transferred!';
            default:
                return 'Transaction successful!';
        }
    };

    return (
        <FormGroup>
            <StyledLabel for="Balance">Balance</StyledLabel>
            <StyledInput type="text" id="Balance" value={balance} readOnly />
            {/* Display error message if there's an error and a transaction has been performed */}
            {transactionPerformed && error && <p style={{ color: 'red' }}>{error}</p>}
            {/* Display success message based on the last transaction type and a transaction has been performed */}
            {transactionPerformed && success && <p style={{ color: 'green' }}> {getSuccessMessage()}</p>}
        </FormGroup>
    );
}

export default TotalAmount;