import React, {useState} from 'react';
import {Modal, Button, Form} from 'react-bootstrap';
import { instance } from '../api/instance';
import generateIdempotencyKey from '../helper/Idempotency';

function PayOffLoanModal({loanId, show, onHide, fetchLoans}) {
    const [amount, setAmount] = useState('');

    const payOffLoan = () => {
        instance
          .post(
            `http://localhost:8080/loan/api/loans/${loanId}/repayments`, parseFloat(amount), {
              headers: {
                "Content-Type": "application/json",
                "Idempotency-Key": generateIdempotencyKey(),
              },
            }
          )
          .then((response) => {
            if (response.status === 200) {
              fetchLoans(); // Refresh the loans list after successful payoff
            }
          })
          .catch((error) => {
            console.error("Error paying off loan", error);
          });
      };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header className='app__modal' closeButton>
                <Modal.Title>Pay Money</Modal.Title>
            </Modal.Header>
            <Modal.Body className='app__modal'>
                <Form>
                    <Form.Group>
                        <Form.Label>Amount</Form.Label>
                        <Form.Control
                            type="number"
                            min="0"
                            value={amount}
                            onChange={(e) => setAmount(e.target.value)}
                            placeholder="Enter amount to repay"
                        />
                    </Form.Group>
                </Form>
            </Modal.Body>
            <Modal.Footer className='app__modal'>
                <Button className='app__button' onClick={onHide}>Cancel</Button>
                <Button className='app__button' onClick={payOffLoan}>payoff</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default PayOffLoanModal;
