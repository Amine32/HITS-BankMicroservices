import React, { useEffect, useState } from "react";
import { Button, Modal, Table } from "react-bootstrap";
import { instance } from "../api/instance";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

function ViewTransactionsModal({ account, show, onHide }) {
    const [transactions, setTransactions] = useState([]);

    useEffect(() => {
        const connectWebSocket = () => {
            const token = sessionStorage.getItem("authToken");
            const socket = new SockJS(`http://localhost:8081/ws?access_token=${token}`);
            const client = Stomp.over(() => socket);  // Ensuring auto-reconnect works

            client.connect({}, (frame) => {
                console.log('Connected: ' + frame);
                client.subscribe('/topic/transactions', (message) => {
                    const newTransaction = JSON.parse(message.body);
                    setTransactions(prev => [...prev, newTransaction]);
                });
            }, (error) => {
                console.error('Error connecting to WebSocket:', error);
            });

            return () => {
                client.disconnect(() => {
                    console.log("Disconnected");
                });
            };
        };

        if (show) {
            const disconnect = connectWebSocket();
            return disconnect;  
        }
    }, [show]);

    useEffect(() => {
        if (show) {
            instance.get(`http://localhost:8080/core/api/transactions/${account.id}`)
                .then(response => setTransactions(response.data))
                .catch(error => console.error("Error fetching transactions", error));
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
                <Button onClick={onHide}>Close</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default ViewTransactionsModal;
