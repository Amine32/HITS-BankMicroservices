import React, {useState} from 'react';
import {Modal, Button, Form} from 'react-bootstrap';
import axios from 'axios';

function DepositModal({account, show, onHide, onDeposit, onAlert}) {
    const [amount, setAmount] = useState('');

    const handleDeposit = () => {
        axios.post(`http://localhost:8080/core/api/accounts/${account.id}/deposit`, parseFloat(amount), {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(() => {
                onDeposit();
                onAlert('Deposit successful!', 'success');
            })
            .catch(error => {
                onAlert(`Error depositing money: ${error.message}`, 'danger');
            })
            .finally(onHide);
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Deposit Money</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group>
                        <Form.Label>Amount</Form.Label>
                        <Form.Control
                            type="number"
                            value={amount}
                            onChange={(e) => setAmount(e.target.value)}
                            placeholder="Enter amount to deposit"
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>Cancel</Button>
                <Button variant="primary" onClick={handleDeposit}>Deposit</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default DepositModal;
