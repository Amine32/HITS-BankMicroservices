import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Table } from 'react-bootstrap';
import { useNavigate } from "react-router-dom";

function ViewAccounts() {
    const [accounts, setAccounts] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // Replace URL with your API endpoint
        axios.get('http://localhost:8080/api/accounts', {
            headers: {
                // Include any necessary headers
            }
        }).then(response => {
            setAccounts(response.data);
        }).catch(error => {
            console.error('Error fetching accounts', error);
        });
    }, []);

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
                            <button onClick={() => navigate(`/account-details/${account.id}`)}>View Details</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </div>
    );
}

export default ViewAccounts;
