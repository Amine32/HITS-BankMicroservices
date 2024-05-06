import React, { useState, useEffect } from "react";
import { instance } from "../api/instance";
import { ListGroup, Button, Card, Table } from "react-bootstrap";
import LoanDetails from "./LoanDetails";
import CreateRate from "./CreateRate";
import EditRate from "./EditRate"; // Import the LoanDetails component

function ViewLoans() {
  const [loans, setLoans] = useState([]);
  const [rates, setRates] = useState([]);
  const [selectedLoan, setSelectedLoan] = useState(null);
  const [selectedRate, setSelectedRate] = useState(null);
  const [showCreateRateModal, setShowCreateRateModal] = useState(false);
  const [showEditRateModal, setShowEditRateModal] = useState(false);

  useEffect(() => {
    instance
      .get("http://localhost:8080/loan/api/loans/all")
      .then((response) => {
        setLoans(response.data);
      })
      .catch((error) => {
        console.error("Error fetching loans", error);
      });

    instance
      .get("http://localhost:8080/loan/api/rates/all")
      .then((response) => {
        setRates(response.data);
      })
      .catch((error) => {
        console.error("Error fetching rates", error);
      });
  }, []);

  const handleCreateRate = () => {
    setShowCreateRateModal(true);
  };

  const handleEditRate = (rate) => {
    setSelectedRate(rate);
    setShowEditRateModal(true);
  };

  return (
    <div>
      <Card style={{ width: "50rem", margin: "2rem auto" }}>
        <Card.Header>
          <strong>Loan List</strong>
        </Card.Header>
        <Card.Body>
          {selectedLoan ? (
            <LoanDetails
              loan={selectedLoan}
              clearSelection={() => setSelectedLoan(null)}
            />
          ) : (
            <ListGroup variant="flush">
              {loans.map((loan) => (
                <ListGroup.Item key={loan.id}>
                  Loan ID: {loan.id} - Amount: {loan.amount}
                  <Button
                    className="app__button"
                    onClick={() => setSelectedLoan(loan)}
                    style={{ float: "right" }}
                  >
                    View Details
                  </Button>
                </ListGroup.Item>
              ))}
            </ListGroup>
          )}
        </Card.Body>
      </Card>
      <Card style={{ width: "50rem", margin: "2rem auto" }}>
        <Card.Header>
          <strong>Rates</strong>
          <Button
            className="app__button"
            onClick={handleCreateRate}
            style={{ float: "right" }}
          >
            Create Rate
          </Button>
        </Card.Header>
        <Card.Body>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>Name</th>
                <th>Interest Rate</th>
                <th>Term Length</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {rates.map((rate) => (
                <tr key={rate.id}>
                  <td>{rate.name}</td>
                  <td>{rate.interestRate}</td>
                  <td>{rate.termLength}</td>
                  <td>
                    <Button
                      className="app__button"
                      onClick={() => handleEditRate(rate)}
                    >
                      Edit
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Card.Body>
      </Card>
      <CreateRate
        show={showCreateRateModal}
        onHide={() => setShowCreateRateModal(false)}
      />
      <EditRate
        rate={selectedRate}
        show={showEditRateModal}
        onHide={() => setShowEditRateModal(false)}
      />
    </div>
  );
}

export default ViewLoans;
