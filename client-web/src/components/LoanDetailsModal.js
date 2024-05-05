import React from "react";
import { Modal, Button, ListGroup } from "react-bootstrap";

function LoanDetailsModal({ loan, show, onHide }) {
  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header className="app__modal" closeButton>
        <Modal.Title>Loan Details</Modal.Title>
      </Modal.Header>
      <Modal.Body className="app__modal">
        <ListGroup>
          <ListGroup.Item>Loan ID: {loan.id}</ListGroup.Item>
          <ListGroup.Item>Rate ID: {loan.rate.id}</ListGroup.Item>
          <ListGroup.Item>Due date: {loan.dueDate}</ListGroup.Item>
          <ListGroup.Item>Amount owed: {loan.amountOwed}</ListGroup.Item>
          <ListGroup.Item>
            Original amount: {loan.originalAmount}
          </ListGroup.Item>
          {/* More loan details go here */}
        </ListGroup>
      </Modal.Body>
      <Modal.Footer className="app__modal">
        <Button className="app__button" onClick={onHide}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default LoanDetailsModal;
