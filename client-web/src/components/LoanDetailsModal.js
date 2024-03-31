import React from 'react';
import { Modal, Button, ListGroup } from 'react-bootstrap';

function LoanDetailsModal({ loan, show, onHide }) {
    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Loan Details</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <ListGroup>
                    <ListGroup.Item>Loan ID: {loan.id}</ListGroup.Item>
                    <ListGroup.Item>Amount: ${loan.amount.toFixed(2)}</ListGroup.Item>
                    {/* More loan details go here */}
                </ListGroup>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>Close</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default LoanDetailsModal;
