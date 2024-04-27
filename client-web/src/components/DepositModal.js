import React, {useState} from 'react';
import {Modal, Button, Form} from 'react-bootstrap';
import { instance } from '../api/instance';
import generateIdempotencyKey from '../helper/Idempotency';

function DepositModal({account, show, onHide, onDeposit, onAlert}) {
    const [amount, setAmount] = useState('');

    const handleDeposit = () => {
        instance.post(`http://localhost:8080/core/api/accounts/${account.id}/deposit`, parseFloat(amount), {
            headers: {
                'Content-Type': 'application/json',
                "Idempotency-Key": generateIdempotencyKey(),
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
            <Modal.Header className='app__modal' closeButton>
                <Modal.Title>Deposit Money</Modal.Title>
            </Modal.Header>
            <Modal.Body className='app__modal'>
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
            <Modal.Footer className='app__modal'>
                <Button className='app__button' onClick={onHide}>Cancel</Button>
                <Button className='app__button' onClick={handleDeposit}>Deposit</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default DepositModal;
