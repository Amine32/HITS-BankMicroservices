import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import SignIn from './components/SignIn';
import ViewAccounts from './components/ViewAccounts';
import ViewLoans from './components/ViewLoans';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Navbar, Nav, Container} from 'react-bootstrap';

function App() {
    const isAuthenticated = true;//sessionStorage.getItem('authToken'); // You can refine this check based on your auth logic


    return (
        <Router>
            <div>
                {isAuthenticated && (
                    <Navbar bg="light" expand="lg">
                        <Container>
                            <Navbar.Brand as={Link} to="/">Banking Client Portal</Navbar.Brand>
                            <Navbar.Toggle aria-controls="basic-navbar-nav" />
                            <Navbar.Collapse id="basic-navbar-nav">
                                <Nav className="me-auto">
                                    <Nav.Link as={Link} to="/view-accounts">View Accounts</Nav.Link>
                                    <Nav.Link as={Link} to="/view-loans">View Loans</Nav.Link>
                                </Nav>
                            </Navbar.Collapse>
                        </Container>
                    </Navbar>
                )}
                <Container>
                    <Routes>
                        <Route path="/" element={<SignIn />} />
                        <Route path="/view-accounts" element={<ViewAccounts />} />
                        <Route path="/view-loans" element={<ViewLoans />} />
                    </Routes>
                </Container>
            </div>
        </Router>
    );
}

export default App;
