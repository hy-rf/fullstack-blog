import { getCookie } from "h3";
import { API_BASE_URL } from "~/constants/api_ constants";


export default defineEventHandler(async (event) => {
  const token = getCookie(event, "token");
  const refreshToken = getCookie(event, "refresh");
  
  const backendUrl = `${API_BASE_URL}/refresh`;
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  };
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }
  if (refreshToken) {
    headers["X-Refresh-Token"] = `Bearer ${refreshToken}`;
  }
  const backendRes = await fetch(backendUrl, {
    method: "GET",
    headers,
  });

  return backendRes;
})