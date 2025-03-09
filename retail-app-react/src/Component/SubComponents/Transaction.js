import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { TransactionContainer, TransactionFilter, TransactionList } from '../../Styles/TransactionStyle';
import { getLastTransactions, getQuarterlyTransactions, getTransactionPeriod } from '../Redux/Slices/transactionListSlice';
import styled from 'styled-components';

function Transaction() {
  const dispatch = useDispatch();
  const [filter, setFilter] = useState('last');
  const [days, setDays] = useState(5);
  const [fromDate, setFromDate] = useState('2025-01-16');
  const [toDate, setToDate] = useState('2025-01-17');

  const { transactions, loading, error } = useSelector((state) => state.transactionList);
  const { accountNumbers, bearerToken } = useSelector(state => state.user);
  const {transaction} = useSelector((state)=>state.performTransaction);

  const accountNumber = accountNumbers[0];

  useEffect(() => {
    if (filter === 'quarterly') {
      dispatch(getQuarterlyTransactions(accountNumber));
    } else if (filter === 'last') {
      dispatch(getLastTransactions({ accountNumber, days }));
    } else if (filter === 'period') {
      dispatch(getTransactionPeriod({ accountNumber, fromDate, toDate }));
    }
  }, [dispatch, filter, accountNumber, days, fromDate, toDate,transaction]);

  const renderTransactions = () => {
    if (loading) return <p>Loading...</p>;
    if (transactions.length === 0) return <p style={{color:'red'}}>No transactions found !</p>;
    if (error) return <p>{error}</p>;
    return <TableWrapper>
    <TableContainer>
      <table>
        <thead>
          <tr>
            <th>Transaction ID</th>
            <th>Account Number</th>
            <th>Transaction Type</th>
            <th>Transaction Amount</th>
            <th>Created At</th>
            <th>Reference Number</th>
            <th>Closing Balance</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((transaction) => (
            <tr key={transaction.transactionId}>
              <td>{transaction.transactionId}</td>
              <td>{transaction.accountNumber}</td>
              <td>{transaction.transactionType}</td>
              <td>{transaction.transactionAmount}</td>
              <td>{transaction.createdAt}</td>
              <td>{transaction.referenceNumber}</td>
              <td>{transaction.closingBalance}</td>
            </tr>
          ))}
        </tbody>
    </table>
    </TableContainer>
    </TableWrapper>
  };

  return (
    <TransactionContainer>
      <h2>Transactions</h2>
      <TransactionFilter>
        <TransactionSelect value={filter} onChange={(e) => setFilter(e.target.value)}>
          <option value="quarterly">Quarterly</option>
          <option value="last">Last N Days</option>
          <option value="period">Period</option>
        </TransactionSelect>
        {filter === 'last' && (
          <TransactInput
            type="number"
            value={days}
            onChange={(e) => setDays(e.target.value)}
            placeholder="Enter number of days" 
          />
        )}
        {filter === 'period' && (
          <>
            <TransactInput
              type="date"
              value={fromDate}
              onChange={(e) => setFromDate(e.target.value)}
              placeholder="Enter from date"
            />
            <TransactInput
              type="date"
              value={toDate}
              onChange={(e) => setToDate(e.target.value)}
              placeholder="Enter to date"
            />
          </>
        )}
      </TransactionFilter>
      <TransactionList>
        {renderTransactions()}
      </TransactionList>
    </TransactionContainer>
  );
}

export default Transaction;

const TableWrapper = styled.div`
  height: 30vh;
  overflow-y: auto;
  overflow-x: hidden;
  margin: 20px;
`;

const TableContainer = styled.table`
  width: auto; /* Adjust the width as needed */
  background-color: rgb(255, 64, 64);
  border: 3px solid #000;
  border-collapse: collapse; /* Ensure borders are collapsed */

  thead {
    position: sticky;
    top: 0;
    background-color: rgb(247, 4, 4);
    z-index: 1; /* Ensure headers are above other content */
  }

  th {
    color: white;
    font-size: 18px; /* Change the font size as needed */
    white-space: nowrap; /* Prevent wrapping */
  }

  th, td {
    border: 1px solid rgb(51, 2, 2);
    padding: 8px;
    text-align: left;
    font-size: 12px;
    white-space: nowrap; /* Prevent wrapping */
    color: white;
  }
`;

const TransactionSelect = styled.select`
  width: 10vw;
  border-radius: 4px;
  background-color: rgb(0, 152, 253);
  color: white;
  margin: 5px;
`;

const TransactInput = styled.input`
  width: auto; /* Allow the width to adjust based on content */
  border-radius: 4px;
  margin: 5px;
  padding: 5px; /* Add padding for better appearance */
  font-size: 14px; /* Adjust font size as needed */
  box-sizing: border-box; /* Ensure padding is included in the width */
`;