import React, { useState, useEffect } from 'react';
import { instance } from '../api/instance';
import { Table } from 'react-bootstrap';

function ViewTransactions({ accountId }) {
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        instance.get(`http://localhost:8080/api/accounts/${accountId}/transactions`, {
            headers: {
                // Include any necessary headers
            }
        }).then(response => {
            setTransactions(response.data);
        }).catch(error => {
            console.error('Error fetching transactions', error);
        });
    }, [accountId]);

    return (
        <div>
            <h2>Transaction History for Account {accountId}</h2>
            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>Transaction ID</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Date</th>
                </tr>
                </thead>
                <tbody>
                {transactions.map(transaction => (
                    <tr key={transaction.id}>
                        <td>{transaction.id}</td>
                        <td>{transaction.transactionType}</td>
                        <td>{transaction.amount}</td>
                        <td>{transaction.transactionDate}</td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </div>
    );
}

export default ViewTransactions;
