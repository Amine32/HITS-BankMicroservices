import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import { instance } from '../api/instance';

function CloseAccountModal({ account, show, onHide, onAccountClosed, onAlert }) {
    const handleCloseAccount = () => {
        instance.delete(`http://localhost:8080/core/api/accounts/${account.id}`)
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
            <Modal.Header className='app__modal' closeButton>
                <Modal.Title>Close Account</Modal.Title>
            </Modal.Header>
            <Modal.Body className='app__modal'>Confirm to close the account.</Modal.Body>
            <Modal.Footer className='app__modal'>
                <Button className='app__button' onClick={onHide}>Cancel</Button>
                <Button className='app__button' onClick={handleCloseAccount}>Close Account</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default CloseAccountModal;
