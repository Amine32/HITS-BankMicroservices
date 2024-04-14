import React, { useState } from 'react';
import { Modal, Button, Form, Dropdown } from 'react-bootstrap';
import { instance } from '../api/instance';

function ApplyLoanModal({ show, onHide, loanRates, onLoanApplied }) {
    const [selectedRate, setSelectedRate] = useState('');
    const [amount, setAmount] = useState('');

    const applyForLoan = () => {
        const ownerId = sessionStorage.getItem('userId');

        const loanData = {
            ownerId: ownerId,
            rateId: selectedRate,
            amount: amount
        };

        instance.post('http://localhost:8080/loan/api/loans', loanData, {
            headers: {
                'Content-Type': 'application/json'
            },
            withCredentials: true
        })
            .then(response => {
                onLoanApplied();
                onHide();
            })
            .catch(error => {
                console.error('Error applying for loan', error);
            });
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header className='app__modal' closeButton>
                <Modal.Title>Apply for a Loan</Modal.Title>
            </Modal.Header>
            <Modal.Body className='app__modal'>
                <Form>
                    <Form.Group>
                        <Form.Label>Select Loan Type</Form.Label>
                        <Dropdown>
                            <Dropdown.Toggle variant="success" id="dropdown-basic">
                                {selectedRate || 'Select Rate'}
                            </Dropdown.Toggle>
                            <Dropdown.Menu>
                                {loanRates.map((rate) => (
                                    <Dropdown.Item key={rate.id} onClick={() => setSelectedRate(rate.id)}>
                                        {rate.name}
                                    </Dropdown.Item>
                                ))}
                            </Dropdown.Menu>
                        </Dropdown>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label>Amount</Form.Label>
                        <Form.Control
                            type="number"
                            value={amount}
                            onChange={(e) => setAmount(e.target.value)}
                            placeholder="Enter amount"
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer className='app__modal'>
                <Button className='app__button' onClick={onHide}>Close</Button>
                <Button className='app__button' onClick={applyForLoan}>Apply</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default ApplyLoanModal;
