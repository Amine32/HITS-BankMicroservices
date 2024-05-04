import React, { useState } from "react";
import { Modal, Button, Form, Dropdown } from "react-bootstrap";
import { instance } from "../api/instance";
import generateIdempotencyKey from '../helper/Idempotency';

function TransferMoneyModal({ accounts, show, onHide, onTransfer, onAlert }) {
  const [fromAccountId, setAccountFrom] = useState("");
  const [toAccountId, setAccountTo] = useState("");
  const [amount, setAmount] = useState('');

  const handleTransfer = () => {
    instance
      .post(`http://localhost:8080/core/api/accounts/transfer`, {
        fromAccountId,
        toAccountId,
        amount,
      }, {
        headers: {
            'Content-Type': 'application/json',
            "Idempotency-Key": generateIdempotencyKey(),
        }
    })
      .then(() => {
        onTransfer();
        onAlert("Transfer successful!", "success");
      })
      .catch((error) => {
        onAlert(`Error depositing money: ${error.message}`, "danger");
      })
      .finally(onHide);
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header className="app__modal" closeButton>
        <Modal.Title>Choose accounts to transfer</Modal.Title>
      </Modal.Header>
      <Modal.Body className="app__modal">
        <Form>
          <Form.Group>
            <Form.Label>Select account from which</Form.Label>
            <Dropdown>
              <Dropdown.Toggle variant="success" id="dropdown-basic">
                {fromAccountId || "Select Account"}
              </Dropdown.Toggle>
              <Dropdown.Menu>
                {accounts.map((account) => (
                  <Dropdown.Item
                    key={account.id}
                    onClick={() => setAccountFrom(account.id)}
                  >
                    {account.id}
                  </Dropdown.Item>
                ))}
              </Dropdown.Menu>
            </Dropdown>
          </Form.Group>

          <Form.Group>
            <Form.Label>Enter account to which</Form.Label>
            <Form.Control
              value={toAccountId}
              onChange={(e) => setAccountTo(e.target.value)}
              placeholder="Enter account to which"
            />
          </Form.Group>

          <Form.Group>
            <Form.Label>Amount</Form.Label>
            <Form.Control
              type="number"
              min="0"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder="Enter amount to transfer"
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer className="app__modal">
        <Button className="app__button" onClick={onHide}>
          Cancel
        </Button>
        <Button className="app__button" onClick={handleTransfer}>
          Transfer
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default TransferMoneyModal;
