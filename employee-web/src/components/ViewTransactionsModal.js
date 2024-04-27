import React, { useState, useEffect } from 'react';
import { Modal, Button, Table } from 'react-bootstrap';
import { instance } from '../api/instance';

function ViewTransactionsModal({ accountId, show, onHide }) {
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        if (show) {
            instance.get(`http://localhost:8080/core/api/transactions/${accountId}`)
                .then(response => {
                    setTransactions(response.data);
                })
                .catch(error => {
                    console.error('Error fetching transactions', error);
                });
        }
    }, [show, accountId]);

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header className='app__modal' closeButton>
                <Modal.Title>Transactions</Modal.Title>
            </Modal.Header>
            <Modal.Body className='app__modal'>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>ID</th>
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
                            <td>{new Date(transaction.transactionDate).toLocaleString()}</td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            </Modal.Body>
            <Modal.Footer className='app__modal'>
                <Button className='app__button' onClick={onHide}>Close</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default ViewTransactionsModal;