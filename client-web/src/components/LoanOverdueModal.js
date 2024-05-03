import React, { useEffect, useState } from "react";
import { Modal, Button, ListGroup } from "react-bootstrap";
import { instance } from "../api/instance";

function LoanOverdueModal({ show, onHide }) {
    const [loanOverdue, setLoanOverdue] = useState([]);

    const fetchLoanOverdue = async () => {
        // Endpoint to fetch loans for the customer
        const ownerId = sessionStorage.getItem("userId");
        if (ownerId) {
          instance
            .get(`http://localhost:8080/loan/api/loans/user/${ownerId}/overdue`, { withCredentials: true })
            .then((response) => {
              setLoanOverdue(response.data);
            })
            .catch((error) => {
              console.error("Error fetching loans overdue", error);
            });
        }
      };

      useEffect(() => {
        fetchLoanOverdue();
      }, []);

  const listItems = loanOverdue.map((loan) => (
    <ListGroup>
      <ListGroup.Item>Loan ID: {loan.id}</ListGroup.Item>
      <ListGroup.Item>Rate ID: {loan.rate.id}</ListGroup.Item>
      <ListGroup.Item>Amount: {loan.amount.toFixed(2)}</ListGroup.Item>
      <ListGroup.Item>Due date: {loan.dueDate}</ListGroup.Item>
      <ListGroup.Item>Amount owed: {loan.amountOwed}</ListGroup.Item>
      <ListGroup.Item>Original amount: {loan.originalAmount}</ListGroup.Item>
      {/* More loan details go here */}
    </ListGroup>
  ));

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header className="app__modal" closeButton>
        <Modal.Title>Loan Overdue</Modal.Title>
      </Modal.Header>
      <Modal.Body className="app__modal">{listItems}</Modal.Body>
      <Modal.Footer className="app__modal">
        <Button className="app__button" onClick={onHide}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default LoanOverdueModal;
