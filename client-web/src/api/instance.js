import axios from "axios";

export const instance = axios.create({
  headers: {
    Authorization: "Bearer " + sessionStorage.getItem("authToken"),
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS",
  },
});