import React, { useEffect, useState } from "react";
import { Alert, Button, Table } from "react-bootstrap";
import { instance } from "../api/instance";
import WithdrawModal from "./WithdrawModal";
import DepositModal from "./DepositModal";
import OpenAccountModal from "./OpenAccountModal";
import CloseAccountModal from "./CloseAccountModal";
import ViewTransactionsModal from "./ViewTransactionsModal";
import TransferMoneyModal from "./TransferMoneyModal";
import generateIdempotencyKey from "../helper/Idempotency";

function ViewAccounts() {
  const [accounts, setAccounts] = useState([]);
  const [preferences, setPreferences] = useState();
  const [hiddenAccounts, setHiddenAcсounts] = useState([]);
  const [selectedAccount, setSelectedAccount] = useState(null);
  const [alert, setAlert] = useState({
    show: false,
    message: "",
    variant: "success",
  });
  const [showModal, setShowModal] = useState({
    openAccount: false,
    closeAccount: false,
    deposit: false,
    withdraw: false,
    transactions: false,
    transfer: false,
  });

  useEffect(() => {
    fetchPreferences();
    fetchAccounts();
  }, []);

  const handlePreferences = () => {
    const ownerId = sessionStorage.getItem("userId");
    instance
      .post(
        `http://localhost:8080/user/api/userPreferences/${ownerId}`,
        {
          userId: preferences.userId,
          theme: preferences.theme,
          hiddenAccountIds: hiddenAccounts,
        },
        {
          headers: {
            "Content-Type": "application/json",
            "Idempotency-Key": generateIdempotencyKey(),
          },
        }
      )
      .catch((error) => {
        console.error("Error fetching preferences ", error);
      });
  };

  const onHideAccount = (id) => {
    setHiddenAcсounts((hiddenAccounts) => [...hiddenAccounts, id]);
    handlePreferences();
  };

  const fetchPreferences = () => {
    const ownerId = sessionStorage.getItem("userId");
    if (ownerId) {
      instance
        .get(`http://localhost:8080/user/api/userPreferences/${ownerId}`, {
          withCredentials: true,
        })
        .then((response) => {
          setHiddenAcсounts(response.data.hiddenAccountIds);
          setPreferences(response.data);
        })
        .catch((error) => {
          console.error("Error fetching preferences ", error);
        });
    }
  };

  const fetchAccounts = () => {
    const ownerId = sessionStorage.getItem("userId");
    if (ownerId) {
      instance
        .get(`http://localhost:8080/core/api/accounts/user/${ownerId}`, {
          withCredentials: true,
        })
        .then((response) => {
          const sortedAccounts = response.data.sort((a, b) => a.id - b.id);
          const result = sortedAccounts.filter(
            (el) => !hiddenAccounts.includes(el.id)
          );
          //console.log(result);
          setAccounts(result); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        })
        .catch((error) => {
          console.error("Error fetching accounts", error);
        });
    }
  };

  // const finalAccounts = () => {
  //     var finAcc;
  //     for (var i=0; i<accounts.length; i++) {
  //        if (preferences.indexOf( accounts[i] ) == -1)
  //         {
  //             finAcc.push(accounts[i])
  //         }
  //     }
  //     console.log(finAcc)
  //     return finAcc;
  // }

  const handleModalShow = (modalName, account = null) => {
    setShowModal({ ...showModal, [modalName]: true });
    setSelectedAccount(account);
  };

  const handleModalClose = (modalName) => {
    setShowModal({ ...showModal, [modalName]: false });
  };

  const handleAlertShow = (message, variant = "success") => {
    setAlert({ show: true, message, variant });
  };

  const handleAlertClose = () => {
    setAlert({ show: false, message: "", variant: "success" });
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
        className="app__button mt-2"
        onClick={() => handleModalShow("openAccount")}
      >
        Open New Account
      </Button>

      <Button
        className="app__button mt-2 ms-2"
        onClick={() => handleModalShow("transfer")}
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
          {accounts.map((account) => (
            <tr key={account.id}>
              <td>{account.id}</td>
              <td>
                {account.currency} {account.balance.toFixed(2)}
              </td>
              <td>
                <div className="d-flex justify-content-between">
                  <div>
                    <Button
                      className="app__button"
                      onClick={() => handleModalShow("deposit", account)}
                    >
                      Deposit
                    </Button>
                    {"  "}
                    <Button
                      className="app__button"
                      onClick={() => handleModalShow("withdraw", account)}
                    >
                      Withdraw
                    </Button>
                  </div>
                  <div>
                    <Button
                      className="app__button"
                      onClick={() => handleModalShow("transactions", account)}
                    >
                      View Transactions
                    </Button>{" "}
                    <Button
                      className="app__button"
                      onClick={() => handleModalShow("closeAccount", account)}
                    >
                      Close Account
                    </Button>
                    <Button
                      className="app__button ms-1"
                      onClick={() => onHideAccount(account.id)}
                    >
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        width="16"
                        height="16"
                        fill="currentColor"
                        className="bi bi-journal-minus"
                        viewBox="0 0 16 16"
                      >
                        <path
                          fillRule="evenodd"
                          d="M5.5 8a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1H6a.5.5 0 0 1-.5-.5"
                        />
                        <path d="M3 0h10a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2v-1h1v1a1 1 0 0 0 1 1h10a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H3a1 1 0 0 0-1 1v1H1V2a2 2 0 0 1 2-2" />
                        <path d="M1 5v-.5a.5.5 0 0 1 1 0V5h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1zm0 3v-.5a.5.5 0 0 1 1 0V8h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1zm0 3v-.5a.5.5 0 0 1 1 0v.5h.5a.5.5 0 0 1 0 1h-2a.5.5 0 0 1 0-1z" />
                      </svg>
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
        onHide={() => handleModalClose("openAccount")}
        onAccountCreated={() => handleAccountActionComplete()}
        onAlert={handleAlertShow}
      />

      <TransferMoneyModal
        accounts={accounts}
        show={showModal.transfer}
        onHide={() => handleModalClose("transfer")}
        onTransfer={() => handleAccountActionComplete()}
        onAlert={handleAlertShow}
      />

      {selectedAccount && (
        <>
          <CloseAccountModal
            account={selectedAccount}
            show={showModal.closeAccount}
            onHide={() => handleModalClose("closeAccount")}
            onAccountClosed={() => handleAccountActionComplete()}
            onAlert={handleAlertShow}
          />
          <DepositModal
            account={selectedAccount}
            show={showModal.deposit}
            onHide={() => handleModalClose("deposit")}
            onDeposit={() => handleAccountActionComplete()}
            onAlert={handleAlertShow}
          />
          <WithdrawModal
            account={selectedAccount}
            show={showModal.withdraw}
            onHide={() => handleModalClose("withdraw")}
            onWithdraw={() => handleAccountActionComplete()}
            onAlert={handleAlertShow}
          />
          <ViewTransactionsModal
            account={selectedAccount}
            show={showModal.transactions}
            onHide={() => handleModalClose("transactions")}
          />
        </>
      )}
    </div>
  );
}

export default ViewAccounts;
