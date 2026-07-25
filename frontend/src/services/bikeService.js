const API_BASE_URL = "http://localhost:8080/api";

export async function getBikes() {
  const response = await fetch(`${API_BASE_URL}/bikes`);

  if (!response.ok) {
    throw new Error("Failed to retrieve bikes");
  }

  return response.json();
}