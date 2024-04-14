import React, { useState, useEffect } from 'react';
import { Modal, Form, Button } from 'react-bootstrap';
import axios from 'axios';

const EditRate = ({ rate, show, onHide, handleShowToast, updateRate }) => {
    const [name, setName] = useState('');
    const [interestRate, setInterestRate] = useState('');
    const [termLength, setTermLength] = useState('');

    useEffect(() => {
        if (rate) {
            setName(rate.name);
            setInterestRate(rate.interestRate);
            setTermLength(rate.termLength);
        }
    }, [rate]);

    const handleUpdateRate = (event) => {
        event.preventDefault();

        const updatedRate = {
            name: name,
            interestRate: interestRate,
            termLength: termLength
        };

        axios.put(`http://localhost:8080/loan/api/rates/${rate.id}`, updatedRate)
            .then((response) => {
                onHide(); // Close the modal
                handleShowToast('Rate updated successfully', 'success');
                updateRate(response.data);
            })
            .catch((error) => {
                console.error('There was an error!', error);
                handleShowToast('Failed to update rate.', 'danger');
            });
    };

    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header className='app__modal' closeButton>
                <Modal.Title>Edit Rate</Modal.Title>
            </Modal.Header>
            <Modal.Body className='app__modal'>
                <Form onSubmit={handleUpdateRate}>
                    <Form.Group className="mb-3">
                        <Form.Label>Name</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter name"
                            value={name}
                            onChange={e => setName(e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Interest Rate</Form.Label>
                        <Form.Control
                            type="number"
                            placeholder="Enter interest rate"
                            value={interestRate}
                            onChange={e => setInterestRate(e.target.value)}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Term Length</Form.Label>
                        <Form.Control
                            type="number"
                            placeholder="Enter term length"
                            value={termLength}
                            onChange={e => setTermLength(e.target.value)}
                        />
                    </Form.Group>
                    <Button className='app__modal' type="submit">
                        Update Rate
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>
    );
};

export default EditRate;