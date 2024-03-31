import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import SignIn from './components/SignIn';
import ViewAccounts from './components/ViewAccounts';
import ViewLoans from './components/ViewLoans';
import ManageUsers from './components/ManageUsers';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Navbar, Nav, Container } from 'react-bootstrap';

function App() {
    const isAuthenticated = true;//sessionStorage.getItem('authToken'); // You can refine this check based on your auth logic


    return (
        <Router>
            <div>
                {isAuthenticated && (
                    <Navbar bg="light" expand="lg">
                        <Container>
                            <Navbar.Brand href="/">Banking Employee Portal</Navbar.Brand>
                            <Nav className="me-auto">
                                <Nav.Link as={Link} to="/view-accounts">View Accounts</Nav.Link>
                                <Nav.Link as={Link} to="/view-loans">View Loans</Nav.Link>
                                <Nav.Link as={Link} to="/manage-users">Manage Users</Nav.Link>
                            </Nav>
                        </Container>
                    </Navbar>
                )}
                <Container>
                    <Routes>
                        <Route path="/" element={<SignIn />} />
                        <Route path="/view-accounts" element={<ViewAccounts />} />
                        <Route path="/view-loans" element={<ViewLoans />} />
                        <Route path="/manage-users" element={<ManageUsers />} />
                    </Routes>
                </Container>
            </div>
        </Router>
    );
}

export default App;
