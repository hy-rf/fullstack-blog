import { getCookie } from "h3";
import { API_BASE_URL } from "~/constants/api_ constants";


export default defineEventHandler(async (event) => {
  const token = getCookie(event, "token");
  const backendUrl = `${API_BASE_URL}/users`;

  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  };

  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const backendRes = await fetch(backendUrl, {
    method: "GET",
    headers,
  });

  return backendRes;
});
