// LoanDetails.js
import React from 'react';
import { Card, Button } from 'react-bootstrap';

function LoanDetails({ loan, clearSelection }) {
    return (
        <Card>
            <Card.Header><strong>Loan Details</strong></Card.Header>
            <Card.Body>
                <p>Loan ID: {loan.id}</p>
                <p>Amount: {loan.amount}</p>
                <p>Interest Rate: {loan.interestRate}</p>
                <p>Duration: {loan.duration}</p>
                <p>Status: {loan.status}</p>
                <Button variant="secondary" onClick={clearSelection}>Back to List</Button>
            </Card.Body>
        </Card>
    );
}

export default LoanDetails;
