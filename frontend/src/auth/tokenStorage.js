const AUTH_TOKEN_KEY = "bikeMaintenanceAuthToken";

export function saveAuthToken(token) {
  if (!token) {
    throw new Error("An authentication token is required.");
  }

  localStorage.setItem(AUTH_TOKEN_KEY, token);
}

export function loadAuthToken() {
  return localStorage.getItem(AUTH_TOKEN_KEY);
}

export function removeAuthToken() {
  localStorage.removeItem(AUTH_TOKEN_KEY);
}