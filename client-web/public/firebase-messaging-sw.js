importScripts(
  "https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js"
);
importScripts(
  "https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js"
);

// Initialize the Firebase app in the service worker by passing the generated config
var firebaseConfig = {
  apiKey: "AIzaSyD217zb8uT6uzKpqXaGFc4c0-DqyBmVdrA",
  authDomain: "banking-microservice-hits.firebaseapp.com",
  projectId: "banking-microservice-hits",
  storageBucket: "banking-microservice-hits.appspot.com",
  messagingSenderId: "402528823927",
  appId: "1:402528823927:web:e0234b4fcc950e3e383472",
};

firebase.initializeApp(firebaseConfig);

// Retrieve firebase messaging
const messaging = firebase.messaging();

messaging.onBackgroundMessage(function (payload) {
  console.log("Received background message ", payload);

  const notificationTitle = payload.notification.title;
  const notificationOptions = {
    body: payload.notification.body,
  };

  self.registration.showNotification(notificationTitle, notificationOptions);
});
