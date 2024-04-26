import React from "react";
import { Modal, Button, Form } from "react-bootstrap";
import { instance } from "../api/instance";
import generateIdempotencyKey from '../helper/Idempotency';

function OpenAccountModal({ show, onHide, onAccountCreated, onAlert }) {
  const ownerId = sessionStorage.getItem("userId");

  var isRUB = false;
  var isUSD = false;
  var isCNY = false;

  const onChangeRUB = () => {
    isRUB = !isRUB
    if(isUSD) {
        isUSD = false;
    }
    if(isCNY) {
        isCNY = false;
    }
  }

  const onChangeUSD = () => {
    isUSD = !isUSD
    if(isRUB) {
        isRUB = false;
    }
    if(isCNY) {
        isCNY = false;
    }
  }

  const onChangeCNY = () => {
    isCNY = !isCNY
    if(isRUB) {
        isRUB = false;
    }
    if(isUSD) {
        isUSD = false;
    }
  }

  function currencyToString() {
    if(isRUB) return "RUB"
    else if(isUSD) return "USD"
    else return "CNY"
  }

  const handleOpenAccount = () => {
    instance
      .post(`http://localhost:8080/core/api/accounts/${ownerId}`, currencyToString(), {
            headers: {
                'Content-Type': 'application/json',
                "Idempotency-Key": generateIdempotencyKey(),
            }
        })
      .then(() => {
        onAccountCreated();
        onAlert("Account created successfully!", "success");
      })
      .catch((error) => {
        onAlert(`Error creating account: ${error.message}`, "danger");
      })
      .finally(onHide);
  };

  return (
    <Modal show={show} onHide={onHide}>
        <Form>
      <Modal.Header className="app__modal" closeButton>
        <Modal.Title>Open New Account</Modal.Title>
      </Modal.Header>
      <Modal.Body className="app__modal">
        Confirm to open a new account and choose currency.
        
        <div>
          <Form.Check
            inline
            label="RUB"
            name="group1"
            type="radio"
            id="inline-radio-1"
            onClick={onChangeRUB}
          />
          <Form.Check
            inline
            label="USD"
            name="group1"
            type="radio"
            id="inline-radio-2"
            onClick={onChangeUSD}
          />
          <Form.Check
            inline
            label="CNY"
            name="group1"
            type="radio"
            id="inline-radio-3"
            onClick={onChangeCNY}
          />
        </div>
      </Modal.Body>
      <Modal.Footer className="app__modal">
        <Button className="app__button" onClick={onHide}>
          Cancel
        </Button>
        <Button className="app__button" onClick={handleOpenAccount}>
          Open Account
        </Button>
      </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default OpenAccountModal;
