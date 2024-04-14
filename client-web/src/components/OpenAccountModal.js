import React from 'react';
import { Modal, Button } from 'react-bootstrap';
import { instance } from '../api/instance';

function OpenAccountModal({ show, onHide, onAccountCreated, onAlert }) {
    const ownerId = sessionStorage.getItem('userId'); // Or fetch from context/global state

    const handleOpenAccount = () => {
        instance.post(`http://localhost:8080/core/api/accounts/${ownerId}`)
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
            <Modal.Header className='app__modal' closeButton>
                <Modal.Title>Open New Account</Modal.Title>
            </Modal.Header>
            <Modal.Body className='app__modal'>Confirm to open a new account.</Modal.Body>
            <Modal.Footer className='app__modal'>
                <Button className='app__button' onClick={onHide}>Cancel</Button>
                <Button className='app__button' onClick={handleOpenAccount}>Open Account</Button>
            </Modal.Footer>
        </Modal>
    );
}

export default OpenAccountModal;
