import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import axios from 'axios';

function CloseAccountModal({ account, show, onHide, onAccountClosed, onAlert }) {
    const handleCloseAccount = () => {
        axios.delete(`http://localhost:8080/core/api/accounts/${account.id}`)
            .then(() => {
                onAccountClosed();
                onAlert('Account closed successfully!', 'success');
            })
            .catch(error => {
                onAlert(`Error closing account: ${error.message}`, 'danger');
            })
            .finally(onHide);
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>Close Account</Modal.Title>
            </Modal.Header>
            <Modal.Body>Confirm to close the account.</Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>Cancel</Button>
                <Button variant="danger" onClick={handleCloseAccount}>Close Account</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default CloseAccountModal;
