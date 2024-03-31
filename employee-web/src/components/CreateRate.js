import React, { useState } from 'react';
import axios from 'axios';
import { Card, Form, Button } from 'react-bootstrap';

function CreateRate() {
    const [name, setName] = useState('');
    const [interestRate, setInterestRate] = useState('');

    const handleCreateRate = () => {
        const rateData = {
            name: name,
            interestRate: interestRate
        };

        axios.post('http://localhost:8080/loan/api/rates', rateData, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                alert('Rate created successfully');
                setName('');
                setInterestRate('');
            })
            .catch(error => {
                console.error('Error creating rate', error);
            });
    };

    return (
        <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
            <Card style={{ width: '22rem' }}>
                <Card.Body>
                    <Card.Title>Create a New Loan Rate</Card.Title>
                    <Form onSubmit={(e) => e.preventDefault()}>
                        <Form.Group className="mb-3" controlId="rateName">
                            <Form.Label>Rate Name</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter rate name"
                                value={name}
                                onChange={e => setName(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="interestRate">
                            <Form.Label>Interest Rate (%)</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter interest rate"
                                value={interestRate}
                                onChange={e => setInterestRate(e.target.value)}
                            />
                        </Form.Group>

                        <Button variant="primary" onClick={handleCreateRate}>
                            Create Rate
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </div>
    );
}

export default CreateRate;
