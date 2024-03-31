import React, { useState, useEffect } from 'react';
import { Modal, Button, Table } from 'react-bootstrap';
import axios from 'axios';

function ViewTransactionsModal({ account, show, onHide }) {
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        if (show) {
            axios.get(`http://localhost:8080/core/api/transactions/${account.id}`)
                .then(response => {
                    setTransactions(response.data);
                })
                .catch(error => {
                    console.error('Error fetching transactions', error);
                });
        }
    }, [show, account.id]);

    return (
        <Modal show={show} onHide={onHide} size="lg">
            <Modal.Header closeButton>
                <Modal.Title>Transaction History for Account {account.id}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>Date</th>
                        <th>Type</th>
                        <th>Amount</th>
                    </tr>
                    </thead>
                    <tbody>
                    {transactions.map(transaction => (
                        <tr key={transaction.id}>
                            <td>{new Date(transaction.transactionDate).toLocaleString()}</td>
                            <td>{transaction.transactionType}</td>
                            <td>${transaction.amount.toFixed(2)}</td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>Close</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default ViewTransactionsModal;