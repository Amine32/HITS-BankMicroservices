import React, {useState, useEffect} from 'react';
import {Button, Table} from 'react-bootstrap';
import { instance } from '../api/instance';
import ApplyLoanModal from './ApplyLoanModal';
import generateIdempotencyKey from '../helper/Idempotency';

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
        const ownerId = sessionStorage.getItem('userId');
        if (ownerId) {
            instance.get(`http://localhost:8080/loan/api/loans/user/${ownerId}`, {withCredentials: true})
                .then(response => {
                    setLoans(response.data);
                })
                .catch(error => {
                    console.error('Error fetching loans', error);
                });
        }
    };

    const fetchLoanRates = () => {
        instance.get('http://localhost:8080/loan/api/rates/all', {withCredentials: true})
            .then(response => {
                const sortedLoanRates = response.data.sort((a, b) => a.id - b.id);
                setLoanRates(sortedLoanRates);
            })
            .catch(error => {
                console.error('Error fetching loan rates', error);
            });
    };

    const payOffLoan = (loanId) => {
        instance.post(`http://localhost:8080/loan/api/loans/payoff/${loanId}`, {withCredentials: true}, {
            headers: {
                'Content-Type': 'application/json',
                "Idempotency-Key": generateIdempotencyKey(),
            }
        })
            .then(response => {
                if (response.status === 200) {
                    fetchLoans(); // Refresh the loans list after successful payoff
                }
            })
            .catch(error => {
                console.error('Error paying off loan', error);
            });
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
                            <Button className='app__button' onClick={() => payOffLoan(loan.id)}>Pay Off</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

            <Button className='app__button' onClick={() => setShowApplyLoanModal(true)}>
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