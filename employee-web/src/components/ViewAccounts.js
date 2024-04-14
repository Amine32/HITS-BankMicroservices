import React, {useState, useEffect} from 'react';
import axios from 'axios';
import {Table} from 'react-bootstrap';
import {useNavigate} from "react-router-dom";
import ViewTransactionsModal from "./ViewTransactionsModal";

function ViewAccounts() {
    const [accounts, setAccounts] = useState([]);
    const [selectedAccountId, setSelectedAccountId] = useState(null);
    const [showTransactionsModal, setShowTransactionsModal] = useState(false);

    useEffect(() => {
        axios.get('http://localhost:8080/core/api/accounts/all')
            .then(response => {
                setAccounts(response.data);
            }).catch(error => {
            console.error('Error fetching accounts', error);
        });
    }, []);

    const handleViewTransactions = (accountId) => {
        setSelectedAccountId(accountId);
        setShowTransactionsModal(true);
    };

    return (
        <div>
            <h2>Client Accounts</h2>
            <Table striped bordered hover>
                <thead>
                <tr>
                    <th>Account ID</th>
                    <th>Owner</th>
                    <th>Balance</th>
                    <th>Details</th>
                </tr>
                </thead>
                <tbody>
                {accounts.map(account => (
                    <tr key={account.id}>
                        <td>{account.id}</td>
                        <td>{account.ownerId}</td>
                        <td>{account.balance}</td>
                        <td>
                            <button className='app__button' onClick={() => handleViewTransactions(account.id)}>View Transactions</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
            <ViewTransactionsModal
                accountId={selectedAccountId}
                show={showTransactionsModal}
                onHide={() => setShowTransactionsModal(false)}
            />
        </div>
    );
}

export default ViewAccounts;
