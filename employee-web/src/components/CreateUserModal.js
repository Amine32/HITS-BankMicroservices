import React, { useState } from 'react';
import { Modal, Form, Button } from 'react-bootstrap';
import { instance } from "../api/instance"
import generateIdempotencyKey from "../helper/Idempotency";

const CreateUserModal = ({ show, handleClose, handleShowToast, addUser }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('CLIENT'); // This can be an array if multiple roles per user are allowed.

    const handleCreateUser = (event) => {
        event.preventDefault();

        // Ensure role is sent as an array to match the backend expectation.
        const roles = [role]; // Wrap the role in an array.

        instance.post('http://localhost:8080/user/api/users', { email, password, roles }, {
            headers: {
                'Content-Type': 'application/json',
                "Idempotency-Key": generateIdempotencyKey(),
            }
        })
            .then((response) => {
                handleClose(); // Close the modal
                handleShowToast('User created successfully', 'success');
                addUser(response.data);
            })
            .catch((error) => {
                console.error('There was an error!', error);
                handleShowToast('Failed to create user.', 'danger');
            });
    };

    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header className='app__modal' closeButton>
                <Modal.Title>Create New User</Modal.Title>
            </Modal.Header>
            <Modal.Body className='app__modal'>
                <Form onSubmit={handleCreateUser}>
                    <Form.Group className="mb-3">
                        <Form.Label>Email</Form.Label>
                        <Form.Control
                            type="email"
                            placeholder="Enter email"
                            value={email}
                            onChange={e => setEmail(e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={e => setPassword(e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Role</Form.Label>
                        <Form.Select value={role} onChange={e => setRole(e.target.value)}>
                            <option value="CLIENT">Client</option>
                            <option value="EMPLOYEE">Employee</option>
                        </Form.Select>
                    </Form.Group>
                    <Button className='app__button' variant="primary" type="submit">
                        Create User
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>
    );
};

export default CreateUserModal;
