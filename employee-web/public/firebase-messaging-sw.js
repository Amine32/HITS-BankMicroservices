importScripts(
  "https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js"
);
importScripts(
  "https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js"
);

// Initialize the Firebase app in the service worker by passing the generated config
var firebaseConfig = {
  // apiKey: "AIzaSyD217zb8uT6uzKpqXaGFc4c0-DqyBmVdrA",
  // authDomain: "banking-microservice-hits.firebaseapp.com",
  // projectId: "banking-microservice-hits",
  // storageBucket: "banking-microservice-hits.appspot.com",
  // messagingSenderId: "402528823927",
  // appId: "1:402528823927:web:e0234b4fcc950e3e383472",


  apiKey: "AIzaSyDLICkZEA7M71JJXOEhZ1JfCKs_fc3Oyjs",
  authDomain: "patterns-c5f7a.firebaseapp.com",
  projectId: "patterns-c5f7a",
  storageBucket: "patterns-c5f7a.appspot.com",
  messagingSenderId: "1098713744497",
  appId: "1:1098713744497:web:65e673052176073ad747d0",
  measurementId: "G-HK2QXTLQ6N"
};

firebase.initializeApp(firebaseConfig);

// Retrieve firebase messaging
const messaging = firebase.messaging();

messaging.onBackgroundMessage(function(payload) {
  console.log('Received background message ', payload);
 // Customize notification here
  const notificationTitle = payload.notification.title;
  const notificationOptions = {
    body: payload.notification.body,
  };

  self.registration.showNotification(notificationTitle,
    notificationOptions);
});