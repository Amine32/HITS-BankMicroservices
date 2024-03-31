import React, {useState} from 'react';
import {Modal, Button, Form} from 'react-bootstrap';
import axios from 'axios';

function WithdrawModal({account, show, onHide, onWithdraw, onAlert}) {
    const [amount, setAmount] = useState('');

    const handleWithdraw = () => {
        axios.post(`http://localhost:8080/core/api/accounts/${account.id}/withdraw`, parseFloat(amount), {
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(() => {
                onWithdraw();
                onAlert('Withdrawal successful!', 'success');
            })
            .catch(error => {
                onAlert(`Error withdrawing money: ${error.message}`, 'danger');
            })
            .finally(onHide);
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Withdraw Money</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group>
                        <Form.Label>Amount</Form.Label>
                        <Form.Control
                            type="number"
                            value={amount}
                            onChange={(e) => setAmount(e.target.value)}
                            placeholder="Enter amount to withdraw"
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>Cancel</Button>
                <Button variant="primary" onClick={handleWithdraw}>Withdraw</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default WithdrawModal;
