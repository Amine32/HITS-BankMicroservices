import React, { useState, useEffect } from "react";
import { instance } from "../api/instance";
import { Modal, Button, ListGroup } from "react-bootstrap";

function LoanOverdueModal({ show, onHide }) {
  const [loans, setLoans] = useState([]);
  const [users, setUsers] = useState([]);

  const fetchLoanOverdue = async (ownerId) => {
    // Endpoint to fetch loans for the customer
    if (ownerId) {
      instance
        .get(`http://localhost:8080/loan/api/loans/user/${ownerId}/overdue`, {
          withCredentials: true,
        })
        .then((response) => {
          setLoans(response.data);
          
        })
        .catch((error) => {
          console.error("Error fetching loans overdue", error);
        });
    }
  };

  const handleLoanOverdue = (ownerId) => {
    fetchLoanOverdue(ownerId);
    var overdueIds;
    {loans.map((loan) => (overdueIds.push(loan.id)))}
    return overdueIds;
  }

  useEffect(() => {
    instance
      .get("http://localhost:8080/user/api/users", { withCredentials: true })
      .then((response) => setUsers(response.data))
      .catch((error) => console.error("Error fetching users", error));
    //fetchLoanOverdue()
  }, []);

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header className="app__modal" closeButton>
        <Modal.Title>Overdue Loans</Modal.Title>
      </Modal.Header>
      <Modal.Body className="app__modal">
        <ListGroup>
          {users.map((user) => (
            <ListGroup.Item key={user.id}>User ID: {user.id}, loanIds: {handleLoanOverdue(user.id)}</ListGroup.Item>
          ))}
        </ListGroup>
      </Modal.Body>
      <Modal.Footer className="app__modal">
        <Button className="app__button" onClick={onHide}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default LoanOverdueModal;
