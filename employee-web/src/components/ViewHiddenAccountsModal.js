import React, { useState, useEffect } from "react";
import { instance } from "../api/instance";
import { Modal, Button, ListGroup } from "react-bootstrap";

function ViewHiddenAccounts({ accountId, show, onHide }) {
  const [accountIds, setAccountIds] = useState([]);

  useEffect(() => {
    instance
      .get(`http://localhost:8080/user/api/userPreferences/${accountId}`, {
        withCredentials: true,
      })
      .then((response) => {
        setAccountIds(response.data.hiddenAccountIds);
      })
      .catch((error) => {
        console.error("Error fetching accounts", error);
      });
  }, [accountId]);

  return (
      <Modal show={show} onHide={onHide}>
        <Modal.Header className="app__modal" closeButton>
          <Modal.Title>Hidden Accounts of {accountId}</Modal.Title>
        </Modal.Header>
        <Modal.Body className="app__modal">
          <ListGroup>
            {accountIds.map((account) => (
              <ListGroup.Item>Account ID: {account}</ListGroup.Item>
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

export default ViewHiddenAccounts;
