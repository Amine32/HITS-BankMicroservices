import React, {useEffect, useState} from 'react';
import {Alert, Button, Table} from 'react-bootstrap';
import { instance } from '../api/instance';
import WithdrawModal from './WithdrawModal';
import DepositModal from './DepositModal';
import OpenAccountModal from "./OpenAccountModal";
import CloseAccountModal from "./CloseAccountModal";
import ViewTransactionsModal from "./ViewTransactionsModal";
import TransferMoneyModal from './TransferMoneyModal';

function ViewAccounts() {
    const [accounts, setAccounts] = useState([]);
    const [selectedAccount, setSelectedAccount] = useState(null);
    const [alert, setAlert] = useState({show: false, message: '', variant: 'success'});
    const [showModal, setShowModal] = useState({
        openAccount: false,
        closeAccount: false,
        deposit: false,
        withdraw: false,
        transactions: false,
        transfer: false
    });

    useEffect(() => {
        fetchAccounts();
    }, []);

    const fetchAccounts = () => {
        const ownerId = sessionStorage.getItem('userId');
        if (ownerId) {
            instance.get(`http://localhost:8080/core/api/accounts/user/${ownerId}`, {withCredentials: true})
                .then(response => {
                    const sortedAccounts = response.data.sort((a, b) => a.id - b.id);
                    setAccounts(sortedAccounts);
                })
                .catch(error => {
                    console.error('Error fetching accounts', error);
                });
        }
    };

    const handleModalShow = (modalName, account = null) => {
        setShowModal({...showModal, [modalName]: true});
        setSelectedAccount(account);
    };

    const handleModalClose = (modalName) => {
        setShowModal({...showModal, [modalName]: false});
    };

    const handleAlertShow = (message, variant = 'success') => {
        setAlert({show: true, message, variant});
    };

    const handleAlertClose = () => {
        setAlert({show: false, message: '', variant: 'success'});
    };

    const handleAccountActionComplete = () => {
        handleModalClose();
        fetchAccounts(); // Refresh the accounts after any modal action
    };

    return (
        <div>
            {alert.show && (
                <Alert variant={alert.variant} onClose={handleAlertClose} dismissible>
                    {alert.message}
                </Alert>
            )}

            <Button
                className='app__button mt-2'
                onClick={() => handleModalShow('openAccount')}
            >
                Open New Account
            </Button>

            <Button
                className='app__button mt-2 ms-2'
                onClick={() => handleModalShow('transfer')}
            >
                Transfer between accounts
            </Button>

            <Table striped bordered hover className="mt-4">
                <thead>
                <tr>
                    <th>Account ID</th>
                    <th>Balance</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {accounts.map(account => (
                    <tr key={account.id}>
                        <td>{account.id}</td>
                        <td>{account.currency} {account.balance.toFixed(2)}</td>
                        <td>
                            <div className="d-flex justify-content-between">
                                <div>
                                    <Button
                                        className='app__button'
                                        onClick={() => handleModalShow('deposit', account)}
                                    >
                                        Deposit
                                    </Button>{'  '}
                                    <Button
                                        className='app__button'
                                        onClick={() => handleModalShow('withdraw', account)}
                                    >
                                        Withdraw
                                    </Button>
                                </div>
                                <div>
                                    <Button
                                        className='app__button'
                                        onClick={() => handleModalShow('transactions', account)}
                                    >
                                        View Transactions
                                    </Button>{' '}
                                    <Button
                                        className='app__button'
                                        onClick={() => handleModalShow('closeAccount', account)}
                                    >
                                        Close Account
                                    </Button>
                                </div>
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

            <OpenAccountModal
                show={showModal.openAccount}
                onHide={() => handleModalClose('openAccount')}
                onAccountCreated={() => handleAccountActionComplete()}
                onAlert={handleAlertShow}
            />

                <TransferMoneyModal
                accounts = {accounts}
                show={showModal.transfer}
                onHide={() => handleModalClose('transfer')}
                onTransfer={() => handleAccountActionComplete()}
                onAlert={handleAlertShow}/>

            {selectedAccount && (
                <>
                    <CloseAccountModal
                        account={selectedAccount}
                        show={showModal.closeAccount}
                        onHide={() => handleModalClose('closeAccount')}
                        onAccountClosed={() => handleAccountActionComplete()}
                        onAlert={handleAlertShow}
                    />
                    <DepositModal
                        account={selectedAccount}
                        show={showModal.deposit}
                        onHide={() => handleModalClose('deposit')}
                        onDeposit={() => handleAccountActionComplete()}
                        onAlert={handleAlertShow}
                    />
                    <WithdrawModal
                        account={selectedAccount}
                        show={showModal.withdraw}
                        onHide={() => handleModalClose('withdraw')}
                        onWithdraw={() => handleAccountActionComplete()}
                        onAlert={handleAlertShow}
                    />
                    <ViewTransactionsModal
                        account={selectedAccount}
                        show={showModal.transactions}
                        onHide={() => handleModalClose('transactions')}
                    />
                </>
            )}
        </div>
    );
}

export default ViewAccounts;