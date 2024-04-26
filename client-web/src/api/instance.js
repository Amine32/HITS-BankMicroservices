import axios from "axios";

export const instance = axios.create({
  headers: {
    "Authorization": "Bearer " + sessionStorage.getItem("authToken"),
    "Idempotency-Key": generateIdempotencyKey(),
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS",
  },
});

function generateIdempotencyKey() {
  return crypto.randomUUID();
}