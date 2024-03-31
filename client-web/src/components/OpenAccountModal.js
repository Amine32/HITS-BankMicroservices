import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import axios from 'axios';

function OpenAccountModal({ show, onHide, onAccountCreated, onAlert }) {
    const ownerId = sessionStorage.getItem('userId'); // Or fetch from context/global state

    const handleOpenAccount = () => {
        axios.post(`http://localhost:8080/core/api/accounts/${ownerId}`)
            .then(() => {
                onAccountCreated();
                onAlert('Account created successfully!', 'success');
            })
            .catch(error => {
                onAlert(`Error creating account: ${error.message}`, 'danger');
            })
            .finally(onHide);
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Open New Account</Modal.Title>
            </Modal.Header>
            <Modal.Body>Confirm to open a new account.</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>Cancel</Button>
                <Button variant="primary" onClick={handleOpenAccount}>Open Account</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default OpenAccountModal;
