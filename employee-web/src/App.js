import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import SignIn from "./components/SignIn";
import ViewAccounts from "./components/ViewAccounts";
import ViewLoans from "./components/ViewLoans";
import ManageUsers from "./components/ManageUsers";
import "bootstrap/dist/css/bootstrap.min.css";
import { Navbar, Nav, Container } from "react-bootstrap";
import { useTheme } from "./hooks/use-theme";
import "./App.css";

function App() {
  const { theme, setTheme } = useTheme();

  const handleLightThemeClick = () => {
    setTheme("light");
  };
  const handleDarkThemeClick = () => {
    setTheme("dark");
  };

  const isAuthenticated = true; //sessionStorage.getItem('authToken'); // You can refine this check based on your auth logic

  return (
    <Router>
      <div>
        {isAuthenticated && (
          <Navbar className="app__nav" expand="lg">
            <Container>
              <Navbar.Brand href="/">Banking Employee Portal</Navbar.Brand>
              <Navbar.Toggle aria-controls="basic-navbar-nav" />
              <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="me-auto">
                <Nav.Link as={Link} to="/view-accounts">
                  View Accounts
                </Nav.Link>
                <Nav.Link as={Link} to="/view-loans">
                  View Loans
                </Nav.Link>
                <Nav.Link as={Link} to="/manage-users">
                  Manage Users
                </Nav.Link>
                <div className="btn-group ms-3" role="group">
                    <button type="button" className="btn btn-outline-info" onClick={handleLightThemeClick}>
                      Light
                    </button>
                    <button type="button" className="btn btn-outline-primary" onClick={handleDarkThemeClick}>
                      Dark
                    </button>
                  </div>
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
            <Route path="/manage-users" element={<ManageUsers />} />
          </Routes>
        </Container>
      </div>
    </Router>
  );
}

export default App;
