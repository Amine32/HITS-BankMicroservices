import React, {useState, useEffect} from 'react';
import { instance } from '../api/instance';
import {Table} from 'react-bootstrap';
import ViewTransactionsModal from "./ViewTransactionsModal";
import ViewHiddenAccounts from './ViewHiddenAccountsModal';

function ViewAccounts() {
    const [accounts, setAccounts] = useState([]);
    const [selectedAccountId, setSelectedAccountId] = useState(null);
    const [showTransactionsModal, setShowTransactionsModal] = useState(false);
    const [showModalHiddenView, setShowModalHiddenView] = useState(false);

    useEffect(() => {
        instance.get('http://localhost:8080/core/api/accounts/all')
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

    const handleViewHiddenAccounts = (accountId) => {
        setSelectedAccountId(accountId);
        setShowModalHiddenView(true);
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
                            <button className='app__button ms-2' onClick={() => handleViewHiddenAccounts(account.ownerId)}>Hidden Accounts</button>
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
            <ViewHiddenAccounts
                accountId={selectedAccountId}
                show={showModalHiddenView}
                onHide={() => setShowModalHiddenView(false)}
            />
        </div>
    );
}

export default ViewAccounts;
