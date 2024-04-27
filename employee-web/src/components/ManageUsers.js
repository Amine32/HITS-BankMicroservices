import React, {useEffect, useState} from 'react';
import { instance } from '../api/instance';
import {Table, Button, Col, Row} from 'react-bootstrap';
import {PlusCircleFill} from "react-bootstrap-icons";
import { Toast, ToastContainer } from 'react-bootstrap';
import CreateUserModal from "./CreateUserModal";

function ManageUsers() {
    const [users, setUsers] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [toast, setToast] = useState({ show: false, message: '', variant: '' });

    const addUser = (newUser) => {
        setUsers([...users, newUser]);
    }

    useEffect(() => {
        // Fetch the users from your API
        instance.get('http://localhost:8080/user/api/users', {withCredentials: true})
            .then(response => setUsers(response.data))
            .catch(error => console.error('Error fetching users', error));
    }, []);

    // Function to handle the opening and closing of the modal
    const handleShowModal = () => setShowModal(true);
    const handleCloseModal = () => setShowModal(false);

    const handleShowToast = (message, variant = 'success') => {
        setToast({ show: true, message, variant });
    };

    const handleCloseToast = () => {
        setToast({ ...toast, show: false });
    };

    const handleBlockToggle = (userId, isActive) => {
        // Toggle user block status
        instance.put(`http://localhost:8080/user/api/users/${userId}/block`, {isActive: !isActive}, {withCredentials: true})
            .then(() => {
                // Update the local state to reflect the change
                setUsers(users.map(user => {
                    if (user.id === userId) {
                        return {...user, isActive: !user.isActive};
                    }
                    handleShowToast('User block status updated successfully!', 'success');
                    return user;
                }));
            })
            .catch(error => console.error('Error updating user status', error));
    };

    return (
        <>
            <div>
                <h2>Manage Users</h2>
                <Row className="justify-content-md-center">
                    <Col md="12" className="text-right mb-3">
                        <Button className='app__button' size="lg" onClick={handleShowModal}>
                            <PlusCircleFill/> Create New User
                        </Button>
                    </Col>
                </Row>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.map((user) => (
                        <tr key={user.id}>
                            <td>{user.email}</td>
                            <td>{user.role}</td>
                            <td>{user.isActive ? 'Blocked' : 'Active'}</td>
                            <td>
                                <Button className='app__button' onClick={() => handleBlockToggle(user.id, user.isActive)}>
                                    {user.isActive ? 'Unblock' : 'Block'}
                                </Button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            </div>
            <CreateUserModal show={showModal} handleClose={handleCloseModal} handleShowToast={handleShowToast} addUser={addUser}/>
            <ToastContainer position="top-end" className="p-3">
                <Toast onClose={handleCloseToast} show={toast.show} delay={3000} autohide bg={toast.variant}>
                    <Toast.Body>{toast.message}</Toast.Body>
                </Toast>
            </ToastContainer>
        </>
    );
}

export default ManageUsers;
