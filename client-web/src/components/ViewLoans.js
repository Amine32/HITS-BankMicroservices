import React, { useState, useEffect } from "react";
import { Button, Table } from "react-bootstrap";
import { instance } from "../api/instance";
import ApplyLoanModal from "./ApplyLoanModal";
import generateIdempotencyKey from "../helper/Idempotency";
import LoanOverdueModal from "./LoanOverdueModal";
import ClientRatingModal from "./ClientRatingModal";
import PayOffLoanModal from "./PayOffLoanModal";

function ViewLoans() {
  const [loans, setLoans] = useState([]);
  const [loanRates, setLoanRates] = useState([]);
  const [showApplyLoanModal, setShowApplyLoanModal] = useState(false);
  const [showOverdueLoanModal, setShowOverdueLoanModal] = useState(false);
  const [showClientRatring, setShowClientRatring] = useState(false);
  const [showPayOffLoan, setShowPayOffLoan] = useState(false);
  const [selectedLoan, setSelectedLoan] = useState(null);

  useEffect(() => {
    fetchLoans();
    fetchLoanRates();
  }, []);

  const handleLoanRepayShow = (loanId = null) => {
    setShowPayOffLoan(true);
    setSelectedLoan(loanId);
  };

  const fetchLoans = async () => {
    // Endpoint to fetch loans for the customer
    const ownerId = sessionStorage.getItem("userId");
    if (ownerId) {
      instance
        .get(`http://localhost:8080/loan/api/loans/user/${ownerId}`, {
          withCredentials: true,
        })
        .then((response) => {
          setLoans(response.data);
        })
        .catch((error) => {
          console.error("Error fetching loans", error);
        });
    }
  };

  const fetchLoanRates = () => {
    instance
      .get("http://localhost:8080/loan/api/rates/all", {
        withCredentials: true,
      })
      .then((response) => {
        const sortedLoanRates = response.data.sort((a, b) => a.id - b.id);
        setLoanRates(sortedLoanRates);
      })
      .catch((error) => {
        console.error("Error fetching loan rates", error);
      });
  };

  return (
    <>
      <Button
        className="app__button mt-2 mb-2"
        onClick={() => setShowOverdueLoanModal(true)}
      >
        Loan overdue
      </Button>
      <Button
        className="app__button mt-2 mb-2 ms-2"
        onClick={() => setShowClientRatring(true)}
      >
        Client Rating
      </Button>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Loan ID</th>
            <th>Amount Due</th>
            <th>Daily Payment</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {loans.map((loan) => (
            <tr key={loan.id}>
              <td>{loan.id}</td>
              <td>{loan.amountOwed.toFixed(2)} RUB</td>
              <td>{loan.dailyPayment.toFixed(2)} RUB</td>
              <td>
                <Button
                  className="app__button"
                  onClick={() => handleLoanRepayShow(loan.id)}
                >
                  Pay Off
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Button
        className="app__button"
        onClick={() => setShowApplyLoanModal(true)}
      >
        Apply for New Loan
      </Button>

      <ApplyLoanModal
        show={showApplyLoanModal}
        onHide={() => setShowApplyLoanModal(false)}
        loanRates={loanRates}
        onLoanApplied={fetchLoans}
      />

      <LoanOverdueModal
        show={showOverdueLoanModal}
        onHide={() => setShowOverdueLoanModal(false)}
      />

      <ClientRatingModal
        show={showClientRatring}
        onHide={() => setShowClientRatring(false)}
      />

      {selectedLoan && (
        <PayOffLoanModal
          show={showPayOffLoan}
          onHide={() => setShowPayOffLoan(false)}
          loanId={selectedLoan}
          fetchLoans={() => fetchLoans()}
        />
      )}
    </>
  );
}

export default ViewLoans;
