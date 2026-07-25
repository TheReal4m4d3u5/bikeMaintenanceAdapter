import { loadAuthToken } from "./authApi";

let onUnauthorized = null;

/**
 * Registers a callback that runs when the backend returns HTTP 401.
 * AuthContext will eventually register its logout function here.
 */
export function setOnUnauthorized(callback) {
  onUnauthorized = callback;
}

/**
 * Sends a GraphQL request to the Spring Boot backend.
 *
 * @param {string} query GraphQL query or mutation
 * @param {Record<string, unknown>} variables GraphQL variables
 * @returns {Promise<any>} GraphQL response data
 */
export async function graphqlRequest(query, variables = {}) {
  const token = loadAuthToken();

  const headers = {
    "Content-Type": "application/json",
  };

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  let response;

  try {
    response = await fetch("/graphql", {
      method: "POST",
      headers,
      body: JSON.stringify({
        query,
        variables,
      }),
    });
  } catch (error) {
    throw new Error(
      "Unable to connect to the Bike Maintenance API.",
      { cause: error }
    );
  }

  if (response.status === 401) {
    onUnauthorized?.();

    throw new Error(
      "Your session has expired. Please sign in again."
    );
  }

  if (response.status === 403) {
    throw new Error(
      "You do not have permission to perform this operation."
    );
  }

  let responseBody;

  try {
    responseBody = await response.json();
  } catch (error) {
    throw new Error(
      `The server returned an invalid response (${response.status}).`,
      { cause: error }
    );
  }

  if (!response.ok) {
    throw new Error(
      responseBody?.message
        ?? `GraphQL request failed with status ${response.status}.`
    );
  }

  if (responseBody.errors?.length) {
    const messages = responseBody.errors
      .map((graphqlError) => graphqlError.message)
      .filter(Boolean);

    throw new Error(
      messages.join("\n") || "The GraphQL operation failed."
    );
  }

  return responseBody.data;
}