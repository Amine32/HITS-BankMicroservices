import React, {useState, useEffect} from "react";
import { Modal, Button } from "react-bootstrap";
import { instance } from "../api/instance";

function ClientRatingModal({ show, onHide }) {
  const [clientRating, setClientRating] = useState();

  const fetchClientRating = async () => {
    const ownerId = sessionStorage.getItem("userId");
    if (ownerId) {
      instance
        .get(`http://localhost:8080/loan/api/loans/user/${ownerId}/credit-rating`, {
          withCredentials: true,
        })
        .then((response) => {
          setClientRating(response.data);
        })
        .catch((error) => {
          console.error("Error fetching client rating", error);
        });
    }
  };

  useEffect(() => {
    fetchClientRating();
  }, []);

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header className="app__modal" closeButton>
        <Modal.Title>Credit Rating</Modal.Title>
      </Modal.Header>
      <Modal.Body className="app__modal">
        {clientRating}
      </Modal.Body>
      <Modal.Footer className="app__modal">
        <Button className="app__button" onClick={onHide}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default ClientRatingModal;
