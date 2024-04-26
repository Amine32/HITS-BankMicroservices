import React, {useState} from 'react';
import {Modal, Button, Form} from 'react-bootstrap';
import { instance } from '../api/instance';
import generateIdempotencyKey from '../helper/Idempotency';

function WithdrawModal({account, show, onHide, onWithdraw, onAlert}) {
    const [amount, setAmount] = useState('');

    const handleWithdraw = () => {
        instance.post(`http://localhost:8080/core/api/accounts/${account.id}/withdraw`, parseFloat(amount), {
            headers: {
                'Content-Type': 'application/json',
                "Idempotency-Key": generateIdempotencyKey(),
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
            <Modal.Header className='app__modal' closeButton>
                <Modal.Title>Withdraw Money</Modal.Title>
            </Modal.Header>
            <Modal.Body className='app__modal'>
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
            <Modal.Footer className='app__modal'>
                <Button className='app__button' onClick={onHide}>Cancel</Button>
                <Button className='app__button' onClick={handleWithdraw}>Withdraw</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default WithdrawModal;
