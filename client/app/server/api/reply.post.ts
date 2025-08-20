import { API_BASE_URL } from "~/constants/api_ constants";

/**
 * Handles the reply submission event.
 * It reads the request body, retrieves the authentication token from cookies,
 * and forwards the request to the backend API for processing.
 * @param {Event} event - The event object containing the request data.
 * @returns {Promise<Response>} - The response from the backend API.
 */
export default defineEventHandler(async (event) => {
  const body = await readBody(event);
  const token = getCookie(event, "token");
  const backendUrl = `${API_BASE_URL}/reply`;

  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  };
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const backendRes = await fetch(backendUrl, {
    method: "POST",
    headers,
    body: JSON.stringify(body)
  });

  return backendRes;
});
