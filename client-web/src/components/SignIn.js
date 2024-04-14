import React, { useState } from 'react';
import axios from 'axios';
import { Card, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { useNavigate } from "react-router-dom";
import decodeJWTAndSave from "../helper/jwtDecode"

function SignIn() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    console.log(sessionStorage.getItem("userId"));
    const handleSubmit = (event) => {
        event.preventDefault();
        setLoading(true);
        setError('');

        const userData = {
            email: email,
            password: password
        };

        axios.post('http://localhost:8080/user/api/users/authenticate', userData, {
            headers: { 'Content-Type': 'application/json' },
            withCredentials: true
        })
            .then(response => {
                sessionStorage.setItem('authToken', response.data.token); 
                setLoading(false);
                decodeJWTAndSave(response.data.token);
                // Based on response structure; redirect as necessary
                navigate('/view-accounts');
            })
            .catch(error => {
                setLoading(false);
                setError('Authentication failed. Please check your credentials.');
                console.error('Authentication failed!', error);
            });
    };

    return (
        <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
            <Card style={{ width: '22rem' }}>
                <Card.Body>
                    <Card.Title>Sign In</Card.Title>
                    {error && <Alert variant="danger">{error}</Alert>}
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3" controlId="formBasicEmail">
                            <Form.Label>Email</Form.Label>
                            <Form.Control type="email" placeholder="Enter email" value={email} onChange={e => setEmail(e.target.value)} required />
                        </Form.Group>
                        <Form.Group className="mb-3" controlId="formBasicPassword">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} required />
                        </Form.Group>
                        <Button className='app__button' type="submit" disabled={loading}>
                            {loading ? <><Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true" /> Loading...</> : 'Sign In'}
                        </Button>
                    </Form>
                </Card.Body>
            </Card>
        </div>
    );
}

export default SignIn;
