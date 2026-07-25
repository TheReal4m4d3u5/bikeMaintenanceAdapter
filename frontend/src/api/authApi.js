async function authRequest(path, options = {}) {
  const response = await fetch(path, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...options.headers,
    },
  });

  const responseBody = await response.json().catch(() => null);

  if (!response.ok) {
    throw new Error(
      responseBody?.message ||
        `Authentication request failed with status ${response.status}`,
    );
  }

  return responseBody;
}

export async function registerAccount({
  email,
  displayName,
  password,
}) {
  return authRequest("/api/auth/register", {
    method: "POST",
    body: JSON.stringify({
      email,
      displayName,
      password,
    }),
  });
}

export async function loginAccount({ email, password }) {
  return authRequest("/api/auth/login", {
    method: "POST",
    body: JSON.stringify({
      email,
      password,
    }),
  });
}

export async function getCurrentUser(token) {
  if (!token) {
    throw new Error("Authentication token is required");
  }

  return authRequest("/api/auth/me", {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
}
