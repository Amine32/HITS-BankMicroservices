import React, { useState, useEffect } from "react";
import { Modal, Button, Table } from "react-bootstrap";
import { instance } from "../api/instance";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";

function ViewTransactionsModal({ account, show, onHide }) {
  const [transactions, setTransactions] = useState([]);
  let stompClient;

  function connect() {
    const socket = new SockJS("http://localhost:8081/ws");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
      console.log("Connected: " + frame);
      stompClient.subscribe("/topic/transactions", function (transaction) {
        const message = JSON.parse(transaction.body);
        console.log(message);
      });
    });
  }

  useEffect(() => {
    // if (show) {
    //   connect();
    // }
    if (show) {
      instance
        .get(`http://localhost:8080/core/api/transactions/${account.id}`)
        .then((response) => {
          setTransactions(response.data);
        })
        .catch((error) => {
          console.error("Error fetching transactions", error);
        });
    }
  }, [show, account.id]);

  return (
    <Modal show={show} onHide={onHide} size="lg">
      <Modal.Header className="app__modal" closeButton>
        <Modal.Title>Transaction History for Account {account.id}</Modal.Title>
      </Modal.Header>
      <Modal.Body className="app__modal">
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Date</th>
              <th>Type</th>
              <th>Amount</th>
            </tr>
          </thead>
          <tbody>
            {transactions.map((transaction) => (
              <tr key={transaction.id}>
                <td>
                  {new Date(transaction.transactionDate).toLocaleString()}
                </td>
                <td>{transaction.transactionType}</td>
                <td>{transaction.amount.toFixed(2)}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </Modal.Body>
      <Modal.Footer className="app__modal">
        <Button className="app__button" onClick={onHide}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default ViewTransactionsModal;
