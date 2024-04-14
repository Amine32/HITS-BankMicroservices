export default function decodeJWTAndSave(token) {
    // Split the token into its components
    const parts = token.split('.');
    if (parts.length !== 3) {
      throw new Error('Invalid JWT: The token must consist of three parts.');
    }
  
    // Decode the payload from Base64Url
    const payload = parts[1];
    const decodedPayload = JSON.parse(window.atob(payload.replace(/-/g, '+').replace(/_/g, '/')));
  
    // Log the decoded payload if necessary (optional)
    console.log(decodedPayload);
  
    // Store userId and authorities in storage
    if (decodedPayload.userId) {
      sessionStorage.setItem('userId', decodedPayload.userId.toString());
    }
    if (decodedPayload.authorities) {
      sessionStorage.setItem('roles', JSON.stringify(decodedPayload.authorities));
    }
  }