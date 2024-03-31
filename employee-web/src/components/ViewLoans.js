import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { ListGroup, Button, Card } from 'react-bootstrap';
import LoanDetails from './LoanDetails'; // Import the LoanDetails component

function ViewLoans() {
    const [loans, setLoans] = useState([]);
    const [selectedLoan, setSelectedLoan] = useState(null);

    useEffect(() => {
        axios.get('http://localhost:8080/api/loans/')
            .then(response => {
                setLoans(response.data);
            })
            .catch(error => {
                console.error('Error fetching loans', error);
            });
    }, []);

    return (
        <Card style={{ width: '50rem', margin: '2rem auto' }}>
            <Card.Header><strong>Loan List</strong></Card.Header>
            <Card.Body>
                {selectedLoan ? (
                    <LoanDetails loan={selectedLoan} clearSelection={() => setSelectedLoan(null)} />
                ) : (
                    <ListGroup variant="flush">
                        {loans.map(loan => (
                            <ListGroup.Item key={loan.id}>
                                Loan ID: {loan.id} - Amount: {loan.amount}
                                <Button variant="primary" onClick={() => setSelectedLoan(loan)} style={{ float: 'right' }}>
                                    View Details
                                </Button>
                            </ListGroup.Item>
                        ))}
                    </ListGroup>
                )}
            </Card.Body>
        </Card>
    );
}

export default ViewLoans;