import React, { useState } from 'react';
import { Modal, Button, Form, Alert } from 'react-bootstrap';
import { instance } from "../api/instance"

function CreateRate({ show, onHide, onRateCreated }) {
    const [name, setName] = useState('');
    const [interestRate, setInterestRate] = useState('');
    const [termLength, setTermLength] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        setError('');
        setSuccess('');

        if (!name || interestRate <= 0 || termLength <= 0) {
            setError('Please provide valid values for all fields.');
            return;
        }

        const loanRateDto = {
            name,
            interestRate: parseFloat(interestRate),
            termLength: parseInt(termLength, 10)
        };

        instance.post('http://localhost:8080/loan/api/rates', loanRateDto)
            .then(response => {
                setSuccess('Rate created successfully.');
                onRateCreated(response.data);
                resetForm();
                onHide();  // Close modal on success
            })
            .catch(error => {
                setError('Failed to create rate. ' + error.message);
            });
    };

    const resetForm = () => {
        setName('');
        setInterestRate('');
        setTermLength('');
    };

    return (
        <Modal show={show} onHide={() => { resetForm(); onHide(); }}>
            <Modal.Header className='app__modal' closeButton>
                <Modal.Title>Create New Rate</Modal.Title>
            </Modal.Header>
            <Modal.Body className='app__modal'>
                {error && <Alert variant="danger">{error}</Alert>}
                {success && <Alert variant="success">{success}</Alert>}
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>Name</Form.Label>
                        <Form.Control
                            type="text"
                            value={name}
                            onChange={e => setName(e.target.value)}
                            placeholder="Enter name"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Interest Rate (%)</Form.Label>
                        <Form.Control
                            type="number"
                            step="0.01"
                            value={interestRate}
                            onChange={e => setInterestRate(e.target.value)}
                            placeholder="Enter interest rate"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Term Length (days)</Form.Label>
                        <Form.Control
                            type="number"
                            value={termLength}
                            onChange={e => setTermLength(e.target.value)}
                            placeholder="Enter term length in days"
                        />
                    </Form.Group>
                    <Button className='app__button' type="submit">
                        Create Rate
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>
    );
}

export default CreateRate;
