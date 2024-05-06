// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getMessaging, getToken, onMessage } from "firebase/messaging";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyD217zb8uT6uzKpqXaGFc4c0-DqyBmVdrA",
  authDomain: "banking-microservice-hits.firebaseapp.com",
  projectId: "banking-microservice-hits",
  storageBucket: "banking-microservice-hits.appspot.com",
  messagingSenderId: "402528823927",
  appId: "1:402528823927:web:e0234b4fcc950e3e383472",
  measurementId: "G-SD63JMNQ5W"

  // apiKey: "AIzaSyDLICkZEA7M71JJXOEhZ1JfCKs_fc3Oyjs",
  // authDomain: "patterns-c5f7a.firebaseapp.com",
  // projectId: "patterns-c5f7a",
  // storageBucket: "patterns-c5f7a.appspot.com",
  // messagingSenderId: "1098713744497",
  // appId: "1:1098713744497:web:65e673052176073ad747d0",
  // measurementId: "G-HK2QXTLQ6N"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
//const analytics = getAnalytics(app);
const messaging = getMessaging(app);

export const requestForToken = () => {
  return getToken(messaging, {vapidKey: 'BKQoZpCLamw4JRo2xF7i3V-3pTAse8sB1usXrMbTG17HuEQ_Z2ulucSmrgfulGw_c6kIfR_HdgidNlJC1hiG-uQ'}).then((currentToken) => {
   // return getToken(messaging, {vapidKey: 'BPIcChBqO9X8F9iTlRO6rqMhqktu_wFG3ja9x-_wBTHc5fdnliq6-5dWW_fba51HixSnGg7EWr_HpZo7AT1K9sk'}).then((currentToken) => {
    if (currentToken) {
      console.log('current token for client: ', currentToken);
      // Track the token -> client mapping, by sending to backend server
      // show on the UI that permission is secured
    } else {
      console.log('No registration token available. Request permission to generate one.');
      // shows on the UI that permission is required 
    }
  }).catch((err) => {
    console.log('An error occurred while retrieving token. ', err);
    // catch error while creating client token
  });
}

export const onMessageListener = () =>
  new Promise((resolve) => {
    onMessage(messaging, (payload) => {
      console.log("payload", payload)
      resolve(payload);
    });
  });