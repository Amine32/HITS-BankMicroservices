import React, { useState, useEffect } from 'react';
import { Button, Table} from 'react-bootstrap';
import axios from 'axios';
import ApplyLoanModal from './ApplyLoanModal';

function ViewLoans() {
    const [loans, setLoans] = useState([]);
    const [loanRates, setLoanRates] = useState([]);
    const [showApplyLoanModal, setShowApplyLoanModal] = useState(false);

    useEffect(() => {
        fetchLoans();
        fetchLoanRates();
    }, []);

    const fetchLoans = async () => {
        // Endpoint to fetch loans for the customer
        const response = await axios.get('http://localhost:8080/loan/api/loans/user/{userId}');
        setLoans(response.data);
    };

    const fetchLoanRates = async () => {
        // Endpoint to fetch available loan rates
        const response = await axios.get('/api/loan-rates');
        setLoanRates(response.data);
    };

    return (
        <>
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
                        <td>${loan.amountOwed.toFixed(2)}</td>
                        <td>${loan.dailyPayment.toFixed(2)}</td>
                        <td>
                            {/* Implement pay off functionality */}
                            <Button variant="success" onClick={() => payOffLoan(loan.id)}>Pay Off</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

            <Button variant="primary" onClick={() => setShowApplyLoanModal(true)}>
                Apply for New Loan
            </Button>

            <ApplyLoanModal
                show={showApplyLoanModal}
                onHide={() => setShowApplyLoanModal(false)}
                loanRates={loanRates}
                onLoanApplied={fetchLoans}
            />
        </>
    );
}

export default ViewLoans;
